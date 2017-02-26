import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.SET;

import java.util.ArrayList;

public class PointSET {
    private SET<Point2D> points;

    // construct an empty set of points
    public         PointSET()
    {
        points = new SET<>();
    }

    // is the set empty?
    public           boolean isEmpty()
    {
        return size() == 0;
    }

    // number of points in the set
    public  int size()
    {
        return points.size();
    }

    // add the point to the set (if it is not already in the set)
    public void insert(Point2D p)
    {
        if (p == null) {
            throw new NullPointerException("argument is null");
        }
        if (!contains(p))
        {
            points.add(p);
        }
    }

    // does the set contain point p?
    public boolean contains(Point2D p)
    {
        if (p == null) {
            throw new NullPointerException("argument is null");
        }
        return points.contains(p);
    }

    // draw all points to standard draw
    public void draw()
    {
        for (Point2D point : points)
        {
            point.draw();
        }
    }

    // all points that are inside the rectangle
    public Iterable<Point2D> range(RectHV rect)
    {
        if (rect == null) {
            throw new NullPointerException("argument is null");
        }
        ArrayList<Point2D> range = new ArrayList<>();
        for (Point2D point : points)
        {
            if (rect.contains(point))
            {
                range.add(point);
            }
        }

        return range;
    }

    // a nearest neighbor in the set to point p; null if the set is empty
    public Point2D nearest(Point2D p)
    {
        if (p == null) {
            throw new NullPointerException("argument is null");
        }
        if (isEmpty())
        {
            return null;
        }
        Point2D nearest = points.max();
        double nearestDistance = nearest.distanceTo(p);
        for (Point2D point : points)
        {
            double currentPointDistance = point.distanceTo(p);
            if (currentPointDistance < nearestDistance)
            {
                nearest = point;
                nearestDistance = currentPointDistance;
            }
        }
        return nearest;
    }


    // unit testing of the methods (optional)
    public static void main(String[] args)
    {
        // create initial board from file
        In in = new In(args[0]);
        PointSET pointSET = new PointSET();
        while (!in.isEmpty())
        {
            double pointX = in.readDouble();
            double pointY = in.readDouble();
            Point2D point = new Point2D(pointX, pointY);
            pointSET.insert(point);
            pointSET.draw();
        }

    }
}
