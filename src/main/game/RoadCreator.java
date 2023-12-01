package main.game;

import main.constants.ColorMode;
import main.constants.Settings;
import main.helper.Point;
import main.helper.Segment;

import java.util.ArrayList;

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

    private ArrayList<Segment> segmentList;

    // Create straight road
    public ArrayList<Segment> createStraightRoad(){
        segmentList = new ArrayList<>();
        int segmentQuantity = 500;
        int segmentLength = Settings.SEGMENT_LENGTH;

        // Create road segments
        for(int index = 0; index < segmentQuantity; index++){
            Segment seg = new Segment(index);

            seg.setP1World(new Point(0,0,index*segmentLength));
            seg.setP2World(new Point(0,0,(index+1)*segmentLength));

            if (Math.floorDiv(index, Settings.RUMBLE_LENGTH) % 2 == 0) {
                seg.setColorMode(ColorMode.LIGHT);
            } else {
                seg.setColorMode(ColorMode.DARK);
            }

            segmentList.add(seg);
        }

        double playerZ = Settings.CAMERA_HEIGHT * Settings.CAMERA_DEPTH;

        int index = (int)Math.floor(playerZ/segmentLength) % segmentQuantity;

        // Start line
        segmentList.get(index + 2).setColorMode(ColorMode.START);
        segmentList.get(index + 3).setColorMode(ColorMode.START);

        // Finish line
        for(int n = 0; n < Settings.RUMBLE_LENGTH; n++){
            segmentList.get(segmentQuantity-1-n).setColorMode(ColorMode.FINISH);
        }

        return segmentList;
    }

    // Create curvy road
    public ArrayList<Segment> createCurvyRoad(){
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

        int index = (int)Math.floor(Settings.PLAYER_Z /Settings.SEGMENT_LENGTH) % segmentList.size();

        // Start line
        segmentList.get(index + 2).setColorMode(ColorMode.START);
        segmentList.get(index + 3).setColorMode(ColorMode.START);

        // Finish line
        for(int n = 0; n < Settings.RUMBLE_LENGTH; n++){
            segmentList.get(segmentList.size() - 1 - n).setColorMode(ColorMode.FINISH);
        }

        return segmentList;
    }

    // create a road with hills and curves
    public ArrayList<Segment> createHillRoad(){
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

        int index = (int)Math.floor(Settings.PLAYER_Z /Settings.SEGMENT_LENGTH) % segmentList.size();

        // Start line
        segmentList.get(index + 2).setColorMode(ColorMode.START);
        segmentList.get(index + 3).setColorMode(ColorMode.START);

        // Finish line
        for(int n = 0; n < Settings.RUMBLE_LENGTH; n++){
            segmentList.get(segmentList.size() - 1 - n).setColorMode(ColorMode.FINISH);
        }

        return segmentList;
    }

    private void addSegment(double curve, double y){
        int segmentLength = Settings.SEGMENT_LENGTH;

        int n = segmentList.size();

        // Create road segments
        Segment seg = new Segment(n);

        seg.setP1World(new Point(0,(int)getLastY(),n*segmentLength));
        seg.setP2World(new Point(0,(int)y,(n+1)*segmentLength));
        seg.setCurve(curve);

        if (Math.floorDiv(n, Settings.RUMBLE_LENGTH) % 2 == 0) {
            seg.setColorMode(ColorMode.LIGHT);
        } else {
            seg.setColorMode(ColorMode.DARK);
        }
        segmentList.add(seg);
    }

    private void addRoad(double enter, double hold, double leave, double curve, double y) {
        double startY = getLastY();
        double endY = startY + (y * Settings.SEGMENT_LENGTH);
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
        addRoad(num, num, num,  0,  height);
        addRoad(num, num, num,  0,  0);
        addRoad(num, num, num,  0,  height/2);
        addRoad(num, num, num,  0,  0);
    }

    private void addDownhillToEnd(Double num){
        num = (num != 0) ? num : 200;
        addRoad(num,num,num,-CURVE_EASY, -(getLastY()/Settings.SEGMENT_LENGTH));
    }

    // adds S curves to the road
    private void addSCurves(){
        addRoad(LENGTH_MEDIUM, LENGTH_MEDIUM, LENGTH_MEDIUM,  -CURVE_EASY, HILL_NONE);
        addRoad(LENGTH_MEDIUM, LENGTH_MEDIUM, LENGTH_MEDIUM,   CURVE_MEDIUM, HILL_MEDIUM);
        addRoad(LENGTH_MEDIUM, LENGTH_MEDIUM, LENGTH_MEDIUM,   CURVE_EASY, -HILL_LOW);
        addRoad(LENGTH_MEDIUM, LENGTH_MEDIUM, LENGTH_MEDIUM,  -CURVE_EASY, HILL_MEDIUM);
        addRoad(LENGTH_MEDIUM, LENGTH_MEDIUM, LENGTH_MEDIUM,  -CURVE_MEDIUM, - HILL_MEDIUM);
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
