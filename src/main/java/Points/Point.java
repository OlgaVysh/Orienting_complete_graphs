package Points;

import java.util.Locale;

/**
 * This class represents a point in Euclidean space with coordinates p=(x,y)
 */
public record Point(double x, double y) {

    @Override
    public String toString() {
        return String.format(Locale.US, "(%.3f, %.3f)", x, y);
    }

    /**
     * calculates Euclidean distance between current vertex and a given vertex
     */
    public double calculateDistance(Point p1) {
        double dx = p1.x - this.x;
        double dy = p1.y - this.y;
        return Math.sqrt(dx * dx + dy * dy);
    }

    public static void main(String[] args) {
        Point p1 = new Point(0,1);
        System.out.println(p1);

        double distance = p1.calculateDistance(new Point(1,0));
        System.out.println("euclidean distance= "+distance);
    }
}
