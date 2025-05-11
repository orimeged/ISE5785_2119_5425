package primitives;

/**
 * class Point
 */
public class Point {
    /**
     * point at (0,0,0)   to check the zero point
     */
    public static final Point ZERO = new Point(0,0,0);
    /**
     * Double3 value containing x,z,z axis
     */
    protected final Double3 xyz;
    /**
     *  constructor for Point
     * @param xyz Double3 value containing x,z,z axis
     */
     Point(Double3 xyz) {
        this.xyz = xyz;
    }

    /**
     * primary constructor for Point
     *
     * @param x coordinate value for X axis
     * @param y coordinate value for Y axis
     * @param z coordinate value for Z axis
     */
    public Point(double x, double y , double z) {
        this.xyz = new Double3(x,y,z);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        return (obj instanceof Point other)
                && this.xyz.equals(other.xyz);
    }

    @Override
    public String toString() {
        return "Point :" + this.xyz;
    }

    /**
     * Adds the given vector to the current point.
     *
     * @param  vector   the vector to be added
     * @return          the resulting point after the addition
     */
    public Point add(Vector vector) {

        return new Point(xyz.add(vector.xyz));
    }
    /**
     * Subtract another point to create a new vector.
     *
     * @param  other  the point to subtract
     * @return       the new vector created
     */
    public Vector subtract(Point other) {
        return new Vector(xyz.subtract(other.xyz));
    }
    /**
     * Calculates the distance between this point and the given point.
     *
     * @param  point2   the point to calculate the distance to
     * @return         the distance between this point and the given point
     */
    public  double distance (Point point2){
        return Math.sqrt(distanceSquared(point2));
    }

    /**
     * Calculate the squared distance between this point and the given point.
     *
     * @param  point2   the other point to calculate the distance to
     * @return          the squared distance between the two points
     */
    public double distanceSquared(Point point2){
        double x1 = xyz.d1();
        double y1 = xyz.d2();
        double z1 = xyz.d3();
        double x2 = point2.xyz.d1();
        double y2 = point2.xyz.d2();
        double z2 = point2.xyz.d3();

        return ((x2 - x1)*(x2 - x1) + (y2 - y1)*(y2 - y1) + (z2 - z1)*(z2 - z1));
    }


}
