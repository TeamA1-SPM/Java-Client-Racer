package main;


import io.socket.client.Socket;
import io.socket.emitter.Emitter;
import main.game.*;
import main.helper.Connection;
import main.helper.GameLoopTimer;
import main.helper.InputListener;
import main.helper.Segment;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

import static main.constants.Settings.*;

public class Game implements Runnable {

    private JFrame context;
    private Graphics2D g2D;
    private InputListener keyListener;
    private GameLoopTimer timer;
    private final Connection connection;
    private SpritesLoader spriteLoader;
    private Player player;
    private Race race;
    private Background background;
    private Road road;
    private HUD hud;
    private boolean isRunning;

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

        spriteLoader = new SpritesLoader();
        background = new Background();
        hud = new HUD();

        RoadCreator roadCreator = new RoadCreator();

        // TODO temporary solution for creating roads
        //ArrayList<Segment> roadSegments = roadCreator.createV1StraightRoad();
        //ArrayList<Segment> roadSegments = roadCreator.createV2CurvyRoad();
        //ArrayList<Segment> roadSegments = roadCreator.createV3HillRoad();
        ArrayList<Segment> roadSegments = roadCreator.createV4Final();
        road = new Road(roadSegments);

        player = new Player("TestDrive", road.getTrackLength());

        // TODO maxLaps needs to be adjusted somewhere
        race = new Race(6, road.getTrackLength());

        // setup emit listener for server activated game functions
        serverFunctions(connection.getSocket());
    }

    // Server activated methods
    private void serverFunctions(Socket socket){

        // get best laptimes from server
        socket.on(GET_BEST_LAP_TIMES, new Emitter.Listener() {
            @Override
            public void call(Object... args) {

                // set player best lap time
                if (args[0] == null || !(args[0] instanceof Number)) {
                    race.setBestLapTime(0.0);
                } else {
                    race.setBestLapTime(((Number) args[0]).doubleValue());
                }
                // set enemies best lap time
                if (args[1] == null || !(args[1] instanceof Number)) {
                    race.setBestEnemyTime(0.0);
                } else {
                    race.setBestEnemyTime(((Number) args[1]).doubleValue());
                }
            }
        });
    }

    // main game loop
    @Override
    public void run() {
        isRunning = true;
        // single frame
        while (isRunning) {
            if(timer.isReady()){
                // update game logic
                update();
                // draw frame
                render();
            }
        }
    }

    private void stop() { isRunning = false; }

    // Updates the game logic
    private void update(){
        Segment playerSegment = road.findSegment(player.getPosition() + PLAYER_Z);
        double curve = playerSegment.getCurve();
        double updown = playerSegment.getP2World().getY()- playerSegment.getP1World().getY();

        // Calc parallax scrolling background
        background.updateOffset(curve, player.getSpeed());

        // Player control actions left nad right
        if (keyListener.isKeyPressed(KeyEvent.VK_LEFT)) {
            player.pressLeft();
        }else if (keyListener.isKeyPressed(KeyEvent.VK_RIGHT)) {
           player.pressRight();
        }
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
        player.update(curve, updown);

        if(race.isLapFinished(player.getPosition())){
            connection.sendLapTime(race.getLastLapTime());
        }
        if(race.isFinished()){
            connection.sendFinishedRace();
            stop();
        }
    }

    // Draws a game frame on the screen
    private void render(){
        // Create Image then draw (Buffering)
        Image bufferImage = context.createImage(SCREEN_WIDTH, SCREEN_HEIGHT);
        Graphics2D g2dNext = (Graphics2D)bufferImage.getGraphics();

        g2dNext.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        background.render(g2dNext);
        road.render(g2dNext, player, spriteLoader);
        player.renderPlayer(g2dNext, spriteLoader);
        hud.render(g2dNext, race, player.getSpeed());

        g2D.drawImage(bufferImage,0,0, SCREEN_WIDTH, SCREEN_HEIGHT, null);
    }
}

