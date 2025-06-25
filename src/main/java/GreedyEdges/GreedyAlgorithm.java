package GreedyEdges;
import Dilation.OrientedDilation;
import InputOutputHandler.Writer;
import Points.Point;
import Orientation.OrientationMatrix;
import Points.PointGenerator;

import java.io.File;
import java.io.IOException;
import java.util.*;
/**
 * This class implements a greedy algorithm for orienting a complete graph based on a set of points. Every time the
 * shortest remaining edge of a graph is oriented. Then all triangles containing this edge are oriented consistently.
 * It calculates an orientation matrix and encodes the graph orientation as a bit string.
 */
public class GreedyAlgorithm {

    final static int INF = 99999;

    /**
     * This method creates a list of all edges of a complete graph. The list is sorted in ascending order
     * of length of the edges. Starting with the shortest edge the edge itself and all triangles
     * containing this edge are oriented. The orientation is held in the orientation matrix.
     * The final graph orientation is then determined and returned.
     *
     * @param points A list of points representing the vertices of the graph.
     * @return A string representing the orientation of the graph.
     */

    public static double orient(List<Point> points) {
        //create sorted list of all edges
        List <Edge> edges = buildEdgesList(points);

        //initialize orientation matrix
        int [][] orientationMatrix = OrientationMatrix.createMatrix(points);

        //orient triangles and update orientation matrix
        orientTriangles(edges,orientationMatrix);

        //decrypt the final orientation
        String orientation = OrientationMatrix.orientGraph(orientationMatrix);
        return OrientedDilation.calculateOrientedDilation(points,orientation);
    }

    /**
     * This method creates a list of all edges of a complete graph on a set of points. For every pair of points
     * an edge object is created. The list is then sorted in ascending order of length of the edges.
     * @param points a list of points representing the graph's vertices.
     * @return the sorted list of all edges.
     */
    public static List <Edge> buildEdgesList(List<Point> points) {
        int numberOfPoints = points.size();
        List <Edge> edges = new ArrayList<>();

        for(int i=0;i<numberOfPoints;i++) {

            for(int j=i+1;j<numberOfPoints;j++) {
                //create a n edge object
                Point p1 = points.get(i);
                Point p2 = points.get(j);
                double weight = p1.calculateDistance(p2);
                edges.add(new Edge(i,j,weight));
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
     * @param edges A list of edges in the graph sorted in ascending order of length.
     * @param orientationMatrix the orientation matrix which holds orientation of all edges.
     */
    public static void orientTriangles(List <Edge> edges, int [][] orientationMatrix)
    {
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

            //orient every triangle with edge (i,j) fixed by this edge
            for(int a=0; a<orientationMatrix.length; a++) {
                if(a!=i && a!=j) {
                    OrientationMatrix.orientTriangle(orientationMatrix, i,j,a);
                }
            }
        }

    }

    public static void main(String[] args) throws IOException {
        //create a new file named greedy.txt
        //for every size in the list generate 10^3 point-sets
        //calculate min,max and avg odil for every size
        //write the data into the file

        int[] indicies = new int[]{4,5,10,15,20,25,30,35,40,45,50};

        String filePath = "src/main/resources/greedy.txt";

        File file = new File(filePath);

        try{
            if(file.createNewFile()) {
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
