package main.game;

import java.awt.*;
import java.util.Random;
import main.constants.SpriteName;

import static main.constants.Settings.*;
import static main.constants.SpriteName.*;

public class Player {

    // player position variables
    private double position = 0;
    private double maxPosition;
    private double playerX = 0;
    private double playerY = 0;
    private double speed = 0;
    private int steer = 0;

    private SpriteName spriteName;



    private String name;
    private double dx;


    public Player(String name, double maxPosition) {
        this.name = name;
        this.maxPosition = maxPosition;
    }

    private void offRoad(){
        if (((playerX < -1) || (playerX > 1)) && (speed > OFF_ROAD_LIMIT))
            speed = accelerate(OFF_ROAD_DECEL);
    }
    private double accelerate(double accel){
        return speed + (accel * STEP);
    }
    private double limit(double value, double min, double max){
        return Math.max(min, Math.min(value, max));
    }

    public void update(double curve, double updown){
        double speedPercent = speed/MAX_SPEED;
        dx = STEP * 2 * speedPercent;
        position += STEP * speed;

        if(position > maxPosition){
            position -= maxPosition;
        }

        // calc player x position in curves
        playerX = playerX - (dx * speedPercent * curve * CENTRIFUGAL);

        offRoad();
        playerX = limit(playerX, -PLAYERX_LIMIT, PLAYERX_LIMIT);
        speed   = limit(speed, 0, MAX_SPEED);


        spriteName = getPlayerSpriteName(updown);
    }

    public void pressUp(){
        speed = accelerate(ACCEL);
    }

    public void pressDown(){
        if(speed > 0){
            speed = accelerate(BREAKING);
        } else {
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
        if(speed > 0){
            speed = accelerate(DECEL);
        }
    }

    public void renderPlayer(Graphics2D g2D, SpritesLoader sprites){

        double scale = CAMERA_DEPTH/PLAYER_Z;
        double destX = (double)SCREEN_WIDTH/2;
        //destY:     (height/2) - (cameraDepth/playerZ * Util.interpolate(playerSegment.p1.camera.y, playerSegment.p2.camera.y, playerPercent) * height/2)
        // TODO remove magic number. implement correct calculation
        double destY = (double)SCREEN_HEIGHT-6;

        sprites.render(g2D, spriteName,scale, destX, destY + calcBounce(), -0.5, -1, 0 );
    }

    private double calcBounce(){
        Random rnd = new Random();
        double sign = rnd.nextInt(2);
        double rndDouble = rnd.nextDouble(1);

        if(sign > 0){
            rndDouble *= -1;
        }

        return (1.5 * Math.random() * speed/MAX_SPEED * SCREEN_WIDTH/SCREEN_HEIGHT) * rndDouble;
    }

    private SpriteName getPlayerSpriteName(double updown) {
        SpriteName sprite;

        if (steer < 0){
            sprite = PLAYER_LEFT;
            if(updown > 0){
                sprite = PLAYER_UPHILL_LEFT;
            }
        }
        else if (steer > 0){
            sprite = PLAYER_RIGHT;
                if(updown > 0){
                    sprite = PLAYER_UPHILL_RIGHT;
                }
        }
        else{
            sprite = PLAYER_STRAIGHT;
            if(updown > 0){
                sprite = PLAYER_UPHILL_STRAIGHT;
            }
        }
        steer = 0;
        return sprite;
    }

    public double getPosition(){ return position; }
    public double getPlayerX(){ return playerX; }
    public double getPlayerY() { return playerY; }
    public void setPlayerY(double playerY) { this.playerY = playerY; }
    public int getSpeed(){ return (int)speed; }

}
