package main;


import io.socket.client.Socket;
import io.socket.emitter.Emitter;
import main.constants.ColorMode;
import main.game.Background;
import main.game.HUD;
import main.game.Player;
import main.game.Road;
import main.helper.Connection;
import main.helper.InputListener;
import main.helper.Point;
import main.helper.Segment;
import main.constants.Settings;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;

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
    private double dt = 0;
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

        background = new Background();
        road = new Road(createRoad(Settings.segmentLength, Settings.segmentQuantity));
        hud = new HUD();


        connection.login("hans","321");
        connection.login("blabla","321");
        connection.findLobby();
        serverFunctions(connection.getSocket());
    }

    // Server receive data functions
    private void serverFunctions(Socket socket){

        // get best laptimes from server
        socket.on(Settings.GET_BEST_LAP_TIMES, new Emitter.Listener() {
            @Override
            public void call(Object... args) {

                // set player best lap time
                if(args[0] == null){
                    player.setBestLapTime(0.0);
                } else{
                    player.setBestLapTime((double)args[0]);
                }
                // set enemies best lap time
                if(args[1] == null){
                    player.setBestEnemyTime(0.0);
                } else{
                    player.setBestEnemyTime((double)args[1]);
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
            dt = Math.min(1, (double)(now - last)/ 1000);
            gdt = gdt + dt;
            while (gdt > step) {
                gdt = gdt - step;
                // update game logic
                update(step);
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
    private void update(double dt){
        // increase player world position
        player.increase(dt);

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

        if (keyListener.isKeyPressed(KeyEvent.VK_UP)) {
           player.pressUp();
        } else if (keyListener.isKeyPressed(KeyEvent.VK_DOWN)) {
           player.pressDown();
        } else {
           player.idle();
        }

       player.offRoad();
       player.xLimit();
       player.speedLimit();
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

    // check if a lap is finished
    private void lapCounter() {
        if (player.getPosition() < player.getPlayerZ()) {
            double currentTime = player.getCurrentLapTime();
            if(currentTime > 0){
                System.out.println(currentTime);

                player.setLastLapTime(currentTime);
                // send current lap time to server
                connection.sendLapTime(currentTime);
                player.setCurrentLapTime(0);
                player.addLap();

                // race is finished
                if(player.getLap() > player.getMaxLaps()){
                    connection.sendFinishedRace();
                    isRunning = false;
                }
            }
        } else {
            // add one deltatime to currentLapTime
            player.addTime();
        }
    }

    // Create Road
    private Segment[] createRoad(int segmentLength, int segmentQuantity){
        Segment[] segments = new Segment[segmentQuantity];

        // Create road segments
        for(int index = 0; index < segmentQuantity; index++){
            Segment seg = new Segment(index);

            seg.setP1World(new Point(0,0,index*segmentLength));
            seg.setP2World(new Point(0,0,(index+1)*segmentLength));

            if (Math.floorDiv(index, Settings.rumbleLength) % 2 == 0) {
                seg.setColorMode(ColorMode.LIGHT);
            } else {
                seg.setColorMode(ColorMode.DARK);
            }

            segments[index] = seg;
        }

        int index = (int)Math.floor(player.getPlayerZ()/segmentLength) % segmentQuantity;

        // Start line
        segments[index + 2].setColorMode(ColorMode.START);
        segments[index + 3].setColorMode(ColorMode.START);

        // Finish line
        for(int n = 0 ; n < Settings.rumbleLength ; n++){
            segments[Settings.segmentQuantity-1-n].setColorMode(ColorMode.FINISH);
        }

        return segments;
    }
}

