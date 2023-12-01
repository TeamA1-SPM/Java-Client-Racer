package main.game;

import main.constants.Settings;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.AffineTransform;
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

        // TODO make it work
        //renderParallax(g2D, sky,0.1, skyOffset);
        //renderParallax(g2D, hills,0, hillOffset);
        //renderParallax(g2D, trees,0, treeOffset);

    }

    // TODO make it work
    public void renderParallax(Graphics2D g2D,Image layer, double rotation, double offset){

        // rotation = rotation || 0;
        //offset   = offset   || 0;
        int layerX = 0;
        int layerY = 0;
        int layerW = layer.getWidth(null);
        int layerH = layer.getHeight(null);

        rotation = rotation == 0 ? 0 : rotation;
        offset = offset == 0 ? 0 : offset;


        int imageW = layerW / 2;
        int imageH = layerH;

        int sourceX = layerX + (int) Math.floor(layerW * rotation);
        int sourceY = layerY;
        int sourceW = Math.min(imageW, layerX + layerW - sourceX);
        int sourceH = imageH;

        int destX = (int)(offset * 100);
        int destY = (int) offset;
        int destW = (int) Math.floor(Settings.SCREEN_WIDTH * (sourceW / (double) imageW));
        int destH = Settings.SCREEN_HEIGHT;

        AffineTransform transform = new AffineTransform();
        transform.translate(destX, destY);
        transform.scale(destW / (double) sourceW, destH / (double) sourceH);

        g2D.drawImage(layer, transform, null);

        if (sourceW < imageW) {
            int remainingWidth = imageW - sourceW;
            g2D.drawImage(layer, layerX, sourceY, remainingWidth, sourceH, destW - 1, destY, Settings.SCREEN_WIDTH - destW, destH,null);
        }

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
