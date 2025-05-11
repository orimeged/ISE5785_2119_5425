package primitives;

/**
 * class Vector
 */
public class Vector extends Point {

    /**
     * secondary  constructor for Vector class
     *
     * @param D3 {@link Double3 } head values of vector
     */
    public Vector(Double3 D3) {
        super(D3);
        if (D3.equals(Double3.ZERO)) {
                throw new IllegalArgumentException(" Vector(0,0,0) is not allowed");
            }

        }
    /**
     * primary Constructor for Vector
     *
     * @param x coordinate value for X axis
     * @param y coordinate value for Y axis
     * @param z coordinate value for Z axis
     */
    public Vector(double x, double y, double z) {
        super(new Double3(x, y, z));
        if (new Double3(x, y, z).equals(Double3.ZERO)) {
                throw new IllegalArgumentException(" Vector(0,0,0) is not allowed");
            }
        }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        return (obj instanceof Vector other)
                && this.xyz.equals(other.xyz);
    }
    @Override
    public String toString() {
        return "Vector{" +
                "xyz=" + this.xyz +
                '}';    }
    /**
     * Calculate the squared length of the xyz vector.
     *
     * @return the squared length of the xyz vector
     */
    public double lengthSquared() {
        return this.xyz.d1() * this.xyz.d1() + this.xyz.d2() * this.xyz.d2() + this.xyz.d3() * this.xyz.d3();
    }
    /**
     * Calculate the length of something.
     *
     * @return         the length of something
     */
    public double length() {
        return Math.sqrt(lengthSquared());
    }
    @Override
    public Vector add(Vector vector2) {
        return new Vector(xyz.add(vector2.xyz));
    }
    /**
     * Scales the vector by the given scalar.
     *
     * @param  scalar   the value by which the vector is to be scaled
     * @return          a new Vector object representing the scaled vector
     */
    public Vector scale(double scalar) {
        return new Vector(xyz.scale(scalar));
    }
    /**
     * Calculates the dot product of this vector with another vector.
     *
     * @param  vector2   the other vector to calculate the dot product with
     * @return          the result of the dot product calculation
     */
    public double dotProduct(Vector vector2) {
        return this.xyz.d1() * vector2.xyz.d1() + this.xyz.d2()* vector2.xyz.d2() + this.xyz.d3() * vector2.xyz.d3();
    }
    /**
     * Calculate the cross product of this vector and the input vector.
     *
     * @param  vector2   the input vector for the cross product
     * @return           a new Vector representing the cross product
     */
    public Vector crossProduct(Vector vector2) {
        double x1 = this.xyz.d1();
        double y1 = this.xyz.d2();
        double z1 = this.xyz.d3();
        double x2 = vector2.xyz.d1();
        double y2 = vector2.xyz.d2();
        double z2 = vector2.xyz.d3();

        double vectorX = y1 * z2 - z1 * y2;
        double vectorY = z1 * x2 - x1 * z2;
        double vectorZ = x1 * y2 - y1 * x2;

        return new Vector(vectorX, vectorY, vectorZ);
    }

    /**
     * Normalize the vector.
     *
     * @return         	the normalized vector
     */

    public Vector normalize(){
        return new Vector(xyz.reduce(length()));
    }






}