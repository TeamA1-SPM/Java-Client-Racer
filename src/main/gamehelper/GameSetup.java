package main.gamehelper;

import main.constants.GameMode;

/*
 * Interface object between menu and game
 * - game mode
 * - track number
 * - laps
 * - player name
 * multiplayer
 * - enemy player name
 * - player number (player1 or player2)
 */
public class GameSetup {
    private final GameMode mode;
    private final int trackNr;
    private final int laps;
    private final String playerName;
    private String enemyName;
    private String playerNumber;


    public GameSetup(GameMode mode, int trackNr, int laps, String playerName){
        this.mode = mode;
        this.trackNr = trackNr;
        this.laps = laps;
        this.playerName = playerName;
    }

    public GameMode getGameMode(){
        return mode;
    }

    public int getTrackNr(){
        return trackNr;
    }

    public int getLaps(){
        return laps;
    }

    public String getPlayerName(){
        return playerName;
    }

    public String getEnemyName(){
        return enemyName;
    }

    // @return player start x position
    public double getStartPosition(){
        if(mode == GameMode.MULTI_PLAYER){
            if(playerNumber.equals("player1")){
                return -0.5;
            }
            else if(playerNumber.equals("player2")){
                return 0.5;
            }
        }
        return 0;
    }

    // set parameters for a multiplayer game
    public void setMultiplayerParameters(String enemyName, String playerNumber){
        this.enemyName = enemyName;
        this.playerNumber = playerNumber;
    }

}
