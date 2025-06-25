package TwoApproximation;

import java.util.ArrayList;
import java.util.List;
import Points.Point;

/**
 * This class represents a triangle used by 2 approximation algorithm.
 *
 * @param perimeter The perimeter of the triangle
 * @param vertices  The vertices of the triangle
 */
public record Triangle(List<Integer> vertices, double perimeter) {

    /**
     * Static method to build a TwoApproximation.Triangle object based on three points.
     *
     * @param points the set of points
     * @param i      the index of the first point (first vertex of the triangle)
     * @param j      the index of the second (second vertex of the triangle)
     * @param k      the index of the third vertex (third vertex of the triangle)
     * @return a TwoApproximation.Triangle object created from the given indices
     */
    public static Triangle buildTriangle(List<Point> points, int i, int j, int k) {
        checkArguments(points, i, j, k);

        // Create a list to store the indices of the vertices
        List<Integer> vertices = new ArrayList<>();
        vertices.add(i);
        vertices.add(j);
        vertices.add(k);

        //Retrieve the points corresponding to the given indices
        Point p1 = points.get(i);
        Point p2 = points.get(j);
        Point p3 = points.get(k);

        // Calculate the perimeter of the triangle
        double perimeter = p1.calculateDistance(p2) +
                p1.calculateDistance(p3) +
                p2.calculateDistance(p3);

        // Return a new TwoApproximation.Triangle object
        return new Triangle(vertices, perimeter);
    }

    public static void checkArguments(List<Point> points, int i, int j, int k) {
        if (points.size() < 3) {
            throw new IllegalArgumentException("Number of points should be at least 3.");
        }

        if (i < 0 || j <= i || k <= j || k >= points.size()) {
            throw new IllegalArgumentException("Indexes are out of range or order.");
        }

    }

    @Override
    public String toString() {
        return "TwoApproximation.Triangle: " + vertices.toString() + ", perimeter : " + perimeter;
    }


    public boolean equals(Triangle t) {
        return this.vertices.equals(t.vertices) && this.perimeter == t.perimeter;

    }
}
