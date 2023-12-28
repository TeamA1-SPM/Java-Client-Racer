package main.game;

import main.helper.Car;
import main.helper.Point;
import main.helper.Segment;
import main.constants.SpriteName;
import main.helper.RoadSideObject;

import java.util.ArrayList;
import java.util.Random;

import static main.constants.ColorMode.*;
import static main.constants.Settings.*;
import static main.constants.SpriteName.*;

public class RoadCreator {

    private final double LENGTH_NONE = 0;
    private final double LENGTH_SHORT = 25;
    private final double LENGTH_MEDIUM = 50;
    private final double LENGTH_LONG = 100;
    private final double HILL_NONE = 0;
    private final double HILL_LOW = 20;
    private final double HILL_MEDIUM = 40;
    private final double HILL_HIGH = 60;
    private final double CURVE_NONE = 0;
    private final double CURVE_EASY = 2;
    private final double CURVE_MEDIUM = 4;
    private final double CURVE_HARD = 6;

    private SpritesLoader spritesLoader;

    private ArrayList<Segment> segmentList;
    private ArrayList<Car> carList;


    public RoadCreator(SpritesLoader spritesLoader){
        this.spritesLoader = spritesLoader;
        this.carList = new ArrayList<>();
    }


    // Create straight road
    public ArrayList<Segment> createV1StraightRoad(){
        segmentList = new ArrayList<>();
        int segmentQuantity = 500;
        int segmentLength = SEGMENT_LENGTH;

        // Create road segments
        for(int index = 0; index < segmentQuantity; index++){
            Segment seg = new Segment(index);

            seg.setP1World(new Point(0,0,index*segmentLength));
            seg.setP2World(new Point(0,0,(index+1)*segmentLength));

            if (Math.floorDiv(index, RUMBLE_LENGTH) % 2 == 0) {
                seg.setColorMode(LIGHT);
            } else {
                seg.setColorMode(DARK);
            }

            segmentList.add(seg);
        }

        addStartFinish();

        return segmentList;
    }

    // Create curvy road
    public ArrayList<Segment> createV2CurvyRoad(){
        segmentList = new ArrayList<>();

        addStraight(LENGTH_SHORT/4);
        addSCurves();
        addStraight(LENGTH_LONG);
        addCurve(LENGTH_MEDIUM, CURVE_MEDIUM,0.d);
        addCurve(LENGTH_LONG, CURVE_MEDIUM,0.d);
        addStraight(LENGTH_MEDIUM);
        addSCurves();
        addCurve(LENGTH_LONG, -CURVE_MEDIUM,0.d);
        addCurve(LENGTH_LONG, CURVE_MEDIUM,0.d);
        addStraight(LENGTH_MEDIUM);
        addSCurves();
        addCurve(LENGTH_LONG, -CURVE_EASY,0.d);

        addStartFinish();

        return segmentList;
    }

    // create a road with hills and curves
    public ArrayList<Segment> createV3HillRoad(){
        segmentList = new ArrayList<>();

        addStraight(LENGTH_SHORT/2);
        addHill(LENGTH_SHORT, HILL_LOW);
        addLowRollingHills(LENGTH_SHORT, HILL_LOW);
        addCurve(LENGTH_MEDIUM, CURVE_MEDIUM, HILL_LOW);
        addLowRollingHills(LENGTH_SHORT, HILL_LOW);
        addCurve(LENGTH_LONG, CURVE_MEDIUM, HILL_MEDIUM);
        addStraight(LENGTH_MEDIUM);
        addCurve(LENGTH_LONG, -CURVE_MEDIUM, HILL_MEDIUM);
        addHill(LENGTH_LONG, HILL_HIGH);
        addCurve(LENGTH_LONG, CURVE_MEDIUM, -HILL_LOW);
        addHill(LENGTH_LONG, -HILL_MEDIUM);
        addStraight(LENGTH_MEDIUM);
        addDownhillToEnd(200.d);

        addStartFinish();

        return segmentList;
    }

    public ArrayList<Segment> createV4Final(){
        segmentList = new ArrayList<>();
        carList = new ArrayList<>();

        addStraight(LENGTH_SHORT);
        addLowRollingHills(LENGTH_SHORT,HILL_LOW);
        addSCurves();
        addCurve(LENGTH_MEDIUM, CURVE_MEDIUM, HILL_LOW);
        addBumps();
        addLowRollingHills(LENGTH_SHORT,HILL_LOW);
        addCurve(LENGTH_LONG*2, CURVE_MEDIUM, HILL_MEDIUM);
        addStraight(LENGTH_MEDIUM);
        addHill(LENGTH_MEDIUM, HILL_HIGH);
        addSCurves();
        addCurve(LENGTH_LONG, -CURVE_MEDIUM, HILL_NONE);
        addHill(LENGTH_LONG, HILL_HIGH);
        addCurve(LENGTH_LONG, CURVE_MEDIUM, -HILL_LOW);
        addBumps();
        addHill(LENGTH_LONG, -HILL_MEDIUM);
        addStraight(LENGTH_MEDIUM);
        addSCurves();
        addDownhillToEnd(200.d);

        addStartFinish();
        addSideRoadSprites();
        addCars();

        return segmentList;
    }

    public ArrayList<Car> getCarList(){
        return carList;
    }

    private void addSideRoadSprites(){

        int segmentsLength = segmentList.size();

        addSprite(20,  BILLBOARD07, -1);
        addSprite(40,  BILLBOARD06, -1);
        addSprite(60,  BILLBOARD08, -1);
        addSprite(80,  BILLBOARD09, -1);
        addSprite(100, BILLBOARD01, -1);
        addSprite(120, BILLBOARD02, -1);
        addSprite(140, BILLBOARD03, -1);
        addSprite(160, BILLBOARD04, -1);
        addSprite(180, BILLBOARD05, -1);

        addSprite(240, BILLBOARD07, -1.2);
        addSprite(240, BILLBOARD06,  1.2);
        addSprite(segmentsLength - 25, BILLBOARD07, -1.2);
        addSprite(segmentsLength - 25, BILLBOARD06,  1.2);

        for(int n = 10 ; n < 200 ; n += (int) (4 + (double) (n / 100))) {
            addSprite(n, PALM_TREE, 0.5 + Math.random()*0.5);
            addSprite(n, PALM_TREE,   1 + Math.random()*2);
        }

        for(int n = 250 ; n < 1000 ; n += 5) {
            addSprite(n,     COLUMN, 1.1);
            addSprite(n + rndInt(5), TREE1, -1 - (Math.random() * 2));
            addSprite(n + rndInt(5), TREE2, -1 - (Math.random() * 2));
        }

        for(int n = 200 ; n < segmentsLength ; n += 3) {
            addSprite(n, getRNDTree(), rndSide() * (2 + Math.random() * 5));
        }

        for(int n = 1000 ; n < (segmentsLength - 50) ; n += 100) {
            int side = rndSide();
            addSprite(n + rndInt(50), getRNDBillboard(), -side);
            for(int i = 0 ; i < 20 ; i++) {
                double offset = side * (1.5 + Math.random());
                addSprite(n + rndInt(50), getRNDTree(), offset);
            }
        }
    }

    private void addSprite(int index, SpriteName name, double offset){
        segmentList.get(index).addRoadsideObj(name, offset, spritesLoader.getSpriteWidth(name));
    }

    private void addCars(){
        for(int i = 0; i < TOTAL_CARS; i++){
            SpriteName name = getRNDCar();
            double offset = getRNDOffset();
            double speed = 3000 + (new Random().nextInt(6000));
            double position = Math.floor(Math.random() * segmentList.size()) * SEGMENT_LENGTH;

            Car car = new Car(name, offset, position,speed,spritesLoader.getSpriteWidth(name));
            carList.add(car);
        }
    }

    private double getRNDOffset(){
        Random rnd = new Random();
        int sign = rnd.nextInt(2);
        int fac = rnd.nextInt(2);

        if(sign == 0){
            fac = -1;
        }
        return fac * 0.8;
    }


    private int rndInt(int bound){
        return new Random().nextInt(bound);
    }

    private int rndSide(){
        Random rnd = new Random();
        int sign = rnd.nextInt(2);
        if(sign == 0){
            return -1;
        }
        return 1;
    }

    private SpriteName getRNDBillboard(){
        SpriteName[] billboards = {BILLBOARD01,BILLBOARD02, BILLBOARD03,BILLBOARD04,BILLBOARD05,BILLBOARD06,BILLBOARD07,BILLBOARD08,BILLBOARD09};
        return billboards[new Random().nextInt(billboards.length - 1)];
    }

    private SpriteName getRNDTree(){
        SpriteName[] trees = {TREE1, TREE2, DEAD_TREE1, DEAD_TREE2, STUMP, BOULDER1, BOULDER2, BOULDER3, BUSH1, BUSH2, CACTUS, PALM_TREE };
        return trees[new Random().nextInt(trees.length - 1)];
    }

    private SpriteName getRNDCar(){
        SpriteName[] cars = {CAR01, CAR02, CAR03, CAR04, SEMI, TRUCK};
        return cars[new Random().nextInt(cars.length - 1)];
    }

    private void addStartFinish(){
        int index = (int)Math.floor(PLAYER_Z /SEGMENT_LENGTH) % segmentList.size();

        // Start line
        segmentList.get(index + 2).setColorMode(START);
        segmentList.get(index + 3).setColorMode(START);

        // Finish line
        for(int n = 0; n < RUMBLE_LENGTH; n++){
            segmentList.get(segmentList.size() - 1 - n).setColorMode(FINISH);
        }
    }

    private void addSegment(double curve, double y){
        int segmentLength = SEGMENT_LENGTH;

        int n = segmentList.size();

        // Create road segments
        Segment seg = new Segment(n);

        seg.setP1World(new Point(0,(int)getLastY(),n*segmentLength));
        seg.setP2World(new Point(0,(int)y,(n+1)*segmentLength));
        seg.setCurve(curve);

        if (Math.floorDiv(n, RUMBLE_LENGTH) % 2 == 0) {
            seg.setColorMode(LIGHT);
        } else {
            seg.setColorMode(DARK);
        }
        segmentList.add(seg);
    }

    private void addRoad(double enter, double hold, double leave, double curve, double y) {
        double startY = getLastY();
        double endY = startY + (y * SEGMENT_LENGTH);
        double total = enter + hold + leave;

        for(int n = 0 ; n < enter ; n++)
            addSegment(easeIn(0, curve, n/enter), easeInOut(startY, endY, n/total));
        for(int n = 0 ; n < hold  ; n++)
            addSegment(curve, easeInOut(startY, endY, (enter+n)/total));
        for(int n = 0 ; n < leave ; n++)
            addSegment(easeInOut(curve, 0, n/leave), easeInOut(startY, endY, (enter+hold+n)/total));
    }

    private double easeIn(double a, double b, double percent){
        return a + (b-a)*Math.pow(percent,2);
    }

    private double easeInOut(double a, double b, double percent){
        return a + (b-a)*((-Math.cos(percent*Math.PI)/2) + 0.5);
    }

    private void addBumps(){
        addRoad(10, 10, 10, 0,  5);
        addRoad(10, 10, 10, 0, -2);
        addRoad(10, 10, 10, 0, -5);
        addRoad(10, 10, 10, 0,  8);
        addRoad(10, 10, 10, 0,  5);
        addRoad(10, 10, 10, 0, -7);
        addRoad(10, 10, 10, 0,  5);
        addRoad(10, 10, 10, 0, -2);
    }

    private void addStraight(Double num){
        num = (num != 0) ? num : LENGTH_MEDIUM;
        addRoad(num,num,num,0,0);
    }

    private void addHill(Double num, Double height){
        num = (num != 0) ? num : LENGTH_MEDIUM;
        height = (height != 0) ? height : HILL_MEDIUM;
        addRoad(num,num,num,0, height);
    }

    private void addCurve(Double num, Double curve, Double height){
        num = (num != 0) ? num : LENGTH_MEDIUM;
        curve = (curve != 0) ? curve : CURVE_MEDIUM;
        height = (height != 0) ? height : HILL_MEDIUM;
        addRoad(num,num,num,curve, height);
    }

    private void addLowRollingHills(Double num, Double height){
        num = (num != 0) ? num : LENGTH_SHORT;
        height = (height != 0) ? height : HILL_LOW;

        addRoad(num, num, num,  0,  height/2);
        addRoad(num, num, num,  0, -height);
        addRoad(num, num, num,  CURVE_EASY,  height);
        addRoad(num, num, num,  0,  0);
        addRoad(num, num, num,  -CURVE_EASY,  height/2);
        addRoad(num, num, num,  0,  0);
    }

    private void addDownhillToEnd(Double num){
        num = (num != 0) ? num : 200;
        addRoad(num,num,num,-CURVE_EASY, -(getLastY()/ SEGMENT_LENGTH));
    }

    // adds S curves to the road
    private void addSCurves(){
        addRoad(LENGTH_MEDIUM, LENGTH_MEDIUM, LENGTH_MEDIUM,  -CURVE_EASY, HILL_NONE);
        addRoad(LENGTH_MEDIUM, LENGTH_MEDIUM, LENGTH_MEDIUM,   CURVE_MEDIUM, HILL_MEDIUM);
        addRoad(LENGTH_MEDIUM, LENGTH_MEDIUM, LENGTH_MEDIUM,   CURVE_EASY, -HILL_LOW);
        addRoad(LENGTH_MEDIUM, LENGTH_MEDIUM, LENGTH_MEDIUM,  -CURVE_EASY, HILL_MEDIUM);
        addRoad(LENGTH_MEDIUM, LENGTH_MEDIUM, LENGTH_MEDIUM,  -CURVE_MEDIUM, -HILL_MEDIUM);
    }

    private double getLastY(){
        double lastY = 0;
        int size = segmentList.size();
        if(size != 0){
            lastY = segmentList.get(size-1).getP2World().getY();
        }
        return lastY;
    }

}
