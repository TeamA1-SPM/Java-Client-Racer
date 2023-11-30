package main.game;

import main.constants.Colors;
import main.constants.Settings;

import java.awt.*;

public class HUD {

    private Graphics2D g2D;
    private final Font font = new Font("Universal Light", Font.BOLD, 14);
    // HUD size
    private final int hudWidth = Settings.SCREEN_WIDTH;
    private final int hudHeight = (int)(Settings.SCREEN_HEIGHT * 0.12);

    // Element size
    // TODO dynamic draw, remove all magic numbers and add scale for each element
    private int pedding = 38;

    private int elemHeight = hudHeight - 48;

    public void render(Graphics2D g2D,Player player){
        this.g2D = g2D;

        g2D.setFont(font);
        // Background Rectangle
        drawBackground();
        // Lap time element
        drawTime(player.getCurrentLapTime());
        // Last lap element
        drawLastLap(player.getLastLapTime());
        // best lap element
        drawFastestLap(player.getBestLapTime());
        // Enemy best lap element
        drawEnemyLap(player.getBestEnemyTime());
        // Speed element
        drawSpeed(player.getSpeed());
        // Lap counter element
        drawRound(player.getLap(),player.getMaxLaps());
    }


    private void drawBackground(){
        drawRect(0, 0, hudWidth, hudHeight, Colors.HUD_BACKGROUND);
    }

    private void drawTime(double time){
        drawRect(15, pedding, 120, elemHeight, Colors.HUD_GREY);

        String text = "Time: " + timeToString(time);
        drawText(20, text);
    }

    private void drawLastLap(double time){
        drawRect(145, pedding, 180, elemHeight, Colors.HUD_YELLOW);

        String text = "Last Lap: " + timeToString(time);
        drawText(150, text);
    }

    private void drawFastestLap(double time){
        drawRect(335, pedding, 180, elemHeight, Colors.HUD_YELLOW);

        String text = "Fastest Lap: " + timeToString(time);
        drawText(340, text);
    }

    private void drawEnemyLap(double time){
        drawRect(525, pedding, 180, elemHeight, Colors.HUD_YELLOW);

        String text = "Enemy Lap: " + timeToString(time);
        drawText(530, text);
    }

    private void drawSpeed(double speed){
        drawRect(715, pedding, 120, elemHeight, Colors.HUD_GREY);

        String text = "MPH: " + (int)(speed/100);
        drawText(720, text);
    }

    private void drawRound(int round, int maxRounds){
        drawRect(845, pedding, 120, elemHeight, Colors.HUD_GREY);

        String text = "Round: " + round + " / " + maxRounds;
        drawText(850, text);
    }

    // time format
    private String timeToString(double time){
        String result = "";
        int min = (int)(time/60);

        time = time%60;

        int sec = (int)time;
        time = time%1;
        int mil = (int)(time * 1000);

        if(min > 0){
            result += min + ".";
        }
        return result + sec + "." + mil;
    }

    // draws a rectangle with frame
    private void drawRect(int startX, int startY, int width, int height, Color color){
        g2D.setColor(color);
        g2D.fillRect(startX, startY, width, height);
        g2D.setColor(Colors.HUD_FRAME);
        g2D.drawRect(startX, startY, width, height);
    }

    // draws text
    private void drawText(int startX, String text){
        g2D.setColor(Colors.HUD_FONT);
        // draws text in the middle of the hud
        // TODO add offset remove magic number 20
        g2D.drawString(text, startX, hudHeight/2 + 20);
    }

}
