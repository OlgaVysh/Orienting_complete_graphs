package BruteForce;
import Dilation.OrientedDilation;

import java.util.Arrays;
import java.util.List;

import Points.Point;


/**
 * This class uses a brute-force approach to iterate over all possible orientations of a graph on a
 * given point set and determine the minimum possible oriented dilation.
 */
public class BruteForceAlgorithm {

    /**
     * Performs the brute-force calculation to determine the minimum oriented dilation for a set of points.
     *
     * @param points The set of points for which the calculation is performed.
     * @return The minimum oriented dilation.
     */
    public static double bruteForce(List<Point> points) {
        checkArguments(points);

        double minDilation = 99999;
        int numberOfPoints = points.size();

        //calculate the length of a bit string for the point set
        int bitStringLength = calculateBitStringLength(numberOfPoints);

        //calculate range for the bit counter
        long minDecimalValue = calculateMinDecimalValue(bitStringLength, numberOfPoints);
        long maxDecimalValue = calculateMaxDecimalValue(bitStringLength, numberOfPoints);

        //create an instance to check bit String
        StringChecker checker = new StringChecker("", numberOfPoints);

        //create strings
        for (long i=minDecimalValue; i<=maxDecimalValue;i++) {
            //convert number from the counter into bit string
            String binaryString = Long.toBinaryString(i);

            //fill string with zeroes to match expected string length
            if(binaryString.length() < bitStringLength) {
                binaryString = String.format("%" + bitStringLength + "s", binaryString).replace(' ', '0');
            }

            //check if string results in a connected graph
            checker.set(binaryString);

            if(checker.checkString() ) {
                //calculate dilation
                double dilation = OrientedDilation.calculateOrientedDilation(points,binaryString);
                //save the smallest dilation
                minDilation= Math.min(minDilation,dilation);
            }
        }

       return minDilation;

    }


    /**
     * Checks whether the given list of points contains at least three points.
     *
     * @param points The list of points to check.
     * @throws IllegalArgumentException If there are fewer than three points.
     */
    public static void checkArguments(List<Point> points) {
        if(points.size()<3) {
            throw new IllegalArgumentException("Number of points must be at least three!");
        }
    }


    /**
     * Calculates the length of the bit string required to represent orientations for the given number of points.
     *
     * @param numberOfPoints The number of points.
     * @return The length of the bit string.
     */
    public static int calculateBitStringLength(int numberOfPoints) {
        return numberOfPoints*(numberOfPoints-1)/2;
    }

    //Binary counter
    /**
     * Calculates the smallest decimal value for which the orientation can be valid.
     * (The first vertex has one outgoing edge and all other bits are '0')
     *
     * @param bitStringLength The length of the bit string.
     * @param numberOfPoints The number of points.
     * @return The smallest decimal value.
     */
    public static long calculateMinDecimalValue(int bitStringLength, int numberOfPoints) {
       //calculate the index of '1' in the whole bit string
       int bitLengthOfFirstVertex = numberOfPoints-1;
       int power = bitStringLength - bitLengthOfFirstVertex;
       //calculate the decimal value
       return (long) Math.pow(2,power);
    }

    /**
     * Calculates the largest decimal value for which the orientation can be valid.
     * (The first vertex has one incoming edge)
     *
     * @param bitStringLength The length of the bit string.
     * @param numberOfPoints The number of points.
     * @return The largest decimal value.
     */
    public static long calculateMaxDecimalValue(int bitStringLength,int numberOfPoints) {
        long minDecimalValue = calculateMinDecimalValue(bitStringLength,numberOfPoints);
        return  (long)Math.pow(2,bitStringLength) - 1 - minDecimalValue;

    }

    public static void main(String[] args) {
        List<Point> points = Arrays.asList(
                new Point(0,0),
                new Point(1,0),
                new Point(0,1),
                new Point(1,1)
        );

        double odil = bruteForce(points);
        System.out.println(points+", dilation: "+odil);
    }
}




