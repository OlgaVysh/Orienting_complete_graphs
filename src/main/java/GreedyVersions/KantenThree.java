package GreedyVersions;

import Dilation.OrientedDilation;
import GreedyEdges.Edge;
import Orientation.OrientationMatrix;
import Points.Point;

import java.util.*;

/**
 * This class implements yet another version of the "Gierige Kanten" algorithm.
 * It orients every edge in ascending order of length random (if not oriented yet) and
 * orients every triangle with this edge consistently if it is still possible (the main
 * version orients only triangles with two unoriented edges).
 * It performs similar to the GreedyAlgorithm.
 */
public class KantenThree {
    final static int INF = 99999;

    public static double orient(List<Point> points) {
        //create sorted list of all edges
        List<Edge> edges = buildEdgesList(points);

        //initialize orientation matrix
        int[][] orientationMatrix = OrientationMatrix.createMatrix(points);

        //orient triangles and update orientation matrix
        orientTriangles(edges, orientationMatrix);

        //decrypt the final orientation
        String orientation = OrientationMatrix.orientGraph(orientationMatrix);
        return OrientedDilation.calculateOrientedDilation(points, orientation);
    }


    /**
     * This method creates a list of all edges of a complete graph on a set of points. For every pair of points
     * an edge object is created. The list is then sorted in ascending order of length of the edges.
     *
     * @param points a list of points representing the graph's vertices.
     * @return the sorted list of all edges.
     */
    public static List<Edge> buildEdgesList(List<Point> points) {
        int numberOfPoints = points.size();
        List<Edge> edges = new ArrayList<>();

        for (int i = 0; i < numberOfPoints; i++) {
            for (int j = i + 1; j < numberOfPoints; j++) {
                //create a n edge object
                Point p1 = points.get(i);
                Point p2 = points.get(j);
                double weight = p1.calculateDistance(p2);
                edges.add(new Edge(i, j, weight));
            }
        }
        //sort the edges by length
        edges.sort(Comparator.comparingDouble(Edge::getWeight));
        return edges;
    }


    /**
     * This method processes a list of edges in ascending order of their length, orients
     * the edges that are not yet oriented, and adjusts the orientation of all triangles
     * associated with each edge.
     *
     * @param edges             A list of edges in the graph sorted in ascending order of length.
     * @param orientationMatrix the orientation matrix which holds orientation of all edges.
     */
    public static void orientTriangles(List<Edge> edges, int[][] orientationMatrix) {
        //for every edge in ascending order of length
        for (Edge edge : edges) {
            //get the end points
            int i = edge.getStart();
            int j = edge.getEnd();

            //orient edge (random) if not oriented yet
            if (orientationMatrix[i][j] == INF) {
                Random random = new Random();
                int randomValue = random.nextInt(2);

                orientationMatrix[i][j] = randomValue;
            }

            //orient every triangle with edge (i,j) fixed by this edge
            for (int a = 0; a < orientationMatrix.length; a++) {
                if (a != i && a != j) {
                    orientTriangle(orientationMatrix, i, j, a);
                }
            }

        }

    }

    public static void orientTriangle(int[][] orientationMatrix, int i, int j, int k) {
        int[] vertexes = new int[]{i, j, k};
        Arrays.sort(vertexes);

        int a = vertexes[0];
        int b = vertexes[1];
        int c = vertexes[2];

        int ab = orientationMatrix[a][b];
        int bc = orientationMatrix[b][c];
        int ac = orientationMatrix[a][c];

        double sum = ab + bc + ac;

        //if one edge is oriented - orient by this edge
        if (sum == 1 + 2 * INF || sum == 2 * INF) {
            OrientationMatrix.orientTriangle(orientationMatrix, i, j, k);
        }

        //if two edges or more are oriented - try to orient consistently
        else if (INF <= sum && sum <= INF + 2) {
            orientAbstractTriangle(orientationMatrix, a, b, c);
        }

    }

    public static void orientAbstractTriangle(int[][] orientationMatrix, int a, int b, int c) {

        int edgeOne = orientationMatrix[a][b];
        int edgeTwo = orientationMatrix[b][c];
        int edgeThree = orientationMatrix[a][c];


        if (edgeOne == edgeTwo && edgeThree == INF) {
            orientationMatrix[a][c] = 1 - edgeOne;
        }
        else if (edgeOne == 1 - edgeThree && edgeTwo == INF) {
            orientationMatrix[b][c] = edgeOne;
        }
        else if (edgeTwo == 1 - edgeThree && edgeOne == INF) {
            orientationMatrix[a][b] = edgeTwo;

        }

    }
}
