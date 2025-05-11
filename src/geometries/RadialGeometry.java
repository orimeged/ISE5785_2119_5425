package geometries;

/**
 * abstract class RadialGeometry
 */
public abstract class RadialGeometry extends Geometry {

    /**
     * double that contains the radius
     */
    protected final double radius;
    /**
     * constructor for RadialGeometry
     */
    RadialGeometry(double r){
        this.radius=r;
    }
}
