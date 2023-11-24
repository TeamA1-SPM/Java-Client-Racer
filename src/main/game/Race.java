package main.game;

import main.helper.Timer;

public class Race {

    private Timer playerTimer;

    private double[] lapTimes;
    private int lap = 1;
    private int laps;

    public Race(int laps){
        this.laps = laps;
        this.lapTimes = new double[laps];
        this.playerTimer = new Timer();
    }

    public void start(){
        playerTimer.startTimer();
    }

    public boolean addLap(){
        if(lap <= laps){
            lapTimes[lap-1] = playerTimer.timeReset();
            // DEBUG
            System.out.println("Lap:" + lap + "\tTime " + lapTimes[lap-1] + " sec");

            lap++;

            return true;
        }
        return false;
    }

}
