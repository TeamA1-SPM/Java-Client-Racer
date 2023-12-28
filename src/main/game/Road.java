package main.game;

import java.awt.*;
import java.util.ArrayList;

import main.helper.*;
import main.helper.Point;

import static main.constants.Colors.*;
import static main.constants.Settings.*;


public class Road {

    private final ArrayList<Segment> segments;
    private final int trackLength;
    private ArrayList<Car> carList;

    public Road(ArrayList<Segment> segments){
        this.segments = segments;
        this.trackLength = SEGMENT_LENGTH * segments.size();
        this.carList = new ArrayList<>();
    }

    public void update(ArrayList<Car> carList) {
        this.carList = carList;
    }

    public void render(Graphics2D g2 , Player player, SpritesLoader spritesLoader){
        double position = player.getPosition();

        Segment baseSegment = findSegment(position);
        double basePercent = percentRemaining(position);

        Segment playerSegment = findSegment(position + PLAYER_Z);
        double playerPercent = percentRemaining(position + PLAYER_Z);

        double playerY = interpolate(playerSegment.getP1World().getY(), playerSegment.getP2World().getY(), playerPercent);
        double playerX = player.getPlayerX();

        double maxy = SCREEN_HEIGHT;

        double x = 0;
        double dx = -(baseSegment.getCurve() * basePercent);

        Segment segment;

        // render road segments
        for(int n = 0; n < DRAW_DISTANCE; n++){

            segment = segments.get((baseSegment.getIndex() + n) % segments.size());
            segment.setLooped(segment.getIndex() < baseSegment.getIndex());
            segment.setClip(maxy);

            int cameraX = (int)((playerX * ROAD_WIDTH) - x);
            int cameraY = (int)(CAMERA_HEIGHT + playerY);
            int cameraZ = (int)position - (segment.isLooped() ? trackLength : 0);

            project(segment.getP1World(),segment.getP1Camera(),segment.getP1Screen(), cameraX , cameraY, cameraZ);
            project(segment.getP2World(),segment.getP2Camera(),segment.getP2Screen(), (int)(cameraX - dx), cameraY, cameraZ);

            x = x + dx;
            dx = dx + segment.getCurve();

            if(segment.getP1Camera().getZ() <= CAMERA_DEPTH
                    || segment.getP2Screen().getY() >= segment.getP1Screen().getY()
                    || segment.getP2Screen().getY() >= maxy){
               continue;
            }

            renderSegment(g2, segment);
            maxy = segment.getP2Screen().getY();
        }


        // render roadside sprites and npc cars
        for(int n = (DRAW_DISTANCE - 1) ; n > 0 ; n--) {
            int index = (baseSegment.getIndex() + n) % segments.size();
            segment = segments.get(index);
            ArrayList<Car> carList = getSegmentCars(index);

            // render cars
            for(int i = 0; i < carList.size(); i++){
                Car car = carList.get(i);
                double percent = percentRemaining(car.getPosition());
                double spriteScale = interpolate(CAMERA_DEPTH/segment.getP1Camera().getZ(), CAMERA_DEPTH/segment.getP2Camera().getZ(), percent);
                double spriteX = interpolate(segment.getP1Screen().getX(),segment.getP2Screen().getX(),percent) + (spriteScale * car.getOffset() * ROAD_WIDTH * SCREEN_WIDTH/2);
                double spriteY = interpolate(segment.getP1Screen().getY(),segment.getP2Screen().getY(),percent);
                spritesLoader.render(g2, car.getName(), spriteScale, spriteX, spriteY,-0.5, -1, segment.getClip());
            }

            // render roadside objects
            for(int i = 0; i < segment.getRoadsideList().size(); i++) {
                RoadSideObject roadSideObject = segment.getRoadsideList().get(i);


                double spriteScale = CAMERA_DEPTH / segment.getP1Camera().getZ();
                double spriteX = segment.getP1Screen().getX() + (spriteScale * roadSideObject.getOffset() * ROAD_WIDTH * SCREEN_WIDTH/2);
                double spriteY = segment.getP1Screen().getY();

                double offset = 0;

                if(roadSideObject.getOffset() < 0){
                    offset = -1;
                }

                spritesLoader.render(g2, roadSideObject.getName(), spriteScale, spriteX, spriteY,offset, -1, segment.getClip());
            }
        }
    }

    public ArrayList<Car> getSegmentCars(int index){
        ArrayList<Car> segmentCars = new ArrayList<>();
        int startPosition = index * SEGMENT_LENGTH;
        for (Car car: carList) {
            double carPosition = car.getPosition();
            if(carPosition >= startPosition && carPosition <= startPosition + SEGMENT_LENGTH){
                segmentCars.add(car);
            }
        }

        return segmentCars;
    }

    private double interpolate(double a, double b, double percent) {
        return a + (b-a)*percent;
    }
    private double percentRemaining(double n) {
        return (n%SEGMENT_LENGTH)/SEGMENT_LENGTH;
    }

    // project from world coordinates to screen coordinates
    private void project(Point pWorld, Point pCamera, Point pScreen, int cameraX, int cameraY, int cameraZ){

        double width = (double)SCREEN_WIDTH/2;
        double height  = (double)SCREEN_HEIGHT/2;

        // camera
        pCamera.setX(pWorld.getX() - cameraX);
        pCamera.setY(pWorld.getY() - cameraY);
        pCamera.setZ(pWorld.getZ() - cameraZ);

        double scale = CAMERA_DEPTH/pCamera.getZ();

        // screen
        pScreen.setX((int)Math.round(width + (scale * pCamera.getX() * width)));
        pScreen.setY((int)Math.round(height - (scale * pCamera.getY() * height)));
        pScreen.setZ((int)Math.round(scale * ROAD_WIDTH * width));
    }

    // render one road segments
    private void renderSegment(Graphics2D g2, Segment segment){

        int x1 = segment.getP1Screen().getX();
        int y1 = segment.getP1Screen().getY();
        int w1 = segment.getP1Screen().getZ();
        int x2 = segment.getP2Screen().getX();
        int y2 = segment.getP2Screen().getY();
        int w2 = segment.getP2Screen().getZ();

        // render grass
        g2.setColor(segment.getColorGrass());
        g2.fillRect(0,y2, SCREEN_WIDTH, y1-y2);

        // render rumble
        int r1 = w1/Math.max(6,  2*LANES);
        int r2 = w2/Math.max(6,  2*LANES);
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
            int lanes = LANES;
            int l1 = w1/Math.max(32, 8*lanes);
            int l2 = w2/Math.max(32, 8*lanes);

            int lanew1 = w1*2/lanes;
            int lanew2 = w2*2/lanes;
            int lanex1 = x1 - w1 + lanew1;
            int lanex2 = x2 - w2 + lanew2;

            for(int lane = 1 ; lane < lanes ; lanex1 += lanew1, lanex2 += lanew2, lane++){
                Polygon pLane = createPolygon(lanex1 - l1/2, y1, lanex1 + l1/2, y1, lanex2 + l2/2, y2, lanex2 - l2/2, y2);
                g2.setColor(LANE);
                g2.fillPolygon(pLane);
            }
        }
    }

    // create polygon
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

    public Segment findSegment(double position) {
        int index = (int)(position/SEGMENT_LENGTH)%segments.size();
        return segments.get(index);
    }

    public ArrayList<Segment> getSegments(){
        return segments;
    }
}
