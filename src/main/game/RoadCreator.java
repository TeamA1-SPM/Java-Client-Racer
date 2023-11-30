package main.game;

import main.constants.ColorMode;
import main.constants.Settings;
import main.helper.Point;
import main.helper.Segment;

import java.util.ArrayList;

public class RoadCreator {

    private final int LENGTH_NONE = 0;
    private final int LENGTH_SHORT = 25;
    private final int LENGTH_MEDIUM = 50;
    private final int LENGTH_LONG = 100;
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
        addCurve(LENGTH_MEDIUM, CURVE_MEDIUM);
        addCurve(LENGTH_LONG, CURVE_MEDIUM);
        addStraight(LENGTH_MEDIUM);
        addSCurves();
        addCurve(LENGTH_LONG, -CURVE_MEDIUM);
        addCurve(LENGTH_LONG, CURVE_MEDIUM);
        addStraight(LENGTH_MEDIUM);
        addSCurves();
        addCurve(LENGTH_LONG, -CURVE_EASY);

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

    private void addSegment(double curve){
        int segmentLength = Settings.SEGMENT_LENGTH;

        int n = segmentList.size();

        // Create road segments
        Segment seg = new Segment(n);

        seg.setP1World(new Point(0,0,n*segmentLength));
        seg.setP2World(new Point(0,0,(n+1)*segmentLength));
        seg.setCurve(curve);

        if (Math.floorDiv(n, Settings.RUMBLE_LENGTH) % 2 == 0) {
            seg.setColorMode(ColorMode.LIGHT);
        } else {
            seg.setColorMode(ColorMode.DARK);
        }
        segmentList.add(seg);
    }

    private void addRoad(int enter, int hold, int leave, double curve) {
        int n;
        for(n = 0 ; n < enter ; n++)
            addSegment(easeIn(0, curve, (double)n/enter));
        for(n = 0 ; n < hold  ; n++)
            addSegment(curve);
        for(n = 0 ; n < leave ; n++)
            addSegment(easeInOut(curve, 0, (double)n/leave));
    }

    private double easeIn(double a, double b, double percent){
        return a + (b-a)*Math.pow(percent,2);
    }

    private double easeInOut(double a, double b, double percent){
        return a + (b-a)*((-Math.cos(percent*Math.PI)/2) + 0.5);
    }

    private void addStraight(Integer num){
        num = (num != 0) ? num : LENGTH_MEDIUM;
        addRoad(num,num,num,0);
    }

    private void addCurve(Integer num, Double curve){
        num = (num != 0) ? num : LENGTH_MEDIUM;
        curve = (curve != 0) ? curve : CURVE_MEDIUM;
        addRoad(num,num,num,curve);
    }

    // adds S curves to the road
    private void addSCurves(){
        addRoad(LENGTH_MEDIUM, LENGTH_MEDIUM, LENGTH_MEDIUM,  -CURVE_EASY);
        addRoad(LENGTH_MEDIUM, LENGTH_MEDIUM, LENGTH_MEDIUM,   CURVE_MEDIUM);
        addRoad(LENGTH_MEDIUM, LENGTH_MEDIUM, LENGTH_MEDIUM,   CURVE_EASY);
        addRoad(LENGTH_MEDIUM, LENGTH_MEDIUM, LENGTH_MEDIUM,  -CURVE_EASY);
        addRoad(LENGTH_MEDIUM, LENGTH_MEDIUM, LENGTH_MEDIUM,  -CURVE_MEDIUM);
    }


}
