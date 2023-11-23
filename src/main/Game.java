package main;


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
        road = new Road(createRoad(Settings.segmentLength, Settings.segmentSize));
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
                update(gdt);
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

        if (keyListener.isKeyPressed(KeyEvent.VK_LEFT))
            player.pressLeft();
        else if (keyListener.isKeyPressed(KeyEvent.VK_RIGHT))
            player.pressRight();

        if (keyListener.isKeyPressed(KeyEvent.VK_UP))
            player.pressUp();
        else if (keyListener.isKeyPressed(KeyEvent.VK_DOWN))
            player.pressDown();
        else
            player.idle();

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

        g2D.clearRect(0,0,Settings.SCREEN_WIDTH,Settings.SCREEN_HEIGHT);
        g2D.drawImage(i,0,0, Settings.SCREEN_WIDTH, Settings.SCREEN_HEIGHT, null);
    }



    // Create Road
    private Segment[] createRoad(int segmentLength, int segmentSize){
        Segment[] segments = new Segment[segmentSize];

        for(int index = 0; index < segmentLength; index++){
            Segment seg = new Segment(index);

            seg.setP1World(new main.Point(0,0,index*segmentLength));
            seg.setP2World(new Point(0,0,(index+1)*segmentLength));

            if (Math.floorMod(index, Settings.rumbleLength) % 2 == 1) {
                seg.setColorMode(ColorMode.DARK);
            } else {
                seg.setColorMode(ColorMode.LIGHT);
            }

            segments[index] = seg;
        }

        int index = (int)Math.floor(0/Settings.segmentLength) % Settings.segmentSize;
        // TODO Paint start and finish line
        segments[index + 4].setColorMode(ColorMode.START);
        segments[index + 5].setColorMode(ColorMode.START);

        //for(int n = 0 ; n < Settings.rumbleLength ; n++)
        //   segments[Settings.segmentSize-1-n].setColorMode(ColorMode.FINISH);

        return segments;
    }
}

