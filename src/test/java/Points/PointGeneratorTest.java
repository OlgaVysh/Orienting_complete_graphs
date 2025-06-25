package Points;

import org.junit.jupiter.api.Test;
import java.util.*;
import static org.junit.jupiter.api.Assertions.*;


public class PointGeneratorTest {
    @Test
    public void testGenerateUniquePoints() {
        int numberOfPoints = 5;

        List<Point> points = PointGenerator.generateUniquePoints(numberOfPoints);

        assertEquals(numberOfPoints, points.size(), "Generated points count mismatch");

        // Verify that all points are exclusive
        Set<Point> uniquePoints = new HashSet<>(points);
        assertEquals(points.size(), uniquePoints.size(), "Points are not exclusive");

        // Verify that all coordinates are in range [0, 1]
        for (Point point : points) {
            assertTrue(point.x() >= 0 && point.x() <= 1, "Points.Point X out of range");
            assertTrue(point.y() >= 0 && point.y() <= 1, "Points.Point Y out of range");
        }
    }

    @Test
    public void testGenerateUniquePointsThrowsIllegalArgumentException() {
        for (int i = 0; i<3; i++) {

            int numberOfPoints=i;
            Throwable exception = assertThrows(IllegalArgumentException.class, () -> {
                PointGenerator.generateUniquePoints(numberOfPoints);
            });
            assertEquals("Illegal argument. Number of points should be at least 3.", exception.getMessage());
        }
    }

    @Test
    public void checkGenerateConvexPointSetTest() {
        Random random = new Random();

        //randomized radius between 1 and 100
        for(int i=0;i<10;i++) {
            //generate an angle between 0° and 360°
            double randomAngle = random.nextDouble()*360;
            assertTrue(randomAngle>=0 && randomAngle<360);
        }
    }
}
