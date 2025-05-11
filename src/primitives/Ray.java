package primitives;


/**
 * class Ray
 */
public class Ray {
    private final Point head;
    private final Vector dir;
    /**
     * Constructor for Ray based on a starting point and a direction vector.
     * The direction vector is normalized during initialization.
     *
     * @param head2 the starting point of the ray
     * @param dir2  the direction vector of the ray (will be normalized)
     */
    public Ray(Point head2, Vector dir2) {
        this.head = head2;
        this.dir = dir2.normalize();
    }
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        return (obj instanceof Ray other)
                && this.head.equals(other.head)
                && this.dir.equals(other.dir);
    }
    @Override
    public String toString() {
        return "Ray{" +
                "p0=" + head +
                ", dir=" +  dir +
                '}';    }

}
