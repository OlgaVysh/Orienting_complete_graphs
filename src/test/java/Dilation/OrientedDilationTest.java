package Dilation;

import Points.Point;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class OrientedDilationTest {

    final int INF = 99999;

    List<Point> points = Arrays.asList(
            new Point(1, 0),
            new Point(1, 1),
            new Point(0, 0),
            new Point(0, 1)
    );

    int numberOfPoints = 4;
    final double sqrt = Math.sqrt(2);

    double[][] adjacencyMatrix = {
            {0,1,0,Math.sqrt(2)},
            {0,0,Math.sqrt(2),0},
            {1,0,0,1},
            {0,1,0,0}

    };
    @Test
    public void calculateTriangleTest() {
        double triangle = 1 + 1 + Math.sqrt(2);
        for (int i = 0; i < numberOfPoints; i++) {
            for (int j = i + 1; j < numberOfPoints; j++) {
                assertEquals(triangle, OrientedDilation.calculateTriangle(adjacencyMatrix, i, j), "Wrong triangle for " + i + " , " + j);
                assertEquals(triangle, OrientedDilation.calculateTriangle(adjacencyMatrix, j, i), "Wrong triangle for " + j + " , " + i);
            }
        }
    }

    @Test
    public void calculateClosedWalkTest()
    {

        for (int i = 0; i < numberOfPoints; i++) {
            for (int j = 0; j < numberOfPoints; j++) {

                if(i==j) {assertEquals(0, OrientedDilation.calculateClosedWalk(adjacencyMatrix,i,j));}

                else if( (i==0 && j==3) || (i==3 && j==0)) {
                    assertEquals(2+2*Math.sqrt(2), OrientedDilation.calculateClosedWalk(adjacencyMatrix,i,j));
                }

                else {
                    assertEquals(2+Math.sqrt(2), OrientedDilation.calculateClosedWalk(adjacencyMatrix,i,j));
                }
            }
        }
    }

    @Test
    public void calculateDilationTest()
    {
        FloydWarshallAlgorithm.floydWarshall(adjacencyMatrix);
        for (int i = 0; i < numberOfPoints; i++) {
            for (int j = 0; j < numberOfPoints; j++) {

                if(i==j) {assertEquals(INF, OrientedDilation.calculateDilation(adjacencyMatrix,i,j));}

                else if( (i==0 && j==3) || (i==3 && j==0)) {
                    double closedWalk = 2+2*Math.sqrt(2);
                    double triangle = 2+Math.sqrt(2);
                    assertEquals(closedWalk/triangle, OrientedDilation.calculateDilation(adjacencyMatrix,i,j));
                }

                else {
                    assertEquals(1, OrientedDilation.calculateDilation(adjacencyMatrix,i,j));
                }
            }
        }
    }


    @Test
    public void calculateMaxDilationTest()
    {
        double closedWalk = 4;
        double triangle = 2+Math.sqrt(2);
        double maxDilation = closedWalk/triangle;

        assertEquals( maxDilation, OrientedDilation.calculateOrientedDilation(points,"010001") );
    }


}