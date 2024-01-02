package main.helper;

import main.constants.SpriteName;

import static main.constants.SpriteName.*;

public class EnemyPlayer {
    private final String name;
    private double position = 0;
    private double playerX = 0;
    private double steer = 0;
    private double upDown = 0;

    public EnemyPlayer(String name){
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public double getPosition() {
        return position;
    }
    public void setPosition(Object str) {
        this.position = objectToDouble(str);
    }

    public double getPlayerX() {
        return playerX;
    }
    public void setPlayerX(Object str) {
        this.playerX = objectToDouble(str);
    }

    public void setSteer(Object str) {
        this.steer = objectToDouble(str);
    }

    public void setUpDown(Object str) {
        this.upDown = objectToDouble(str);
    }

    public SpriteName getSpriteName() {
        if(steer < 0){
            if(upDown > 0){
                return PLAYER_UPHILL_LEFT;
            }
            return PLAYER_LEFT;
        }
        if(steer > 0){
            if(upDown > 0){
                return PLAYER_UPHILL_RIGHT;
            }
            return PLAYER_RIGHT;
        }

        if(upDown > 0){
            return PLAYER_UPHILL_STRAIGHT;
        }

        return PLAYER_STRAIGHT;
    }

    private double objectToDouble(Object str){
        if (!(str instanceof Number)) {
            return 0.0;
        } else {
            return ((Number) str).doubleValue();
        }
    }
}
