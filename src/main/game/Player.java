package main.game;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.Random;
import main.constants.SpriteName;
import main.gamehelper.InputListener;

import static main.constants.Settings.*;
import static main.constants.SpriteName.*;

public class Player {

    private double position = 0;
    private final double maxPosition;
    private double playerX = 0;
    private double speed = 0;
    private double accel = 0;
    private int steer = 0;
    private double upDown = 0;
    private final SpritesLoader spritesLoader;

    public Player(double maxPosition, SpritesLoader spritesLoader) {
        this.maxPosition = maxPosition;
        this.spritesLoader = spritesLoader;
    }

    public void update(InputListener keyListener){

        // Player control actions left and right
        if (keyListener.isKeyPressed(KeyEvent.VK_LEFT)) {
            if (speed > 0){
                steer = -1;
            }
        }else if (keyListener.isKeyPressed(KeyEvent.VK_RIGHT)) {
            if (speed > 0){
                steer = 1;
            }
        }
        // Player control actions up and down
        if (keyListener.isKeyPressed(KeyEvent.VK_UP)) {
            accel = ACCEL;
        } else if (keyListener.isKeyPressed(KeyEvent.VK_DOWN)) {
            if(speed > 0){
                accel = BREAKING;
            } else {
                steer = 0;
            }
        } else {
            // Player not pressing up or down
            if(speed > 0){
                accel = DECEL;
            }
        }

    }

    public void renderPlayer(Graphics2D g2D){
        double scale = CAMERA_DEPTH/PLAYER_Z;
        double destX = (double)SCREEN_WIDTH/2;
        double destY = (double)SCREEN_HEIGHT-7;

        spritesLoader.render(g2D, getPlayerSpriteName(), scale, destX, destY + calcBounce(), -0.5, -1, 0 );
    }

    private double calcBounce(){
        Random rnd = new Random();
        double rndDouble = rnd.nextDouble(1);

        double sign = rnd.nextInt(2);
        if(sign > 0){
            rndDouble *= -1;
        }

        return (1.5 * Math.random() * speed/MAX_SPEED * SCREEN_WIDTH/SCREEN_HEIGHT) * rndDouble;
    }

    // @return sprite name based on player direction
    private SpriteName getPlayerSpriteName() {
        SpriteName sprite;

        if (steer < 0){
            sprite = PLAYER_LEFT;
            if(upDown > 0){
                sprite = PLAYER_UPHILL_LEFT;
            }
        }
        else if (steer > 0){
            sprite = PLAYER_RIGHT;
                if(upDown > 0){
                    sprite = PLAYER_UPHILL_RIGHT;
                }
        }
        else{
            sprite = PLAYER_STRAIGHT;
            if(upDown > 0){
                sprite = PLAYER_UPHILL_STRAIGHT;
            }
        }
        steer = 0;
        return sprite;
    }

    public double getPosition(){ return position; }
    public void setPosition(double position){
        if(position >= maxPosition){
            this.position = position - maxPosition;
        }else if (position < 0) {
            this.position = position + maxPosition;
        }else{
            this.position = position;
        }
    }
    public double getPlayerX(){ return playerX; }
    public void setPlayerX(double playerX){
        if(playerX >= -PLAYER_X_LIMIT && playerX <= PLAYER_X_LIMIT){
            this.playerX = playerX;
        }
    }

    public double getSpeed(){ return speed; }
    public void setSpeed(double speed){
        if(speed > MAX_SPEED){
            speed = MAX_SPEED;
        }else if(speed < 0){
            speed = 0;
        }
        this.speed = speed;
    }

    public double getAccel(){ return accel; }
    public void setAccel(double accel){ this.accel = accel; }

    public int getSteer(){ return steer; }

    public void setUpDown(double upDown){ this.upDown = upDown; }
    public double getUpDown(){
        return upDown;
    }

}
