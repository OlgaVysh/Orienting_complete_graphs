package GreedyVersions;

import BruteForce.StringChecker;
import Dilation.OrientedDilation;
import GreedyEdges.Edge;
import GreedyEdges.GreedyAlgorithm;
import Orientation.OrientationMatrix;
import Points.Point;

import java.util.*;

/**
 * This class implements the experimental version of the "Gierige Kanten" algorithm,
 * where for every edge, in ascending order of length, the smallest triangle containing
 * this edge is found and oriented.
 * This class experimentally guarantees a strongly connected graph.
 */

public class KantenTwo {
    final static int INF = 99999;

    //main method

    public static double orient(List<Point> points) {
        //create sorted list of all edges
        List <Edge> edges = GreedyAlgorithm.buildEdgesList(points);

        //initialize orientation matrix
        int [][] orientationMatrix = OrientationMatrix.createMatrix(points);

        //orient triangles and update orientation matrix
        orientTriangles(edges,orientationMatrix, points);

        //decrypt the final orientation
        String orientation =  OrientationMatrix.orientGraph(orientationMatrix);

        //check that the orientation is a strongly connected graph
        StringChecker checker = new StringChecker(orientation,points.size());
        if(!checker.checkString()) {
            System.out.println("Graph nicht stark zusammenhängend");
            return INF;
        }

        //calculate the dilation and return
        return OrientedDilation.calculateOrientedDilation(points,orientation);
    }

    //helper methods
    /**
     * This method processes every edge in ascending order of their length and
     * orients the smallest triangle with this edge that can still be oriented consistently.
     */
    public static void orientTriangles(List <Edge> edges, int [][] orientationMatrix, List<Point> points) {
        //for every edge in ascending order of length
        for (Edge edge : edges) {
            //get the end points
            int i = edge.getStart();
            int j = edge.getEnd();

            //create a list on indices to iterate through
            List<Integer> indices = new ArrayList<>();

            for(int k=0;k<orientationMatrix.length;k++) {
                if(k!=i && k!=j)
                {
                    indices.add(k);
                }
            }

            //orient the smallest triangle with edge ij consistently
            orientTriangle(i,j,indices,orientationMatrix,points);
        }

    }

    public static void orientTriangle(int i, int j, List<Integer> indices, int [][] orientationMatrix, List<Point> points) {

        if(indices.isEmpty()) {
            return;
        }

        int smallestPoint=0;
        double perimeter = INF;
        int edgeCounter=0;

        //iterate through every triangle with edge (i,j) to find
        //the smallest one that could still be oriented
        for(int k=0;k<indices.size();k++) {
            int a=indices.get(k);
            //how many edges are oriented
                int calc = isOriented(i,j,a,orientationMatrix);

                if(calc!=3) {
                   double length = points.get(i).calculateDistance(points.get(a))+
                        points.get(a).calculateDistance(points.get(j));

                  //find the smallest not completely oriented triangle
                    if(length<perimeter ) {
                    smallestPoint = a;
                    perimeter = length;
                    edgeCounter=calc;
                }
           }
        }

        //if such triangle was found
        if(perimeter!=INF) {
            //try to orient the triangle consistently
           boolean b = tryToOrient(i,j,smallestPoint,edgeCounter, orientationMatrix);

           //if failed
           if(!b) {
               //remove the point from the list
               indices.remove(Integer.valueOf(smallestPoint));
               //search for another triangle
               orientTriangle(i,j,indices,orientationMatrix,points);
           }

        }

        //if no such triangle was found, still orient ij if not oriented yet
        else {
            if(orientationMatrix[i][j]==INF) {
                Random random = new Random();
                orientationMatrix[i][j] = random.nextInt(2);
            }
        }
    }

    /**
     * This method processes a triangle ijk and orients it consistently.
     * Returns true if the triangle was successfully oriented.
     */
    public static boolean tryToOrient(int i,int j, int k, int counter, int[][] orientationMatrix) {
       //if no edge is oriented - orient ij
        if(counter==0) {
            Random random = new Random();
            orientationMatrix[i][j]= random.nextInt(2);
            OrientationMatrix.orientTriangle(orientationMatrix,i,j,k);
            return true;
        }


        //if two edges are already oriented
        if(counter==2) {
            //try to orient the triangle consistently
            return orientAbstractTriangle(orientationMatrix,i,j,k);
        }

        //if one edge is oriented
        //determine the oriented edge and orient the triangle consistently
        else{

            int[] arr=new int[]{i,j,k};
            Arrays.sort(arr);
            int a = arr[0];
            int b = arr[1];
            int c = arr[2];

            if(orientationMatrix[b][c]!=INF) {
                OrientationMatrix.orientTriangle(orientationMatrix,b,c,a);
            }

            else if(orientationMatrix[a][b]!=INF) {
                OrientationMatrix.orientTriangle(orientationMatrix,a,b,c);
            }

            else {
                OrientationMatrix.orientTriangle(orientationMatrix,a,c,b);
            }

            return true;
        }


    }

    /**
     * This method processes a triangle abc and returns a number of its oriented edges.
     */
    public static int isOriented(int a, int b, int c, int [][] orientationMatrix) {
        int[] vertexes = new int[]{a,b,c};
        Arrays.sort(vertexes);

        int ab = orientationMatrix[vertexes[0]][vertexes[1]];
        int bc = orientationMatrix[vertexes[1]][vertexes[2]];
        int ac = orientationMatrix[vertexes[0]][vertexes[2]];

        double sum = ab+bc+ac;

        //calculate how many edges are oriented and return the number
         if(sum== 3*INF) return 0;
         else if(sum == 1+2*INF || sum == 2*INF) return 1;
         else if(INF<=sum && sum<=INF+2 ) return 2;
         else return 3;

    }

    /**
     * This method orients a triangle abc in which two edges are already oriented consistently.
     * Returns true if the triangle was successfully oriented.
     */
    public static boolean orientAbstractTriangle(int [][] orientationMatrix, int a, int b, int c) {

        int[] arr = new int[] {a,b,c};
        Arrays.sort(arr);

        int i = arr[0];
        int j = arr[1];
        int k = arr[2];

        int edgeOne = orientationMatrix[i][j];
        int edgeTwo = orientationMatrix[j][k];
        int edgeThree = orientationMatrix[i][k];


        if(edgeOne==edgeTwo && edgeThree==INF) {
            orientationMatrix[i][k]= 1-edgeOne;
            return true;
        }

        else if(edgeOne==1-edgeThree && edgeTwo==INF) {
            orientationMatrix[j][k]=edgeOne;
            return true;
        }

        else if(edgeTwo==1-edgeThree && edgeOne==INF) {
            orientationMatrix[i][j]= edgeTwo;
            return true;

        }

        return false;
    }

}
