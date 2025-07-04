package geometries;
import primitives.*;

import java.util.List;

import static primitives.Util.isZero;

/**
 * class Triangle is a basic class representing a triangle
 * of Euclidean geometry in Cartesian 3-Dimensional coordinate system.
 *
 * @author ori meged and nethanel hasid
 */
public class Triangle  extends Polygon
{

    /**
     * Constructs a Triangle object with three given points.
     * @param p1 the first point of the triangle
     * @param p2  the second point of the triangle
     * @param p3 the third point of the triangle
     */
    public Triangle(Point p1, Point p2, Point p3)
    {
        super(p1, p2, p3);
    }

    @Override
    public List<GeoPoint> findGeoIntersectionsHelper(Ray ray) {
//        if(this.plane.findIntersections(ray)==null)
//            return null;
//        Vector v1 = this.vertices.get(0).subtract(ray.getPoint());
//        Vector v2 =this.vertices.get(1).subtract(ray.getPoint());
//        Vector v3 = this.vertices.get(2).subtract(ray.getPoint());
//
//        Vector n1 = (v1.crossProduct(v2)).normalize();
//        Vector n2 = (v2.crossProduct(v3)).normalize();
//        Vector n3 = (v3.crossProduct(v1)).normalize();
//
//        double t1=n1.dotProduct(ray.getDirection());
//        double t2=n2.dotProduct(ray.getDirection());
//        double t3=n3.dotProduct(ray.getDirection());
//
//        if((t1 > 0 && t2 > 0 && t3 > 0) ||(t1 < 0 && t2 < 0 && t3 < 0))
//        {
//            return this.plane.findGeoIntersections(ray);
//        }
//        return null;
//    }
        List<Point> intersections = plane.findIntersections(ray);
        // if there are no intersections with the plane, there are no intersections with the triangle
        if (intersections == null) {
            return null;
        }

        // if the ray intersects the plane at the triangle's plane
        Vector v1 = vertices.get(0).subtract(ray.getPoint());
        Vector v2 = vertices.get(1).subtract(ray.getPoint());
        Vector v3 = vertices.get(2).subtract(ray.getPoint());

        Vector n1 = v1.crossProduct(v2).normalize();
        Vector n2 = v2.crossProduct(v3).normalize();
        Vector n3 = v3.crossProduct(v1).normalize();

        double s1 = ray.getDirection().dotProduct(n1);
        double s2 = ray.getDirection().dotProduct(n2);
        double s3 = ray.getDirection().dotProduct(n3);

        // if the ray is parallel to the triangle's plane
        if (isZero(s1) || isZero(s2) || isZero(s3)) {
            return null;
        }

        if ((s1 > 0 && s2 > 0 && s3 > 0) || (s1 < 0 && s2 < 0 && s3 < 0)) {
            return List.of(new GeoPoint(this, intersections.get(0)));
        }
        // if the ray intersects the plane but not the triangle
        return null;
    }
}
