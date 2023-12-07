package main.game;

import static main.constants.Settings.PLAYER_Z;
import static main.constants.Settings.STEP;

public class Race {
    private double currentLapTime = 0;
    private double lastLapTime = 0;
    private double bestLapTime = 0;
    private double bestEnemyTime = 0;

    private final double trackMid;
    private boolean crossedCheckPoint = false;
    private int lap = 1;
    private final int maxLaps;


    public Race(int maxLaps, double trackLength){
        this.maxLaps = maxLaps;
        this.trackMid = trackLength/2;
    }

    public boolean isLapFinished(double position){
        currentLapTime += STEP;

        // player crossed checkpoint
        if(position > trackMid & position < trackMid + PLAYER_Z){
            crossedCheckPoint = true;
        }

        // player crossed checkpoint and finish line
        if(crossedCheckPoint & position < PLAYER_Z){

            lastLapTime = currentLapTime;
            lap++;
            currentLapTime = 0;
            crossedCheckPoint = false;
            return true;
        }

        return false;
    }

    public boolean isFinished(){
        return lap > maxLaps;
    }

    private double getFormatedTime(double time){
        time = (int)(time * 1000);
        return time/1000 ;
    }

    // returns time in seconds
    public double getCurrentLapTime() { return getFormatedTime(currentLapTime); }
    public double getLastLapTime() { return getFormatedTime(lastLapTime) ; }
    public double getBestLapTime() { return bestLapTime; }
    public void setBestLapTime(double bestLapTime) { this.bestLapTime = bestLapTime; }
    public double getBestEnemyTime() { return bestEnemyTime; }
    public void setBestEnemyTime(double bestEnemyTime) { this.bestEnemyTime = bestEnemyTime; }
    public int getLap() { return lap; }
    public int getMaxLaps() { return maxLaps; }
}
