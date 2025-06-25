package Dilation;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
public class FloydWarshallAlgorithmTest {
    @Test
    public void testFloydWarshallAlgorithm() {

        final double sqrt = Math.sqrt(2);

        double[][] graph = {
                {0,1,99999,sqrt},
                {99999,0,sqrt,99999},
                {1,99999,0,1},
                {99999,1,99999,0}

        };


        double[][] expected = {
                {0,1,1+sqrt,sqrt},
                {1+sqrt,0,sqrt,1+sqrt},
                {1,2,0,1},
                {2+sqrt,1,1+sqrt,0}
        };

        double[][] result = FloydWarshallAlgorithm.floydWarshall(graph);

        for (int i = 0; i < result.length; i++) {
            assertArrayEquals(expected[i], result[i], "Row " + i + " does not match");
        }
    }
}
