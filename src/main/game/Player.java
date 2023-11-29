package main.game;

import main.constants.Settings;

import javax.swing.*;
import java.awt.*;
import java.util.Objects;

public class Player {

    // player stripes variables
    private Image playerStraight;
    private Image playerLeft;
    private Image playerRight;
    private Image currentPlayerSprite;

    private enum Direction { STRAIGHT, LEFT, RIGHT }
    private Direction currentDirection = Direction.STRAIGHT;
    private boolean isLeftKeyPressed = false;
    private boolean isRightKeyPressed = false;

    // player position variables
    private double position = 0;
    private double playerX = 0;
    private double playerZ = Settings.cameraHeight * Settings.cameraDepth;

    // player speed variables
    private double speed = 0;
    private double maxSpeed = Settings.segmentLength/Settings.STEP;
    private double accel =  maxSpeed/5;
    private double breaking      = -maxSpeed;
    private double decel         = -maxSpeed/5;
    private double offRoadDecel  = -maxSpeed/2;
    private double offRoadLimit  =  maxSpeed/4;

    private String name;

    // player time variables
    private double currentLapTime = 0;
    private double lastLapTime = 0;
    private double bestLapTime = 0;
    private double bestEnemyTime = 0;

    private int lap = 1;
    private int maxLaps;

    private double dx;
    private double dt;


    public Player(String name) {
        this.name = name;
        loadSprites();

    }

    public void increase(double dt) {
        this.dt = dt;
        dx = dt * 2 * (speed/maxSpeed);
        double increment = dt * speed;
        int max = Settings.trackLength;

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
    public void offRoad(){
        if (((playerX < -1) || (playerX > 1)) && (speed > offRoadLimit))
            speed = accelerate(speed, offRoadDecel, dt);
    }
    public void xLimit(){
        playerX = limit(playerX, -3, 3);
    }
    public void speedLimit(){
        speed   = limit(speed, 0, maxSpeed);
    }
    public void addTime(){
        currentLapTime += dt;
    }


    public void pressUp(){
        speed = accelerate(speed, accel, dt);

        if (isLeftKeyPressed) {
            adjustCurrentDirection(Direction.LEFT);
        } else if (isRightKeyPressed) {
            adjustCurrentDirection(Direction.RIGHT);
        } else {
            adjustCurrentDirection(Direction.STRAIGHT);
        }
    }

    public void pressDown(){
        speed = accelerate(speed, breaking, dt);

        if (speed == 0) {
            adjustCurrentDirection(Direction.STRAIGHT);
        }
    }

    public void pressLeft(){
        playerX = playerX - dx;
        isLeftKeyPressed = true;
        adjustCurrentDirection(Direction.LEFT);
    }

    public void pressRight(){
        playerX = playerX + dx;
        isRightKeyPressed = true;
        adjustCurrentDirection(Direction.RIGHT);
    }

    public void releaseLeft() {
        isLeftKeyPressed = false;
        if (!isRightKeyPressed) {
            adjustCurrentDirection(Direction.STRAIGHT);
        }
    }

    public void releaseRight() {
        isRightKeyPressed = false;
        if (!isLeftKeyPressed) {
            adjustCurrentDirection(Direction.STRAIGHT);
        }
    }
    public void idle(){
        speed = accelerate(speed, decel, dt);

        if (speed == 0) {
            adjustCurrentDirection(Direction.STRAIGHT);
        }
    }

    public void adjustCurrentDirection(Direction newDirection) {
        if (newDirection != currentDirection) {
            switch (newDirection) {
                case STRAIGHT -> currentPlayerSprite = playerStraight;
                case LEFT -> currentPlayerSprite = playerLeft;
                case RIGHT -> currentPlayerSprite = playerRight;
            }
            currentDirection = newDirection;
        }
    }

    private void loadSprites() {
        String playerStraightPath = "../images/sprites/player_straight.png";
        ImageIcon imageIcon = new ImageIcon(Objects.requireNonNull(this.getClass().getResource(playerStraightPath)));
        playerStraight = imageIcon.getImage();

        String playerLeftPath = "../images/sprites/player_left.png";
        imageIcon = new ImageIcon(Objects.requireNonNull(this.getClass().getResource(playerLeftPath)));
        playerLeft = imageIcon.getImage();

        String playerRightPath = "../images/sprites/player_right.png";
        imageIcon = new ImageIcon(Objects.requireNonNull(this.getClass().getResource(playerRightPath)));
        playerRight = imageIcon.getImage();

        currentPlayerSprite = playerStraight;
    }

    public void renderPlayer(Graphics2D g2D){
        g2D.drawImage(currentPlayerSprite, 430, 550, 180, 120, null);
    }


    public double getPosition(){
        return position;
    }
    public double getPlayerX(){
        return playerX;
    }
    public double getPlayerZ(){
        return playerZ;
    }
    public int getSpeed(){
        return (int)speed;
    }
    public double getCurrentLapTime() {
        int time = (int)(currentLapTime * 1000);
        return (double)time/1000 ;
    }
    public void setCurrentLapTime(double currentLapTime) {
        this.currentLapTime = currentLapTime;
    }
    public double getLastLapTime() {
        return lastLapTime;
    }
    public void setLastLapTime(double lastLapTime) {
        this.lastLapTime = lastLapTime;
    }
    public double getBestLapTime() {
        return bestLapTime;
    }
    public void setBestLapTime(double bestLapTime) { this.bestLapTime = bestLapTime; }
    public double getBestEnemyTime() { return bestEnemyTime; }
    public void setBestEnemyTime(double bestEnemyTime) { this.bestEnemyTime = bestEnemyTime; }
    public void addLap() { lap++; }
    public int getLap() { return lap; }
    public int getMaxLaps() { return maxLaps; }
    public void setMaxLaps(int maxLaps) { this.maxLaps = maxLaps; }


}
