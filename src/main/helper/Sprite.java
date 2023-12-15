package main.helper;

import main.constants.SpriteName;

public class Sprite {
    SpriteName name;
    double offset;
    double z;
    double speed;
    double width;

    public Sprite(SpriteName name, double offset, double z, double speed, double width){
        this.name = name;
        this.offset = offset;
        this.z = z;
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

    public double getZ(){
        return z;
    }
    public void setZ(double z){
        this.z = z;
    }

    public double getSpeed(){
        return speed;
    }
    public void setSpeed(double speed){
        this.z = speed;
    }

    public double getWidth(){
        return width;
    }

}
