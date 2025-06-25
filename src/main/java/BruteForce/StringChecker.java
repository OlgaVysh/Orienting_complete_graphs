package BruteForce;

/**
 * This class checks a given orientation string for its validity.
 * The method checkString returns true if a given string returns a strongly connected graph.
 */

public class StringChecker {

    private String bitString;

    private final int numberOfPoints;

    public StringChecker(String bitString, int numberOfPoints) {
        this.bitString = bitString;
        this.numberOfPoints = numberOfPoints;
    }

    public void set(String bitString) {
        this.bitString = bitString;
    }

    /**
     * Checks if a given orientation string represents a strongly connected graph
     * @return true if
     */
    public boolean checkString() {
        //the first point is validated by binary counter
        //therefore start with the second point
        int i=0;
        boolean b = checkVertex(i);

        while(b && i< numberOfPoints -1) {
            i++;
            b=checkVertex(i);
        }

        return b;
    }


    /**
     * Calculates the quantity of bits in the bit string representing the row i of the matrix.
     * For a vertex i these are entries in the matrix[i][j] for j<i.
     * @param vertexIndex is the index of a vertex in the point set
     * @return quantity of bits representing the row vertexIndex in the bit String
     */
    public int calculateLength(int vertexIndex) {
        return numberOfPoints-vertexIndex-1;
    }

    /**
     * Calculates index in the bit string from where bits represent orientation
     * of the given vertex
     * @param vertexIndex index of the vertex in the point set
     * @return start position of the orientation bits of the given vertex in the bit string.
     */
    public int calculateStart(int vertexIndex) {
        if(vertexIndex==0) return 0;
        else return calculateLength(vertexIndex-1)+calculateStart(vertexIndex-1);
    }

   /**
     * Calculates the bit string index for an element A[i][j] in the upper half (excluding the diagonal)
     * of an n x n adjacency matrix.
     *
     * @param i The row index (i < j)
     * @param j The column index
     * @return The index in the bit string
     */
    public int calculatePosition(int i, int j) {
        if (i >= j) {
            throw new IllegalArgumentException("Indexes are invalid (i>j).");
        }
        int start =  calculateStart(i);
        return start+j-i-1;
    }


    /**
     * Creates a substring representing the orientation of a given vertex
     * @param vertexIndex index of the vertex in the point set
     * @return orientation of edges of a vertex under the given index
     */
    public String calculateEdges(int vertexIndex) {
        StringBuilder sb = new StringBuilder();

        //vertical entries of the matrix for the vertex
       for(int i=0;i<vertexIndex;i++) {
           int index = calculatePosition(i,vertexIndex);
           sb.append(bitString.charAt(index));
       }

       // horizontal entries of the matrix for the vertex
       int index_ = calculateStart(vertexIndex); //start point of the row in the bit string
       int length = calculateLength(vertexIndex); //number of bits representing the row


       sb.append(bitString, index_, index_+ length);


       return sb.toString();
    }

    /**
     * Checks if a given vertex has in- and outgoing edges in the orientation
     * @param vertexIndex index of the vertex in the point set
     * @return true if given vertex has at least one outgoing and one ingoing edge
     */
    public boolean checkVertex(int vertexIndex) {
        //regular expressions
        String regex;
        String regex1;

        int d = numberOfPoints -1; //number of bits coding an orientation of a vertex

        //check first and last vertexes
        if(vertexIndex==0 || vertexIndex== numberOfPoints -1) {
            regex = "0{" + d +"}"; // if only zeroes - no outgoing edge
            regex1 = "1{" + d +"}"; // if only ones - no ingoing edge
        }

        else {
           int c= numberOfPoints -vertexIndex-1;
            regex = "0{" + vertexIndex + "}1{" + c + "}"; //only outgoing edges
            regex1 = "1{" + vertexIndex + "}0{" + c + "}"; //only ingoing edges

        }
        return !(calculateEdges(vertexIndex).matches(regex) || calculateEdges(vertexIndex).matches(regex1));

    }

    public static void main(String[] args) {
        String orientation = "0000000";
        StringChecker checker = new StringChecker(orientation,5);

        System.out.println("The orientation is valid: "+checker.checkString());
    }
}
