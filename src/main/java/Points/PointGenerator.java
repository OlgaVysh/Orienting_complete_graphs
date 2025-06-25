package Points;

import java.util.*;

/**
 * This class generates different sets of points
 */
public class PointGenerator {

    /**
     * This method generates set of n different points between 0 and 1
     * @param numberOfPoints is the size of the set
     */
    public static List<Point> generateUniquePoints(int numberOfPoints) {

        if(numberOfPoints<3) {throw new IllegalArgumentException("Illegal argument. Number of points should be at least 3.");}
        Set<Point> uniquePoints = new HashSet<>();
        Random random = new Random();

        while (uniquePoints.size() < numberOfPoints) {
            double x = (random.nextDouble());
            double y = (random.nextDouble());
            uniquePoints.add(new Point(x, y));
        }

        return new ArrayList<>(uniquePoints);
    }


    /**
     * This method generates set of different convex points.
     * Each point is located on the circle with center at (0,0)
     * @param radius is the radius of the circle
     * @param numberOfPoints is the size of the set
     */
    public static List<Point> generateConvexPoints(int radius, int numberOfPoints) {

        if(numberOfPoints<3) {throw new IllegalArgumentException("Illegal argument. Number of points should be at least 3.");}
        Set<Point> uniquePoints = new HashSet<>();
        Random random = new Random();

        //randomized radius between 1 and 100
        if(radius==0) {
            radius = random.nextInt(99)+1;
        }

        while (uniquePoints.size() < numberOfPoints) {
            //generate an angle between 0° and 360°
            double randomAngle = random.nextDouble()*360;
            //convert degrees to radians
            double radians = Math.toRadians(randomAngle);

            //calculate coordinates of a point
            double x= radius*Math.cos(radians);
            double y = radius*Math.sin(radians);

            //add point to the set
            uniquePoints.add(new Point(x, y));
        }

        return new ArrayList<>(uniquePoints);
    }

    public static void main(String[] args) {
        List<Point> points = generateUniquePoints(4);
        System.out.println(points);
    }
}
