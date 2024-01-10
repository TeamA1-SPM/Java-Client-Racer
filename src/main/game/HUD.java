package main.game;

import main.constants.GameMode;
import main.constants.SpriteName;
import main.gamehelper.GameSetup;
import main.gamehelper.InputListener;
import main.gamehelper.Result;

import java.awt.*;
import java.awt.event.KeyEvent;

import static main.constants.Colors.*;
import static main.constants.GameState.END;
import static main.constants.GameState.RUNNING;
import static main.constants.Settings.*;
import static main.constants.SpriteName.*;

/*
 * overlay layer to render on top of the screen
 * - countdown
 * - player stats heads up display
 * - pause menu
 * - result screen
 */
public class HUD {
    private Graphics2D g2D;
    private final SpritesLoader spritesLoader;
    private final int hudHeight = (int) (SCREEN_HEIGHT * 0.12);

    // Element size
    private final int padding = 38;
    private final int elemHeight = hudHeight - 48;

    // information giver
    private final Race race;
    private Result result;
    private final GameSetup setup;

    // Style
    private final Font fontStats = new Font("Universal Light", Font.BOLD, 15);
    private final Font fontNames = new Font("Universal Light", Font.BOLD, 28);

    // menu selected position
    private int pausePos = 1;


    public HUD(GameSetup setup, Race race, SpritesLoader spritesLoader){
        this.setup = setup;
        this.race = race;
        this.spritesLoader = spritesLoader;
    }

    // pause menu navigation
    public void update(InputListener keyListener){

        if(keyListener.isKeyPressed(KeyEvent.VK_ESCAPE)){
            // continue game whe press ESC
            keyListener.keyRelease(KeyEvent.VK_ESCAPE);
            race.setGameState(RUNNING);
        } else if (keyListener.isKeyPressed(KeyEvent.VK_UP)) {
            pausePos = 1;
        } else if (keyListener.isKeyPressed(KeyEvent.VK_DOWN)) {
            pausePos = 2;
        } else if (keyListener.isKeyPressed(KeyEvent.VK_ENTER)) {
            // execute selected option with ENTER
            if(pausePos == 2){
                race.setGameState(END);
            }else{
                race.setGameState(RUNNING);
            }
        }
    }

    // render overlay based on game state
    public void render(Graphics2D g2D, double speed) {
        this.g2D = g2D;

        switch (race.getGameState()) {
            case RUNNING:
                renderStats(race, speed);
                break;
            case COUNTDOWN:
                if(setup.getGameMode() == GameMode.MULTI_PLAYER){
                    drawPlayerNames();
                }
                renderCountdown(race.getCountdown());
                break;
            case RESULT:
                if(result != null){
                    renderResult();
                }
                break;
            case PAUSE:
                renderPauseMenu();
                break;
        }
    }

    // add race result when game is finished
    public void setResult(Result result){ this.result = result; }

    // render countdown numbers in the middle of the screen
    private void renderCountdown(int countdown) {
        SpriteName sprite = switch (countdown) {
            case 2 -> COUNTDOWN_TWO;
            case 1 -> COUNTDOWN_ONE;
            default -> COUNTDOWN_THREE;
        };
        spritesLoader.render(g2D, sprite, 0.0005, SCREEN_WIDTH / 2.0, SCREEN_HEIGHT / 2.0, -0.5, -0.7, 0);
    }

    // draw player names in multiplayer mode
    private void drawPlayerNames(){
        // baseline 1/4 of the screen
        int y = SCREEN_HEIGHT / 4;

        // draw background rectangle
        drawRect(0, y - 100, SCREEN_WIDTH, 120, HUD_BACKGROUND);

        int startX1 = (SCREEN_WIDTH /3) - 60;
        int startX2 = (SCREEN_WIDTH * 2/3);

        // draw text
        g2D.setFont(fontNames);
        g2D.setColor(Color.WHITE);
        g2D.drawString(setup.getPlayerName(), startX1, y);
        g2D.drawString(setup.getEnemyName(), startX2, y);
        g2D.setColor(HUD_GREY);
        g2D.drawString("VS", (SCREEN_WIDTH/2) - 28, SCREEN_HEIGHT / 4);
        g2D.drawString("Track " + setup.getTrackNr(), (SCREEN_WIDTH/2) - 58, (SCREEN_HEIGHT / 4) - 50);
    }

    // draw result overlay
    private void renderResult(){
        int width = 300;
        int height = 400;
        if(setup.getGameMode() == GameMode.MULTI_PLAYER){
            width = 600;
        }

        int xStart = (SCREEN_WIDTH - width) / 2;
        int yStart = (SCREEN_HEIGHT - height)/ 2;

        // draw result background
        drawRect(xStart,yStart, width, height, HUD_BACKGROUND);
        drawRect(xStart + 20, yStart + 65, width - 40, height - 110, HUD_GREY);

        // title
        spritesLoader.render(g2D, RESULT, 0.0005, (double) SCREEN_WIDTH /2, (double) SCREEN_HEIGHT /2, -0.5, -3.9, 0);

        if(result.getPlayerWon() == null){
            // waiting for other player to finish
            spritesLoader.render(g2D, WAITING, 0.0005, (double) SCREEN_WIDTH /2, (double) SCREEN_HEIGHT /2, -0.5, -0.5, 0);
        }else{
            // show result
            if(setup.getGameMode() == GameMode.MULTI_PLAYER){
                renderPlayerResult(result.getPlayerName(), result.getPlayerBestTime(), SCREEN_WIDTH/3, result.getPlayerWon());
                renderPlayerResult(result.getEnemyName(), result.getEnemyBestTime(), (SCREEN_WIDTH * 2)/3, !result.getPlayerWon());
            }else{
                renderPlayerResult(result.getPlayerName(), result.getPlayerBestTime(), SCREEN_WIDTH/2, result.getPlayerWon());
            }

            SpriteName sprite;
            if(result.getPlayerWon()){
                // show player win spite
                sprite = WIN;
            } else {
                // show player lose spite
                sprite = LOSE;
            }
            spritesLoader.render(g2D, sprite, 0.0004, (double) SCREEN_WIDTH /2, (double) SCREEN_HEIGHT /2, -0.5, 2.5, 0);
        }
        // footer
        spritesLoader.render(g2D, PRESS_ENTER, 0.0003, (double) SCREEN_WIDTH /2, (double) SCREEN_HEIGHT /2, -0.5, 5.8, 0);
    }

    // draw result text
    private void renderPlayerResult(String name, double bestTime, int axis, boolean win){
        String bestLapTitle = "best lap:";
        String playerName = name;
        if(playerName == null || playerName.isEmpty()){
            playerName = "PLAYER";
        }
        axis -= (int) (fontNames.getSize() * (playerName.length()/2) * 0.75);

        g2D.setFont(fontNames);
        g2D.setColor(HUD_FONT);
        g2D.drawString(playerName, axis, (SCREEN_HEIGHT /2) - 70);
        g2D.drawString(bestLapTitle, axis, (SCREEN_HEIGHT /2) - 10);
        if(win){
            g2D.setColor(HUD_FONT_GREEN);
        }else{
            g2D.setColor(HUD_FONT_RED);
        }
        g2D.drawString(timeToString(bestTime),axis , (SCREEN_HEIGHT /2) + 40);
    }

    // draw pause menu overlay
    private void renderPauseMenu(){
        drawRect(0,0, SCREEN_WIDTH, SCREEN_HEIGHT, SCREEN_DARK);
        drawPauseSelect(pausePos);
        spritesLoader.render(g2D, PAUSE_MENU, 0.0005, (double) SCREEN_WIDTH / 2, (double) SCREEN_HEIGHT / 2, -0.5, -0.7, 0);
    }

    // draw pause menu select rectangle
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

    // draw player heads up display
    private void renderStats(Race race, double speed) {
        g2D.setFont(fontStats);
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
    private void drawBackground() {
        // HUD size
        drawRect(0, 0, SCREEN_WIDTH, hudHeight, HUD_BACKGROUND);
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

    // time format double seconds to min.sec.mil
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
        g2D.drawString(text, startX, hudHeight / 2 + 20);
    }
}
