package geometries;

import primitives.Point;
import primitives.Vector;

import primitives.Ray;

/**
 *  class Cylinder
 */
public class Cylinder extends Tube {

    private final double height;
    /**
     * constructor for Cylinder
     */
    public Cylinder(double radius, Ray axisRay, double height) {
        super(axisRay, radius);
        this.height = height;

    }
    @Override
    public Vector getNormal(Point point) {
        return null;
    }

}
