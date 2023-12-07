package main.game;

import javax.swing.*;
import java.awt.*;
import java.util.Objects;

import static main.constants.Settings.*;

public class Background {

    private Image sky;
    private Image hills;
    private Image trees;

    private double skyOffset = 0;               // current sky scroll offset
    private double hillOffset = 0;              // current hill scroll offset
    private double treeOffset = 0;              // current tree scroll offset


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

    public void render(Graphics2D g2D){

        double ratio = (double) SCREEN_HEIGHT / sky.getHeight(null);

        int x = 0;
        int y = 0;
        int width = (int)(SCREEN_WIDTH * ratio);
        int height = SCREEN_HEIGHT;

        g2D.drawImage(sky, x, y, width, height, null);
        g2D.drawImage(hills, x, y, width, height, null);
        g2D.drawImage(trees, x, y, width, height, null);

        // TODO make it work
        //renderParallax(g2D, sky,0, skyOffset);
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

        //rotation = rotation == 0 ? 0 : rotation;
        //offset = offset == 0 ? 0 : offset;


        int imageW = layerW / 2;
        int imageH = layerH;

        int sourceX = layerX + (int) Math.floor(layerW * rotation);
        int sourceY = layerY;
        int sourceW = Math.min(imageW, layerX + layerW - sourceX);
        int sourceH = imageH;

        int destX = 0;
        int destY = (int) offset;
        int destW = (int) Math.floor(SCREEN_WIDTH * (sourceW / (double) imageW));
        int destH = SCREEN_HEIGHT;


        g2D.drawImage(layer, destX, destY, destX+destW, destY+destH, sourceX, sourceY,sourceX+sourceW, sourceY+sourceH, null);
        if (sourceW < imageW) {

            int remainingWidth = imageW - sourceW;
            int destX1 = destW - 1;

            g2D.drawImage(layer, destX1, destY, destX1 + (SCREEN_WIDTH - destW), destY + destH, layerX, sourceY, remainingWidth + layerX, sourceY + sourceH, null);
        }
    }


    public void updateOffset(double curve, double speed){
        double increment;
        double speedPercent = speed/MAX_SPEED;
        increment = SKY_SPEED * curve * speedPercent;
        skyOffset = increase(skyOffset, increment);

        increment = HILL_SPEED * curve * speedPercent;
        hillOffset = increase(hillOffset, increment);

        increment = TREE_SPEED * curve * speedPercent;
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
}
