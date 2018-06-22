/*----------------------------------------------------------------
 *  Author:        Judah Anthony
 *  Written:       1/28/2017
 *  Last updated:  2/1/2017
 *
 *  Compilation:   javac-algs4 BruteCollinearPoints.java
 *  
 *  Slow way to find collinear points.
 *
 *----------------------------------------------------------------*/
import java.util.Arrays;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;
//import edu.princeton.cs.algs4.StdRandom;

public class BruteCollinearPoints {
    private int _length = 0;
    private LineSegment[] _segments;
    
    /**
     * finds all line segments containing 4 points
     */
    public BruteCollinearPoints(Point[] points) {
        if (points == null) {
            throw new NullPointerException("points must not be null.");
        }
        for (int i = 0; i < points.length; i++) {
            if (points[i] == null) {
                throw new NullPointerException("No point in the array may be null");
            }
        }
        Point[] s = new Point[8];
        Point[] ps = new Point[4];
        for (int i = 0; i < points.length; ++i) {
            for (int j = i + 1; j < points.length; ++j) {
                double slopeij = _slopeTo(points, i, j);
                for (int k = j + 1; k < points.length; ++k) {
                    double slopeik = _slopeTo(points, i, k);
                    double slopejk = _slopeTo(points, j, k);
                    for (int l = k + 1; l < points.length; ++l) {
                        double slopeil = _slopeTo(points, i, l);
                        double slopejl = _slopeTo(points, j, l);
                        double slopekl = _slopeTo(points, k, l);
                        if (slopeij == slopeik && slopeij == slopeil) {
                            ps[0] = points[i];
                            ps[1] = points[j];
                            ps[2] = points[k];
                            ps[3] = points[l];
                            
                            Arrays.sort(ps);
                            
                            Point segmin = ps[0];
                            Point segmax = ps[3];
                            
                            if (add(s, segmin, segmax)) {
                                s = expand(s);
                            }
                        }
                    }
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
        BruteCollinearPoints collinear = new BruteCollinearPoints(points);
//        StdOut.println("First:");
        LineSegment[] segments = collinear.segments();
        for (LineSegment segment : segments) {
            StdOut.println(segment);
            segment.draw();
        }
        StdDraw.show();
        
//        StdRandom.shuffle(points);
//        points[0] = new Point(1, 2);
//        StdRandom.shuffle(segments);
//        segments[0] = new LineSegment(points[0], points[1]);
//        StdOut.println("Second:");
//        for (LineSegment segment : collinear.segments()) {
//            StdOut.println(segment);
//        }
    }
    
    private double _slopeTo(Point[] points, int from, int to) {
        double slope = points[from].slopeTo(points[to]);
        if (slope == Double.NEGATIVE_INFINITY) {
            throw new IllegalArgumentException("Cannot have duplicate points");
        }
        return slope;
    }
}
