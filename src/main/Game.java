package main;


import io.socket.client.Socket;
import io.socket.emitter.Emitter;
import main.constants.GameMode;
import main.constants.GameState;
import main.game.*;
import main.helper.*;
import main.hidden.MarioRoad;
import main.tracks.RoadParser;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Random;

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
    private GameState gameState = LOADING;
    private final GameMode gameMode;

    public Game(JFrame context, Connection connection, GameMode gameMode) {
      this.context = context;
      this.gameMode = gameMode;
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

        spriteLoader = new SpritesLoader();
        background = new Background();
        hud = new HUD();


        roadParser = new RoadParser(spriteLoader);

        ArrayList<Segment> roadSegments = roadParser.getTrack(1);
        ArrayList<Car> roadCars = roadParser.getCarList();


        road = new Road(roadSegments);
        carSim = new CarSimulation(roadCars, road.getTrackLength());


        player = new Player("TestDrive", road.getTrackLength(), spriteLoader);
        player.setPlayerX(new Random().nextDouble(1));


        gamePhysics = new Physics(player);

        // TODO maxLaps needs to be adjusted somewhere
        race = new Race(1, road.getTrackLength(), connection, gameMode);

        gameState = READY;
    }


    // server activated methods
    private void serverFunctions(Socket socket){
        // get best lap times from server
        socket.on(GET_BEST_LAP_TIMES, new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                race.setBestLapTime(args[0]);
                race.setBestEnemyTime(args[1]);
            }
        }).on(SERVER_COUNTDOWN, new Emitter.Listener(){
            @Override
            public void call(Object... args) {
                race.setCountdown(args[0]);
            }
        }).on(GET_POSITION, new Emitter.Listener(){
            @Override
            public void call(Object... args) {
                enemy.setPosition(args[0]);
                enemy.setPlayerX(args[1]);
                enemy.setSteer(args[2]);
                enemy.setUpDown(args[3]);
            }
        });
    }

    // main game loop
    @Override
    public void run() {

        race.setGameState(COUNTDOWN);
        this.carSim.addPlayer(player);
        switch (gameMode){
            case MULTI_PLAYER:
                this.enemy = new EnemyPlayer("Enemy");
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
        while (gameState != END) {
            if(timer.isReady()){
                // update game logic
                update();
                // draw frame
                render();
            }
        }
    }

    // game logic update
    private void update(){
        // get player segment based of player position
        Segment playerSegment = road.findSegment(player.getPosition() + PLAYER_Z);
        ArrayList<Car> segmentCars = road.getSegmentCars(playerSegment.getIndex());

        // update parallax scrolling background
        background.update(playerSegment.getCurve(), player.getSpeed());
        // simulate npc car movement
        carSim.update(player, enemy);
        // update npc cars on the road
        road.update(carSim.getCarList());
        // update player speed and position

        switch (race.getGameState()) {
            case RUNNING:
                player.update(keyListener);
                // updates collision
                gamePhysics.update(playerSegment, segmentCars);
                // update laps and lap time
                race.update(player.getPosition());
                break;
            case COUNTDOWN:
                if(gameMode == SINGLE_PLAYER || gameMode == MARIO){
                    race.setCountdown(timer.getCountdown());
                }
                break;
            case RESULT:
                // TODO show result
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

