package BruteForce;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class StringCheckerTest {

    @Test
    public void testCalculateLength() {
        StringChecker stringChecker = new StringChecker("", 4);

        assertEquals(3, stringChecker.calculateLength(0));
        assertEquals(2, stringChecker.calculateLength(1));
        assertEquals(1, stringChecker.calculateLength(2));
        assertEquals(0, stringChecker.calculateLength(3));
    }

    @Test
    public void testCalculateStart() {
         
        int n = 4;
        StringChecker stringChecker = new StringChecker("", n);


        assertEquals(0, stringChecker.calculateStart(0));
        assertEquals(3, stringChecker.calculateStart(1));
        assertEquals(5, stringChecker.calculateStart(2));
    }

    @Test
    public void testCalculatePosition() {
         
        int n = 4;
        StringChecker stringChecker = new StringChecker("", n);


        assertEquals(0, stringChecker.calculatePosition(0, 1));
        assertEquals(1, stringChecker.calculatePosition(0, 2));
        assertEquals(2, stringChecker.calculatePosition(0, 3));
        assertEquals(3, stringChecker.calculatePosition(1, 2));
        assertEquals(4, stringChecker.calculatePosition(1, 3));
        assertEquals(5, stringChecker.calculatePosition(2, 3));

    }

    @Test
    public void testCalculatePositionInvalid() {
         
        int n = 5;
        StringChecker stringChecker = new StringChecker("", n);

         
        assertThrows(IllegalArgumentException.class, () -> stringChecker.calculatePosition(2, 2));
        assertThrows(IllegalArgumentException.class, () -> stringChecker.calculatePosition(3, 1));
    }

    @Test
    public void testCalculateEdges() {
         
        String bitString = "0110011011";
        int n = 5;
        StringChecker stringChecker = new StringChecker(bitString, n);

         
        assertEquals("0110", stringChecker.calculateEdges(0));
        assertEquals("0011", stringChecker.calculateEdges(1));
        assertEquals("1001", stringChecker.calculateEdges(2));
        assertEquals("1101", stringChecker.calculateEdges(3));
        assertEquals("0111", stringChecker.calculateEdges(4));
    }

    @Test
    public void testCheckVertex() {
        String bitString = "0110011011";
        int n = 5;
        StringChecker stringChecker = new StringChecker(bitString, n);

        for(int i=0;i<5;i++) {
            assertTrue(stringChecker.checkVertex(i));
        }

    }

    @Test
    public void testCheckString() {
        String bitString = "1111011011";
        StringChecker checker = new StringChecker(bitString,5);
        assertFalse(checker.checkString());
    }
}

