package main.gamehelper;

import main.constants.SpriteName;

public class Sprite {
    SpriteName name;
    double offset;
    double position;
    double speed;
    double width;

    public Sprite(SpriteName name, double offset, double position, double speed, double width){
        this.name = name;
        this.offset = offset;
        this.position = position;
        this.speed = speed;
        this.width = width;
    }

    public SpriteName getName(){
        return name;
    }

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
    public void setSpeed(double speed){
        this.position = speed;
    }

    public double getWidth(){
        return width;
    }

}
