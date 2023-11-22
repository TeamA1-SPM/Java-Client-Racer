package main;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;

public class Window extends JFrame implements Runnable{

    private Graphics2D g2;
    private InputListener keyListener = new InputListener();

    private Background background;
    private Road road;

    private boolean isRunning = true;



    public Window(){
        this.setSize(Settings.SCREEN_WIDTH, Settings.SCREEN_HEIGHT);
        this.setTitle(Settings.SCREEN_TITLE);
        this.setResizable(Settings.SCREEN_RESIZABLE);
        this.setVisible(Settings.SCREEN_VISIBLE);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        this.addKeyListener(keyListener);

        g2 = (Graphics2D)this.getGraphics();
        background = new Background();
        road = new Road(createRoad(Settings.segmentLength, Settings.segmentSize));

        run();
    }


    @Override
    public void run() {
        // game loop
        while(isRunning ){
            frame();
        }

    }

    private long now = 0;
    private long last = System.currentTimeMillis();
    private double dt = 0;
    private double gdt = 0;
    private final double step = Settings.STEP;

    //


    private void frame(){

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

        System.out.println("Speed: " + speed);

    }


    private double position = 0;
    private double playerX = 0;
    private double playerZ = Settings.cameraHeight * Settings.cameraDepth;
    private double speed = 0;

    private double maxSpeed = Settings.segmentLength/step;
    private double accel =  maxSpeed/5;
    private double breaking      = -maxSpeed;
    private double decel         = -maxSpeed/5;
    private double offRoadDecel  = -maxSpeed/2;
    private double offRoadLimit  =  maxSpeed/4;


    private void update(double dt){
        position = increase(position, dt * speed, Settings.trackLength);

        double dx = dt * 2 * (speed/maxSpeed);

        if (keyListener.isKeyPressed(KeyEvent.VK_LEFT))
            playerX = playerX - dx;
        else if (keyListener.isKeyPressed(KeyEvent.VK_RIGHT))
            playerX = playerX + dx;

        if (keyListener.isKeyPressed(KeyEvent.VK_UP))
            speed = accelerate(speed, accel, dt);
        else if (keyListener.isKeyPressed(KeyEvent.VK_DOWN))
            speed = accelerate(speed, breaking, dt);
        else
            speed = accelerate(speed, decel, dt);

        if (((playerX < -1) || (playerX > 1)) && (speed > offRoadLimit))
            speed = accelerate(speed, offRoadDecel, dt);

        playerX = limit(playerX, -2, 2);
        speed   = limit(speed, 0, maxSpeed);

    }

    // Util
    private double increase(double start, double increment, double max) { // with looping
        double result = start + increment;
        while (result >= max)
            result -= max;
        while (result < 0)
            result += max;
        return result;
    }

    // Util
    private double accelerate(double v, double accel,double dt){
        return v + (accel * dt);
    }
    // Util
    private double limit(double value, double min, double max){
        return Math.max(min, Math.min(value, max));
    }

    // Create Road
    private Segment[] createRoad(int segmentLength, int segmentSize){
        Segment[] segments = new Segment[segmentSize];

        for(int index = 0; index < segmentLength; index++){
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

        // TODO Paint start and finish line
        //segments[findSegment(playerZ).index + 2].color = COLORS.START;
        //segments[findSegment(playerZ).index + 3].color = COLORS.START;
        //for(var n = 0 ; n < rumbleLength ; n++)
        //    segments[segments.length-1-n].color = COLORS.FINISH;

        return segments;
    }

    private void render(){

        g2.clearRect(0,0, Settings.SCREEN_WIDTH, Settings.SCREEN_HEIGHT);

        background.render(g2);
        road.render(g2, position, playerX);
        renderPlayer();

    }

    private void renderPlayer(){

    }

}
