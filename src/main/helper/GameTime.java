package main.helper;

public class GameTime {

    private long timeBegin = 0;
    private boolean isRunning = false;

    public void start(){
        if(!isRunning){
            isRunning = true;
            timeBegin = System.currentTimeMillis();
        }
    }

    public void reset(){
        if(isRunning){
            timeBegin = System.currentTimeMillis();
        }
    }

    public double getTime(){
        if(isRunning){
            return (System.currentTimeMillis() - timeBegin) / 1000.0;
        }
        return 0;
    }

}
