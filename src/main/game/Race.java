package main.game;

import main.constants.GameMode;
import main.constants.GameState;
import main.gamehelper.Connection;

import static main.constants.GameMode.*;
import static main.constants.GameState.*;
import static main.constants.Settings.*;

public class Race {
    private Connection connection;

    private GameState gameState;
    private int countdown = 4;
    private double currentLapTime = 0;
    private double lastLapTime = 0;
    private double bestLapTime = 0;
    private double bestEnemyTime = 0;

    private final double trackMid;
    private boolean crossedCheckPoint = false;
    private int lap = 1;
    private final int maxLaps;

    private GameMode mode;


    public Race(int maxLaps, double trackLength, Connection connection, GameMode mode){
        this.mode = mode;
        if(mode == MULTI_PLAYER){
            this.connection = connection;
        }

        this.maxLaps = maxLaps;
        this.trackMid = trackLength/2;
    }

    public void update(double playerPosition){
        // lap is finished
        if(isLapFinished(playerPosition)){
            startNewLap();
            // multi player mode
            if(mode == MULTI_PLAYER){
                connection.sendLapTime(getLastLapTime());
            }else{
                // single player mode
                if(bestLapTime == 0){
                    bestLapTime = lastLapTime;
                } else if (lastLapTime < bestLapTime) {
                    bestLapTime = lastLapTime;
                }
            }
        }
        // race is finished
        if(lap > maxLaps){
            if(mode == MULTI_PLAYER){
                connection.sendFinishedRace();
            }
            gameState = RESULT;
        }
        currentLapTime += STEP;
    }

    private boolean isLapFinished(double playerPosition){
        // player crossed checkpoint
        if(playerPosition > trackMid & playerPosition < (trackMid + PLAYER_Z)){
            crossedCheckPoint = true;
        }
        // player crossed checkpoint and finish line
        return crossedCheckPoint && playerPosition < PLAYER_Z;
    }

    private void startNewLap(){
        lastLapTime = currentLapTime;
        currentLapTime = 0;
        crossedCheckPoint = false;
        lap++;
    }

    private double getFormatedTime(double time){
        time = (int)(time * 10000);
        return time/10000 ;
    }

    private double objectToDouble(Object str){
        if (!(str instanceof Number)) {
            return 0.0;
        } else {
            return ((Number) str).doubleValue();
        }
    }

    public GameState getGameState(){
        return gameState;
    }
    public void setGameState(GameState state){
        this.gameState = state;
    }

    // returns time in seconds
    public double getCurrentLapTime() { return getFormatedTime(currentLapTime); }
    public double getLastLapTime() { return getFormatedTime(lastLapTime) ; }
    public double getBestLapTime() { return bestLapTime; }

    public void setBestLapTime(Object bestLapTime) {
        this.bestLapTime = objectToDouble(bestLapTime);
    }
    public double getBestEnemyTime() { return bestEnemyTime; }

    public void setBestEnemyTime(Object bestEnemyTime) {
        this.bestEnemyTime = objectToDouble(bestEnemyTime);
    }
    public int getLap() { return lap; }
    public int getMaxLaps() { return maxLaps; }

    public int getCountdown() {
        return countdown;
    }
    public void setCountdown(Object countdown) {
        if(gameState == COUNTDOWN){
            int result = (int)objectToDouble(countdown);
            if(result <= 0){
                gameState = RUNNING;
            }else{
                this.countdown = result;
            }
        }
    }

}
