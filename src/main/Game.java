package main;


import io.socket.client.Socket;
import main.constants.GameMode;
import main.game.*;
import main.helper.*;
import main.hidden.MarioRoad;
import main.music.MusicPlayer;
import main.tracks.RoadParser;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

import static main.constants.GameState.*;
import static main.constants.GameMode.*;
import static main.constants.Settings.*;
import static main.constants.Server.*;

public class Game implements Runnable {

    private final JFrame context;
    private Graphics2D g2D;
    private InputListener keyListener;
    private GameLoopTimer timer;
    private Connection connection;
    private SpritesLoader spriteLoader;
    private Player player;
    private EnemyPlayer enemy;
    private Race race;
    private Background background;
    private Road road;
    private CarSimulation carSim;
    private HUD hud;
    private Physics gamePhysics;
    RoadParser roadParser;
    private final GameSetup gameSetup;
    private final GameMode gameMode;

    public Game(JFrame context, Connection connection, GameSetup gameSetup) {
      this.context = context;
      this.gameSetup = gameSetup;
      this.gameMode = gameSetup.getGameMode();

      if(gameMode == MULTI_PLAYER){
          this.connection = connection;
          // setup emit listener for server activated game functions
          if(connection != null){
              serverFunctions(connection.getSocket());
          }
      }
      init();
    }

    // initialising the game variables
    private void init(){
        g2D = (Graphics2D)context.getGraphics();
        keyListener = new InputListener();
        context.addKeyListener(keyListener);
        timer = new GameLoopTimer();
        MusicPlayer musicPlayer = new MusicPlayer();
        musicPlayer.stop();

        spriteLoader = new SpritesLoader();
        background = new Background();
        hud = new HUD();

        roadParser = new RoadParser(spriteLoader);
        road = new Road(roadParser.getTrack(gameSetup.getTrackNr()));
        double maxPosition = road.getTrackLength();
        carSim = new CarSimulation(roadParser.getCarList(), maxPosition);

        player = new Player(gameSetup.getPlayerName(), maxPosition, spriteLoader);
        player.setPlayerX(gameSetup.getStartPosition());

        gamePhysics = new Physics(player);
        race = new Race(gameSetup.getLaps(), maxPosition, connection, gameMode);

    }


    // server activated methods
    private void serverFunctions(Socket socket){
        // get best lap times from server
        socket.on(GET_BEST_LAP_TIMES, args -> {
            race.setBestLapTime(args[0]);
            race.setBestEnemyTime(args[1]);
        }).on(SERVER_COUNTDOWN, args ->
                race.setCountdown(args[0])
        ).on(GET_POSITION, args -> {
            enemy.setPosition(args[0]);
            enemy.setPlayerX(args[1]);
            enemy.setSteer(args[2]);
            enemy.setUpDown(args[3]);
        });
    }

    // main game loop
    @Override
    public void run() {

        race.setGameState(COUNTDOWN);
        this.carSim.addPlayer(player);
        switch (gameMode){
            case MULTI_PLAYER:
                this.enemy = new EnemyPlayer(gameSetup.getEnemyName());
                this.carSim.addEnemy(enemy);
                break;
            case SINGLE_PLAYER:
                timer.startCountdown();
                break;
            case MARIO:
                new MarioRoad().changeRoad(road.getSegments());
                spriteLoader.setMarioMode();
                timer.startCountdown();
                break;
        }

        // single frame
        while (race.getGameState() != END) {
            if(timer.isReady()){
                // update game logic
                update();
                // draw frame
                render();
            }
        }

        exit();
    }

    private void exit(){
        this.context.dispose();
    }

    // game logic update
    private void update(){
        // get player segment based of player position
        Segment playerSegment = road.findSegment(player.getPosition() + PLAYER_Z);
        ArrayList<Car> segmentCars = road.getSegmentCars(playerSegment.getIndex());

        switch (race.getGameState()) {
            case RUNNING:
                // update parallax scrolling background
                background.update(playerSegment.getCurve(), player.getSpeed());
                // simulate npc car movement
                carSim.update(player, enemy);
                // update npc cars on the road
                road.update(carSim.getCarList());
                // update player input
                player.update(keyListener);
                // updates collision and player position
                gamePhysics.update(playerSegment, segmentCars);
                // update laps and lap time
                race.update(player.getPosition());

                // pause the game
                if(keyListener.isKeyPressed(KeyEvent.VK_ESCAPE)){
                    keyListener.keyRelease(KeyEvent.VK_ESCAPE);
                    race.setGameState(PAUSE);
                }

                break;
            case COUNTDOWN:
                if(gameMode == SINGLE_PLAYER || gameMode == MARIO){
                    race.setCountdown(timer.getCountdown());
                }
                // simulate npc car movement
                carSim.update(player, enemy);
                // update npc cars on the road
                road.update(carSim.getCarList());
                break;
            case RESULT:
                // simulate npc car movement
                carSim.update(player, enemy);
                // update npc cars on the road
                road.update(carSim.getCarList());
                break;
            case PAUSE:
                if(gameMode == MULTI_PLAYER){
                    // simulate npc car movement
                    carSim.update(player, enemy);
                    // update npc cars on the road
                    road.update(carSim.getCarList());
                    // update laps and lap time
                    race.update(player.getPosition());
                }

                if(keyListener.isKeyPressed(KeyEvent.VK_ESCAPE)){
                    keyListener.keyRelease(KeyEvent.VK_ESCAPE);
                    carSim.isRunning(true);
                    race.setGameState(RUNNING);
                } else if (keyListener.isKeyPressed(KeyEvent.VK_UP)) {
                    hud.setPausePos(1);
                } else if (keyListener.isKeyPressed(KeyEvent.VK_DOWN)) {
                    hud.setPausePos(2);
                } else if (keyListener.isKeyPressed(KeyEvent.VK_ENTER)) {
                    int pos = hud.getPausePos();
                    if(pos == 2){
                        race.setGameState(END);
                    }else{
                        carSim.isRunning(true);
                        race.setGameState(RUNNING);
                    }
                }

                break;
        }

        if(gameMode == MULTI_PLAYER){
            connection.sendPosition(player.getPosition(), player.getPlayerX(), player.getSteer(), player.getUpDown());
        }
    }

    // draw a game frame on the screen
    private void render(){
        // create buffered image then draw (Buffering)
        Image bufferImage = context.createImage(SCREEN_WIDTH, SCREEN_HEIGHT);
        Graphics2D g2dNext = (Graphics2D)bufferImage.getGraphics();

        // add game scene layers to buffered graphic
        background.render(g2dNext);
        road.render(g2dNext, player, spriteLoader);
        player.renderPlayer(g2dNext);
        hud.render(g2dNext, race, player.getSpeed(),spriteLoader);

        // draw buffered image on the screen
        g2D.drawImage(bufferImage,0,0, SCREEN_WIDTH, SCREEN_HEIGHT, null);
    }
}

