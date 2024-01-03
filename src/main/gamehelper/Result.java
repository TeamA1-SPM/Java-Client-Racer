package main.gamehelper;

public class Result {
    private String playerName;
    private double playerBestTime;
    private Boolean playerWon = null;
    private String enemyName;
    private double enemyBestTime;

    public Result(String playerName){
        this.playerName = playerName;
    }

    public String getPlayerName(){
        return playerName;
    }

    public void setEnemyName(String enemyName){
        this.enemyName = enemyName;
    }
    public String getEnemyName(){
        return enemyName;
    }

    public void setPlayerBestTime(Object time){
        this.playerBestTime = objectToDouble(time);
    }
    public double getPlayerBestTime(){
        return playerBestTime;
    }


    public void setEnemyBestTime(Object time){
        this.enemyBestTime = objectToDouble(time);
    }
    public double getEnemyBestTime(){
        return enemyBestTime;
    }

    public void setPlayerWon(boolean playerWon){
        this.playerWon = playerWon;
    }
    public Boolean getPlayerWon() {
        return playerWon;
    }

    private double objectToDouble(Object str){
        if (!(str instanceof Number)) {
            return 0.0;
        } else {
            return ((Number) str).doubleValue();
        }
    }

}
