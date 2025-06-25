package Orientation;

import Points.Point;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;

public class OrientationMatrixTest {
    final int INF = 99999;

    @Test
    public void createMatrixTest() {
        List<Point> points = Arrays.asList(
                new Point(0, 0),
                new Point(3, 0),
                new Point(0, 4)
        );

        int[][] matrix = OrientationMatrix.createMatrix(points);

        assertEquals(3, matrix.length, "Matrix size does not match the number of points");
        for (int[] row : matrix) {
            for (int value : row) {
                assertEquals(INF, value, "Matrix value should be initialized to INF");
            }
        }
    }

    @Test
    public void addEdgeTest()
    {
        int [][] orientationMatrix = {
                {INF,  1 , 0, INF},
                {INF, INF, 1,   0},
                {INF, INF, INF, INF},
                {INF, INF, INF, INF},
        };

        OrientationMatrix.addEdge(orientationMatrix,0,3);
        assertEquals(1, orientationMatrix[0][3]);

        OrientationMatrix.addEdge(orientationMatrix,3,2);
        assertEquals(0, orientationMatrix[2][3]);
    }

    @Test
    public void orientTriangleTest() {
        int[][] orientationMatrix = {
                {INF, 1, INF, INF},
                {INF, INF, INF, INF},
                {INF, INF, INF, INF},
                {INF, INF, INF, INF},
        };

        OrientationMatrix.orientTriangle(orientationMatrix, 0, 1, 2);

        //with one edge oriented
        int[][] expectedMatrix = {
                {INF, 1, 0, INF},
                {INF, INF, 1, INF},
                {INF, INF, INF, INF},
                {INF, INF, INF, INF},
        };

        assertArrayEquals(orientationMatrix,expectedMatrix,"wrong orientation");

        //with two edges oriented - skip triangle
        orientationMatrix[1][3]=0;
        expectedMatrix[1][3]=0;

        OrientationMatrix.orientTriangle(orientationMatrix, 0, 1, 3);
        assertArrayEquals(orientationMatrix,expectedMatrix,"wrong orientation");
    }

    @Test
    public void orientGraphTest()
    {
        int [][] orientationMatrix = {
                {INF,  1 , 0, INF},
                {INF, INF, 1,   0},
                {INF, INF, INF, 1},
                {INF, INF, INF, INF},
        };
        String orientation = OrientationMatrix.orientGraph(orientationMatrix);

        assertTrue(orientation.equals("100101") || orientation.equals("101101"),
                "Orientation String does not match orientation matrix.");
    }

}
