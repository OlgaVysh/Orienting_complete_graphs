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
 * this edge and where the other two edges are not yet oriented is found and oriented.
 * This class does not guarantee a strongly connected graph.
 */
public class KantenOne {
    final static int INF = 99999;

    public static double orient(List<Point> points) {
        //create sorted list of all edges
        List <Edge> edges = GreedyAlgorithm.buildEdgesList(points);

        //initialize orientation matrix
        int [][] orientationMatrix = OrientationMatrix.createMatrix(points);

        //orient triangles and update orientation matrix
        orientTriangles(edges,orientationMatrix, points);

        //decrypt the final orientation
        String orientation =  OrientationMatrix.orientGraph(orientationMatrix);

        //check if the orientation is strongly connected
        StringChecker checker = new StringChecker(orientation,points.size());

        if(!checker.checkString()) {
            System.out.println("Graph nicht stark zusammenhängend "+orientation+" , "+points.toString());
            return INF;
        }

        return OrientedDilation.calculateOrientedDilation(points,orientation);
    }


    /**
     * This method processes a list of edges in ascending order of their length. For every edge in the list
     * the method orients the edge (if not already oriented) and the smallest triangle with that edge
     * that can still be oriented consistently.
     */
    public static void orientTriangles(List <Edge> edges, int [][] orientationMatrix, List<Point> points) {
        //for every edge in ascending order of length
        for (Edge edge : edges) {
            //get the end points
            int i = edge.getStart();
            int j = edge.getEnd();

            //orient edge (random) if not oriented yet
            if(orientationMatrix[i][j] == INF) {
                Random random = new Random();
                int randomValue = random.nextInt(2);

                orientationMatrix[i][j]=randomValue;
            }

            int smallestPoint=0;
            double perimeter = INF;


            //iterate through every triangle with edge (i,j) to find
            //the smallest not oriented one
            for(int a=0; a<orientationMatrix.length; a++) {

                if(a!=i && a!=j) {
                    boolean notOr = isNotOriented(i,j,a,orientationMatrix);
                    double length = points.get(i).calculateDistance(points.get(a))+
                            points.get(a).calculateDistance(points.get(j));

                    if(length<perimeter && notOr) {
                        smallestPoint=a;
                        perimeter=length;
                    }
                }
            }

            //if such triangle was found
            if(perimeter!=INF) {
                //orient it by the edge ij
                OrientationMatrix.orientTriangle(orientationMatrix,i,j,smallestPoint);
            }

        }

    }

    /**
     *This method checks orientation of all edges of a triangle given by points a,b,c.
     * Returns true if only one edge is oriented.
     */
    public static boolean isNotOriented(int a, int b, int c, int [][] orientationMatrix) {
        int[] vertexes = new int[]{a,b,c};
        Arrays.sort(vertexes);

        int firstEdge = orientationMatrix[vertexes[0]][vertexes[1]];
        int secondEdge = orientationMatrix[vertexes[1]][vertexes[2]];
        int thirdEdge = orientationMatrix[vertexes[0]][vertexes[2]];

        //check that only one edge is oriented in the triangle
        return firstEdge+secondEdge+thirdEdge == 1+2*INF || firstEdge+secondEdge+thirdEdge == 2*INF;
    }

    public static void main(String[]args) {
        List<Point> points = Arrays.asList(new Point(0.143, 0.167),
                new Point(0.783, 0.854),
                new Point(0.011, 0.523),
                new Point(0.450, 0.877));

        double odil = orient(points);
        System.out.println(odil);
    }
}
