package main.gamehelper;

import static main.constants.Settings.FPS;

/*
 * Time controller for the frames
 */

public class GameLoopTimer {

    // game loop time variables
    private long now;
    private long last;
    private double countdown = 0;

    public GameLoopTimer(){
        now = 0;
        last = System.nanoTime();
    }

    // is ready when 1/FPS sec passed
    public boolean isReady(){
        double timePerFrame = 1000000000.0 / FPS;
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

    // set countdown to 4 sec
    public void startCountdown(){
        countdown = 4000000000.0;
    }

    // get countdown in sec
    public int getCountdown(){
        return (int)(countdown/1000000000);
    }
}
