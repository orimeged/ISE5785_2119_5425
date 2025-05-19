package primitives;

import java.util.List;

/**
 * Class Ray is the basic class representing a ray of Euclidean geometry in Cartesian
 * 3-Dimensional coordinate system.
 *
 * @author Ori meged and Natanel hasid
 */
public class Ray {
    /**
     * starting point of the ray
     */
    private final Point point;
    /**
     * direction vector of the ray
     */
    private final Vector dir;

    /**
     * Constructor to initialize Ray based on point and a vector
     *
     * @param p1 starting point of the ray
     * @param v1 direction vector of the ray
     */
    public Ray(Point p1, Vector v1) {
        point = p1;
        dir = v1.normalize();
        p1 = point;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        return (obj instanceof Ray other)
                && this.point.equals(other.point)
                && this.dir.equals(other.dir);
    }

    public Point getPoint() {
        return point;
    }

    public Vector getDir() {
        return dir;
    }

    public Point getPoint(double t) {
        try {
            return point.add(dir.scale(t));
        } catch (Exception e) {
            return point;
        }
    }

    /**
     * Finds the closest point to the ray's starting point from a given list of points.
     *
     * @param list the list of points to check
     * @return the closest point to the ray's starting point, or null if the list is empty or null
     */
    public Point findClosestPoint(List<Point> list) {
        if (list == null) // If the list is null, return null
            return null;

        double distance = Double.POSITIVE_INFINITY; // Initialize the minimum distance as infinity
        Point closest = null; // Variable to store the closest point

        // Iterate through the list to find the closest point
        for (Point p : list) {
            if (p.distance(point) < distance) { // Check if the current point is closer
                distance = p.distance(point); // Update the minimum distance
                closest = p; // Update the closest point
            }
        }
        return closest; // Return the closest point
    }
}
