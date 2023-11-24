package main;


import main.constants.ColorMode;
import main.game.Background;
import main.game.Player;
import main.game.Road;
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
    private Background background;
    private Road road;
    private boolean isRunning;

    private long now = 0;
    private long last = System.currentTimeMillis();
    private double dt = 0;
    private double gdt = 0;
    private final double step = Settings.STEP;


    public Game(JFrame context) {
      this.context = context;
      init();
    }

    private void init(){
        context.addKeyListener(keyListener);
        g2D = (Graphics2D)context.getGraphics();
        player = new Player();
        background = new Background();
        road = new Road(createRoad(Settings.segmentLength, Settings.segmentQuantity));
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
                update(step);
            }
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

    private void update(double dt){
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
    }

    private void render(){
        // Create Image then draw
        Image i = context.createImage(Settings.SCREEN_WIDTH, Settings.SCREEN_HEIGHT);
        Graphics2D g2Dnext = (Graphics2D)i.getGraphics();

        background.render(g2Dnext);
        road.render(g2Dnext, player);
        player.renderPlayer(g2Dnext);

        g2D.drawImage(i,0,0, Settings.SCREEN_WIDTH, Settings.SCREEN_HEIGHT, null);
    }



    // Create Road
    private Segment[] createRoad(int segmentLength, int segmentQuantity){
        Segment[] segments = new Segment[segmentQuantity];

        for(int index = 0; index < segmentQuantity; index++){
            Segment seg = new Segment(index);

            seg.setP1World(new Point(0,0,index*segmentLength));
            seg.setP2World(new Point(0,0,(index+1)*segmentLength));

            if (Math.floorMod(index, Settings.rumbleLength) % 2 == 1) {
                seg.setColorMode(ColorMode.DARK);
            } else {
                seg.setColorMode(ColorMode.LIGHT);
            }

            segments[index] = seg;
        }

        int index = (int)Math.floor(player.getPlayerZ()/segmentLength) % segmentQuantity;

        // Start line
        segments[index + 2].setColorMode(ColorMode.START);
        segments[index + 3].setColorMode(ColorMode.START);

        // Finish line
        for(int n = 0 ; n < Settings.rumbleLength ; n++){
            segments[Settings.segmentQuantity -1-n].setColorMode(ColorMode.FINISH);
        }

        return segments;
    }
}

