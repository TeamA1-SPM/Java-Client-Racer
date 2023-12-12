package main.game;

import java.awt.*;
import java.util.Random;
import main.constants.SpriteName;
import main.helper.Segment;
import main.helper.Sprite;

import static main.constants.Settings.*;
import static main.constants.SpriteName.*;

public class Player {

    // player position variables
    private double position = 0;
    private final double maxPosition;
    private double playerX = 0;
    private double playerY = 0;
    private double speed = 0;
    private int steer = 0;

    private SpriteName spriteName;
    private SpritesLoader spritesLoader;



    private String name;
    private double dx;


    public Player(String name, double maxPosition, SpritesLoader spritesLoader) {
        this.name = name;
        this.maxPosition = maxPosition;
        this.spritesLoader = spritesLoader;
    }

    private double accelerate(double accel){
        return speed + (accel * STEP);
    }
    private double limit(double value, double min, double max){
        return Math.max(min, Math.min(value, max));
    }

    private boolean overlap(double x1, double w1, double x2, double w2, double percent){
        double half = (percent != 0) ? percent / 2 : (double) 1 / 2;
        double min1 = x1 - (w1 * half);
        double max1 = x1 + (w1 * half);
        double min2 = x2 - (w2 * half);
        double max2 = x2 + (w2 * half);

        return !((max1 < min2) || (min1 > max2));
    }

    public void update(Segment playerSegment){
        double speedPercent = speed/MAX_SPEED;
        dx = STEP * 2 * speedPercent;
        position += STEP * speed;

        if(position > maxPosition){
            position -= maxPosition;
        }

        // calc player x position in curves
        playerX = playerX - (dx * speedPercent * playerSegment.getCurve() * CENTRIFUGAL);


        if ((playerX < -1) || (playerX > 1)){

            if(speed > OFF_ROAD_LIMIT){
                speed = accelerate(OFF_ROAD_DECEL);
            }

            for(int n = 0 ; n < playerSegment.getSpriteList().size() ; n++) {
                Sprite sprite  = playerSegment.getSpriteList().get(n);
                double spriteW = spritesLoader.getSpriteWidth(sprite.getName()) * SPRITE_SCALE;
                if (overlap(playerX, PLAYER_W, (sprite.getOffset() + spriteW/2 * (sprite.getOffset() > 0 ? 1 : -1)), spriteW,0.0)) {
                    speed = MAX_SPEED/5;

                    position = playerSegment.getP1World().getZ() - PLAYER_Z;
                    if(position >= maxPosition){
                        position -= maxPosition;
                    }
                    if (position < 0) {
                        position += maxPosition;
                    }
                    break;
                }
            }
        }

        playerX = limit(playerX, -PLAYER_X_LIMIT, PLAYER_X_LIMIT);
        speed   = limit(speed, 0, MAX_SPEED);

        double updown = playerSegment.getP2World().getY() - playerSegment.getP1World().getY();
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

    public void renderPlayer(Graphics2D g2D){

        double scale = CAMERA_DEPTH/PLAYER_Z;
        double destX = (double)SCREEN_WIDTH/2;
        //destY:     (height/2) - (cameraDepth/playerZ * Util.interpolate(playerSegment.p1.camera.y, playerSegment.p2.camera.y, playerPercent) * height/2)
        // TODO remove magic number. implement correct calculation
        double destY = (double)SCREEN_HEIGHT-6;

        spritesLoader.render(g2D, spriteName,scale, destX, destY + calcBounce(), -0.5, -1, 0 );
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
