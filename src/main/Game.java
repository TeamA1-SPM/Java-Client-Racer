package main;

import io.socket.client.Socket;
import main.constants.GameMode;
import main.game.*;
import main.gamehelper.*;
import main.hidden.MarioRoad;
import main.menu.MainMenu;
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



/*
 * Main game class.
 *
 * This class contains the game loop and
 * manages the interaction between the different layers of the game object classes
 */
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
    private final GameSetup gameSetup;
    private Result result;
    private MusicPlayer musicPlayer;
    private final GameMode gameMode;
    private final MainMenu menu;

    public Game(MainMenu menu, JFrame context, Connection connection, GameSetup gameSetup) {
        this.menu = menu;
        this.context = context;
        this.gameSetup = gameSetup;
        this.gameMode = gameSetup.getGameMode();

        // add and configure server functions
        if(gameMode == MULTI_PLAYER){
            this.connection = connection;
            // setup emit listener for server activated game functions
            if(connection != null){
                serverFunctions(connection.getSocket());
            }
            // setup enemy representation in game
            this.enemy = new EnemyPlayer();
            this.enemy.setPlayerX(gameSetup.getStartPosition() * -1);
        }
        init();
    }

    // initialising the game variables
    private void init(){
        g2D = (Graphics2D)context.getGraphics();
        keyListener = new InputListener();
        context.addKeyListener(keyListener);
        timer = new GameLoopTimer();
        musicPlayer = new MusicPlayer();
        spriteLoader = new SpritesLoader();

        // load the road from json file
        RoadParser roadParser = new RoadParser(spriteLoader);
        road = new Road(roadParser.getTrack(gameSetup.getTrackNr()));
        double maxPosition = road.getTrackLength();

        background = new Background();
        player = new Player(maxPosition, spriteLoader);
        // set player start position
        player.setPlayerX(gameSetup.getStartPosition());

        int lookAhead = 20;
        if(gameSetup.getGameMode() == MULTI_PLAYER){
            lookAhead = 5;
        }

        carSim = new CarSimulation(roadParser.getCarList(), maxPosition, gameSetup.getStartPosition(), lookAhead);
        gamePhysics = new Physics(player);
        race = new Race(gameSetup.getLaps(), maxPosition, connection, gameMode);
        hud = new HUD(gameSetup, race, spriteLoader);
        result = new Result(gameSetup.getPlayerName());
    }


    // server activated functions
    private void serverFunctions(Socket socket){
        // get best lap times from server
        socket.on(GET_BEST_LAP_TIMES, args -> {
            // args (my_time, enemy_time)
            race.setBestLapTime(args[0]);
            race.setBestEnemyTime(args[1]);
        }).on(GET_SERVER_COUNTDOWN, args -> {
            // args (countdown)
            race.setCountdown(args[0]);
        }).on(GET_POSITION, args -> {
            // args (position, playerX, steer, gradient)
            enemy.setPosition(args[0]);
            enemy.setPlayerX(args[1]);
            enemy.setSteer(args[2]);
            enemy.setUpDown(args[3]);
        }).on(GET_RESULT, args -> {
            // args (enemy_name, my_best_lap, enemy_best_lap, won_bool)
            result.setEnemyName((String)args[0]);
            result.setPlayerBestTime(args[1]);
            result.setEnemyBestTime(args[2]);
            result.setPlayerWon((boolean)args[3]);

            // server ends the race if enemy disconnected
            race.setGameState(RESULT);
        });
    }

    // main game loop
    @Override
    public void run() {
        race.setGameState(COUNTDOWN);
        // parameter setup for different game modes
        switch (gameMode){
            case MULTI_PLAYER:
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
        // end game when game loop is finished
        exit();
    }

    // end game functions
    private void exit(){
        musicPlayer.stop();
        menu.setVisible(true);
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

                if(gameMode == MULTI_PLAYER){
                    // send player position to the server
                    connection.sendPosition(player.getPosition(), player.getPlayerX(), player.getSteer(), player.getUpDown());
                }

                // update laps and lap time
                race.update(player.getPosition());
                // pause the game
                if(keyListener.isKeyPressed(KeyEvent.VK_ESCAPE)){
                    keyListener.keyRelease(KeyEvent.VK_ESCAPE);
                    race.setGameState(PAUSE);
                }
                break;
            case COUNTDOWN:
                if(gameMode != MULTI_PLAYER){
                    // get local countdown count
                    race.setCountdown(timer.getCountdown());
                }
                // start updating npc cars movement on 2
                if(race.getCountdown() == 2){
                    carSim.isRunning(true);
                }
                carSim.update(player, enemy);
                road.update(carSim.getCarList());
                break;
            case RESULT:
                // single player result screen setup
                if(gameMode != MULTI_PLAYER){
                    result.setPlayerBestTime(race.getBestLapTime());
                    result.setPlayerWon(true);
                }
                hud.setResult(result);
                // continue car movement update
                carSim.update(player, enemy);
                road.update(carSim.getCarList());
                // end game with enter
                if(keyListener.isKeyPressed(KeyEvent.VK_ENTER)){
                    race.setGameState(END);
                }
                break;
            case PAUSE:
                if(gameMode == MULTI_PLAYER){
                    // resume some functions when game is paused in multiplayer
                    carSim.update(player, enemy);
                    road.update(carSim.getCarList());
                    race.update(player.getPosition());
                }
                // pause menu navigation
                hud.update(keyListener);
                break;
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
        hud.render(g2dNext, player.getSpeed());

        // draw buffered image on the screen
        g2D.drawImage(bufferImage,0,0, SCREEN_WIDTH, SCREEN_HEIGHT, null);
    }
}

