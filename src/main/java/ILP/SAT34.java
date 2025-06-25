package ILP;
import Points.Point;
import com.google.ortools.Loader;
import com.google.ortools.sat.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * This class implements the SAT Model for finding the minimum t-spanner on some point-set.
 * It only considers the cycles with 3 and 4 edges. The Version which considers all Cycles is
 * implemented in the class SAT.
 * This version performs well for point-sets with up to 20 points.
 */
public class SAT34 {
    private final int numberOfPoints;

    //array that holds all Euclidean distances
    private final double[][] distances;

    //array that holds perimeters of smallest triangle between every two points
    private final double[][] triangles;

    //list of all indices
    private final List<Integer> sortedList;

    /**
     * Constructor
     */
    public SAT34(List<Point> points) {
        //initialise the attributes
        numberOfPoints = points.size();

        distances = new double[numberOfPoints][numberOfPoints];
        triangles = new double[numberOfPoints][numberOfPoints];

        for(int i=0;i<numberOfPoints;i++) {
            distances[i][i]=0;

            for (int j=i+1;j<numberOfPoints;j++) {
                double distance = points.get(i).calculateDistance(points.get(j));
                distances[i][j]=distance;
                distances[j][i]= distance;
            }

        }

        for(int i=0;i<numberOfPoints;i++) {

            triangles[i][i]=0;

            for (int j=i+1;j<numberOfPoints;j++) {
                double triangle = calculateTriangle(distances,i,j);
                triangles[i][j]=triangle;
                triangles[j][i]= triangle;
            }
        }

        sortedList = new ArrayList<>();

        for(int i=0;i<numberOfPoints;i++) {
            sortedList.add(i);
        }

    }

    /**
     * This method performs a binary search within the range [1.0, 2.0] to find the smallest
     * value for that the SAT34 model can be solved
     */
    public double binarySearch() {
        // Lower and upper bounds for the binary search
        double low = 1.0;
        double high = 2.0;

        // Perform binary search until the difference between high and low is smaller than 0.0001
        while (high - low > 0.0001) {
            // Calculate the midpoint of the current range
            double mid = (low + high) / 2.0;

            // Try to solve the model
            if (this.solveSAT(mid, 4)) {
                // If solved, update the upper bound to mid
                high = mid;
            }
            else {
                // Otherwise, update the lower bound to mid
                low = mid;
            }
        }

        // Return the result rounded to four decimal places
        return roundToFourDecimals(high);
    }

    /**
     * This method creates and solves a SAT34 model for a given t
     */
    public Boolean solveSAT(double t, int length) {
        Loader.loadNativeLibraries();

        // Create new SAT34 model for  every t
        CpModel model = new CpModel();

        int n = this.numberOfPoints;

        //Array for orientation variables
        BoolVar[][] x = new BoolVar[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; ++j) {
                if (i != j) {
                    x[i][j] = model.newBoolVar("X_"+i+"_"+j);
                }

            }
        }

        // Constrains

        //every node has at least one outgoing and one ingoing edge
        for (int i = 0; i < n; i++) {
            IntVar s = model.newIntVar(1, n - 2, "sum_"+i);
            List<IntVar> expr = new ArrayList<>();
            for (int j = 0; j < n; j++) {
                if (i != j) {
                    expr.add(x[i][j]);
                }

            }
            model.addEquality(s, LinearExpr.sum(expr.toArray(new IntVar[n-1])));
        }

        //every edge is one-directed
        for (int i = 0; i < n; i++) {
            for (int j = i+1; j < n; j++) {
                if (i != j) {
                    model.addBoolXor(new BoolVar[]{x[i][j],x[j][i]});
                }

            }
        }

        //create an array to hold all cycles
        //allCycles[i][j] holds a list of all possible cycles through i-j
        List<BoolVar>[][] allCycles = new List[n][n];

        // initialize the list
        for (int i = 0; i < n; i++) {
            for (int j = i+1; j < n; j++) { //work only on the top half of the matrix
                allCycles[i][j] = new ArrayList<>();
            }
        }


        //for every two points i,j with i<j build a list with all cycles of length 3 and 4
        for(int i=0;i<n;i++) {
            for (int j = i + 1; j < n; j++) {

                //list that holds every cycle through i and j
                List<List<Integer>> cyclesList = new ArrayList<>();

                //add cycles to the list
                List<Integer> sorted = Arrays.asList(i, j);
                List<Integer> unsorted = new ArrayList<>(sortedList);
                unsorted.remove(Integer.valueOf(i));
                unsorted.remove(Integer.valueOf(j));

                generateCycles(length, cyclesList, sorted, unsorted);

                //for every cycle in the list

                for (List<Integer> list : cyclesList) {

                    int a = list.get(0); //the i
                    int b = list.get(1); //the j

                    double perimeter = this.triangles[a][b]; //smallest triangle through ij
                    double size = calculateLength(list); //length of the closed walk

                    // if length of the cycle is at most t*perimeter of the smallest triangle through ij
                    if(t>= size/perimeter) {
                        //build the clauses

                        //variable for a closed cycle
                        BoolVar Cij = model.newBoolVar("C_" + i + "_" + j);

                        //clock wise cycle
                        BoolVar cW = model.newBoolVar("cW");
                        List<Literal> cWCycle = new ArrayList<>();

                        //counter clock wise cycle
                        BoolVar cCW = model.newBoolVar("cCW");
                        List<Literal> cCWCycle = new ArrayList<>();

                        //build the cycles
                        //for every edge cd in the cycle with c<d:
                        //add Xcd to the clock wise cycle and Xdc to the counter clock wise cycle
                        for (int k = 0; k < list.size() - 1; k++)
                        {
                            int c = list.get(k);
                            int d = list.get(k + 1);

                            cWCycle.add(x[c][d]);
                            cCWCycle.add(x[d][c]);
                        }

                        int c = list.get(0);
                        int d = list.get(list.size() - 1);
                        cWCycle.add(x[d][c]);
                        cCWCycle.add(x[c][d]);

                        //Cycle variable is true only if all edges of the cycle are 1
                        model.addMultiplicationEquality(cW, cWCycle.toArray(new Literal[0]));
                        model.addMultiplicationEquality(cCW, cCWCycle.toArray(new Literal[0]));

                        //check that there is indeed a closed cycle
                        //Cij = true if cW = true or cCW = true
                        model.addMaxEquality(Cij, new Literal[]{cW, cCW});

                        // add the closed walk to the list of i,j
                        allCycles[a][b].add(Cij);
                    }
                }
            }
        }

        //for every two edges i,j there should be at least one cycle through i,j
        for (int i = 0; i < n; i++) {
            for (int j = i+1; j < n; j++) {
                model.addAtLeastOne(allCycles[i][j].toArray(new Literal[0]));
            }
        }

        //solve the model
        CpSolver solver = new CpSolver();
        CpSolverStatus status = solver.solve(model);

        if (status == CpSolverStatus.OPTIMAL || status == CpSolverStatus.FEASIBLE) {
            //print statistics
            //System.out.println("Problem solved in " + solver.wallTime() + " milliseconds");
            return true;
        }

        else return false;
    }


/**
 * Recursively generates cycles of elements and stores them in whereTo.
 * Each generated sequence is stored as a separate list in whereTo.
 */
    public static void generateCycles(int length, List<List<Integer>> whereTo, List<Integer> sorted, List<Integer> unsorted)
    {
        // Base case: if the sorted list has reached the desired length, stop recursion
        if (sorted.size() >= length) {
            return;
        }

        // Iterate through each element in the unsorted list
        for (int i : unsorted) {
            // Create a new sorted list by copying the existing one and adding the current element
            List<Integer> sortedNeu = new ArrayList<>(sorted);
            sortedNeu.add(i);

            // Add the new cycle to the list of results
            whereTo.add(sortedNeu);

            // Create a new unsorted list by copying the existing one and removing the current element
            List<Integer> unsortedNeu = new ArrayList<>(unsorted);
            unsortedNeu.remove(Integer.valueOf(i));

            // Continue recursion if there are still elements left to process
            if (!unsorted.isEmpty()) {
                generateCycles(length, whereTo, sortedNeu, unsortedNeu);
            }
        }
    }

    /**
     * This method calculates and returns the length of a closed walk given by a list on indices
     */
    public double calculateLength(List<Integer> closedWalk) {
        int a = closedWalk.get(0);
        int b = closedWalk.get(closedWalk.size() - 1);
        double length = this.distances[a][b];

        for (int k = 0; k < closedWalk.size() - 1; k++) {
            a = closedWalk.get(k);
            b = closedWalk.get(k + 1);

            length = length + this.distances[a][b];
        }
        return length;
    }

    /**
     * This method calculates the perimeter of the smallest triangle between two points i and j
     * and saves it to the triangles[i][j]
     */
    public double calculateTriangle(double[][] adjacencyMatrix, int i, int j) {
        if(i==j) return 99999;

        else {
            //get the length of the side i-j
            double ijEdge = adjacencyMatrix[i][j];

            double minDistance=99999;

            //loop through all points
            for(int k=0;k<adjacencyMatrix.length;k++) {

                if(k!=i && k!=j) {
                    //determine length of sides j-k and k-i
                    double distance = adjacencyMatrix[i][k] + adjacencyMatrix[j][k];
                    //save the smallest sum of lengths
                    minDistance = Math.min(minDistance,distance);
                }
            }
            //calculate the smallest perimeter as length(i,j) + minimal sum of (i,k) and (j,k)
            return ijEdge + minDistance;
        }
    }

    public double roundToFourDecimals(double value)
    {
        return Math.round(value * 10000.0) / 10000.0;
    }

    public static void main(String[] args) {

        //these are the coordinates of a regular heptagon
        List<Point> points = Arrays.asList(
                new Point(Math.cos(0), Math.sin(0)),
                new Point(Math.cos(2 * Math.PI / 7), Math.sin(2 * Math.PI / 7)),
                new Point(Math.cos(4 * Math.PI / 7), Math.sin(4 * Math.PI / 7)),
                new Point(Math.cos(6 * Math.PI / 7), Math.sin(6 * Math.PI / 7)),
                new Point(Math.cos(8 * Math.PI / 7), Math.sin(8 * Math.PI / 7)),
                new Point(Math.cos(10 * Math.PI / 7), Math.sin(10 * Math.PI / 7)),
                new Point(Math.cos(12 * Math.PI / 7), Math.sin(12 * Math.PI / 7))
        );

        //check if it's a 1-spanner
        SAT34 model = new SAT34(points);
        double odil = model.binarySearch();
        System.out.println("best odil of a hexagon: "+odil);
    }
}
