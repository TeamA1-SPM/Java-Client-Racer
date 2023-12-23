package main.game;


import main.gamehelper.Sprite;

import java.util.ArrayList;
import java.util.Comparator;

import static main.constants.Settings.*;

public class CarSimulation {
    private ArrayList<Sprite> carList;
    private double maxPosition;

    private Sprite player;
    private Sprite enemy;

    public CarSimulation(ArrayList<Sprite> carList, double maxPosition){
        this.carList = carList;
        this. maxPosition = maxPosition;
    }

    private void setPlayerSprites(Sprite player){
        int index = carList.indexOf(player);
        Sprite s = carList.get(index);
        s.setPosition(player.getPosition());
        s.setOffset(player.getOffset());
    }

    public void update(){

        setPlayerSprites(player);
        setPlayerSprites(enemy);

        for(Sprite car: carList){
            // calculate overtake
            double offset = car.getOffset();
            offset += updateCarOffset(car);
            car.setOffset(offset);
            increaseNPCPosition(car);
        }

        carList.sort(Comparator.comparing(Sprite::getPosition).reversed());
    }

    private void increaseNPCPosition(Sprite car){
        double position = car.getPosition();
        position +=STEP * car.getSpeed();
        // loop car position
        if(position >= maxPosition){
            position -= maxPosition;
        }
        car.setPosition(position);
    }

    private double updateCarOffset(Sprite car){
        double position = car.getPosition();

        //for(int pos = position )

        if(car.getOffset() < -0.9){
            return 0.1;
        }else if(car.getOffset() > 0.9){
            return -0.1;
        }
        return 0;
    }

    // calculation for sprite overlap
    private boolean overlap(double x1, double w1, double x2, double w2, double percent){
        double half = (percent != 0) ? percent / 2 : (double) 1 / 2;
        double min1 = x1 - (w1 * half);
        double max1 = x1 + (w1 * half);
        double min2 = x2 - (w2 * half);
        double max2 = x2 + (w2 * half);

        return !((max1 < min2) || (min1 > max2));
    }

}
