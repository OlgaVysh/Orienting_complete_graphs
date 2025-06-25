package TwoApproximation;

import Dilation.OrientedDilation;
import GreedyVersions.TwoApproxTwo;
import Points.Point;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.*;

public class TwoApproximationTest {

    final int INF = 99999;

    @Test
    public void testTwoApproximationAlgorithm_InsufficientPoints() {

        List<Point> points = Arrays.asList(
                new Point(0, 0),
                new Point(1, 1)
        );
        Exception exception = assertThrows(IllegalArgumentException.class,
                () -> TwoApproximationAlgorithm.twoApproximationAlgorithm(points));

        assertEquals("Number of points must be at least three!", exception.getMessage());
    }

    @Test
    public void testBuildTrianglesWithThreePoints() {
        List<Point> points = Arrays.asList(
                new Point(0, 0),
                new Point(3, 0),
                new Point(0, 4)
        );

        List<Triangle> triangles = TwoApproximationAlgorithm.buildTriangles(points);

        assertEquals(1, triangles.size(), "Only one triangle should be possible with 3 points");
        Triangle triangle = triangles.get(0);
        assertEquals(Arrays.asList(0, 1, 2), triangle.vertices(), "TwoApproximation.Triangle vertices do not match");
    }


    @Test
    public void buildTrianglesTest() {
        List<Point> points = Arrays.asList(
                new Point(0, 0),
                new Point(2, 0),
                new Point(0, 3),
                new Point(3, 4)
        );
        List<Triangle> triangles = TwoApproximationAlgorithm.buildTriangles(points);

        assertEquals(4, triangles.size(), "Number of triangles does not match.");

        Triangle t1 = new Triangle(Arrays.asList(0, 1, 2), 8.60555127546399);
        Triangle t2 = new Triangle(Arrays.asList(0, 1, 3), 11.123105625617661);
        Triangle t3 = new Triangle(Arrays.asList(0, 2, 3), 11.16227766016838);
        Triangle t4 = new Triangle(Arrays.asList(1, 2, 3), 10.89093456125003);

        assertTrue(t1.equals(triangles.get(0)),"First triangle does not match.");
        assertTrue(t2.equals(triangles.get(1)),"Second triangle does not match.");
        assertTrue(t3.equals(triangles.get(2)),"Third triangle does not match.");
        assertTrue(t4.equals(triangles.get(3)),"Fourth triangle does not match.");
    }

    @Test
    public void sortTrianglesTest() {
        List<Point> points = Arrays.asList(
                new Point(0, 0),
                new Point(2, 0),
                new Point(0, 3),
                new Point(3, 4)
        );
        List<Triangle> triangles = TwoApproximationAlgorithm.buildTriangles(points);
        TwoApproximationAlgorithm.sortTriangles(triangles);

        for (int i = 0; i < triangles.size() - 1; i++) {
            assertTrue(
                    triangles.get(i).perimeter() <= triangles.get(i + 1).perimeter(),
                    "Triangles are not sorted by perimeter"
            );
        }
    }

        @Test
        public void checkTrianglesTest() {
            int inf = INF;

            Triangle t1 = new Triangle(Arrays.asList(0, 1, 2), 8.60555127546399);

            int [][] orientationMatrix = {
                    {inf,  0 , inf, inf},   //edge 1->0 oriented
                    {inf, inf, inf, inf},
                    {inf, inf, inf, inf},
                    {inf, inf, inf, inf},
            };

            //triangle(0,1,2) orientation fixed by 1->0 : add edges 0->2 and 2->1
            TwoApproximationAlgorithm.orientTriangles(orientationMatrix,t1);
            assertEquals(1, orientationMatrix[0][2],"Greedy.Edge 0->2 wrong.");
            assertEquals(0, orientationMatrix[1][2], "Greedy.Edge 2->1 wrong.");
        }


    @Test
    public void twoApproximationAlgorithmTest() {
        List<Point> points = Arrays.asList(
                new Point(0, 0),
                new Point(2, 0),
                new Point(0, 3),
                new Point(3, 4)
        );

        String orientation = TwoApproximationAlgorithm.twoApproximationAlgorithm(points);

        String expectedOrientation1 = "100101";
        String expectedOrientation2 = "101101";
        String expectedOrientation3 = "011010";
        String expectedOrientation4 = "010010";

        assertTrue(orientation.equals(expectedOrientation1) || orientation.equals(expectedOrientation2)
                ||orientation.equals(expectedOrientation3) || orientation.equals(expectedOrientation4),
                "Orientations do not match.");
    }

    @Test
    public void twoApproximationAlgorithmDilationTest() {
        List<Point> points = Arrays.asList(
                new Point(0, 0),
                new Point(2, 0),
                new Point(0, 3),
                new Point(3, 4)
        );
        String orientation = TwoApproximationAlgorithm.twoApproximationAlgorithm(points);
        double odil = OrientedDilation.calculateOrientedDilation(points,orientation);

        assertTrue(odil <= 2);
    }

    @Test
    public void orientAbstractTriangleTest() {
        int [][] orientationMatrix = {
                {INF,  0 , 1},   //edge 1->0 oriented
                {INF, INF, INF},
                {INF, INF, INF}
        };

        TwoApproxTwo.orientAbstractTriangle(orientationMatrix,0,1,2);

        int [][] expected = {
                {INF,  0 , 1},   //edge 1->0 oriented
                {INF, INF, 0},
                {INF, INF, INF}
        };
        assertTrue(Arrays.deepEquals(orientationMatrix,expected));
    }

    @Test
    public void checkTrianglesTwoTest() {

        Triangle t1 = new Triangle(Arrays.asList(0, 1, 2), 8.60555127546399);

        int [][] orientationMatrix = {
                {INF,  0 , 1},   //edge 1->0 oriented
                {INF, INF, INF},
                {INF, INF, INF},
        };

        TwoApproxTwo.orientTriangles(orientationMatrix,t1);

        int [][] expected = {
                {INF,  0 , 1},   //edge 1->0 oriented
                {INF, INF, 0},
                {INF, INF, INF}
        };
        assertTrue(Arrays.deepEquals(orientationMatrix,expected));

    }

    @Test
    public void checkGenerateAllStringsTest() {
        int [][] orientationMatrix = {
                {INF, INF, INF , INF},   //edge 1->0 oriented
                {INF, INF, INF, INF},
                {INF, INF, INF, INF},
                {INF, INF, INF, INF},
        };

        List<String> results = ApproxBrute.generateAllAdjacencyMatrixStrings(orientationMatrix);
        double size = Math.pow(2,6);
        assertEquals(size,results.size());
    }
}
