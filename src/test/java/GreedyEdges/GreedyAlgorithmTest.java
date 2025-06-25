package GreedyEdges;

import Points.Point;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class GreedyAlgorithmTest {
    final static int INF = 99999;

    @Test
    public void buildEdgesListTest() {
        List<Point> points = Arrays.asList(
                new Point(0, 0),
                new Point(0, 1),
                new Point(1, 0),
                new Point(1, 1)
        );

        List<Edge> expectedEdges = Arrays.asList(
                new Edge(0,1,1),
                new Edge(0,2,1),
                new Edge(1,3,1),
                new Edge(2,3,1),
                new Edge(0,3,Math.sqrt(2)),
                new Edge(1,2,Math.sqrt(2))
        );

        List<Edge> edges = GreedyAlgorithm.buildEdgesList(points);

        for(int i=0;i< edges.size();i++) {
           assertTrue(edges.get(i).equals(expectedEdges.get(i)));
        }

    }


    @Test
    public void orientTrianglesTest() {
        int[][] orientationMatrix = {
                {INF, INF, INF, INF},
                {INF, INF, INF, INF},
                {INF, INF, INF, INF},
                {INF, INF, INF, INF},
        };
        List<Edge> edges = Arrays.asList(
                new Edge(0,1,1),
                new Edge(1,2,2.236),
                new Edge(0,2,2.8),
                new Edge(2,3,3.354),
                new Edge(1,3,4.031),
                new Edge(0,3,5.025)
        );

        GreedyAlgorithm.orientTriangles(edges,orientationMatrix);

        //get orientation of the first edge that is oriented random
       int orientationOfFirstEdge = orientationMatrix[0][1];

       if(orientationOfFirstEdge==0) {
           assertArrayEquals(orientationMatrix[0], new int[]{INF, 0, 1, 1});
           assertArrayEquals(orientationMatrix[1], new int[]{INF, INF, 0, 0});
           assertTrue(Arrays.equals(orientationMatrix[2], new int[]{INF, INF, INF, 0}) ||
                   Arrays.equals(orientationMatrix[2], new int[]{INF, INF, INF, 1}) );
           assertArrayEquals(orientationMatrix[3], new int[]{INF, INF, INF, INF});
       }

        if(orientationOfFirstEdge==1) {
            assertArrayEquals(orientationMatrix[0], new int[]{INF, 1, 0, 0});
            assertArrayEquals(orientationMatrix[1], new int[]{INF, INF, 1, 1});
            assertTrue(Arrays.equals(orientationMatrix[2], new int[]{INF, INF, INF, 0}) ||
                    Arrays.equals(orientationMatrix[2], new int[]{INF, INF, INF, 1}) );
            assertArrayEquals(orientationMatrix[3], new int[]{INF, INF, INF, INF});
        }

    }
}
