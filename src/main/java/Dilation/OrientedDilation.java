package Dilation;
import java.util.Arrays;
import java.util.List;
import Points.Point;


/**
 * This class implements calculations related to "oriented dilation," which involves analyzing the geometry of points
 * in a graph. Specifically, it calculates values related to closed walks, triangles, dilation, and max dilation.
 */
public class OrientedDilation {
    final static int INF = 99999;

    /**
     * Calculates the maximum dilation in the entire graph.
     * This method loops through all pairs of points and computes the dilation for each pair,
     * returning the maximum value found.
     *
     * @param points A list of Points.Point objects representing the vertices of the graph.
     * @param orientation is a bit string representing an orientation of a complete graph on this point set
     * @return The maximum dilation value in the graph.
     */
    public static double calculateOrientedDilation(List<Point> points, String orientation) {
        //build an adjacency matrix for the graph
        double[][] adjacencyMatrix = AdjacencyMatrix.createDistanceMatrix(points,orientation);

        int numberPfPoints = points.size();
        double maxDilation = 0;

        //loop through the top half of the matrix
        for(int i=0;i<numberPfPoints;i++) {

            for(int j=i+1;j<numberPfPoints;j++) {
                //calculate dilation for every pair of points
                double dilation = calculateDilation(adjacencyMatrix,i,j);
                //save the biggest dilation
                maxDilation = Math.max(maxDilation,dilation);
            }
        }
        return maxDilation;
    }

    /**
     * Calculates the oriented dilation between two points.
     * @param adjacencyMatrix An n×n matrix which for every pair of vertexes i,j holds
     * the distance between them if there is a directed edge i->j and 0 otherwise.
     * @param i The index of the first point.
     * @param j The index of the second point.
     * @return the ratio of the length of the shortest closed walk through i,j to the perimeter of
     * the smallest triangle through i,j.
     */
    public static double calculateDilation(double [][] adjacencyMatrix, int i, int j)
    {
        if(i==j) return INF;

        else {
            double walk = calculateClosedWalk(adjacencyMatrix,i,j);
            double triangle = calculateTriangle(adjacencyMatrix, i,j);
            return walk/triangle;
        }
    }


    /**
     * Calculates the closed walk between two points in the distance matrix.
     * A closed walk is the sum of the distance from point i to point j and the distance from j to i.
     * If the points are the same, the distance is zero.
     *
     * @param adjacencyMatrix @param adjacencyMatrix An n×n matrix which for every pair of vertexes i,j holds
     * the distance between them if there is a directed edge i->j and 0 otherwise.
     * @param i The index of the first point.
     * @param j The index of the second point.
     * @return The length of a closed walk through i and j
     */
    public static double calculateClosedWalk(double [][] adjacencyMatrix, int i, int j)
    {
        if(i==j) return 0;

        else {
            //build an all-to-all shortest path matrix
            double[][] distanceMatrix = FloydWarshallAlgorithm.floydWarshall(adjacencyMatrix);
            //calculate closed walk i->j->i
            return distanceMatrix[i][j] + distanceMatrix[j][i];
        }
    }

    /**
     * Calculates the perimeter of the smallest triangle through points i,j.
     * @param adjacencyMatrix @param adjacencyMatrix An n×n matrix which for every pair of vertexes i,j holds
     * the distance between them if there is a directed edge i->j and 0 otherwise.
     * @param i The index of the first point.
     * @param j The index of the second point.
     * @return The perimeter of the smallest triangle through points i,j.
     */
    public static double calculateTriangle(double[][] adjacencyMatrix, int i, int j)
    {
        if(i==j) return INF;

        else {
            //get the length of the side i-j
            double ijEdge = adjacencyMatrix[i][j] + adjacencyMatrix[j][i];

            double minDistance=INF;

            //loop through all points
            for(int k=0;k<adjacencyMatrix.length;k++) {

                if(k!=i && k!=j) {
                    //determine length of sides j-k and k-i
                    double kiEdge = adjacencyMatrix[i][k] + adjacencyMatrix[k][i];
                    double jkEdge = adjacencyMatrix[j][k] + adjacencyMatrix[k][j];
                    double distance = kiEdge + jkEdge;

                    //save the smallest sum of lengths
                    minDistance = Math.min(minDistance,distance);
                }
            }
            //calculate the smallest perimeter as length(i,j) + minimal sum of (i,k) and (j,k)
            return ijEdge + minDistance;
        }
    }

    public static void main(String[] args)
    {
        List<Point> points = Arrays.asList(
                new Point(0, 0),
                new Point(1, 0),
                new Point(0, 1),
                new Point(1, 1)
        );

        String orientation = "101010";
        double odil =calculateOrientedDilation(points,orientation);
        System.out.println("point-set: "+points+", orientation: "+orientation+", dilation: "+odil);
}
}
