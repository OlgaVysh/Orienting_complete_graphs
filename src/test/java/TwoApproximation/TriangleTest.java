package TwoApproximation;

import Points.Point;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.*;


public class TriangleTest {

    @Test
    public void testBuildTriangle() {

        List<Point> points = Arrays.asList(
                new Point(0, 0),
                new Point(3, 0),
                new Point(0, 4)
        );

        Triangle triangle = Triangle.buildTriangle(points, 0, 1, 2);

        assertNotNull(triangle, "TwoApproximation.Triangle should not be null");
        assertEquals(Arrays.asList(0, 1, 2), triangle.vertices(), "Vertices do not match");
        assertEquals(12, triangle.perimeter(), "Perimeter does not match");
    }

    @Test
    public void testEquals() {

        List<Point> points = Arrays.asList(
                new Point(0, 0),
                new Point(3, 0),
                new Point(0, 4),
                new Point(5, 0)
        );

        Triangle triangle1 = Triangle.buildTriangle(points, 0, 1, 2);
        Triangle triangle2 = Triangle.buildTriangle(points, 1, 2, 3);


        assertFalse(triangle1.equals(triangle2), "Triangles should not be equal");
        assertTrue(triangle1.equals(Triangle.buildTriangle(points, 0, 1, 2)),"Triangles should be equal");
    }

    @Test
    public void testCheckArgumentsInsufficientPoints() {

        List<Point> points = Arrays.asList(
                new Point(0, 0),
                new Point(1, 1)
        );


        Exception exception = assertThrows(IllegalArgumentException.class, () -> Triangle.checkArguments(points, 0, 1, 2));
        assertEquals("Number of points should be at least 3.", exception.getMessage(), "Exception message does not match");
    }

    @Test
    public void testCheckArgumentsInvalidIndexes() {

        List<Point> points = Arrays.asList(
                new Point(0, 0),
                new Point(1, 1),
                new Point(2, 2),
                new Point(3, 3)
        );

        Exception exception1 = assertThrows(IllegalArgumentException.class, () -> Triangle.checkArguments(points, -1, 1, 2));
        assertEquals("Indexes are out of range or order.", exception1.getMessage());

        Exception exception2 = assertThrows(IllegalArgumentException.class, () -> Triangle.checkArguments(points, 0, 0, 2));
        assertEquals("Indexes are out of range or order.", exception2.getMessage());

        Exception exception3 = assertThrows(IllegalArgumentException.class, () -> Triangle.checkArguments(points, 0, 2, 1));
        assertEquals("Indexes are out of range or order.", exception3.getMessage());

        Exception exception4 = assertThrows(IllegalArgumentException.class, () -> Triangle.checkArguments(points, 0, 1, 4));
        assertEquals("Indexes are out of range or order.", exception4.getMessage());
    }
}

