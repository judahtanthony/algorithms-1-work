/*----------------------------------------------------------------
 *  Author:        Judah Anthony
 *  Written:       1/28/2017
 *  Last updated:  2/1/2017
 *
 *  Compilation:   javac-algs4 FastCollinearPoints.java
 *
 *  Fast way to find collinear points.
 *
 *----------------------------------------------------------------*/
import java.util.Arrays;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

public class FastCollinearPoints {
    private int _length = 0;
    private LineSegment[] _segments;

    /**
     * finds all line segments containing 4 or more points
     */
    public FastCollinearPoints(Point[] points) {
        // Check of the arg is null.
        if (points == null) {
            throw new NullPointerException("points must not be null.");
        }
        // Check if any of the elements are null.
        for (int i = 0; i < points.length; i++) {
            if (points[i] == null) {
                throw new NullPointerException("No point in the array may be null");
            }
        }
        Point[] s = new Point[8];
        if (points.length > 1) {
            Point[] others = new Point[points.length];
            System.arraycopy(points, 0, others, 0, points.length);
            // Step through each point and check it against the others.
            for (int i = 0; i < points.length; i++) {
                Arrays.sort(others, points[i].slopeOrder());
                // Check for duplicates.
                if (others[0].compareTo(others[1]) == 0) {
                    throw new IllegalArgumentException("All points must be unique");
                }
                // Find all of its segments;
                int from = 1; // The first one is always itself.
                int len = segLength(points[i], others, from);
                // Check if all points fall on the same slope.
                if (i == 0 && len >= 3 && len == (points.length - 1)) {
                  Arrays.sort(others);
                  add(s, others[0], others[others.length - 1]);
                  break;
                }
                while (len > 0) {
                    if (len >= 3) {
                        // Find the max and min of the segment.
                        Point[] ps = new Point[len + 1];
                        for (int j = 0; j < len; j++) {
                            ps[j] = others[from + j];
                        }
                        ps[len] = points[i];
                        Arrays.sort(ps);
                        Point segmin = ps[0];
                        Point segmax = ps[len];
                        if (add(s, segmin, segmax)) {
                            s = expand(s);
                        }
                    }
                    from += len;
                    len = segLength(points[i], others, from);
                }
            }
        }
        _segments = new LineSegment[_length];
        for (int i = 0; i < _length; ++i) {
            _segments[i] = new LineSegment(s[i*2], s[i*2 + 1]);
        }
    }

    /**
     * the number of line segments
     */
    public           int numberOfSegments() {
        return _length;
    }

    /**
     * the line segments
     */
    public LineSegment[] segments() {
        LineSegment[] segs = new LineSegment[_length];
        System.arraycopy(_segments, 0, segs, 0, _length);
        return segs;
    }

    private int segLength(Point point, Point[] points, int from) {
        if (from >= points.length - 1) {
            return 0;
        }
        double lastSlope = point.slopeTo(points[from]);
        int len = 1;
        while ((from+len) < points.length) {
            double slope = point.slopeTo(points[from+len]);
            if (slope != lastSlope) {
                break;
            }
            lastSlope = slope;
            len += 1;
        }
        return len;
    }

    private boolean add(Point[] points, Point from, Point to) {
        for (int i = 0; i < _length; ++i) {
            if (points[i*2] == from && points[i*2 + 1] == to) {
                return false;
            }
        }
        points[_length*2] = from;
        points[_length*2 + 1] = to;
        _length += 1;
        return true;
    }

    private Point[] expand(Point[] points) {
        if (((_length+1)*2) >= points.length) {
            Point[] newPoints = new Point[points.length*2];
            System.arraycopy(points, 0, newPoints, 0, points.length);
            return newPoints;
        }
        return points;
    }

    /**
     * Unit tests the Point data type.
     */
    public static void main(String[] args) {
        // read the n points from a file
        In in = new In(args[0]);
        int n = in.readInt();
        Point[] points = new Point[n];
        for (int i = 0; i < n; i++) {
            int x = in.readInt();
            int y = in.readInt();
            points[i] = new Point(x, y);
        }

        // draw the points
        StdDraw.enableDoubleBuffering();
        StdDraw.setXscale(0, 32768);
        StdDraw.setYscale(0, 32768);
        for (Point p : points) {
            p.draw();
        }
        StdDraw.show();

        // print and draw the line segments
        FastCollinearPoints collinear = new FastCollinearPoints(points);
        for (LineSegment segment : collinear.segments()) {
            StdOut.println(segment);
            segment.draw();
        }
        StdDraw.show();
    }
}
