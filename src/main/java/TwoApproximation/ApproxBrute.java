package TwoApproximation;

import Dilation.OrientedDilation;
import InputOutputHandler.Writer;
import Orientation.OrientationMatrix;
import Points.Point;
import Points.PointGenerator;

import java.io.File;
import java.io.IOException;
import java.util.*;

/**
 * This class implements extended version of the Two-Approximation algorithm.
 * It uses Bruteforce to determine the best orientation of the remaining edges.
 */

public class ApproxBrute {
    final static int INF = 99999;

    /**
     * Main method to orient the graph based on the input points.
     */
    public static double orient(List<Point> points) {

        if(points.size()<3)
        {
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
            //check if a triangle can be oriented consistently
            //orient if max. one edge is oriented
            TwoApproximationAlgorithm.orientTriangles(orientationMatrix,t);
        }

        //process the orientation matrix and bruteforce all remaining edges
        //create a list of all possible orientations
        List<String> allOrientations = generateAllAdjacencyMatrixStrings(orientationMatrix);

        double minDilation = INF;

        //calculate dilation of every orientation
        for(String orientation : allOrientations) {
            double dilation = OrientedDilation.calculateOrientedDilation(points,orientation);
            //save the smallest dilation
            minDilation = Math.min(minDilation,dilation);
        }

        return minDilation;
    }

    /**
     * This method processes an orientation matrix and returns a list containing
     * all possible orientations of the graph based on remaining edges.
     */
    public static List<String> generateAllAdjacencyMatrixStrings(int[][] matrix) {
        Set<String> results = new HashSet<>();
        generateStrings(matrix, 0, 1, results);
        return new ArrayList<>(results);
    }

    /**
     * This method recursively generates all possible orientations of a given orientation matrix
     * by systematically assigning values to unoriented edges. If an edge is already oriented,
     * it remains unchanged. For each unoriented edge, the method explores two possibilities:
     * one where the edge is assigned a value of zero and another where it is assigned a value of one.
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
     *This method count the remaining edges of the graph after it has been oriented with the main algorithm.
     */
    public static int countRemainingEdges(List<Point> points) {
        // Initialize the orientation matrix
        int [][] orientationMatrix = OrientationMatrix.createMatrix(points);

        // Generate all possible triangles from the points
        List <Triangle> triangles = TwoApproximationAlgorithm.buildTriangles(points);
        // Sort triangles by their perimeter
        TwoApproximationAlgorithm.sortTriangles(triangles);

        // Process each triangle to orient its edges
        for (Triangle t : triangles) {
            TwoApproximationAlgorithm.orientTriangles(orientationMatrix,t);
        }

        //count the remaining edges
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

    public static void main(String[] args) throws IOException {
        //create a new file named 2Approx.txt
        //for every size in the list generate 10^3 point-sets
        //calculate min,max and avg odil for every size
        //write the data into the file

        int[] indicies = new int[]{4,5,10,15,20,25,30,35,40,45,50};

        String filePath = "src/main/resources/2Approx.txt";

        File file = new File(filePath);

        try{
            if(file.createNewFile())
            {
                System.out.println(filePath+" created.");
            }

            else System.out.println(filePath+" exists already.");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        for (int i : indicies) {
            int j = 0;

            double min = 99999;
            double max = 0;
            double sum = 0;

            while(j<1000) {
                List<Point> points = PointGenerator.generateUniquePoints(i);
                double odil=orient(points);

                sum = sum + odil;
                min = Math.min(odil, min);
                max = Math.max(odil, max);

                j++;
            }

            sum = sum / 1000;

            Writer.saveResult(i,sum,min,max,filePath);

            System.out.println(i+" done.");
        }
    }
}





