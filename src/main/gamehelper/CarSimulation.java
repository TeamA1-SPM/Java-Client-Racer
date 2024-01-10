package main.gamehelper;

import main.game.Player;
import java.util.ArrayList;

import static main.constants.Settings.*;

/*
 * Simulation for the npc cars movement
 * - adds a player hit box
 * - calc npc car velocity movement
 * - calc npc evasion
 * multiplayer:
 * - integrate enemy player to the simulation
 */
public class CarSimulation {
    private final ArrayList<Car> carList;
    private final double maxPosition;

    private Car playerCar;
    private Car enemyCar;
    private final int lookAhead;

    private boolean isRunning = false;

    public CarSimulation(ArrayList<Car> carList, double maxPosition, double playerX, int lookAhead){
        this.carList = carList;
        this.maxPosition = maxPosition;
        this.lookAhead = lookAhead;
        addPlayerHitBox(playerX);
    }

    // add an invisible sprite behind the player
    private void addPlayerHitBox(double playerX){
        playerCar = new Car(null,playerX,PLAYER_Z - SEGMENT_LENGTH,0, PLAYER_W);
        carList.add(playerCar);
    }

    // add enemy player to the simulation
    public void addEnemy(EnemyPlayer enemy){
        if(enemy != null){
            this.enemyCar = new Car(enemy.getSpriteName(),enemy.getPlayerX(), PLAYER_Z,0, PLAYER_W);
            carList.add(enemyCar);
        }
    }

    // simulation start
    public void isRunning(boolean isRunning){
        this.isRunning = isRunning;
    }

    // non npc car position
    private void updatePlayerSprites(Car car, double position, double x){
        int index = carList.indexOf(car);
        Car s = carList.get(index);
        s.setPosition(position);
        s.setOffset(x);
    }

    public void update(Player player, EnemyPlayer enemy){
        if(!isRunning){
            return;
        }

        // player hit box update
        updatePlayerSprites(playerCar,(player.getPosition() + PLAYER_Z) - SEGMENT_LENGTH, player.getPlayerX());

        // enemy position update
        if(enemy != null){
            enemyCar.setSpriteName(enemy.getSpriteName());
            updatePlayerSprites(enemyCar, enemy.getPosition() + PLAYER_Z, enemy.getPlayerX());
        }

        // npc cars update
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

    // move npc cars forward
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

        // look ahead x segments
        for(int counter = 1; counter <= lookAhead; counter++){
            segmentStart += SEGMENT_LENGTH;
            if(segmentStart >= maxPosition){
                segmentStart -= maxPosition;
            }
            // check collision for each car in the segment
            for (Car otherCar : getSegmentCars(segmentStart)) {
                if ((car.getSpeed() > otherCar.getSpeed()) && carOverlap(car.getOffset(), car.getWidth(), otherCar.getOffset(), otherCar.getWidth())) {
                    // overtake left
                    if (otherCar.getOffset() > 0.5)
                        result = -1;
                    // overtake right
                    else if (otherCar.getOffset() < -0.5)
                        result = 1;
                    else
                        result = (car.getOffset() > otherCar.getOffset()) ? 1 : -1;
                    // calc new offset
                    return result * 1 / counter * (car.getSpeed() - otherCar.getSpeed()) / MAX_SPEED;
                }
            }
        }

        // offset correction if car left the road
        if(car.getOffset() < -0.9){
            return 0.1;
        }else if(car.getOffset() > 0.9){
            return -0.1;
        }
        return 0;
    }

    // calculation for sprite overlap
    private boolean carOverlap(double x1, double w1, double x2, double w2){
        double half = 1.2 / 2;
        double min1 = x1 - (w1 * half);
        double max1 = x1 + (w1 * half);
        double min2 = x2 - (w2 * half);
        double max2 = x2 + (w2 * half);

        return !((max1 < min2) || (min1 > max2));
    }

}
