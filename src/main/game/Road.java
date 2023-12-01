package main.game;

import main.helper.Point;
import main.helper.Segment;
import main.constants.Colors;
import main.constants.Settings;

import java.awt.*;
import java.util.ArrayList;

public class Road {

    public int segmentQuantity;
    public int trackLength;


    private ArrayList<Segment> segments;

    public Road(ArrayList<Segment> segments){
        this.segments = segments;
        segmentQuantity = segments.size();
        trackLength = Settings.SEGMENT_LENGTH * segmentQuantity;
    }

    public void render(Graphics2D g2 , Player player){
        double position = player.getPosition();
        int segmentLength = Settings.SEGMENT_LENGTH;
        double playerZ = Settings.PLAYER_Z;

        Segment baseSegment = findSegment(position);
        double basePercent = percentRemaining(position, segmentLength);

        Segment playerSegment = findSegment(position + playerZ);
        double playerPercent = percentRemaining(position + playerZ, segmentLength);

        player.setPlayerY(interpolate(playerSegment.getP1World().getY(), playerSegment.getP2World().getY(), playerPercent));
        double playerY = player.getPlayerY();
        double playerX = player.getPlayerX();
        double maxy = Settings.SCREEN_HEIGHT;

        double x = 0;
        double dx = -(baseSegment.getCurve() * basePercent);

        Segment segment;
        for(int n = 0; n < Settings.DRAW_DISTANCE; n++){

            segment = segments.get((baseSegment.getIndex() + n) % segments.size());
            segment.setLooped(segment.getIndex() < baseSegment.getIndex());

            int cameraX = (int)((playerX * Settings.ROAD_WIDTH) - x);
            int cameraY = (int)(Settings.CAMERA_HEIGHT + playerY);
            int cameraZ = (int)position - (segment.isLooped() ? trackLength : 0);

            project(segment.getP1World(),segment.getP1Camera(),segment.getP1Screen(), cameraX , cameraY, cameraZ);
            project(segment.getP2World(),segment.getP2Camera(),segment.getP2Screen(), (int)(cameraX - dx), cameraY, cameraZ);

            x = x + dx;
            dx = dx + segment.getCurve();

            if(segment.getP1Camera().getZ() <= Settings.CAMERA_DEPTH || segment.getP2Screen().getY() >= segment.getP1Screen().getY() || segment.getP2Screen().getY() >= maxy){
               continue;
            }

            renderSegment(g2, segment);
            maxy = segment.getP2Screen().getY();
        }
    }

    public Segment findSegment(double position) {
        int index = (int)(position/Settings.SEGMENT_LENGTH)%segments.size();
        return segments.get(index);
    }

    private double interpolate(double a, double b, double percent) {
        return a + (b-a)*percent;
    }

    private double percentRemaining(double n, double total) {
        return (n%total)/total;
    }

    // project from world coordinates to screen coordinates
    private void project(Point pWorld, Point pCamera, Point pScreen, int cameraX, int cameraY, int cameraZ){

        double width = (double)Settings.SCREEN_WIDTH/2;
        double height  = (double)Settings.SCREEN_HEIGHT/2;
        double cameraDepth = Settings.CAMERA_DEPTH;

        // camera
        pCamera.setX(pWorld.getX() - cameraX);
        pCamera.setY(pWorld.getY() - cameraY);
        pCamera.setZ(pWorld.getZ() - cameraZ);

        double scale = cameraDepth/pCamera.getZ();

        // screen
        pScreen.setX((int)Math.round(width + (scale * pCamera.getX() * width)));
        pScreen.setY((int)Math.round(height - (scale * pCamera.getY() * height)));
        pScreen.setZ((int)Math.round(scale * Settings.ROAD_WIDTH * width));
    }

    private void renderSegment(Graphics2D g2, Segment segment){

        int x1 = segment.getP1Screen().getX();
        int y1 = segment.getP1Screen().getY();
        int w1 = segment.getP1Screen().getZ();
        int x2 = segment.getP2Screen().getX();
        int y2 = segment.getP2Screen().getY();
        int w2 = segment.getP2Screen().getZ();

        // render grass
        g2.setColor(segment.getColorGrass());
        g2.fillRect(0,y2, Settings.SCREEN_WIDTH, y1-y2);

        // render rumble
        int r1 = w1/Math.max(6,  2*Settings.LANES);
        int r2 = w2/Math.max(6,  2*Settings.LANES);
        g2.setColor(segment.getColorRumble());
        Polygon pRumbleLeft = createPolygon(x1-w1-r1, y1, x1-w1, y1, x2-w2, y2, x2-w2-r2, y2);
        g2.fillPolygon(pRumbleLeft);
        Polygon pRumbleRight = createPolygon(x1+w1+r1, y1, x1+w1, y1, x2+w2, y2, x2+w2+r2, y2);
        g2.fillPolygon(pRumbleRight);

        // render road
        Polygon pRoad = createPolygon( x1-w1, y1, x1+w1, y1, x2+w2, y2, x2-w2, y2);
        g2.setColor(segment.getColorRoad());
        g2.fillPolygon(pRoad);

        // render lanes
        if(segment.isLane()){
            int lanes = Settings.LANES;
            int l1 = w1/Math.max(32, 8*lanes);
            int l2 = w2/Math.max(32, 8*lanes);

            int lanew1 = w1*2/lanes;
            int lanew2 = w2*2/lanes;
            int lanex1 = x1 - w1 + lanew1;
            int lanex2 = x2 - w2 + lanew2;

            for(int lane = 1 ; lane < lanes ; lanex1 += lanew1, lanex2 += lanew2, lane++){
                Polygon pLane = createPolygon(lanex1 - l1/2, y1, lanex1 + l1/2, y1, lanex2 + l2/2, y2, lanex2 - l2/2, y2);
                g2.setColor(Colors.LANE);
                g2.fillPolygon(pLane);
            }
        }
    }

    private Polygon createPolygon(int x1, int y1, int x2, int y2, int x3, int y3, int x4, int y4){
        Polygon p = new Polygon();
        p.addPoint(x1,y1);
        p.addPoint(x2,y2);
        p.addPoint(x3,y3);
        p.addPoint(x4,y4);
        return p;
    }

    public int getTrackLength(){
        return trackLength;
    }
}
