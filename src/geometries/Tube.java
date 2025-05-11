package geometries;
import primitives.Point;
import primitives.Ray;
import primitives.Vector;

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
        return null;
    }

}

