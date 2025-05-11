package geometries;
import primitives.Vector;
import primitives.Point;
/**
 *  * Abstract class Geometry  that represents the geometries
 */
public abstract  class Geometry {

    /**
     * Get the normal vector at the given point.
     *
     * @param  point	The point at which to calculate the normal vector
     * @return      	The normal vector at the given point
     */
    public abstract Vector getNormal(Point point);
}