package main.game;

import main.constants.Settings;

import javax.swing.*;
import java.awt.*;
import java.util.Objects;
import java.util.Random;

public class Player {

    // player stripes variables
    private Image playerStraight;
    private Image playerUp;
    private Image playerLeft;
    private Image playerLeftUp;
    private Image playerRight;
    private Image playerRightUp;
    private Image currentPlayerSprite;
    private Sprite sprite;


    // player position variables
    private double position = 0;
    private double playerX = 0;
    private double playerY = 0;
    private double speed = 0;
    private int steer = 0;



    private String name;

    // player time variables
    private double currentLapTime = 0;
    private double lastLapTime = 0;
    private double bestLapTime = 0;
    private double bestEnemyTime = 0;

    private int lap = 1;
    private int maxLaps;

    private double dx;
    private double dt = Settings.STEP;


    public Player(String name) {
        this.name = name;
        loadSprites();
        sprite = new Sprite();
    }

    public void increase(int trackLength) {
        dx = dt * 2 * (speed/Settings.MAX_SPEED);
        double increment = dt * speed;
        int max = trackLength;

        double result = position + increment;
        while (result >= max)
            result -= max;
        while (result < 0)
            result += max;

        position = result;
    }

    private double accelerate(double v, double accel,double dt){
        return v + (accel * dt);
    }
    private double limit(double value, double min, double max){
        return Math.max(min, Math.min(value, max));
    }

    public void update(){
        if (((playerX < -1) || (playerX > 1)) && (speed > Settings.OFF_ROAD_LIMIT)){
            speed = accelerate(speed, Settings.OFF_ROAD_DECEL, dt);
        }

        playerX = limit(playerX, -(Settings.PLAYERX_LIMIT), Settings.PLAYERX_LIMIT);
        speed   = limit(speed, 0, Settings.MAX_SPEED);
        currentLapTime += dt;
    }
    public void offRoad(){
        if (((playerX < -1) || (playerX > 1)) && (speed > Settings.OFF_ROAD_LIMIT))
            speed = accelerate(speed, Settings.OFF_ROAD_DECEL, dt);
    }

    public void resetTime() { currentLapTime = 0; }


    public void pressUp(){
        speed = accelerate(speed, Settings.ACCEL, dt);
    }

    public void pressDown(){
        speed = accelerate(speed, Settings.BREAKING, dt);
        if (speed == 0) {
            steer = 0;
        }
    }

    public void pressLeft(){
        playerX = playerX - dx;
        if (speed > 0){
            steer = -1;
        }
    }

    public void pressRight(){
        playerX = playerX + dx;
        if (speed > 0){
            steer = 1;
        }
    }

    public void idle(){
        speed = accelerate(speed, Settings.DECEL, dt);
        steer = 0;
    }


    private void loadSprites() {
        String playerStraightPath = "../images/sprites/player_straight.png";
        ImageIcon imageIcon = new ImageIcon(Objects.requireNonNull(this.getClass().getResource(playerStraightPath)));
        playerStraight = imageIcon.getImage();

        String playerUpPath = "../images/sprites/player_uphill_straight.png";
        imageIcon = new ImageIcon(Objects.requireNonNull(this.getClass().getResource(playerUpPath)));
        playerUp = imageIcon.getImage();

        String playerLeftPath = "../images/sprites/player_left.png";
        imageIcon = new ImageIcon(Objects.requireNonNull(this.getClass().getResource(playerLeftPath)));
        playerLeft = imageIcon.getImage();

        String playerLeftUpPath = "../images/sprites/player_uphill_left.png";
        imageIcon = new ImageIcon(Objects.requireNonNull(this.getClass().getResource(playerLeftUpPath)));
        playerLeftUp = imageIcon.getImage();

        String playerRightPath = "../images/sprites/player_right.png";
        imageIcon = new ImageIcon(Objects.requireNonNull(this.getClass().getResource(playerRightPath)));
        playerRight = imageIcon.getImage();

        String playerRightUpPath = "../images/sprites/player_uphill_right.png";
        imageIcon = new ImageIcon(Objects.requireNonNull(this.getClass().getResource(playerRightUpPath)));
        playerRightUp = imageIcon.getImage();

        currentPlayerSprite = playerStraight;
    }

    public void renderPlayer(Graphics2D g2D, double updown){
        double bounce =(1.5 * Math.random() * speed/Settings.MAX_SPEED * Settings.SCREEN_WIDTH/Settings.SCREEN_HEIGHT) * rndDouble();

        double scale = Settings.CAMERA_DEPTH/Settings.PLAYER_Z;
        double destX = (double)Settings.SCREEN_WIDTH/2;
        //destY:     (height/2) - (cameraDepth/playerZ * Util.interpolate(playerSegment.p1.camera.y, playerSegment.p2.camera.y, playerPercent) * height/2)
        // TODO remove magic number. implement correct calculation
        double destY = (double)Settings.SCREEN_HEIGHT-6;


        if (steer < 0){
            currentPlayerSprite = playerLeft;
            if(updown > 0){
                currentPlayerSprite = playerLeftUp;
            }
        }
        else if (steer > 0){
                currentPlayerSprite = playerRight;
                if(updown > 0){
                    currentPlayerSprite = playerRightUp;
                }
        }
        else{
            currentPlayerSprite = playerStraight;
            if(updown > 0){
                currentPlayerSprite = playerUp;
            }
        }

        steer = 0;

        sprite.render(g2D, currentPlayerSprite,scale, destX, destY + bounce, -0.5, -1, 0 );
    }

    private double rndDouble(){
        double max = 1d;
        Random rnd = new Random();
        double sign = rnd.nextDouble(2);
        double result = rnd.nextDouble(2);
         if(sign > max/2){
             return result;
         } else {
             return result * -1;
         }
    }

    private double interpolate(double a, double b, double percent){
        return a + (b-a)*percent;
    }


    public double getPosition(){ return position; }
    public double getPlayerX(){ return playerX; }
    public void setPlayerX(double playerX) { this.playerX = playerX; }
    public double getPlayerY() { return playerY; }

    public void setPlayerY(double playerY) { this.playerY = playerY; }
    public int getSpeed(){ return (int)speed; }
    public double getDx() { return dx;}
    // returns time in seconds
    public double getCurrentLapTime() {
        int time = (int)(currentLapTime * 1000);
        return (double)time/1000 ;
    }
    public double getLastLapTime() { return lastLapTime; }
    public void setLastLapTime(double lastLapTime) { this.lastLapTime = lastLapTime; }
    public double getBestLapTime() { return bestLapTime; }
    public void setBestLapTime(double bestLapTime) { this.bestLapTime = bestLapTime; }
    public double getBestEnemyTime() { return bestEnemyTime; }
    public void setBestEnemyTime(double bestEnemyTime) { this.bestEnemyTime = bestEnemyTime; }
    public void addLap() { lap++; }
    public int getLap() { return lap; }
    public int getMaxLaps() { return maxLaps; }
    public void setMaxLaps(int maxLaps) { this.maxLaps = maxLaps; }

}
