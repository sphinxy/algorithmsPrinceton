import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class FastCollinearPoints {
    private ArrayList<LineSegment> segments = new ArrayList<>();
    private ArrayList<String> foundSlopes = new ArrayList<>();
    // finds all line segments containing 4 or more points
    public FastCollinearPoints(Point[] points)
    {
        checkCornerCases(points);
        Point[] sortedPoint  = Arrays.copyOf(points, points.length);

        for (int p = 0; p < points.length; p++)
        {
            Arrays.sort(sortedPoint);
            Arrays.sort(sortedPoint, points[p].slopeOrder());

            ArrayList<Point> fourPoints = new ArrayList<>();

            fourPoints.add(points[p]);

            for (int s = 1; s < sortedPoint.length; s++)
                {

                    double prevSlope = sortedPoint[s-1].slopeTo(points[p]);
                    double currentSlope = sortedPoint[s].slopeTo(points[p]);

                    if (currentSlope == prevSlope)
                    {
                        if (fourPoints.size() == 1)
                        {
                            fourPoints.add(sortedPoint[s-1]);
                        }
                        fourPoints.add(sortedPoint[s]);
                    }


                 }
            if (fourPoints.size() == 4)
            {

                Collections.sort(fourPoints);
                Collections.sort(fourPoints, fourPoints.get(0).slopeOrder());
                String lineHash = fourPoints.get(0).toString()+fourPoints.get(fourPoints.size()-1).toString();
                if (foundSlopes.contains(lineHash))
                {
                    continue;
                }
                LineSegment line = new LineSegment(
                        fourPoints.get(0),
                        fourPoints.get(fourPoints.size()-1));

                segments.add(line);
                foundSlopes.add(lineHash);
                // StdOut.println(p + "line :" + line + ", slope "+ currentSlope);
            }
        }
    }
    // the number of line segments
    public int numberOfSegments()
    {
        return segments.size();
    }
    // the line segments
    public LineSegment[] segments()
    {
        return segments.toArray(new LineSegment[numberOfSegments()]);
    }

    private static void checkCornerCases(Point[] points) {
        if (points == null)
        {
            throw new NullPointerException();
        }
        Point[] sortedPoint  = Arrays.copyOf(points, points.length);
        Arrays.sort(sortedPoint);

        Point prevPoint = new Point(-1, -1);
        for (int p = 0; p < sortedPoint.length; p++)
        {
            if (sortedPoint[p] == null) {
                throw new NullPointerException();
            }

            if (sortedPoint[p].equals(prevPoint)) {
                throw new IllegalArgumentException();
            }
            prevPoint = sortedPoint[p];
        }
    }
}