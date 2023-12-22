package main.helper;

import static main.constants.Settings.FPS;

public class GameLoopTimer {

    // game loop time variables
    private long now;
    private long last;
    private final double timePerFrame = 1000000000.0 / FPS;

    private double countdown = 0;

    public GameLoopTimer(){
        now = 0;
        last = 0;
    }

    public boolean isReady(){
        now = System.nanoTime();
        if(now - last >= timePerFrame){
            if(countdown >= 0){
                countdown -= timePerFrame;
            }
            last = now;
            return true;
        }
        return false;
    }

    public void startCountdown(){
        countdown = 4000000000.0;
    }

    public int getCountdown(){
        return (int)(countdown/1000000000);
    }
}
