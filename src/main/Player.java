package main;

import java.awt.*;

public class Player {
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
    }

    public void pressDown(){
        speed = accelerate(speed, breaking, dt);
    }

    public void pressLeft(){
        playerX = playerX - dx;
    }

    public void pressRight(){
        playerX = playerX + dx;
    }

    public void idle(){
        speed = accelerate(speed, decel, dt);
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

    public void renderPlayer(Graphics2D g2D){

        //TODO render player
    }

}
