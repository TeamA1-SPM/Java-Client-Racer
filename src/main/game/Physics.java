package main.game;

import main.helper.Segment;
import main.helper.Sprite;

import java.util.ArrayList;

import static main.constants.Settings.*;

public class Physics {

    private final Player player;
    private final double maxPosition;
    ArrayList<Segment> roadSegments;
    Segment playerSegment;



    public Physics(Player player, ArrayList<Segment> roadSegments){
        this.player = player;
        this.roadSegments = roadSegments;
        this.maxPosition = roadSegments.size() * SEGMENT_LENGTH;
    }

    public void update(){
        int playerIndex = (int) ( ((player.getPosition() + PLAYER_Z)/SEGMENT_LENGTH)%roadSegments.size() );
        this.playerSegment = roadSegments.get(playerIndex);

        // player X position
        playerXPosition();
        // player forward movement
        playerVelocity();
        // road up or down hill check
        playerUpDown();
        // collision with road side objects
        roadSideCollision();
        // collision with other cars
        carCollision();
    }

    // hill up or down check
    private void playerUpDown(){
        player.setUpDown(playerSegment.getP2World().getY() - playerSegment.getP1World().getY());
    }

    // player movement on the x-axis
    private void playerXPosition(){
        double speed = player.getSpeed();
        double speedPercent = speed/MAX_SPEED;

        double playerX = player.getPlayerX();
        double dx = STEP * 2 * speedPercent;

        // player physics in curves
        playerX -= dx * speedPercent * CENTRIFUGAL * playerSegment.getCurve();
        player.setPlayerX(playerX);

        // player left and right movement
        int steer = player.getSteer();
        if(steer == 1){
            player.setPlayerX(playerX + dx);
        }else if(steer == -1){
            player.setPlayerX(playerX - dx);
        }

        // player offroad check
        if ((playerX < -1) || (playerX > 1)) {
            if (speed > OFF_ROAD_LIMIT) {
                player.accelerate(OFF_ROAD_DECEL);
            }
        }
    }

    // player forward movement
    private void playerVelocity(){
        double position = player.getPosition();
        double speed = player.getSpeed();

        // player speed update
        speed += player.getAccel() * STEP;
        player.setSpeed(speed);
        // player position update
        position += speed * STEP;
        player.setPosition(position);
        // reset acceleration
        player.setAccel(0);
    }

    // road side collision check
    private void roadSideCollision(){
        double playerX = player.getPlayerX();

        if ((playerX < -1) || (playerX > 1)){
            ArrayList<Sprite> roadSideSprites = playerSegment.getRoadsideList();
            for(int n = 0; n < roadSideSprites.size() ; n++) {
                Sprite sprite  = roadSideSprites.get(n);
                double spriteW = sprite.getWidth();

                if (overlap(playerX, PLAYER_W, (sprite.getOffset() + spriteW/2 * (sprite.getOffset() > 0 ? 1 : -1)), spriteW,0.0)) {
                    player.setSpeed(MAX_SPEED/5);
                    player.setPosition(playerSegment.getP1World().getZ() - PLAYER_Z);
                    break;
                }
            }
        }
    }

    // npc car collision check
    private void carCollision(){
        ArrayList<Sprite> carList = playerSegment.getCarList();
        for (int i = 0; i < carList.size(); i++){
            Sprite car = carList.get(i);
            if(player.getSpeed() > car.getSpeed()){
                if(overlap(player.getPlayerX(), PLAYER_W, car.getOffset(), car.getWidth(), 0.8)){
                    double carSpeed = car.getSpeed();
                    player.setSpeed(carSpeed * (carSpeed/player.getSpeed()));

                    player.setPosition(car.getPosition() - PLAYER_Z);
                }
            }
        }
    }

    // calculation for sprite overlap
    private boolean overlap(double x1, double w1, double x2, double w2, double percent){
        double half = (percent != 0) ? percent / 2 : (double) 1 / 2;
        double min1 = x1 - (w1 * half);
        double max1 = x1 + (w1 * half);
        double min2 = x2 - (w2 * half);
        double max2 = x2 + (w2 * half);

        return !((max1 < min2) || (min1 > max2));
    }
}
