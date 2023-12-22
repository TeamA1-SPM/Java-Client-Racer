package main.game;

import javax.swing.*;
import java.awt.*;
import java.util.Objects;

import static main.constants.Settings.*;

/*
 *  Background layer
 *   - load background layer images
 *   - update the offset for parallax effect
 *   - draw background layers with endless scroll
 */


public class Background {

    public static final double SKY_SPEED = 0.006;                   // background sky layer scroll speed when going around curve (or up hill)
    public static final double HILL_SPEED = 0.009;                   // background hill layer scroll speed when going around curve (or up hill)
    public static final double TREE_SPEED = 0.02;                   // background tree layer scroll speed when going around curve (or up hill)

    private Image sky;
    private Image hills;
    private Image trees;

    private int skyOffset = 0;               // current sky scroll offset
    private int hillOffset = 0;              // current hill scroll offset
    private int treeOffset = 0;              // current tree scroll offset

    private int imageHeight;
    private int imageWidth;


    public Background(){
        loadImages();
        scaleImages();
    }

    // calc the scale the image size based on screen height
    private void scaleImages(){
        double ratio = (double) SCREEN_HEIGHT / sky.getHeight(null);
        imageHeight = (int)(sky.getHeight(null) * ratio);
        imageWidth = (int)(sky.getWidth(null) * ratio);
    }

    // load and assign images
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

    // update offset for parallax effect
    public void update(double curve, double speed){
        double factor = curve * speed/100;

        // offset sky
        skyOffset += (int)(SKY_SPEED * factor);
        if(skyOffset >= imageWidth){
            skyOffset -=  imageWidth;
        }else if(skyOffset <= -imageWidth){
            skyOffset +=  imageWidth;
        }

        // offset hills
        hillOffset += (int)(HILL_SPEED * factor);
        if(hillOffset >= imageWidth){
            hillOffset -=  imageWidth;
        }else if(hillOffset <= -imageWidth){
            hillOffset +=  imageWidth;
        }

        // offset trees
        treeOffset += (int)(TREE_SPEED * factor);
        if(treeOffset >= imageWidth){
            treeOffset -=  imageWidth;
        }else if(treeOffset <= -imageWidth){
            treeOffset +=  imageWidth;
        }
    }

    // draw background layers on graphic uses parallax function
    public void render(Graphics2D g2D){
        renderParallax(g2D, sky, skyOffset);
        renderParallax(g2D, hills, hillOffset);
        renderParallax(g2D, trees, treeOffset);
    }

    // draw two images for endless scrolling
    private void renderParallax(Graphics2D g2d,Image image, int offsetX){
        // draw base image
        g2d.drawImage(image, offsetX, 0,imageWidth, imageHeight, null);

        // fill left image
        if(offsetX > 0){
            g2d.drawImage(image, offsetX - imageWidth, 0,imageWidth, imageHeight, null);
        }
        // fill right image
        if((offsetX < 0)) {
            g2d.drawImage(image, offsetX + imageWidth, 0,imageWidth, imageHeight, null);
        }
    }
}
