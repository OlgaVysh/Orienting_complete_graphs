package InputOutputHandler;
import Points.Point;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

/**
 * This class provides methods to save computation results, values, and point sets to a file.
 */
public class Writer {

    /**
     * Saves computation results including number of points, dilation, min, and max values to a file.
     * Uses the following scheme : numberOfPoints;avr;min;max;
     *
     * @param numberOfPoints The number of points in the set.
     * @param dilation       The dilation value.
     * @param min            The minimum value.
     * @param max            The maximum value.
     * @param filePath       The path to the file where results should be saved.
     */
    public static void saveResult(int numberOfPoints, double dilation, double min, double max, String filePath) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath,true))) {

            writer.write(numberOfPoints + ";" +  dilation + ";"+min + ";"+max);
            writer.newLine();
        }
        catch (IOException e) {
            System.err.println("Wright-exception " + e.getMessage());
        }
    }

    /**
     * Saves a single dilation value to a file.
     *
     * @param dilation The dilation value to save.
     * @param filePath The path to the file where the value should be saved.
     */
    public static void saveValue(double dilation, String filePath) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath,true))) {

            writer.write(dilation + ";");
        }
        catch (IOException e) {
            System.err.println("Wright-exception " + e.getMessage());
        }
    }

    /**
     * Saves a list of points along with a dilation to a file.
     *
     * @param points   The list of points to save.
     * @param dilation The dilation value.
     * @param filePath The path to the file where the data should be saved.
     */
    public static void savePointSet(List<Point> points, double dilation, String filePath) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath,true))) {

            writer.write(points.toString()+", dilation : "+ dilation+ ";");
        }
        catch (IOException e) {
            System.err.println("Wright-exception " + e.getMessage());
        }
    }
}
