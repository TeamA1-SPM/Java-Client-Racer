package main.gamehelper;

import main.constants.SpriteName;

public class RoadSideObject {
    SpriteName name;
    double offset;

    double width;

    public RoadSideObject(SpriteName name, double offset, double width){
        this.name = name;
        this.offset = offset;
        this.width = width;
    }

    public SpriteName getName(){
        return name;
    }

    public double getOffset(){
        return offset;
    }
    public void setOffset(double offset){ this.offset = offset; }
    public double getWidth(){
        return width;
    }

}
