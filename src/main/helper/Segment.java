package main.helper;

import main.constants.ColorMode;
import main.constants.Colors;

import java.awt.*;

public class Segment {

    private int index;
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


    public Segment(int index){
        this.index = index;

        p1World = new main.helper.Point(0,0,0);
        p1Camera = new main.helper.Point(0,0,0);
        p1Screen = new main.helper.Point(0,0,0);

        p2World = new main.helper.Point(0,0,0);
        p2Camera = new main.helper.Point(0,0,0);
        p2Screen = new main.helper.Point(0,0,0);

    }

    // sets color mode for each segment type
    public void setColorMode(ColorMode mode){
        switch (mode) {
            case LIGHT:
                colorRoad = Colors.ROAD_LIGHT;
                colorGrass = Colors.GRASS_LIGHT;
                colorRumble = Colors.RUMBLE_DARK;
                isLane = true;
                break;
            case DARK:
                colorRoad = Colors.ROAD_DARK;
                colorGrass = Colors.GRASS_DARK;
                colorRumble = Colors.RUMBLE_LIGHT;
                break;
            case START:
                colorRoad = Colors.START;
                colorGrass = Colors.START;
                colorRumble = Colors.START;
                break;
            case FINISH:
                colorRoad = Colors.FINISH;
                colorGrass = Colors.FINISH;
                colorRumble = Colors.FINISH;
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

    public main.helper.Point getP1World() {
        return p1World;
    }

    public void setP1World(main.helper.Point p1World) {
        this.p1World = p1World;
    }

    public main.helper.Point getP1Camera() {
        return p1Camera;
    }

    public void setP1Camera(main.helper.Point p1Camera) {
        this.p1Camera = p1Camera;
    }

    public main.helper.Point getP1Screen() {
        return p1Screen;
    }

    public void setP1Screen(main.helper.Point p1Screen) {
        this.p1Screen = p1Screen;
    }

    public main.helper.Point getP2World() {
        return p2World;
    }

    public void setP2World(main.helper.Point p2World) {
        this.p2World = p2World;
    }

    public main.helper.Point getP2Camera() {
        return p2Camera;
    }

    public void setP2Camera(main.helper.Point p2Camera) {
        this.p2Camera = p2Camera;
    }

    public main.helper.Point getP2Screen() {
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
}
