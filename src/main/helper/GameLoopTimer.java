package main.helper;

import main.constants.Settings;

import static main.constants.Settings.FPS;

public class GameLoopTimer {

    // game loop time variables
    private long now;
    private long last;
    private final double timePerFrame = 1000000000.0 / FPS;

    public GameLoopTimer(){
        now = 0;
        last = 0;
    }

    public boolean isReady(){
        now = System.nanoTime();
        if(now - last >= timePerFrame){
            last = now;
            return true;
        }
        return false;
    }
}
