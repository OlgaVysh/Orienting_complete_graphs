package TwoApproximation;
import java.util.*;
import Points.Point;
import Orientation.OrientationMatrix;

/**
 * This class implements a basic Two-Approximation algorithm for orienting a complete graph based on a set of points.
 * It calculates an orientation matrix and encodes the graph orientation as a bit string.
 * It will attempt to orient a triangle only if at most one edge of the triangle is oriented.
 * The remaining edges are oriented random, not with bruteforce.
 *
 * The methods of this class are used in other versions of this algorithm.
 */

public class TwoApproximationAlgorithm {

    final static int INF = 99999;

    /**
     * Main method to orient the graph based on the input points.
     * @param points a list of points representing the vertices of the graph.
     * @return a bit string representing the orientation of the graph.
     */
    public static String twoApproximationAlgorithm(List<Point> points) {

        if(points.size()<3) {
            throw new IllegalArgumentException("Number of points must be at least three!");
        }

        // Initialize the orientation matrix
        int [][] orientationMatrix = OrientationMatrix.createMatrix(points);

        // Generate all possible triangles from the points
        List <Triangle> triangles = buildTriangles(points);
        // Sort triangles by their perimeter
        sortTriangles(triangles);

        // Process each triangle to orient its edges
        for (Triangle t : triangles) {
            orientTriangles(orientationMatrix,t);
        }

        //Orient remaining edges random
        // Encode the final orientation as a bit string
        return OrientationMatrix.orientGraph(orientationMatrix);
    }


    /**
     * Builds all possible triangles from the given set of points.
     * @param points a list of points representing the graph's vertices.
     * @return list of triangles.
     */
    public static List<Triangle> buildTriangles(List<Point> points) {
        int numberOfPoints = points.size();
        //build all possible triangles (i,j,k) where i<j<k
        //vertexes of such triangle (i,j,k) are indexes of the point set for the graph

        List<Triangle> triangles = new ArrayList<>();
        for (int i = 0; i < numberOfPoints; i++) {
            for (int j = i + 1; j < numberOfPoints; j++) {
                for (int k = j + 1; k < numberOfPoints; k++) {
                    triangles.add(Triangle.buildTriangle(points, i, j, k));
                }
            }
        }

        return triangles;
    }

    /**
     * Sorts the triangles in ascending order of their perimeter.
     * @param triangles is a list of triangles to be sorted.
     */
    public static void sortTriangles(List<Triangle> triangles) {
        triangles.sort(Comparator.comparingDouble(Triangle::perimeter));
    }

    /**
     * Processes a triangle and update its orientation in the matrix. Orients a triangle,
     * if no two edges of the triangle already oriented.
     * @param t the triangle to check and orient.
     * @param orientationMatrix the orientation matrix which holds orientation of all edges.
     */
    public static void orientTriangles(int [][] orientationMatrix,Triangle t) {

        //get every vertex of a triangle
        List<Integer> vertices = t.vertices();
        int a = vertices.get(0);
        int b = vertices.get(1);
        int c = vertices.get(2);

        //get orientations of all edges of a triangle from the matrix
        int ab = orientationMatrix[a][b];
        int ac = orientationMatrix[a][c];
        int bc = orientationMatrix[b][c];

        // If all edges are not oriented
        if (ab + ac + bc == 3 * INF) {
            Random random = new Random();
            int randomValue = random.nextInt(2);

            //Orient the first edge random
            orientationMatrix[a][b] = randomValue;

            // Orient triangle based on the oriented edge
            OrientationMatrix.orientTriangle(orientationMatrix,a, b, c);
        }

        // If exactly one edge is oriented
        else if (ab + ac + bc == 2 * INF + 1 || ab + ac + bc == 2 * INF) {
            //if ab oriented
            if (ab != INF) {
                OrientationMatrix.orientTriangle(orientationMatrix,a, b, c);// Orient triangle based on edge ab
            }
            //if ac oriented
            else if (ac != INF) {
                OrientationMatrix.orientTriangle(orientationMatrix,a, c, b);// Orient triangle based on edge ac
            }
            //if bc oriented
            else {
                OrientationMatrix.orientTriangle(orientationMatrix,b, c, a);// Orient triangle based on edge bc
            }
        }
        // Otherwise, more than one edge in the triangle is already oriented - skip triangle
    }

}
