package main.helper;

import main.constants.SpriteName;

public class Car {
    SpriteName name;
    double offset;
    double position;
    double speed;
    double width;

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
    public void setName(SpriteName name){ this.name = name; }

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
