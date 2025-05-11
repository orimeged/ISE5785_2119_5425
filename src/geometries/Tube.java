package geometries;
import primitives.Point;
import primitives.Ray;
import primitives.Vector;

import static primitives.Util.isZero;

/**
 * class Tube
 */
public class Tube extends RadialGeometry {

    /**
     *   @param axis that contains the ray
     */
    protected  final Ray axis;
    /**
     *   @param axisRay ray
     *   @param radius radius
     */
    public Tube(Ray axisRay, double radius) {
        super(radius);
        this.axis = axisRay;
    }

    @Override
    public Vector getNormal(Point point) {
        Point p0= axis.getHead();
        Vector v = axis.getDir();
        Vector P0_p= point.subtract(p0);
        double t= axis.getDir().dotProduct(P0_p);
        if (isZero(t)) {
            return P0_p.normalize();
        }
        Point O = axis.getPoint(t);
        return point.subtract(O).normalize();
    }
}

