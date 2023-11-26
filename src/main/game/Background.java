package main.game;

import main.constants.Settings;

import javax.swing.*;
import java.awt.*;
import java.util.Objects;

public class Background {

    private Image sky;
    private Image hills;
    private Image trees;

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
}
