package main.game;

import main.constants.GameMode;
import main.constants.SpriteName;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import static main.constants.Colors.*;
import static main.constants.Settings.*;

public class HUD {

    private final Font font = new Font("Universal Light", Font.BOLD, 14);
    // HUD size
    private final int hudWidth = SCREEN_WIDTH;
    private final int hudHeight = (int) (SCREEN_HEIGHT * 0.12);
    private Graphics2D g2D;
    // Element size
    // TODO dynamic draw, remove all magic numbers and add scale for each element
    private final int padding = 38;

    private final int elemHeight = hudHeight - 48;

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
                //TODO render result screen
                break;
            case PAUSE:
                int xMid = SCREEN_WIDTH/2;
                int yMid = SCREEN_HEIGHT/2;

                int xStart = xMid - (yMid/2);
                int yStart = yMid - (yMid/2);
                drawRect(xStart, yStart, yMid, yMid, HUD_BACKGROUND);
                break;
        }
    }

    private void renderCountdown(int countdown, SpritesLoader spritesLoader) {
        switch (countdown) {
            case 3:
                spritesLoader.render(g2D, SpriteName.COUNTDOWN_THREE, 0.0005, (double) SCREEN_WIDTH / 2, (double) SCREEN_HEIGHT / 2, -0.5, -0.7, 0);
                break;
            case 2:
                spritesLoader.render(g2D, SpriteName.COUNTDOWN_TWO, 0.0005, (double) SCREEN_WIDTH / 2, (double) SCREEN_HEIGHT / 2, -0.5, -0.7, 0);
                break;
            case 1:
                spritesLoader.render(g2D, SpriteName.COUNTDOWN_ONE, 0.0005, (double) SCREEN_WIDTH / 2, (double) SCREEN_HEIGHT / 2, -0.5, -0.7, 0);
                break;
        }
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
        if(race.getMode() == GameMode.MULTI_PLAYER){
            drawEnemyLap(race.getBestEnemyTime());
        }
        // Speed element
        drawSpeed(speed);
        // Lap counter element
        drawRound(race.getLap(), race.getMaxLaps());
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
