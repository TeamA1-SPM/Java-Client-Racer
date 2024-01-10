package main.gamehelper;

import main.constants.SpriteName;

/*
 * npc car
 * used by simulation
 * is rendered by Road
 */

public class Car {
    private SpriteName name;
    private double offset;
    private double position;
    private final double speed;
    private final double width;

    public Car(SpriteName name, double offset, double position, double speed, double width){
        this.name = name;
        this.offset = offset;
        this.position = position;
        this.speed = speed;
        this.width = width;
    }

    public SpriteName getName(){
        return name;
    }
    public void setSpriteName(SpriteName name){ this.name = name; }

    public double getOffset(){
        return offset;
    }
    public void setOffset(double offset){ this.offset = offset; }

    public double getPosition(){
        return position;
    }
    public void setPosition(double position){
        this.position = position;
    }

    public double getSpeed(){
        return speed;
    }

    public double getWidth(){
        return width;
    }
}
