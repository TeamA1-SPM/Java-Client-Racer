package main.game;

import main.constants.Settings;

import javax.swing.*;
import java.awt.*;
import java.util.Objects;

public class Background {

    private Image sky;
    private Image hills;
    private Image trees;

    private double skyOffset = 0;                       // current sky scroll offset
    private double hillOffset = 0;                       // current hill scroll offset
    private double treeOffset = 0;

    private boolean isReady = false;


    public Background(){
       loadImages();
    }

    private void loadImages(){
        String skyPath = "../images/background/sky.png";
        ImageIcon imageIcon = new ImageIcon(Objects.requireNonNull(this.getClass().getResource(skyPath)));
        sky = imageIcon.getImage();

        String hillsPath = "../images/background/hills.png";
        imageIcon = new ImageIcon(Objects.requireNonNull(this.getClass().getResource(hillsPath)));
        hills = imageIcon.getImage();

        String treesPath = "../images/background/trees.png";
        imageIcon = new ImageIcon(Objects.requireNonNull(this.getClass().getResource(treesPath)));
        trees = imageIcon.getImage();
    }

    public boolean isReady() {
        return isReady;
    }

    public void render(Graphics2D g2D){

        double ratio = (double) Settings.SCREEN_HEIGHT / sky.getHeight(null);

        int x = 0;
        int y = 0;
        int width = (int)(Settings.SCREEN_WIDTH * ratio);
        int height = Settings.SCREEN_HEIGHT;

        g2D.drawImage(sky, x, y, width, height, null);
        g2D.drawImage(hills, x, y, width, height, null);
        g2D.drawImage(trees, x, y, width, height, null);

    }

    public void render2(Graphics2D g2D){

        //if()





    }


    public void updateOffset(double curve, double speedPercent){
        double increment = Settings.skySpeed * curve * speedPercent;
        skyOffset = increase(skyOffset, increment);
        increment = Settings.hillSpeed * curve * speedPercent;
        hillOffset = increase(hillOffset, increment);
        increment = Settings.treeSpeed * curve * speedPercent;
        treeOffset = increase(treeOffset, increment);
    }

    private double increase(double start, double increment){
        double max = 1;
        double result = start + increment;
        while (result >= max)
            result -= max;
        while (result < 0)
            result += max;
        return result;
    }

    public double getSkyOffset() {
        return skyOffset;
    }

    public void setSkyOffset(double skyOffset) {
        this.skyOffset = skyOffset;
    }

    public double getHillOffset() {
        return hillOffset;
    }

    public void setHillOffset(double hillOffset) {
        this.hillOffset = hillOffset;
    }

    public double getTreeOffset() {
        return treeOffset;
    }

    public void setTreeOffset(double treeOffset) {
        this.treeOffset = treeOffset;
    }
}
