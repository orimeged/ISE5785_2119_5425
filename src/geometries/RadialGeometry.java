package geometries;

/**
 * class radialGeometry is a class representing a geometry
 * with a radius of Euclidean geometry in Cartesian 3-Dimensional coordinate system.
 *
 * @author ori meged and nethanel hasid
 */
public abstract class RadialGeometry extends Geometry
{
    /**
     * the geometry radius
     */
    protected double radius;

    /**
     * Constructor to initialize radialGeometry based on a radius
     * @param radius
     */
    public RadialGeometry(double radius)
    {
        this.radius = radius;
    }


}
