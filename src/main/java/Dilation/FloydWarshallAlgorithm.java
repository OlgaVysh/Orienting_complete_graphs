package Dilation;
import java.util.Arrays;

/**
 * This class implements the Floyd-Warshall algorithm,
 * which is used to find the shortest directed paths between all pairs of vertices in a weighted graph.
 */
public class FloydWarshallAlgorithm {

    /**
     * Implementation of the Floyd-Warshall algorithm to calculate the shortest paths between all pairs of vertices.
     * @param adjacencyMatrix The adjacency matrix of the graph.
     * @return A matrix containing the shortest closed walks between all pairs of vertices.
     */
    public static double[][] floydWarshall(double [][] adjacencyMatrix) {

        double [][] distanceMatrix = createDistanceMatrix(adjacencyMatrix);
        int n = adjacencyMatrix.length;

        int i, j, k;

        for (k = 0; k < n; k++) {

            // Pick all vertices as source one by one
            for (i = 0; i < n; i++) {

                // Pick all vertices as destination for the
                // above picked source
                for (j = 0; j < n; j++) {

                    // If vertex k is on the shortest path
                    // from i to j, then update the value of
                    // dist[i][j]
                    if (distanceMatrix[i][k] + distanceMatrix[k][j]
                            < distanceMatrix[i][j])
                        distanceMatrix[i][j]
                                = distanceMatrix[i][k] + distanceMatrix[k][j];
                }
            }
        }
        return distanceMatrix;
    }

    /**
     * This method creates a distance matrix of a graph that it used by FloydWarshall algorithm.
     * The distance matrix D is created by copying and updating the adjacency matrix.
     * If there is a directed edge from vertex i to vertex j with weight w (distance between points[i] and points[j]),
     * then D[i][j] = A[i][j] and that is the weight(i,j).
     * If there is no edge from vertex i to vertex j, then: D[i][j] = INFINITY (99999).
     * @param adjacencyMatrix The adjacency matrix of the graph.
     * @return distance matrix.
     */
   public static double[][] createDistanceMatrix(double[][] adjacencyMatrix) {
       int n = adjacencyMatrix.length;
       double[][] distanceMatrix = new double[n][n];
       for(int i=0;i<n;i++) {

           for(int j=0;j<n;j++) {

               if(j!=i && adjacencyMatrix[i][j]==0) {
                   distanceMatrix[i][j] = 99999;
               }

               else distanceMatrix[i][j] = adjacencyMatrix[i][j];
           }
       }

       return distanceMatrix;
   }
}
