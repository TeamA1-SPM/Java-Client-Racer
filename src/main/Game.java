package main;


import io.socket.client.Socket;
import io.socket.emitter.Emitter;
import main.game.*;
import main.helper.Connection;
import main.helper.InputListener;
import main.helper.Segment;
import main.constants.Settings;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

public class Game implements Runnable {

    private JFrame context;
    private Graphics2D g2D;
    private InputListener keyListener = new InputListener();
    private Player player;
    private Connection connection;
    private Background background;
    private Road road;
    private HUD hud;
    private boolean isRunning;

    private int maxLaps = 6;


    // game loop time variables
    private long now = 0;
    private long last = System.currentTimeMillis();
    private double gdt = 0;
    private final double step = Settings.STEP;


    public Game(JFrame context, Connection connection) {
      this.context = context;
      this.connection = connection;
      init();
    }

    // initialising the game variables
    private void init(){
        context.addKeyListener(keyListener);
        g2D = (Graphics2D)context.getGraphics();

        player = new Player("TestDrive");

        // TODO add maxLaps
        player.setMaxLaps(maxLaps);
        // TODO temporary solution for creating roads
        RoadCreator roadCreator = new RoadCreator();
        ArrayList<Segment> roadSegments = roadCreator.createStraightRoad();
        //ArrayList<Segment> roadSegments = roadCreator.createCurvyRoad();
        road = new Road(roadSegments);


        background = new Background();
        hud = new HUD();

        // setup emit listener
        serverFunctions(connection.getSocket());
    }

    // Server receive data functions
    private void serverFunctions(Socket socket){

        // get best laptimes from server
        socket.on(Settings.GET_BEST_LAP_TIMES, new Emitter.Listener() {
            @Override
            public void call(Object... args) {

                // set player best lap time
                if (args[0] == null || !(args[0] instanceof Number)) {
                    player.setBestLapTime(0.0);
                } else {
                    player.setBestLapTime(((Number) args[0]).doubleValue());
                }
                // set enemies best lap time
                if (args[1] == null || !(args[1] instanceof Number)) {
                    player.setBestEnemyTime(0.0);
                } else {
                    player.setBestEnemyTime(((Number) args[1]).doubleValue());
                }
            }
        });
    }

    // main.Game loop single frame
    @Override
    public void run() {
        isRunning = true;
        while (isRunning) {
            now = System.currentTimeMillis();
            double dt = Math.min(1, (double)(now - last)/ 1000);
            gdt = gdt + dt;
            while (gdt > step) {
                gdt = gdt - step;
                // update game logic
                update();
            }
            // draw frame
            render();
            last = now;
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
        Segment playerSegment = road.findSegment(player.getPosition() + Settings.PLAYER_Z);

        // increase player world position
        player.increase(road.getTrackLength());

        // player control actions left nad right
        if (keyListener.isKeyPressed(KeyEvent.VK_LEFT)) {
            player.pressLeft();
        } else {
            player.releaseLeft();
        }
        if (keyListener.isKeyPressed(KeyEvent.VK_RIGHT)) {
           player.pressRight();
        } else {
            player.releaseRight();
        }

        // sets playerX in curves
        double speedPercent = player.getSpeed()/Settings.MAX_SPEED;
        player.setPlayerX(player.getPlayerX() - (player.getDx() * speedPercent * playerSegment.getCurve() * Settings.CENTRIFUGAL ));

        // player control actions up and down
        if (keyListener.isKeyPressed(KeyEvent.VK_UP)) {
            player.pressUp();
        } else if (keyListener.isKeyPressed(KeyEvent.VK_DOWN)) {
            player.pressDown();
        } else {
            // player not pressing any keys
            player.idle();
        }

        player.update();
        lapCounter();
    }

    // Draws a game frame on the screen
    private void render(){
        // Create Image then draw (Buffering)
        Image i = context.createImage(Settings.SCREEN_WIDTH, Settings.SCREEN_HEIGHT);
        Graphics2D g2dNext = (Graphics2D)i.getGraphics();

        background.render(g2dNext);
        hud.render(g2dNext, player);
        road.render(g2dNext, player);
        player.renderPlayer(g2dNext);

        g2D.drawImage(i,0,0, Settings.SCREEN_WIDTH, Settings.SCREEN_HEIGHT, null);
    }

    // each update loop checks player position
    private void lapCounter() {
        // check if lap is finished
        if (player.getPosition() < Settings.PLAYER_Z) {
            double lapTime = player.getCurrentLapTime();
            // player completed the lap
            if(lapTime > 0){

                player.setLastLapTime(lapTime);
                // send current lap time to server
                connection.sendLapTime(lapTime);
                player.resetTime();
                player.addLap();

                // checks if race is finished
                if(player.getLap() > player.getMaxLaps()){
                    connection.sendFinishedRace();
                    stop();
                }
            }
        } else {
            // add one step time to currentLapTime
            //player.addTime();
        }
    }
}

