package GreedyVersions;

import Dilation.OrientedDilation;
import Orientation.OrientationMatrix;
import Points.Point;
import TwoApproximation.Triangle;
import TwoApproximation.TwoApproximationAlgorithm;

import java.util.*;

/**
 * This class implements another version of the 2-approximation algorithm.
 * It orients a triangle consistently, if it is possible (by one or two edges).
 * This version performs worse that the main version.
 */

public class TwoApproxTwo {
    final static int INF = 99999;
    public static double twoApproximationAlgorithm(List<Point> points) {

        if(points.size()<3) {
            throw new IllegalArgumentException("Number of points must be at least three!");
        }

        // Initialize the orientation matrix
        int [][] orientationMatrix = OrientationMatrix.createMatrix(points);

        // Generate all possible triangles from the points
        List <Triangle> triangles = TwoApproximationAlgorithm.buildTriangles(points);
        // Sort triangles by their perimeter
        TwoApproximationAlgorithm.sortTriangles(triangles);

        // Process each triangle to orient its edges
        for (Triangle t : triangles) {
            //orient consistently if still possible
            orientTriangles(orientationMatrix,t);
        }

        //bruteforce through remaining edges
        List<String> allOrientations = generateAllAdjacencyMatrixStrings(orientationMatrix);

        double minDilation = INF;

        for(String orientation : allOrientations) {
            double dilation = OrientedDilation.calculateOrientedDilation(points,orientation);
            minDilation = Math.min(minDilation,dilation);
        }

        return minDilation;
    }

    /**
     * Processes a triangle and update its orientation in the matrix. Orients a triangle,
     * if it still can be oriented consistently.
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

        //if two edges are oriented
        else if(INF<=ab+bc+ac && ab+bc+ac<=INF+2) {
            //orient by two edges
            orientAbstractTriangle(orientationMatrix,a,b,c);
        }

        // Otherwise, the triangle is completely oriented
    }

    /**
     * This method processes a triangle (i,j,k) where two edges are already oriented and orients its
     * consistently if it is still possible
     */
    public static void orientAbstractTriangle(int [][] orientationMatrix, int i, int j, int k) {
        int edgeOne = orientationMatrix[i][j];
        int edgeTwo = orientationMatrix[j][k];
        int edgeThree = orientationMatrix[i][k];

        if(edgeOne==edgeTwo && edgeThree==INF) {
            orientationMatrix[i][k]= 1-edgeOne;
        }

        else if(edgeOne==1-edgeThree && edgeTwo==INF) {
            orientationMatrix[j][k]=edgeOne;
        }

        else if(edgeTwo==1-edgeThree && edgeOne==INF) {
            orientationMatrix[i][j]= edgeTwo;
        }
    }

    /**
     * This is the main method that processes a matrix to generate all possible orientations
     */
    public static List<String> generateAllAdjacencyMatrixStrings(int[][] matrix) {
        Set<String> results = new HashSet<>();
        generateStrings(matrix, 0, 1, results);
        return new ArrayList<>(results);
    }

    /**
     * This method generates all orientations to bruteforce through the remaining edges
     */
    private static void generateStrings(int[][] matrix, int row, int col, Set<String> results) {

        //work only on the top right half of the matrix

        //if an edge not oriented
        if(matrix[row][col]==INF) {
            //try with 0
            matrix[row][col]=0;
            generateStrings(matrix,row,col,results);

            //try with 1
            matrix[row][col]=1;
            generateStrings(matrix,row,col,results);

            //backtrack
            matrix[row][col]=INF;
        }

        //if the matrix was fully processed
        else if(row==matrix.length-2 && col== matrix.length-1) {
            //create the orientation string
            String orientation = OrientationMatrix.orientGraph(matrix);
            //add orientation to the list
            results.add(orientation);
        }

        else{
            //if the last entry of the row
            //move to the next row
            if(col== matrix.length-1) {
                row=row+1;
                col=row+1;
                generateStrings(matrix,row,col,results);
            }

            else {
                //move to the nex entry
                col=col+1;
                generateStrings(matrix,row,col,results);
            }
        }

    }

    /**
     * This method is used to count the remaining not oriented edges of the graph
     * after it has been oriented with the main algorithm.
     */
    public static int countRemainingEdgesTwo(List<Point> points) {
        // Initialize the orientation matrix
        int [][] orientationMatrix = OrientationMatrix.createMatrix(points);

        // Generate all possible triangles from the points
        List <Triangle> triangles = TwoApproximationAlgorithm.buildTriangles(points);
        // Sort triangles by their perimeter
        TwoApproximationAlgorithm.sortTriangles(triangles);

        // Process each triangle to orient its edges
        for (Triangle t : triangles) {
            orientTriangles(orientationMatrix,t);
        }

        //count not oriented edges
        int counter=0;
        int numberOfPoints = orientationMatrix.length;
        for(int i=0;i<numberOfPoints;i++) {
            for(int j=i+1;j<numberOfPoints;j++) {
                if(orientationMatrix[i][j]==INF) {
                    counter++;
                }
            }
        }
        return counter;
    }
}
