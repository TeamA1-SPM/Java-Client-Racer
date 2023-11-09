package main;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;

public class Window extends JFrame implements Runnable{

    private Graphics2D g2;
    private InputListener keyListener = new InputListener();

    private Image skyImage = null;
    private Image hillsImage = null;
    private Image treesImage = null;

    public Window(){
        this.setSize(Constants.SCREEN_WIDTH,Constants.SCREEN_HEIGHT);
        this.setTitle(Constants.SCREEN_TITLE);
        this.setResizable(Constants.SCREEN_RESIZABLE);
        this.setVisible(Constants.SCREEN_VISIBLE);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        this.addKeyListener(keyListener);

        g2 = (Graphics2D)this.getGraphics();

        loadImages();

        run();
    }

    @Override
    public void run() {
        // game loop
        while(true){
            frame();
        }

    }

    private long now = 0;
    private long last = System.currentTimeMillis();
    private double dt = 0;
    private double gdt = 0;
    private final double step = (double) 1 /60;

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

    }


    private double position = 0;
    private double playerX = 0;
    private double speed = 0;
    private int segmentLength = 200;
    private double trackLength = 500 * 200/step;
    private double maxSpeed = segmentLength/step;
    private double accel =  maxSpeed/5;
    private double breaking      = -maxSpeed;
    private double decel         = -maxSpeed/5;
    private double offRoadDecel  = -maxSpeed/2;
    private double offRoadLimit  =  maxSpeed/4;


    private void update(double dt){
        position = increase(position, dt * speed, trackLength);

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
            result =- max;
        while (result < 0)
            result =+ max;
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


    private  void loadImages(){
        ImageIcon imageIcon = new ImageIcon(this.getClass().getResource("images/background/sky.png"));
        skyImage = imageIcon.getImage();
        imageIcon = new ImageIcon(this.getClass().getResource("images/background/hills.png"));
        hillsImage = imageIcon.getImage();
        imageIcon = new ImageIcon(this.getClass().getResource("images/background/trees.png"));
        treesImage = imageIcon.getImage();

    }

    private void render(){
        this.removeAll();
        renderBackground();


    }



    private void renderBackground(){
        g2.drawImage(skyImage,0,0,1280,480,null);
        g2.drawImage(hillsImage,0,0,1280,480,null);
        g2.drawImage(treesImage,0,0,1280,480,null);
    }





}
