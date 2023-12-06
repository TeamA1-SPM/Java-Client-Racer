package main;


import io.socket.client.Socket;
import io.socket.emitter.Emitter;
import main.game.*;
import main.helper.Connection;
import main.helper.GameLoopTimer;
import main.helper.InputListener;
import main.helper.Segment;
import main.constants.Settings;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.awt.image.ConvolveOp;
import java.awt.image.Kernel;
import java.util.ArrayList;

public class Game implements Runnable {

    private JFrame context;
    private Graphics2D g2D;
    private InputListener keyListener;
    private GameLoopTimer timer;
    private Player player;
    private final Connection connection;
    private Background background;
    private Road road;
    private HUD hud;
    private boolean isRunning;

    private int maxLaps = 6;
    private boolean playerCheckpoint = false;

    private Segment playerSegment;

    public Game(JFrame context, Connection connection) {
      this.context = context;
      this.connection = connection;
      init();
    }

    // initialising the game variables
    private void init(){
        g2D = (Graphics2D)context.getGraphics();
        keyListener = new InputListener();
        context.addKeyListener(keyListener);
        timer = new GameLoopTimer();

        background = new Background();
        hud = new HUD();
        player = new Player("TestDrive");
        // TODO maxLaps needs to be adjusted somewhere
        player.setMaxLaps(maxLaps);

        // TODO temporary solution for creating roads
        RoadCreator roadCreator = new RoadCreator();
        //ArrayList<Segment> roadSegments = roadCreator.createV1StraightRoad();
        //ArrayList<Segment> roadSegments = roadCreator.createV2CurvyRoad();
        //ArrayList<Segment> roadSegments = roadCreator.createV3HillRoad();
        ArrayList<Segment> roadSegments = roadCreator.createV4Final();
        road = new Road(roadSegments);


        // TODO temporary solution login function
        connection.login("hans","321");
        //connection.login("blabla","321");
        connection.findLobby();


        // setup emit listener for server activated game functions
        serverFunctions(connection.getSocket());
    }

    // Server activated methods
    private void serverFunctions(Socket socket){

        // get best laptimes from server
        socket.on(Settings.GET_BEST_LAP_TIMES, new Emitter.Listener() {
            @Override
            public void call(Object...args) {
                String myTime = "" + args[0];
                String enemyTime = "" + args[1];

                // set player best lap time
                if(myTime.equals("null")){
                    player.setBestLapTime(0.0);
                } else{
                    player.setBestLapTime(Double.parseDouble(myTime));
                }
                // set enemies best lap time
                if(enemyTime.equals("null")){
                    player.setBestEnemyTime(0.0);
                } else{
                    player.setBestEnemyTime(Double.parseDouble(enemyTime));
                }
            }
        });
    }

    // main.Game loop single frame
    @Override
    public void run() {
        isRunning = true;
        while (isRunning) {
            timer.frameStart();
            while (timer.isGDT()) {
                timer.updateGDT();
                // update game logic
                update();
            }
            // draw frame
            render();
            timer.frameFinished();
            try{
                Thread.sleep(1000 / 60);
            }catch(Exception e){

            }
        }
    }

    public void stop() {
        isRunning = false;
    }

    // Updates the game logic
    private void update(){
        playerSegment = road.findSegment(player.getPosition() + Settings.PLAYER_Z);
        double speedPercent = player.getSpeed()/Settings.MAX_SPEED;

        // Increase player world position
        player.increase(road.getTrackLength());
        // Calc parallax scrolling background
        background.updateOffset(playerSegment.getCurve(), speedPercent);

        // Player control actions left nad right
        if (keyListener.isKeyPressed(KeyEvent.VK_LEFT)) {
            player.pressLeft();
        }else if (keyListener.isKeyPressed(KeyEvent.VK_RIGHT)) {
           player.pressRight();
        }

        // Sets playerX in curves
        player.setPlayerX(player.getPlayerX() - (player.getDx() * speedPercent * playerSegment.getCurve() * Settings.CENTRIFUGAL ));

        // Player control actions up and down
        if (keyListener.isKeyPressed(KeyEvent.VK_UP)) {
            player.pressUp();
        } else if (keyListener.isKeyPressed(KeyEvent.VK_DOWN)) {
            player.pressDown();
        } else {
            // Player not pressing any keys
            player.idle();
        }

        // Update player speed and time
        player.update();
        countLaps();
    }

    // Draws a game frame on the screen
    private void render(){
        // Create Image then draw (Buffering)
        Image i = context.createImage(Settings.SCREEN_WIDTH, Settings.SCREEN_HEIGHT);
        Graphics2D g2dNext = (Graphics2D)i.getGraphics();

        g2dNext.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        background.render(g2dNext);
        hud.render(g2dNext, player);
        road.render(g2dNext, player);
        player.renderPlayer(g2dNext,playerSegment.getP2World().getY()-playerSegment.getP1World().getY());

        g2D.drawImage(i,0,0, Settings.SCREEN_WIDTH, Settings.SCREEN_HEIGHT, null);
    }

    // Checks if player finished the lap
    private void countLaps(){
        double position = player.getPosition();
        double trackMid = (double)road.getTrackLength()/2;
        double playerZ = Settings.PLAYER_Z;

        // player crossed checkpoint
        if(position > trackMid  & position < trackMid  + playerZ){
            playerCheckpoint = true;
        }

        // player crossed checkpoint and finish line
        if(playerCheckpoint & position < playerZ){
            double lapTime = player.getCurrentLapTime();
            player.setLastLapTime(lapTime);
            // send current lap time to server
            connection.sendLapTime(lapTime);
            player.resetTime();
            player.addLap();
            playerCheckpoint = false;

            // check if race is finished
            if(player.getLap() > player.getMaxLaps()){
                connection.sendFinishedRace();
                stop();
            }
        }
    }
}

