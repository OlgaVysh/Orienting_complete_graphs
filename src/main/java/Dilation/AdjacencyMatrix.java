package Dilation;
import java.util.List;
import Points.Point;

/**
 * This class is responsible for creating and managing an adjacency matrix for a given directed graph
 * and checking the validity of input arguments for the all-pairs shortest path problem.
 * Adjacency Matrix is a square matrix of size n x n, where n is the number of vertices in the graph.
 * If there is a directed edge from vertex i to vertex j with weight w (distance between points[i] and points[j]), then:
 * A[i][j] = weight(i,j).
 * If there is no edge from vertex i to vertex j, then: A[i][j] = 0
 */
public class AdjacencyMatrix {
    /**
     * Creates adjacency matrix of a graph for a given point set and orientation
     * @param points A list of Points.Point objects representing the vertices of the graph.
     * @param orientation A string representing the orientation of edges.
     * @return adjacency matrix where
     * A[i][j] is marked with distance between i,j if edge i->j exists
     * and with 0 otherwise
     */
    public static double[][] createDistanceMatrix(List<Point> points, String orientation) {
        checkArguments(points,orientation);

        int numberOfPoints = points.size();
        double [][] matrix= new double[numberOfPoints][numberOfPoints];

        int index=0;

        for( int i=0;i<numberOfPoints-1;i++) {
            //fill diagonal with 0
            matrix[i][i]=0;

            for( int j=i+1;j<numberOfPoints;j++) {
                //get a bit for i->j with j>i
                char edge = orientation.charAt(index);

                if(edge == '1') {
                    //if edge i->j exists
                    matrix[i][j] = points.get(i).calculateDistance(points.get(j));
                    matrix[j][i] = 0;
                }

                else if(edge == '0') {
                    //j->i is saved
                    matrix[i][j] = 0;
                    matrix[j][i] = points.get(i).calculateDistance(points.get(j));

                }

                index++;
            }


        }
        return matrix;
    }

    /**
     * Validates the input arguments: the list of points and the orientation bit string.
     * It checks that the number of points is at least 3 and that the length of the bit string
     * matches the expected size based on the number of points.
     *
     * @param points A list of Point objects representing the vertices of the graph.
     * @param orientation A string representing the orientation of edges.
     * @throws IllegalArgumentException if the arguments are invalid (e.g., not enough points or mismatched bit string length).
     */
    private static void checkArguments(List<Point> points, String orientation) {
        int numberOfPoints = points.size();
        if(numberOfPoints<3) {
            throw new IllegalArgumentException("Number of points should be at least 3.");
        }

        int stringLength = numberOfPoints*(numberOfPoints-1)/2;

        if(orientation.length() != stringLength) {
            throw new IllegalArgumentException("Bit string doesn't match point set: "+orientation);
        }

    }

}

