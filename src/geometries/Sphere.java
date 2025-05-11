package geometries;
import primitives.Point;
import primitives.Vector;


/**
 * class      Sphere
 */
public class Sphere extends RadialGeometry {

    final private  Point center;
    /**
     *   @param c point on  the sphere
     *   @param r radius
     */
    public Sphere(Point c, double r) {
        super(r);
        this.center = c;
    }

    @Override
    public Vector getNormal(Point point) {
        return point.subtract(center).normalize();
    }

}

