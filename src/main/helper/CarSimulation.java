package main.helper;

import main.constants.SpriteName;
import main.game.Player;

import java.util.ArrayList;

import static main.constants.Settings.*;

public class CarSimulation {
    private ArrayList<Car> carList;
    private final double maxPosition;

    private Car playerCar;
    private Car enemyCar;

    public CarSimulation(ArrayList<Car> carList, double maxPosition){
        this.carList = carList;
        this.maxPosition = maxPosition;
    }

    public void addPlayer(Player player){
        playerCar = new Car(null,player.getPlayerX(),PLAYER_Z - SEGMENT_LENGTH,0, PLAYER_W);
        carList.add(playerCar);
    }

    public void addEnemy(EnemyPlayer enemy){
        if(enemy != null){
            this.enemyCar = new Car(enemy.getSpriteName(),enemy.getPlayerX(), PLAYER_Z,0, PLAYER_W);
            carList.add(enemyCar);
        }
    }

    private void updatePlayerSprites(Car car, double position, double x){
        int index = carList.indexOf(car);
        Car s = carList.get(index);
        s.setPosition(position);
        s.setOffset(x);
    }

    public void update(Player player, EnemyPlayer enemy){

        updatePlayerSprites(playerCar,(player.getPosition() + PLAYER_Z) - SEGMENT_LENGTH, player.getPlayerX());

        if(enemy != null){
            enemyCar.setSpriteName(enemy.getSpriteName());
            updatePlayerSprites(enemyCar, enemy.getPosition() + PLAYER_Z, enemy.getPlayerX());
        }

        for(Car car: carList){
            // calculate overtake
            double offset = car.getOffset();
            offset += updateCarOffset(car);
            car.setOffset(offset);
            // move forward
            increaseNPCPosition(car);
        }
    }

    public ArrayList<Car> getCarList(){
        return carList;
    }


    private ArrayList<Car> getSegmentCars(double segmentStart){
        ArrayList<Car> segmentCars = new ArrayList<>();
        for (Car car: carList) {
            double carPosition = car.getPosition();
            if(carPosition >= segmentStart && carPosition <= segmentStart + SEGMENT_LENGTH){
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


    // look ahead and evade other cars
    private double updateCarOffset(Car car){
        double position = car.getPosition();
        double segmentStart = position - (position%SEGMENT_LENGTH);
        double result;

        // look ahead 20 segments
        for(int counter = 1; counter <= 20; counter++){
            segmentStart += SEGMENT_LENGTH;
            if(segmentStart >= maxPosition){
                segmentStart -= maxPosition;
            }

            ArrayList<Car> segmentCars = getSegmentCars(segmentStart);

            for (Car otherCar : segmentCars) {
                if ((car.getSpeed() > otherCar.getSpeed()) && overlap(car.getOffset(), car.getWidth(), otherCar.getOffset(), otherCar.getWidth(), 1.2)) {
                    if (otherCar.getOffset() > 0.5)
                        result = -1;
                    else if (otherCar.getOffset() < -0.5)
                        result = 1;
                    else
                        result = (car.getOffset() > otherCar.getOffset()) ? 1 : -1;
                    return result * 1 / counter * (car.getSpeed() - otherCar.getSpeed()) / MAX_SPEED;
                }
            }
        }

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
