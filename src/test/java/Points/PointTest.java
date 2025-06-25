package Points;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
public class PointTest {
    @Test
    public void testToString() {
        Point point = new Point(1.12345, 2.67891);
        String expected = "(1.123, 2.679)";
        Assertions.assertEquals(expected, point.toString(), "Points.Point representation mismatch");
    }

    @Test
    public void testCalculateDistance() {
        Point point1 = new Point(0, 0);
        Point point2 = new Point(3, 4);

        double expectedDistance = 5.0;
        Assertions.assertEquals(expectedDistance, point1.calculateDistance(point2), "Distance mismatch");

        // Distance should be the same in reverse
        Assertions.assertEquals(expectedDistance, point2.calculateDistance(point1), "Symmetric distance mismatch");
    }

    @Test
    public void testCalculateDistanceNegative() {
        Point point1 = new Point(-1, -1);
        Point point2 = new Point(0, 0);

        // Verify the Euclidean distance
        double expectedDistance = Math.sqrt(2);
        Assertions.assertEquals(expectedDistance, point1.calculateDistance(point2), "Distance with negative coordinates mismatch");
    }
}
