package main.game;

import main.helper.Point;
import main.helper.Segment;
import main.constants.Colors;
import main.constants.Settings;

import java.awt.*;

public class Road {

    private Segment[] segments;

    public Road(Segment[] segments){
        this.segments = segments;
    }


    public void render(Graphics2D g2 , Player player){
        double position = player.getPosition();
        double playerX = player.getPlayerX();


        Segment baseSegment = findSegment(position);
        double maxy = Settings.SCREEN_HEIGHT;

        Segment segment;
        for(int n = 0; n < Settings.drawDistance; n++){

            segment = segments[(baseSegment.getIndex() + n) % Settings.segmentQuantity];
            segment.setLooped(segment.getIndex() < baseSegment.getIndex());

            int cameraX = (int)(playerX * Settings.roadWidth);
            int cameraY = (int)Settings.cameraHeight;
            int cameraZ = (int)position - (segment.isLooped() ? Settings.trackLength : 0);

            project(segment.getP1World(),segment.getP1Camera(),segment.getP1Screen(), cameraX , cameraY, cameraZ);
            project(segment.getP2World(),segment.getP2Camera(),segment.getP2Screen(), cameraX , cameraY, cameraZ);

            if(segment.getP1Camera().getZ() <= Settings.cameraDepth || segment.getP2Screen().getY() >= maxy){
               continue;
            }


            renderSegment(g2, segment);
            maxy = segment.getP2Screen().getY();
        }
    }

    public Segment findSegment(double position) {
        int index = (int)(position/Settings.segmentLength)%Settings.segmentQuantity;
        return segments[index];
    }

    // project from world coordinates to screen coordinates
    private void project(Point pWorld, Point pCamera, Point pScreen, int cameraX, int cameraY, int cameraZ){

        double width = (double)Settings.SCREEN_WIDTH/2;
        double height  = (double)Settings.SCREEN_HEIGHT/2;
        double cameraDepth = Settings.cameraDepth;

        // camera
        pCamera.setX(pWorld.getX() - cameraX);
        pCamera.setY(pWorld.getY() - cameraY);
        pCamera.setZ(pWorld.getZ() - cameraZ);

        double scale = cameraDepth/pCamera.getZ();

        // screen
        pScreen.setX((int)Math.round(width + (scale * pCamera.getX() * width)));
        pScreen.setY((int)Math.round(height - (scale * pCamera.getY() * height)));
        pScreen.setZ((int)Math.round(scale * Settings.roadWidth * width));
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
        int r1 = w1/Math.max(6,  2*Settings.lanes);
        int r2 = w2/Math.max(6,  2*Settings.lanes);
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
            int lanes = Settings.lanes;
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
}
