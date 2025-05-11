package geometries;
import primitives.Vector;
import primitives.Point;


/**
 * class Plane
 */
public class Plane extends Geometry {
    private final Point q;
    private final Vector normal;
    /**
     * constructor for Plane
     *   @param q0 point on the plane
     *      @param normal2 normal vector
     */
    public Plane(Point q0, Vector normal2) {
        this.q = q0;
        this.normal= normal2.normalize();
    }
    /**
     * Constructor for Plane based on three points on the plane.
     * The normal vector is calculated and stored as null in this stage (to be fully implemented later).
     *
     * @param p1 first point on the plane
     * @param p2 second point on the plane
     * @param p3 third point on the plane
     * @throws IllegalArgumentException if any two points are identical
     */
    public Plane(Point p1, Point p2, Point p3) {
        if (p1.equals(p2) || p2.equals(p3) || p3.equals(p1))
            throw new IllegalArgumentException("Plane cannot be defined with identical points");
        this.q = p1;
        this.normal = null; // Normal calculation to be implemented in the next stage
    }

    @Override
    public Vector getNormal(Point point) {
        return normal;
    }

    public Vector getNormal( ) {
        return normal;
    }

}
