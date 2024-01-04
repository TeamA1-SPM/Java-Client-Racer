package main.game;

import main.constants.GameMode;
import main.gamehelper.GameSetup;
import main.gamehelper.Result;

import java.awt.*;

import static main.constants.Colors.*;
import static main.constants.Settings.*;
import static main.constants.SpriteName.*;

public class HUD {

    private final Font font = new Font("Universal Light", Font.BOLD, 15);
    // HUD size
    private final int hudWidth = SCREEN_WIDTH;
    private final int hudHeight = (int) (SCREEN_HEIGHT * 0.12);
    private Graphics2D g2D;
    // Element size
    // TODO dynamic draw, remove all magic numbers and add scale for each element
    private final int padding = 38;
    private final int elemHeight = hudHeight - 48;
    private int pausePos = 1;
    private Result result;
    private GameSetup setup;


    public HUD(GameSetup setup){
        this.setup = setup;
    }

    public void render(Graphics2D g2D, Race race, double speed, SpritesLoader spritesLoader) {
        this.g2D = g2D;

        switch (race.getGameState()) {
            case RUNNING:
                renderStats(race, speed);
                break;
            case COUNTDOWN:
                renderCountdown(race.getCountdown(), spritesLoader);
                break;
            case RESULT:
                if(result != null){
                    renderResult(spritesLoader);
                }
                break;
            case PAUSE:
                renderPauseMenu(spritesLoader);
                break;
        }
    }


    public void setResult(Result result){ this.result = result; }
    public void setPausePos(int pos){
        this.pausePos = pos;
    }
    public int getPausePos(){
        return pausePos;
    }


    private void renderCountdown(int countdown, SpritesLoader spritesLoader) {

        if(setup.getGameMode() == GameMode.MULTI_PLAYER){

            int startX1 = (SCREEN_WIDTH/3);
            int startX2 = (SCREEN_WIDTH * 2/3);
            int y = SCREEN_HEIGHT / 4;

            drawRect(0, y - 40, SCREEN_WIDTH, 60, HUD_BACKGROUND);

            int fontSize = 28;
            Font font = new Font("Universal Light", Font.BOLD, fontSize);

            g2D.setFont(font);
            g2D.setColor(Color.WHITE);

            String player1 = setup.getPlayerName();
            String player2 = setup.getEnemyName();

            g2D.drawString(player1, startX1, y);
            g2D.drawString(player2, startX2, y);
            g2D.drawString("VS", SCREEN_WIDTH/2, y);
        }

        switch (countdown) {
            case 3:
                spritesLoader.render(g2D, COUNTDOWN_THREE, 0.0005, (double) SCREEN_WIDTH / 2, (double) SCREEN_HEIGHT / 2, -0.5, -0.7, 0);
                break;
            case 2:
                spritesLoader.render(g2D, COUNTDOWN_TWO, 0.0005, (double) SCREEN_WIDTH / 2, (double) SCREEN_HEIGHT / 2, -0.5, -0.7, 0);
                break;
            case 1:
                spritesLoader.render(g2D, COUNTDOWN_ONE, 0.0005, (double) SCREEN_WIDTH / 2, (double) SCREEN_HEIGHT / 2, -0.5, -0.7, 0);
                break;
        }
    }

    private void renderResult(SpritesLoader spritesLoader){
        int width = 300;
        int height = 400;

        if(setup.getGameMode() == GameMode.MULTI_PLAYER){
            width = 600;
        }

        int xStart = (SCREEN_WIDTH - width) / 2;
        int yStart = (SCREEN_HEIGHT - height)/ 2;

        drawRect(xStart,yStart, width, height, HUD_BACKGROUND);
        drawRect(xStart + 20, yStart + 65, width - 40, height - 110, HUD_GREY);

        // title
        spritesLoader.render(g2D, RESULT, 0.0005, (double) SCREEN_WIDTH /2, (double) SCREEN_HEIGHT /2, -0.5, -3.9, 0);

        if(result.getPlayerWon() == null){
            // waiting for other player to finish
            spritesLoader.render(g2D, WAITING, 0.0005, (double) SCREEN_WIDTH /2, (double) SCREEN_HEIGHT /2, -0.5, -0.5, 0);
        }else{
            if(setup.getGameMode() == GameMode.MULTI_PLAYER){
                renderPlayerResult(result.getPlayerName(), result.getPlayerBestTime(), SCREEN_WIDTH/3, result.getPlayerWon());
                renderPlayerResult(result.getEnemyName(), result.getEnemyBestTime(), (SCREEN_WIDTH * 2)/3, !result.getPlayerWon());
            }else{
                renderPlayerResult(result.getPlayerName(), result.getPlayerBestTime(), SCREEN_WIDTH/2, result.getPlayerWon());
            }

            if(result.getPlayerWon()){
                // show player win
                spritesLoader.render(g2D, WIN, 0.0004, (double) SCREEN_WIDTH /2, (double) SCREEN_HEIGHT /2, -0.5, 2.5, 0);
            } else if (!result.getPlayerWon()) {
                // show player lose
                spritesLoader.render(g2D, LOSE, 0.0004, (double) SCREEN_WIDTH /2, (double) SCREEN_HEIGHT /2, -0.5, 2.5, 0);
            }
        }

        // footer
        spritesLoader.render(g2D, PRESS_ENTER, 0.0003, (double) SCREEN_WIDTH /2, (double) SCREEN_HEIGHT /2, -0.5, 5.8, 0);
    }

    private void renderPlayerResult(String name, double bestTime, int axis, boolean win){
        int fontSize = 28;
        Font font = new Font("Universal Light", Font.BOLD, fontSize);
        g2D.setFont(font);
        g2D.setColor(HUD_FONT);

        String bestLapTitle = "best lap:";
        String playerName = name;
        if(playerName == null || playerName.isEmpty()){
            playerName = "PLAYER";
        }

        axis -= (int) (fontSize * (playerName.length()/2) * 0.75);

        g2D.drawString(playerName, axis, (SCREEN_HEIGHT /2) - 70);
        g2D.drawString(bestLapTitle, axis, (SCREEN_HEIGHT /2) - 10);
        if(win){
            g2D.setColor(HUD_FONT_GREEN);
        }else{
            g2D.setColor(HUD_FONT_RED);
        }
        g2D.drawString(timeToString(bestTime),axis , (SCREEN_HEIGHT /2) + 40);
    }

    private void renderStats(Race race, double speed) {
        g2D.setFont(font);
        // Background Rectangle
        drawBackground();
        // Lap time element
        drawTime(race.getCurrentLapTime());
        // Last lap element
        drawLastLap(race.getLastLapTime());
        // best lap element
        drawFastestLap(race.getBestLapTime());
        // Enemy best lap element
        if(setup.getGameMode() == GameMode.MULTI_PLAYER){
            drawEnemyLap(race.getBestEnemyTime());
        }
        // Speed element
        drawSpeed(speed);
        // Lap counter element
        drawRound(race.getLap(), race.getMaxLaps());
    }

    private void renderPauseMenu(SpritesLoader spritesLoader){
        drawRect(0,0, SCREEN_WIDTH, SCREEN_HEIGHT, SCREEN_DARK);
        drawPauseSelect(pausePos);
        spritesLoader.render(g2D, PAUSE_MENU, 0.0005, (double) SCREEN_WIDTH / 2, (double) SCREEN_HEIGHT / 2, -0.5, -0.7, 0);
    }

    private void drawPauseSelect(int pos){
        int xMid = SCREEN_WIDTH/2;
        int yMid = SCREEN_HEIGHT/2;
        int offset = 50;

        int xStart = xMid - yMid/2;
        int yStart = yMid;
        if(pos == 2){
            yStart += 12;
        }else{
            yStart -= 50;
        }

        drawRect(xStart+offset, yStart, yMid-(2*offset), 50, HUD_BACKGROUND);
    }

    private void drawBackground() {
        drawRect(0, 0, hudWidth, hudHeight, HUD_BACKGROUND);
    }

    private void drawTime(double time) {
        drawRect(15, padding, 120, elemHeight, HUD_GREY);

        String text = "Time: " + timeToString(time);
        drawText(20, text);
    }

    private void drawLastLap(double time) {
        drawRect(145, padding, 180, elemHeight, HUD_YELLOW);

        String text = "Last Lap: " + timeToString(time);
        drawText(150, text);
    }

    private void drawFastestLap(double time) {
        drawRect(335, padding, 180, elemHeight, HUD_YELLOW);

        String text = "Fastest Lap: " + timeToString(time);
        drawText(340, text);
    }

    private void drawEnemyLap(double time) {
        drawRect(525, padding, 180, elemHeight, HUD_YELLOW);

        String text = "Enemy Lap: " + timeToString(time);
        drawText(530, text);
    }

    private void drawSpeed(double speed) {
        drawRect(715, padding, 120, elemHeight, HUD_GREY);

        String text = "MPH: " + (int) (speed / 100);
        drawText(720, text);
    }

    private void drawRound(int round, int maxRounds) {
        drawRect(845, padding, 120, elemHeight, HUD_GREY);

        String text = "Round: " + round + " / " + maxRounds;
        drawText(850, text);
    }

    // time format
    private String timeToString(double time) {
        String result = "";
        int min = (int) (time / 60);

        time = time % 60;

        int sec = (int) time;
        time = time % 1;
        int mil = (int) (time * 1000);

        if (min > 0) {
            result += min + ".";
        }
        return result + sec + "." + mil;
    }

    // draws a rectangle with frame
    private void drawRect(int startX, int startY, int width, int height, Color color) {
        g2D.setColor(color);
        g2D.fillRect(startX, startY, width, height);
        g2D.setColor(HUD_FRAME);
        g2D.drawRect(startX, startY, width, height);
    }

    // draws text
    private void drawText(int startX, String text) {
        g2D.setColor(HUD_FONT);
        // draws text in the middle of the hud
        // TODO add offset remove magic number 20. Title overlap problem.
        g2D.drawString(text, startX, hudHeight / 2 + 20);
    }
}
