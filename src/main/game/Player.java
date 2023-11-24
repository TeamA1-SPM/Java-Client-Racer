package main.game;

import main.constants.Settings;

import javax.swing.*;
import java.awt.*;
import java.util.Objects;

public class Player {

    private enum Direction { STRAIGHT, LEFT, RIGHT }
    private Direction currentDirection = Direction.STRAIGHT;
    private boolean isLeftKeyPressed = false;
    private boolean isRightKeyPressed = false;
    private Image playerStraight;
    private Image playerLeft;
    private Image playerRight;
    private Image currentPlayerSprite;
    private double position = 0;
    private double playerX = 0;
    private double playerZ = Settings.cameraHeight * Settings.cameraDepth;
    private double speed = 0;

    private double maxSpeed = Settings.segmentLength/Settings.STEP;
    private double accel =  maxSpeed/5;
    private double breaking      = -maxSpeed;
    private double decel         = -maxSpeed/5;
    private double offRoadDecel  = -maxSpeed/2;
    private double offRoadLimit  =  maxSpeed/4;

    private double dx;
    private double dt;


    public Player() {
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

    public void offRoad(){
        if (((playerX < -1) || (playerX > 1)) && (speed > offRoadLimit))
            speed = accelerate(speed, offRoadDecel, dt);
    }


    public void xLimit(){
        playerX = limit(playerX, -2, 2);
    }

    public void speedLimit(){
        speed   = limit(speed, 0, maxSpeed);
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

    public void renderPlayer(Graphics2D g2D){
        g2D.drawImage(currentPlayerSprite, 440, 550, 180, 120, null);
    }
}
