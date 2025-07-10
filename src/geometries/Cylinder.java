package geometries;

import primitives.Point;
import primitives.Ray;
import primitives.Vector;

import java.util.LinkedList;
import java.util.List;

/**
 * A finite cylinder: a Tube of given radius and finite height,
 * capped by two circular planes at its ends.
 */
public class Cylinder extends Tube {
    /**
     * The height of the cylinder.
     */
    private final double height;

    /**
     * Constructs a Cylinder with the given radius, central axis ray, and height.
     *
     * @param radius  the radius of the cylinder's circular cross-section
     * @param axisRay the central axis ray of the cylinder
     * @param height  the finite height of the cylinder; must be positive
     * @throws IllegalArgumentException if height is not positive
     */
    public Cylinder(double radius, Ray axisRay, double height) {
        super(radius, axisRay);
        if (height <= 0) {
            throw new IllegalArgumentException("Height must be positive");
        }
        this.height = height;
    }

    /**
     * Returns the height of the cylinder.
     *
     * @return the cylinder's height
     */
    public double getHeight() {
        return height;
    }

    /**
     * Calculates the intersections (if any) of a given ray with this cylinder,
     * including its circular end caps.
     *
     * @param ray the Ray to intersect with the cylinder
     * @return a list of Intersection objects representing hit points, or null if no intersections
     */
    @Override
    protected List<Intersectable.Intersection> calculateIntersectionsHelper(Ray ray) {
        Vector axisDir = getAxisRay().getDirection();
        Point baseCenter = getAxisRay().getPoint();
        Point topCenter = baseCenter.add(axisDir.scale(height));
        double radiusSq = getRadius() * getRadius();

        List<Intersectable.Intersection> result = new LinkedList<>();

        // Distance along axis from base to ray origin projection
        double originProj = axisDir.dotProduct(ray.getPoint().subtract(baseCenter));

        // Determine if ray is parallel to cylinder axis
        boolean isVertical = Math.abs(Math.abs(ray.getDirection().dotProduct(axisDir)) - 1) < 1e-10;

        // Prepare planes for end caps
        Plane bottomPlane = new Plane(baseCenter, axisDir);
        Plane topPlane = new Plane(topCenter, axisDir);

        if (isVertical) {
            double dirProj = ray.getDirection().dotProduct(axisDir);

            // Ray starts above, below, or inside, handle caps accordingly
            if (originProj > height) {
                // Only top cap
                addCapIntersections(topPlane, topCenter, radiusSq, ray, result);
            } else if (originProj < 0) {
                // Bottom then top cap
                addCapIntersections(bottomPlane, baseCenter, radiusSq, ray, result);
                addCapIntersections(topPlane, topCenter, radiusSq, ray, result);
            } else {
                // Inside cylinder: exit through one cap
                if (dirProj > 0) {
                    addCapIntersections(topPlane, topCenter, radiusSq, ray, result);
                } else if (dirProj < 0) {
                    addCapIntersections(bottomPlane, baseCenter, radiusSq, ray, result);
                }
            }
        } else {
            // 1. Side intersections within the finite height
            List<Intersectable.Intersection> sideHits = super.calculateIntersectionsHelper(ray);
            if (sideHits != null) {
                for (var h : sideHits) {
                    double proj = axisDir.dotProduct(h.point.subtract(baseCenter));
                    if (proj >= 0 && proj <= height) {
                        result.add(new Intersectable.Intersection(this, h.point));
                    }
                }
            }
            // 2. Bottom cap
            addCapIntersections(bottomPlane, baseCenter, radiusSq, ray, result);
            // 3. Top cap
            addCapIntersections(topPlane, topCenter, radiusSq, ray, result);
        }

        return result.isEmpty() ? null : result;
    }

    /**
     * Helper to add intersections with a circular cap if within radius.
     *
     * @param capPlane  the plane representing the cap
     * @param capCenter the center point of the cap circle
     * @param radiusSq  squared radius for distance check
     * @param ray       the ray to test
     * @param result    the list to add valid intersections to
     */
    private void addCapIntersections(Plane capPlane, Point capCenter,
                                     double radiusSq, Ray ray,
                                     List<Intersectable.Intersection> result) {
        var hits = capPlane.calculateIntersectionsHelper(ray);
        if (hits != null) {
            for (var h : hits) {
                if (h.point.subtract(capCenter).lengthSquared() <= radiusSq) {
                    result.add(new Intersectable.Intersection(this, h.point));
                }
            }
        }
    }

    /**
     * Returns a string representation of the cylinder, including axis, radius, and height.
     *
     * @return a descriptive string of this cylinder
     */
    @Override
    public String toString() {
        return "Cylinder{" +
                "axis=" + getAxisRay() +
                ", radius=" + getRadius() +
                ", height=" + height +
                '}';
    }
}
