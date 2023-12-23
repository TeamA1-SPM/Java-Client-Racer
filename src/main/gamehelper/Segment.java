package main.gamehelper;

import java.awt.*;
import java.util.ArrayList;

import main.constants.ColorMode;
import main.constants.SpriteName;
import static main.constants.Colors.*;

public class Segment {

    private final int index;
    private boolean isLooped = false;
    private boolean isLane = false;
    private Color colorRoad;
    private Color colorGrass;
    private Color colorRumble;

    private Point p1World;
    private Point p1Camera;
    private Point p1Screen;

    private Point p2World;
    private Point p2Camera;
    private Point p2Screen;

    private double curve;
    private double clip;

    private final ArrayList<Sprite> roadsideList;
    private final ArrayList<Sprite> carList;


    public Segment(int index){
        this.index = index;

        p1World = new main.gamehelper.Point(0,0,0);
        p1Camera = new main.gamehelper.Point(0,0,0);
        p1Screen = new main.gamehelper.Point(0,0,0);

        p2World = new main.gamehelper.Point(0,0,0);
        p2Camera = new main.gamehelper.Point(0,0,0);
        p2Screen = new main.gamehelper.Point(0,0,0);

        this.curve = 0;

        this.roadsideList = new ArrayList<>();
        this.carList = new ArrayList<>();

    }

    // sets color mode for each segment type
    public void setColorMode(ColorMode mode){
        switch (mode) {
            case LIGHT:
                colorRoad = ROAD_LIGHT;
                colorGrass = GRASS_LIGHT;
                colorRumble = RUMBLE_DARK;
                isLane = true;
                break;
            case DARK:
                colorRoad = ROAD_DARK;
                colorGrass = GRASS_DARK;
                colorRumble = RUMBLE_LIGHT;
                break;
            case START:
                colorRoad = START;
                colorRumble = START;
                break;
            case FINISH:
                colorRoad = FINISH;
                colorRumble = FINISH;
                break;
        }
    }

    public int getIndex() {
        return index;
    }

    public Color getColorRoad() {
        return colorRoad;
    }

    public Color getColorGrass() {
        return colorGrass;
    }

    public Color getColorRumble() {
        return colorRumble;
    }

    public void setColors(Color grass, Color road, Color rumble){
        if(grass != null)
            this.colorGrass = grass;
        if(road != null)
            this.colorRoad = road;
        if(rumble != null)
            this.colorRumble = rumble;
    }

    public Point getP1World() {
        return p1World;
    }

    public void setP1World(main.gamehelper.Point p1World) {
        this.p1World = p1World;
    }

    public Point getP1Camera() {
        return p1Camera;
    }

    public void setP1Camera(main.gamehelper.Point p1Camera) {
        this.p1Camera = p1Camera;
    }

    public Point getP1Screen() {
        return p1Screen;
    }

    public void setP1Screen(main.gamehelper.Point p1Screen) {
        this.p1Screen = p1Screen;
    }

    public Point getP2World() {
        return p2World;
    }

    public void setP2World(main.gamehelper.Point p2World) {
        this.p2World = p2World;
    }

    public Point getP2Camera() {
        return p2Camera;
    }

    public void setP2Camera(main.gamehelper.Point p2Camera) {
        this.p2Camera = p2Camera;
    }

    public Point getP2Screen() {
        return p2Screen;
    }

    public void setP2Screen(Point p2Screen) {
        this.p2Screen = p2Screen;
    }

    public boolean isLooped() {
        return isLooped;
    }

    public void setLooped(boolean looped) {
        isLooped = looped;
    }

    public boolean isLane(){
        return isLane;
    }

    public double getCurve() {
        return curve;
    }

    public void setCurve(double curve) {
        this.curve = curve;
    }

    public ArrayList<Sprite> getRoadsideList(){
        return roadsideList;
    }
    public void addRoadsideObj(SpriteName name, double offset, double width){
        roadsideList.add(new Sprite(name, offset, 0, 0, width));
    }
    public ArrayList<Sprite> getCarList() { return carList; }
    public void addCar(SpriteName name, double offset, double z, double speed, double width) {
        carList.add(new Sprite(name, offset, z, speed, width));
    }

    public double getClip() { return clip; }

    public void setClip(double clip) { this.clip = clip; }

}
