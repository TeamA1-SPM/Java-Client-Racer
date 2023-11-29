package main.helper;

public class Timer {

    private long timeBegin = 0;
    private boolean isRunning = false;

    public void startTimer(){
        if(!isRunning){
            isRunning = true;
            timeBegin = System.currentTimeMillis();
        }
    }

    public double timeReset(){
        if(isRunning){
            double seconds = (double)(System.currentTimeMillis() - timeBegin) / 1000;
            timeBegin = System.currentTimeMillis();
            return seconds;
        }
        return 0;
    }

    public long getTime(){
        if(isRunning){
            return (System.currentTimeMillis() - timeBegin) / 1000;
        }
        return 0;
    }


}
