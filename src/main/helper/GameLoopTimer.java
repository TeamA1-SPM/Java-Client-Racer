package main.helper;

import main.constants.Settings;

public class GameLoopTimer {

    // game loop time variables
    private long now;
    private long last;
    private double gdt;
    private final double step;

    public GameLoopTimer(){
        now = 0;
        last = System.currentTimeMillis();
        gdt = 0;
        step = Settings.STEP;

    }

    public void frameStart(){
        now = System.currentTimeMillis();
        double dt = Math.min(1, (double)(now - last)/ 1000);
        gdt = gdt + dt;
    }

    public void frameFinished(){
        last = now;
    }

    public boolean isGDT(){
        return gdt > step;
    }

    public void updateGDT(){
        gdt = gdt - step;
    }

}
