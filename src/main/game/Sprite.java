package main.game;

import main.constants.Settings;

import java.awt.*;

public class Sprite {


    public void render(Graphics2D g2D, Image sprite, double scale, double destX, double destY, double offsetX, double offsetY, double clipY){
        double widthMid = (double)Settings.SCREEN_WIDTH/2;
        double spriteWidth = sprite.getWidth(null);
        double spriteHeight = sprite.getHeight(null);

        //  scale for projection AND relative to roadWidth (for tweakUI)
        double destW  = (spriteWidth * scale * widthMid) * (Settings.SPRITE_SCALE * Settings.ROAD_WIDTH);
        double destH  = (spriteHeight * scale * widthMid) * (Settings.SPRITE_SCALE * Settings.ROAD_WIDTH);

        destX = destX + (destW * offsetX);
        destY = destY + (destH * offsetY);

        double clipH = Math.max(0, destY+destH-clipY);

        System.out.println("Dest X = " + destX);
        System.out.println("Dest Y = " + destY);

        if (clipH < destH){
            g2D.drawImage(sprite, (int)destX, (int)destY, (int)destW, (int)(destH - clipH), null);
        }
    }

}
