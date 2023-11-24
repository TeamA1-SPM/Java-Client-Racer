package main.game;

import main.constants.Settings;

import javax.swing.*;
import java.awt.*;

public class Background {
    private String skyPath = "images/background/sky.png";
    private String hillsPath = "images/background/hills.png";
    private String treesskyPath = "images/background/trees.png";

    private Image sky;
    private Image hills;
    private Image trees;

    private boolean isReady = false;


    public Background(){
        isReady = loadImages();
    }

    private boolean loadImages(){
        ImageIcon imageIcon = new ImageIcon(this.getClass().getResource(skyPath));
        sky = imageIcon.getImage();
        imageIcon = new ImageIcon(this.getClass().getResource(hillsPath));
        hills = imageIcon.getImage();
        imageIcon = new ImageIcon(this.getClass().getResource(treesskyPath));
        trees = imageIcon.getImage();

        if(sky != null & hills != null & trees != null){
            return true;
        }

        return false;
    }

    public boolean isReady() {
        return isReady;
    }

    public void render(Graphics2D g2D){

        g2D.drawImage(sky, 0, 0, sky.getWidth(null), sky.getHeight(null), 0, 0, Settings.SCREEN_WIDTH/2,Settings.SCREEN_HEIGHT/2,null);
        g2D.drawImage(hills, 0, 0, sky.getWidth(null), sky.getHeight(null), 0, 0, Settings.SCREEN_WIDTH/2,Settings.SCREEN_HEIGHT/2,null);
        g2D.drawImage(trees, 0, 0, sky.getWidth(null), sky.getHeight(null), 0, 0, Settings.SCREEN_WIDTH/2,Settings.SCREEN_HEIGHT/2,null);

    }

}
