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

    private int lap = 1;
    private int maxLaps = 4;

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
        player = new Player("TestDrive");
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
       lapCounter();
    }


    private void lapCounter() {
        if (player.getPosition() < player.getPlayerZ()) {
            double currentTime = player.getCurrentLapTime();
            if(currentTime > 0){
                player.setBestLapTime(currentTime);
                System.out.println("Lap: " + lap + " Laptime: " + player.getCurrentLapTime() + " BestTime: " + player.getBestLapTime());
                lap++;
                player.setCurrentLapTime(0);
                if(lap > maxLaps){
                    isRunning = false;
                }
            }
        } else {
            player.addTime();
        }
    }


    private void render(){
        // Create Image then draw
        Image i = context.createImage(Settings.SCREEN_WIDTH, Settings.SCREEN_HEIGHT);
        Graphics2D g2dNext = (Graphics2D)i.getGraphics();

        background.render(g2dNext);

        // Background Rectangle
        g2dNext.setColor(new Color(150, 0, 0, 100));
        g2dNext.fillRect(0, 0, 1024, 90);
        g2dNext.setColor(Color.BLACK);
        g2dNext.drawRect(0, 0, 1024, 90);

        // Rectangle for 'Time'
        g2dNext.setColor(new Color(255, 255, 255, 150));
        g2dNext.fillRect(15, 38, 120, 45);
        g2dNext.setColor(Color.BLACK);
        g2dNext.drawRect(15, 38, 120, 45);

        // Text for 'Time'
        g2dNext.setColor(Color.BLACK);
        String textTime = "Time:";
        Font fontTime = new Font("Universal Light", Font.BOLD, 14);
        g2dNext.setFont(fontTime);
        g2dNext.drawString(textTime, 20, 65);

        // Rectangle for 'Last Lap'
        g2dNext.setColor(new Color(255, 200, 0, 150));
        g2dNext.fillRect(145, 38, 180, 45);
        g2dNext.setColor(Color.BLACK);
        g2dNext.drawRect(145, 38, 180, 45);

        // Text for 'Last Lap'
        g2dNext.setColor(Color.BLACK);
        String textLastLap = "Last Lap:";
        Font fontLastLap = new Font("Universal Light", Font.BOLD, 14);
        g2dNext.setFont(fontLastLap);
        g2dNext.drawString(textLastLap, 150, 65);

        // Rectangle for 'Fastest Lap'
        g2dNext.setColor(new Color(255, 200, 0, 150));
        g2dNext.fillRect(335, 38, 180, 45);
        g2dNext.setColor(Color.BLACK);
        g2dNext.drawRect(335, 38, 180, 45);

        // Text for 'Fastest Lap'
        g2dNext.setColor(Color.BLACK);
        String textFastestLap = "Fastest Lap:";
        Font fontFastestLap = new Font("Universal Light", Font.BOLD, 14);
        g2dNext.setFont(fontFastestLap);
        g2dNext.drawString(textFastestLap, 340, 65);

        // Rectangle for 'Enemy Lap'
        g2dNext.setColor(new Color(255, 200, 0, 150));
        g2dNext.fillRect(525, 38, 180, 45);
        g2dNext.setColor(Color.BLACK);
        g2dNext.drawRect(525, 38, 180, 45);

        // Text for 'Enemy Lap'
        g2dNext.setColor(Color.BLACK);
        String textEnemyLap = "Enemy Lap:";
        Font fontEnemyLap = new Font("Universal Light", Font.BOLD, 14);
        g2dNext.setFont(fontEnemyLap);
        g2dNext.drawString(textEnemyLap, 530, 65);

        // Rectangle for 'MPH'
        g2dNext.setColor(new Color(255, 255, 255, 150));
        g2dNext.fillRect(715, 38, 120, 45);
        g2dNext.setColor(Color.BLACK);
        g2dNext.drawRect(715, 38, 120, 45);

        // Text for 'MPH'
        g2dNext.setColor(Color.BLACK);
        String textMPH = "MPH:";
        Font fontMPH = new Font("Universal Light", Font.BOLD, 14);
        g2dNext.setFont(fontMPH);
        g2dNext.drawString(textMPH, 720, 65);

        // Rectangle for 'Round'
        g2dNext.setColor(new Color(255, 255, 255, 150));
        g2dNext.fillRect(845, 38, 162, 45);
        g2dNext.setColor(Color.BLACK);
        g2dNext.drawRect(845, 38, 162, 45);

        // Text for 'Round'
        g2dNext.setColor(Color.BLACK);
        String textRound = "Round:";
        Font fontRound = new Font("Universal Light", Font.BOLD, 14);
        g2dNext.setFont(fontRound);
        g2dNext.drawString(textRound, 850, 65);

        road.render(g2dNext, player);
        player.renderPlayer(g2dNext);

        g2D.drawImage(i,0,0, Settings.SCREEN_WIDTH, Settings.SCREEN_HEIGHT, null);
    }

    // Create Road
    private Segment[] createRoad(int segmentLength, int segmentQuantity){
        Segment[] segments = new Segment[segmentQuantity];

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

