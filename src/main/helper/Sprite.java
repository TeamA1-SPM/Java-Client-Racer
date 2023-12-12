package main.helper;

import main.constants.SpriteName;

public class Sprite {
    SpriteName name;
    double offset;

    public Sprite(SpriteName name, double offset){
        this.name = name;
        this.offset = offset;
    }

    public SpriteName getName(){
        return name;
    }

    public double getOffset(){
        return offset;
    }

}
