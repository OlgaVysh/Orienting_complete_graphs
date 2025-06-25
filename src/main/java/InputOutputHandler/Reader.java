package InputOutputHandler;
import Points.Point;

import java.io.*;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * This class provides a method for reading point sets from a file.
 */
public class Reader {

    /**
     * Reads a file containing sets of 2D points and returns a list of these point sets.
     *
     * @param filePath The path to the file containing the point sets.
     * @return A list of lists, where each inner list represents a set of points.
     */
    public static List<List<Point>> readPointSet(String filePath) {
        List<List<Point>> points = new ArrayList<>();
        File file = new File(filePath);

        try (Scanner scanner = new Scanner(file)) {
            while (scanner.hasNextLine()) {
                List<Point> pointSet = new ArrayList<>();

                // Pattern to recognize points in the format (x,y)
                Pattern pattern = Pattern.compile("\\((-?\\d+\\.\\d+|-?\\d+),(-?\\d+\\.\\d+|-?\\d+)\\)");
                Matcher matcher = pattern.matcher(scanner.nextLine());

                // Extracts points from the line and stores them in the list
                while (matcher.find()) {
                    double x = Double.parseDouble(matcher.group(1));
                    double y = Double.parseDouble(matcher.group(2));
                    pointSet.add(new Point(x, y));
                }

                points.add(pointSet);
            }
        } catch (FileNotFoundException e) {
            System.err.println("Error reading the file: " + e.getMessage());
        } catch (InputMismatchException e) {
            System.err.println("The result could not be converted to a number: " + e.getMessage());
        }

        return points;
    }

    /**
     * This method reads and prints point-sets from pointSets.txt
     */
    public static void main(String[] args) {
        String filepath = "src/main/resources/pointSets.txt";

        List<List<Point>> points = readPointSet(filepath);

        for(List<Point> p : points) {
            System.out.println(p);
        }
    }
}
