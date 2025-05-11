package geometries;

import primitives.Point;
import primitives.Vector;

import primitives.Ray;

import static primitives.Util.isZero;

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

        //The point is on the base whom the cylinder's ray's point is on
        if (point.equals(axis.getHead()) // p == p0
                || isZero(point.subtract(axis.getHead()).dotProduct(axis.getDir())))
            return axis.getDir().scale(-1);
        //The point is on the base whom nat the cylinder's ray's point is on
        if (point.equals( axis.getHead().add(axis.getDir().scale(height))) // p == (p0 + height * v)
                || isZero(point.subtract(axis.getHead().add(axis.getDir().scale(height))).dotProduct(axis.getDir())))
            return axis.getDir();


        return super.getNormal(point);
    }

}
