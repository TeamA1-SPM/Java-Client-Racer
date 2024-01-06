package main.menuhelper;

import java.awt.*;

import static main.menu.MainMenu.*;
import static main.menuhelper.FontManager.FontSize30;
import static main.menuhelper.FontManager.FontSize35;

public class TableManager {

    // Creates the leaderboard
    public static void drawTable(Graphics g) {
        if (lbBtnPressed) {

            drawRect(g, 275, 175, 450, 450, Color.BLACK);

            Graphics2D g2 = (Graphics2D) g;
            float lineWidth = 4.0f;
            g2.setStroke(new BasicStroke(lineWidth));

            // Horizontal lines
            g.drawLine(275, 175, 725, 175);
            g.drawLine(275, 225, 725, 225);
            g.drawLine(275, 265, 725, 265);
            g.drawLine(275, 305, 725, 305);
            g.drawLine(275, 345, 725, 345);
            g.drawLine(275, 385, 725, 385);
            g.drawLine(275, 425, 725, 425);
            g.drawLine(275, 465, 725, 465);
            g.drawLine(275, 505, 725, 505);
            g.drawLine(275, 545, 725, 545);
            g.drawLine(275, 585, 725, 585);
            g.drawLine(275, 625, 725, 625);

            // Vertical lines
            g.drawLine(275, 175, 275, 625);
            g.drawLine(360, 175, 360, 625);
            g.drawLine(575, 175, 575, 625);
            g.drawLine(725, 175, 725, 625);

            // Column headlines
            drawText(g, "Rank", 280, 210, Color.RED, FontSize35);
            drawText(g, "Player", 365, 210, Color.RED, FontSize35);
            drawText(g, "Time", 580, 210, Color.RED, FontSize35);

            // Text positions of rank data
            drawText(g, "1", 280, 257, Color.BLUE, FontSize30);
            drawText(g, "2", 280, 297, Color.BLUE, FontSize30);
            drawText(g, "3", 280, 337, Color.BLUE, FontSize30);
            drawText(g, "4", 280, 377, Color.BLUE, FontSize30);
            drawText(g, "5", 280, 417, Color.BLUE, FontSize30);
            drawText(g, "6", 280, 457, Color.BLUE, FontSize30);
            drawText(g, "7", 280, 497, Color.BLUE, FontSize30);
            drawText(g, "8", 280, 537, Color.BLUE, FontSize30);
            drawText(g, "9", 280, 577, Color.BLUE, FontSize30);
            drawText(g, "10", 280, 617, Color.BLUE, FontSize30);

            // Text positions of player data
            drawText(g, leaderboardData[0], 365, 257, Color.BLUE, FontSize30);
            drawText(g, leaderboardData[2], 365, 297, Color.BLUE, FontSize30);
            drawText(g, leaderboardData[4], 365, 337, Color.BLUE, FontSize30);
            drawText(g, leaderboardData[6], 365, 377, Color.BLUE, FontSize30);
            drawText(g, leaderboardData[8], 365, 417, Color.BLUE, FontSize30);
            drawText(g, leaderboardData[10], 365, 457, Color.BLUE, FontSize30);
            drawText(g, leaderboardData[12], 365, 497, Color.BLUE, FontSize30);
            drawText(g, leaderboardData[14], 365, 537, Color.BLUE, FontSize30);
            drawText(g, leaderboardData[16], 365, 577, Color.BLUE, FontSize30);
            drawText(g, leaderboardData[18], 365, 617, Color.BLUE, FontSize30);

            // Text positions of time data
            drawText(g, leaderboardData[1], 580, 257, Color.BLUE, FontSize30);
            drawText(g, leaderboardData[3], 580, 297, Color.BLUE, FontSize30);
            drawText(g, leaderboardData[5], 580, 337, Color.BLUE, FontSize30);
            drawText(g, leaderboardData[7], 580, 377, Color.BLUE, FontSize30);
            drawText(g, leaderboardData[9], 580, 417, Color.BLUE, FontSize30);
            drawText(g, leaderboardData[11], 580, 457, Color.BLUE, FontSize30);
            drawText(g, leaderboardData[13], 580, 497, Color.BLUE, FontSize30);
            drawText(g, leaderboardData[15], 580, 537, Color.BLUE, FontSize30);
            drawText(g, leaderboardData[17], 580, 577, Color.BLUE, FontSize30);
            drawText(g, leaderboardData[19], 580, 617, Color.BLUE, FontSize30);
        }
    }

    // Draws the white Background
    public static void drawRect (Graphics g,int x, int y, int width, int height, Color color){
        g.setColor(Color.WHITE);
        g.fillRect(x, y, width, height);
        g.setColor(color);
        g.drawRect(x, y, width, height);
    }

    // Draws the data
    public static void drawText (Graphics g, String text,int x, int y, Color color, Font font){
        g.setColor(color);
        g.setFont(font);
        g.drawString(text, x, y);
    }
}
