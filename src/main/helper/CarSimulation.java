package main.helper;


import main.helper.Car;
import main.helper.RoadSideObject;

import java.awt.*;
import java.util.ArrayList;
import java.util.Comparator;

import static main.constants.Settings.*;

public class CarSimulation {
    private ArrayList<Car> carList;
    private double maxPosition;

    private Car player;
    private Car enemy;

    public CarSimulation(ArrayList<Car> carList, double maxPosition){
        this.carList = carList;
        this. maxPosition = maxPosition;
    }

    private void setPlayerSprites(Car player){
        int index = carList.indexOf(player);
        Car s = carList.get(index);
        s.setPosition(player.getPosition());
        s.setOffset(player.getOffset());
    }

    public void update(){

        //setPlayerSprites(player);
        //setPlayerSprites(enemy);

        for(Car car: carList){
            // calculate overtake
            double offset = car.getOffset();
            offset += updateCarOffset(car);
            car.setOffset(offset);
            increaseNPCPosition(car);
        }

        carList.sort(Comparator.comparing(Car::getPosition).reversed());
    }


    public ArrayList<Car> getSegmentCars(int index){
        ArrayList<Car> segmentCars = new ArrayList<>();

        double segmentStart = index * SEGMENT_LENGTH;
        double segmentEnd = segmentStart + SEGMENT_LENGTH;

        for (Car car: carList) {
            double carPosition = car.getPosition();
            if(carPosition >= segmentStart && carPosition <= segmentEnd){
                segmentCars.add(car);
            }
        }

        return segmentCars;
    }

    private void increaseNPCPosition(Car car){
        double position = car.getPosition();
        position += STEP * car.getSpeed();
        // loop car position
        if(position >= maxPosition){
            position -= maxPosition;
        }
        car.setPosition(position);
    }

    private double updateCarOffset(Car car){
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
