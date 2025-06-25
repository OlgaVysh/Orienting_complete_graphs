package BruteForce;

import Points.Point;
import org.junit.jupiter.api.Test;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class BruteForceTest {

    @Test
    public void testCalculateBitStringLength() {
        //calculate orientation length for 4 points
        assertEquals(6, BruteForceAlgorithm.calculateBitStringLength(4),
                "Wrong length for 4 points.");

        //calculate orientation length for 5 points
        assertEquals(10, BruteForceAlgorithm.calculateBitStringLength(5),
                "Wrong length for 5 points.");
    }

    @Test
    public void testCalculateMinDecimalValue() {

        int bitStringLength = BruteForceAlgorithm.calculateBitStringLength(4);
        assertEquals(8, BruteForceAlgorithm.calculateMinDecimalValue( bitStringLength,4),
                "Wrong min value for 4 points.");

        bitStringLength = BruteForceAlgorithm.calculateBitStringLength(5);
        assertEquals(64, BruteForceAlgorithm.calculateMinDecimalValue( bitStringLength,5),
                "Wrong min value for 5 points.");
    }

    @Test
    public void testCalculateMaxDecimalValue() {
        int bitStringLength = BruteForceAlgorithm.calculateBitStringLength(4);
        assertEquals(55, BruteForceAlgorithm.calculateMaxDecimalValue( bitStringLength,4),
                "Wrong max value for 4 points.");

        bitStringLength = BruteForceAlgorithm.calculateBitStringLength(5);
        assertEquals(959, BruteForceAlgorithm.calculateMaxDecimalValue( bitStringLength,5),
                "Wrong max value for 4 points.");
    }

    @Test
    public void dilationTest()
    {
        List<Point> pointSetOne = Arrays.asList(
                new Point(1, 0),
                new Point(1, 1),
                new Point(0, 0),
                new Point(0, 1)
        );

        assertEquals(1.17157287525381, BruteForceAlgorithm.bruteForce(pointSetOne),
                "The calculated dilation is not minimal.");

        double b = Math.sqrt(3);

        List<Point> pointSetTwo = Arrays.asList(
                new Point(0, 0),
                new Point(6, 0),
                new Point(3, 3*b),
                new Point(3, b)
        );

        assertEquals(1.4641016151377544, BruteForceAlgorithm.bruteForce(pointSetTwo),
                "The calculated dilation is not minimal.");
    }

    @Test
    public void produceListOfOrientationsTest()
    {

        List<String> expectedOrientations= Arrays.asList(
                "001000",
                "001010",
                "001100",
                "001101",
                "010001",
                "010010",
                "010011",
                "010101",
                "011000",
                "011001",
                "011010",
                "011101",
                "100010",
                "100101",
                "100110",
                "100111",
                "101010",
                "101100",
                "101101",
                "101110",
                "110010",
                "110011",
                "110101",
                "110111");

        for(String orientation : expectedOrientations) {
            assertTrue(expectedOrientations.contains(orientation),"The orientation "+orientation+
                    "is not in the list");
        }
    }
}

