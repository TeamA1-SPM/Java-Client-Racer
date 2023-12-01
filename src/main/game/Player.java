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

    private Sprite sprite;


    private enum Direction { STRAIGHT, LEFT, RIGHT }
    private Direction currentDirection = Direction.STRAIGHT;
    private boolean isLeftKeyPressed = false;
    private boolean isRightKeyPressed = false;

    // player position variables
    private double position = 0;
    private double playerX = 0;
    private double playerY = 0;
    private double speed = 0;



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

        if (isLeftKeyPressed) {
            adjustCurrentDirection(Direction.LEFT);
        } else if (isRightKeyPressed) {
            adjustCurrentDirection(Direction.RIGHT);
        } else {
            adjustCurrentDirection(Direction.STRAIGHT);
        }
    }

    public void pressDown(){
        speed = accelerate(speed, Settings.BREAKING, dt);

        if (speed == 0) {
            adjustCurrentDirection(Direction.STRAIGHT);
        }
    }

    public void pressLeft(){
        playerX = playerX - dx;
        isLeftKeyPressed = true;
        if(speed > 0){
            adjustCurrentDirection(Direction.LEFT);
        }
    }

    public void pressRight(){
        playerX = playerX + dx;
        isRightKeyPressed = true;
        if(speed > 0){
            adjustCurrentDirection(Direction.RIGHT);
        }
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
        speed = accelerate(speed, Settings.DECEL, dt);

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

        // Render.sprite(  sprites, sprite, scale, destX, destY + bounce, -0.5, -1);
        // player: function( sprites, speedPercent, scale, destX, destY, steer, updown)
        //       Render.player(  sprites, speed/maxSpeed,
        //                    cameraDepth/playerZ,
        //                    width/2,
        //                    (height/2) - (cameraDepth/playerZ * Util.interpolate(playerSegment.p1.camera.y, playerSegment.p2.camera.y, playerPercent) * height/2),
        //                    speed * (keyLeft ? -1 : keyRight ? 1 : 0),
        //                    playerSegment.p2.world.y - playerSegment.p1.world.y);

        // public void render( double destX, double destY, double offsetX, double offsetY, double clipY){

        double scale = Settings.CAMERA_DEPTH/Settings.PLAYER_Z;
        double destX = (double)Settings.SCREEN_WIDTH/2;
        double destY = (double)Settings.SCREEN_HEIGHT/2;

        //sprite.render(g2D, currentPlayerSprite,scale, destX, destY, 0.5, 1, 1 );
        g2D.drawImage(currentPlayerSprite, 430, 550, 180, 120, null);
    }


    public double getPosition(){
        return position;
    }
    public double getPlayerX(){
        return playerX;
    }
    public void setPlayerX(double playerX) { this.playerX = playerX; }
    public double getPlayerY() { return playerY; }

    public void setPlayerY(double playerY) { this.playerY = playerY; }
    public int getSpeed(){
        return (int)speed;
    }
    public double getDx() { return dx;}
    // returns time in seconds
    public double getCurrentLapTime() {
        int time = (int)(currentLapTime * 1000);
        return (double)time/1000 ;
    }
    public double getLastLapTime() {
        return lastLapTime;
    }
    public void setLastLapTime(double lastLapTime) {
        this.lastLapTime = lastLapTime;
    }
    public double getBestLapTime() { return bestLapTime; }
    public void setBestLapTime(double bestLapTime) { this.bestLapTime = bestLapTime; }
    public double getBestEnemyTime() { return bestEnemyTime; }
    public void setBestEnemyTime(double bestEnemyTime) { this.bestEnemyTime = bestEnemyTime; }
    public void addLap() { lap++; }
    public int getLap() { return lap; }
    public int getMaxLaps() { return maxLaps; }
    public void setMaxLaps(int maxLaps) { this.maxLaps = maxLaps; }

}
