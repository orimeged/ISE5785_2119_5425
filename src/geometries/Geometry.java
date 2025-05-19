package geometries;

import primitives.*;

import java.util.List;

/**
 * class Geometries is a class representing a set of geometric shapes
 * in Cartesian 3-Dimensional coordinate system.
 *
 * @author Ori meged and Natanel hasid
 */
public class Geometry implements Intersectable {
    // Constructor
    public Geometry() {
        // ... any initialization if needed ...
    }

    public Vector getNormal(Point point) {
        // This method should be overridden in subclasses
        throw new UnsupportedOperationException("getNormal not implemented");
    }

    @Override
    public List<Point> findIntersections(Ray ray) {
        return List.of();
    }

    // Implement other methods from Intersectable if needed
}
