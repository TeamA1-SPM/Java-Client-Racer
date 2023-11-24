package main.game;

import main.constants.Settings;

import javax.swing.*;
import java.awt.*;

public class Background {
    private String skyPath = "../images/background/sky.png";
    private String hillsPath = "../images/background/hills.png";
    private String treesskyPath = "../images/background/trees.png";

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

        double ratio = (double) Settings.SCREEN_HEIGHT / sky.getHeight(null);

        int x = 0;
        int y = 0;
        int width = (int)(Settings.SCREEN_WIDTH * ratio);
        int height = Settings.SCREEN_HEIGHT;

        g2D.drawImage(sky, x, y, width, height, null);
        g2D.drawImage(hills, x, y, width, height, null);
        g2D.drawImage(trees, x, y, width, height, null);


    }


}
