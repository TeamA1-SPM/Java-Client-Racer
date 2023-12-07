package main.helper;

import main.constants.SpriteName;

public class SpriteCoordinates {
    SpriteName name;

    int x;
    int y;
    int w;
    int h;

    public SpriteCoordinates(int x, int y, int w, int h){
        this.x = x;
        this.y = y;
        this.w = w;
        this.h = h;
    }

    public int getX(){
        return x;
    }
    public int getY(){
        return y;
    }
    public int getW(){
        return w;
    }
    public int getH(){
        return h;
    }


}
