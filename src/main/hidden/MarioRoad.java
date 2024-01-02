package main.hidden;

import main.constants.Colors;
import main.gamehelper.Segment;

import java.awt.*;
import java.util.ArrayList;

public class MarioRoad {

    private final Color[] rainbow = {
            new Color(56,120,248),
            new Color(0,248,176),
            new Color(16,248,16),
            new Color(248,248,16),
            new Color(248,136,32),
            new Color(248,32,8),
            new Color(192,152,248)
    };

    private final Color[] grass = {
            new Color(50,50,50),
            new Color(100,100,100)
    };

    private int counter = -1;

    public void changeRoad(ArrayList<Segment> segments){
        for (Segment seg : segments){
            if(seg.getIndex()%3 == 0){
                counter++;
            }
            int roadI = (counter%7);
            int rumbleI = (counter + 3)%7;
            int grassI = counter%2;

            if(seg.getColorRoad() == Colors.FINISH || seg.getColorRoad() == Colors.START){
                seg.setColors(grass[grassI],null,null);
            }else{
                seg.setColors(grass[grassI], rainbow[roadI], rainbow[rumbleI]);
            }

        }
    }

}
