package main.game;

import main.constants.SpriteName;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.Objects;

import static main.constants.Settings.*;
import static main.constants.SpriteName.*;


class SpriteCoordinates {

    int x;
    int y;
    int w;
    int h;

    public SpriteCoordinates(int x, int y, int w, int h){
        this.x = x;
        this.y = y;
        this.w = w;
        this.h = h;
    }

    public int getX(){
        return x;
    }
    public int getY(){
        return y;
    }
    public int getW(){
        return w;
    }
    public int getH(){
        return h;
    }


}


public class SpritesLoader {
    private final HashMap<SpriteName, SpriteCoordinates> spritesMap = new HashMap<>();
    private final String path = "../images/sprites.png";
    private Image image;

    public SpritesLoader(){
        addCoordinates();
        loadImage(this.path);
    }

    private void addCoordinates(){

        spritesMap.put(BILLBOARD01, new SpriteCoordinates( 625, 375, 300, 170 ));
        spritesMap.put(BILLBOARD02, new SpriteCoordinates(245, 1262, 215, 220));
        spritesMap.put(BILLBOARD03, new SpriteCoordinates(5, 1262, 230, 220));
        spritesMap.put(BILLBOARD04, new SpriteCoordinates(1205, 310, 268, 170));
        spritesMap.put(BILLBOARD05, new SpriteCoordinates(5, 897, 298, 190));
        spritesMap.put(BILLBOARD06, new SpriteCoordinates(488, 555, 298, 190));
        spritesMap.put(BILLBOARD07, new SpriteCoordinates(313, 897, 298, 190));
        spritesMap.put(BILLBOARD08, new SpriteCoordinates(230,5, 385, 265));
        spritesMap.put(BILLBOARD09, new SpriteCoordinates(150, 555, 328, 282));


        spritesMap.put(PALM_TREE, new SpriteCoordinates(5, 5,215,540));
        spritesMap.put(TREE1, new SpriteCoordinates(625, 5, 360, 360 ));
        spritesMap.put(TREE2, new SpriteCoordinates(1205, 5, 282, 295));
        spritesMap.put(DEAD_TREE1, new SpriteCoordinates(5, 555, 135, 332));
        spritesMap.put(DEAD_TREE2, new SpriteCoordinates(1205, 490, 150, 260));
        spritesMap.put(BUSH1, new SpriteCoordinates(5, 1097, 240, 155));
        spritesMap.put(BUSH2, new SpriteCoordinates(255, 1097, 232, 152));
        spritesMap.put(CACTUS, new SpriteCoordinates(929, 897, 235, 118));
        spritesMap.put(STUMP, new SpriteCoordinates(995, 330, 195, 140));
        spritesMap.put(BOULDER1, new SpriteCoordinates(1205, 760, 168, 248));
        spritesMap.put(BOULDER2, new SpriteCoordinates(621,897, 298, 140));
        spritesMap.put(BOULDER3, new SpriteCoordinates(230, 280, 320, 220));
        spritesMap.put(COLUMN, new SpriteCoordinates(995, 5, 200, 315));

        spritesMap.put(SEMI, new SpriteCoordinates(1365, 490, 122, 144));
        spritesMap.put(TRUCK, new SpriteCoordinates(1365, 644, 100, 78));
        spritesMap.put(CAR01, new SpriteCoordinates(1205, 1018, 80, 56));
        spritesMap.put(CAR02, new SpriteCoordinates(1383, 825, 80, 59));
        spritesMap.put(CAR03, new SpriteCoordinates(1383, 760, 88, 55));
        spritesMap.put(CAR04, new SpriteCoordinates(1383, 894, 80, 57));

        spritesMap.put(PLAYER_UPHILL_LEFT, new SpriteCoordinates(1383, 961, 80, 45));
        spritesMap.put(PLAYER_UPHILL_STRAIGHT, new SpriteCoordinates(1295, 1018, 80, 45));
        spritesMap.put(PLAYER_UPHILL_RIGHT, new SpriteCoordinates(1385, 1018, 80, 45));
        spritesMap.put(PLAYER_LEFT, new SpriteCoordinates(995, 480, 80, 41));
        spritesMap.put(PLAYER_STRAIGHT, new SpriteCoordinates(1085, 480, 80, 41));
        spritesMap.put(PLAYER_RIGHT, new SpriteCoordinates(995, 531, 80, 41));

        spritesMap.put(COUNTDOWN_ONE, new SpriteCoordinates(1212,1370,77,106));
        spritesMap.put(COUNTDOWN_TWO, new SpriteCoordinates(1291,1370,92,106));
        spritesMap.put(COUNTDOWN_THREE, new SpriteCoordinates(1382,1370,96,106));

        spritesMap.put(PAUSE_MENU, new SpriteCoordinates(1088,1372,106,104));
    }

    private void loadImage(String path){
        ImageIcon imageIcon = new ImageIcon(Objects.requireNonNull(this.getClass().getResource(path)));
        image = imageIcon.getImage();
    }

    public double getSpriteWidth(SpriteName name){
        return spritesMap.get(name).getW() * SPRITE_SCALE;
    }

    public void render(Graphics2D g2D, SpriteName name, double scale, double destX, double destY, double offsetX, double offsetY, double clipY){

        if(name == null){
            return;
        }

        double widthMid = (double) SCREEN_WIDTH/2;

        SpriteCoordinates sC = spritesMap.get(name);

        //  scale for projection AND relative to roadWidth (for tweakUI)
        double destW  = (sC.getW() * scale * widthMid) * (SPRITE_SCALE * ROAD_WIDTH);
        double destH  = (sC.getH() * scale * widthMid) * (SPRITE_SCALE * ROAD_WIDTH);

        destX = destX + (destW * offsetX);
        destY = destY + (destH * offsetY);

        double clipH = clipY != 0 ? Math.max(0, destY + destH - clipY) : 0;

        if (clipH < destH){
            double destX2 = destW + destX;
            double destY2 = (destH - clipH) + destY;
            double srcX2 = sC.getX() + sC.getW();
            double srcY2 = sC.getY() + ((sC.getH() - (sC.getH()*clipH/destH)));
            g2D.drawImage(image, (int)destX, (int)destY, (int)destX2, (int)destY2 , sC.getX(), sC.getY(), (int)srcX2, (int)srcY2,null);
        }
    }

    public void setMarioMode(){
        loadImage("../images/mario_sprites.png");
    }

}
