package main;

import java.util.ArrayList;
import java.util.List;

/**
 * Build Road Geometry
 */
public class RoadReset {

    private static final int NUM_SEGMENTS = 500;
    private static final int rumbleLength = 3;
    private static final double segmentLength = 100.0;

    private static int[] COLORS;

    private List<Segment> segments;
    private double trackLength;
    private double playerZ;

    public RoadReset() {
        resetRoad();
    }

    private void resetRoad() {
        segments = new ArrayList<>();
        for (int n = 0; n < NUM_SEGMENTS; n++) {
            segments.add(new Segment(
                    n,
                    new Point(0.0, 0.0, n * segmentLength),
                    new Point(0.0, 0.0, (n + 1) * segmentLength),
                    Math.floor(n / rumbleLength) % 2 == 1 ? COLORS[2] : COLORS[1]
            ));
        }

        segments.get(findSegment(playerZ).index + 2).color = COLORS[3];
        segments.get(findSegment(playerZ).index + 3).color = COLORS[3];
        for (int n = 0; n < rumbleLength; n++)
            segments.get(segments.size() - 1 - n).color = COLORS[0];

        trackLength = segments.size() * segmentLength;
    }

    private Segment findSegment(double z) {
        return segments.get((int) (Math.floor(z / segmentLength) % segments.size()));
    }

    private static class Segment {
        int index;
        Point p1;
        Point p2;
        int color;

        Segment(int index, Point p1, Point p2, int color) {
            this.index = index;
            this.p1 = p1;
            this.p2 = p2;
            this.color = color;
        }
    }

    private static class Point {
        double x, y, z;

        Point(double x, double y, double z) {
            this.x = x;
            this.y = y;
            this.z = z;
        }
    }
}
