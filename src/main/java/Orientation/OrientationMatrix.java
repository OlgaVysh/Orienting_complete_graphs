package Orientation;

import Points.Point;
import java.util.List;
import java.util.Random;


/**
 *  This class provides operations for an orientation matrix of the graph such as
 *  generating an empty orientation matrix, entering the orientation of a specific edge,
 *  entering the orientation of a triangle and decoding the matrix to a final bit string.
 */
public class OrientationMatrix
{
    final static int INF = 99999;

    /**
     * Creates the orientation matrix which will hold the orientation of a graph.
     * Initializes it with INF values (no edge has been oriented yet).
     * @param points a list of points representing the vertexes of a graph.
     * @return the orientation matrix
     */
    public static int [][] createMatrix(List<Point> points) {
        int numberOfPoints = points.size();
        int [][] orientationMatrix = new int[numberOfPoints][numberOfPoints];

        for (int i = 0; i < numberOfPoints; i++) {
            for (int j = 0; j < numberOfPoints; j++) {
                // Assign the INF value to each cell because no edge gas been oriented yet
                orientationMatrix[i][j] = INF;
            }
        }
        return orientationMatrix;
    }


    /**
     * Encodes the final orientation matrix from the orientation matrix as a bit string.
     * Randomly assigns orientation to edges that remain not oriented.
     * @param orientationMatrix the orientation matrix which holds orientation of a graph.
     * @return a bit string representing the graph orientation.
     */
    public static String orientGraph(int [][] orientationMatrix) {
        int numberOfEdges = orientationMatrix.length;
        StringBuilder bitString = new StringBuilder();
        Random random = new Random();

        //Add every edge from matrix to the bit string
        for (int i = 0; i < numberOfEdges; i++) {
            for (int j = i + 1; j < numberOfEdges; j++) {
                //if this edge was not oriented through a triangle
                if (orientationMatrix[i][j] == INF) {
                    // Add random orientation (0 or 1) for this edge to the bit string
                    bitString.append(random.nextInt(2));
                }

                else {
                    bitString.append(orientationMatrix[i][j]);
                }
            }
        }

        return bitString.toString();
    }


    /**
     * Orients a triangle by a fixed edge (start->end) and determining the other two edges.
     * @param orientationMatrix the orientation matrix which holds orientation of all edges.
     * @param startVertex the starting vertex of the fixed edge.
     * @param endVertex the ending vertex of the fixed edge.
     * @param middleVertex the third vertex of the triangle.
     */
    public static void orientTriangle(int [][] orientationMatrix, int startVertex, int endVertex, int middleVertex) {
        //check if other two edges of the triangle are oriented (needed for GreedyAlgorithm)

         int secondEdge = orientationMatrix[startVertex][middleVertex] + orientationMatrix[middleVertex][startVertex];
         int thirdEdge = orientationMatrix[endVertex][middleVertex] + orientationMatrix[middleVertex][endVertex];

        //only orient the triangles if other two edges are not oriented
         if(secondEdge+thirdEdge==4*INF) {
            //get the value the orientation of edge start -> end
            int fixedEdge = orientationMatrix[startVertex][endVertex];


            //if start->end
            if (fixedEdge == 1) {
                OrientationMatrix.addEdge(orientationMatrix,endVertex, middleVertex);
                OrientationMatrix.addEdge(orientationMatrix,middleVertex, startVertex);
            }

            //if start<-end
            else {
                OrientationMatrix.addEdge(orientationMatrix,startVertex, middleVertex);
                OrientationMatrix.addEdge(orientationMatrix,middleVertex, endVertex);
            }
         }
    }

    /**
     * Adds an oriented edge to the orientation matrix.
     * @param orientationMatrix the orientation matrix which holds orientation of all edges.
     * @param startVertex the starting vertex of the edge.
     * @param endVertex the ending vertex of the edge.
     */
    public static void addEdge(int [][] orientationMatrix, int startVertex, int endVertex) {

        //we only fill the right half of the matrix where matrix[i][j] implies i<j

        //save edge i->j in the matrix as matrix[i][j]=1
        if (startVertex < endVertex) {
            orientationMatrix[startVertex][endVertex] = 1;
        }

        //save edge j->i in the matrix as matrix[i][j]=0
        else {
            orientationMatrix[endVertex][startVertex] = 0;

        }
    }
}
