import java.util.ArrayList;
import java.util.Arrays;

// finds all line segments containing 4 points
public class BruteCollinearPoints {
    private ArrayList<LineSegment> segments = new ArrayList<>();

    public BruteCollinearPoints(Point[] points)
    {
        checkCornerCases(points);

        for (int p = 0; p < points.length; p++)
        {
            for (int q = p+1; q < points.length; q++)
            {
                for (int r = q+1; r < points.length; r++)
                {
                    for (int s = r+1; s < points.length; s++)
                    {
                        double slopePQ = points[p].slopeTo(points[q]);
                        double slopePR = points[p].slopeTo(points[r]);
                        double slopePS = points[p].slopeTo(points[s]);
                        if (slopePQ == slopePR && slopePR == slopePS)
                        {
                            Point[] fourPoints = new Point[4];
                            fourPoints[0] = points[p];
                            fourPoints[1] = points[q];
                            fourPoints[2] = points[r];
                            fourPoints[3] = points[s];
                            Arrays.sort(fourPoints);
                            Arrays.sort(fourPoints, fourPoints[0].slopeOrder());

                            LineSegment line = new LineSegment(
                                    fourPoints[0],
                                    fourPoints[3]);
                            segments.add(line);
                        }
                    }
                }
            }

        }
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
}
