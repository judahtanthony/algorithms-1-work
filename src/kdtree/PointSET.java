import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.SET;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;

public class PointSET {
  private SET<Point2D> set;
  /**
   * construct an empty set of points
   */
  public PointSET() {
    set = new SET<Point2D>();
  }
  /**
   * is the set empty?
   */
  public boolean isEmpty() {
    return set.isEmpty();
  }
  /**
   * number of points in the set
   */
  public int size() {
    return set.size();
  }
  /**
   * add the point to the set (if it is not already in the set)
   */
  public void insert(Point2D p) {
    set.add(p);
  }
  /**
   * does the set contain point p?
   */
  public boolean contains(Point2D p) {
    return set.contains(p);
  }
  /**
   * draw all points to standard draw
   */
  public void draw() {
    if (set != null) {
      for (Point2D p : set) {
        StdDraw.point(p.x(), p.y());
      }
    }
  }
  /**
   * all points that are inside the rectangle
   */
  public Iterable<Point2D> range(RectHV rect) {
    SET<Point2D> inRange = new SET<Point2D>();
    if (set != null) {
      for (Point2D p : set) {
        if (rect.contains(p)) {
          inRange.add(p);
        }
      }
    }
    return inRange;
  }
  /**
   * a nearest neighbor in the set to point p; null if the set is empty
   */
  public Point2D nearest(Point2D p) {
    Point2D min = null;
    if (set != null) {
      double minDest = 0;
      for (Point2D point : set) {
        double dest = p.distanceTo(point);
        if (min == null || dest < minDest) {
          min = point;
          minDest = dest;
        }
      }
    }
    return min;
  }

  /**
   * unit testing of the methods (optional)
   */
   public static void main(String[] args) {
       String filename = args[0];
       In in = new In(filename);

       StdDraw.enableDoubleBuffering();

       // initialize the two data structures with point from standard input
       PointSET brute = new PointSET();
       while (!in.isEmpty()) {
           double x = in.readDouble();
           double y = in.readDouble();
           Point2D p = new Point2D(x, y);
           brute.insert(p);
       }

       while (true) {
           // the location (x, y) of the mouse
           double x = StdDraw.mouseX();
           double y = StdDraw.mouseY();
           Point2D query = new Point2D(x, y);

           // draw all of the points
           StdDraw.clear();
           StdDraw.setPenColor(StdDraw.BLACK);
           StdDraw.setPenRadius(0.01);
           brute.draw();

           // draw in red the nearest neighbor (using brute-force algorithm)
           StdDraw.setPenRadius(0.03);
           StdDraw.setPenColor(StdDraw.RED);
           brute.nearest(query).draw();
           StdDraw.setPenRadius(0.02);

           StdDraw.show();
           StdDraw.pause(40);
       }
   }
}
