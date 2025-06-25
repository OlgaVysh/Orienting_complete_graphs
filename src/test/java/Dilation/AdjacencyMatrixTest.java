package Dilation;

import Points.Point;
import org.junit.jupiter.api.Test;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class AdjacencyMatrixTest {

        @Test
        public void checkArgumentsPointSetTest() {
            List<Point> points = Arrays.asList(
                    new Point(1, 0),
                    new Point(1, 1)
            );
            String bits = "101101";

            Throwable exception = assertThrows(IllegalArgumentException.class, () -> {
                AdjacencyMatrix.createDistanceMatrix(points, bits);});
            assertEquals("Number of points should be at least 3.", exception.getMessage());
        }
        @Test
        public void checkArgumentsBitStringTest() {
          List<Point> points = Arrays.asList(
                new Point(1, 0),
                new Point(1, 1),
                new Point(0, 0),
                new Point(0, 1)
          );
          String bits = "10110";

          Throwable exception = assertThrows(IllegalArgumentException.class, () -> {
              AdjacencyMatrix.createDistanceMatrix(points, bits);});
          assertEquals("Bit string doesn't match point set: "+bits, exception.getMessage());
        }

        @Test
        public void createDistanceMatrixTest() {
            List<Point> points = Arrays.asList(
                    new Point(1, 0),
                    new Point(1, 1),
                    new Point(0, 0),
                    new Point(0, 1)
            );
            String bits = "101101";

            double[][] expected = {
                    {0,1,0,Math.sqrt(2)},
                    {0,0,Math.sqrt(2),0},
                    {1,0,0,1},
                    {0,1,0,0}

            };

            double[][] matrix = AdjacencyMatrix.createDistanceMatrix(points, bits);

           assertEquals(4, matrix.length, "Matrix size does not match number of points");
           int matrixLength= expected.length;

            for(int i=0;i< matrixLength;i++)
            {
                for(int j=0; j<matrixLength;j++)
                {
                    assertEquals(expected[i][j], matrix[i][j], "Matrix " + i +" -> " +j+" incorrect.");
                }
            }
        }

}
