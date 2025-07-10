package renderer;

import geometries.Intersectable.GeoPoint;
import lighting.LightSource;
import primitives.*;
import primitives.Vector;
import renderer.RayTracerBase;
import scene.Scene;

import java.util.*;

import static java.awt.Color.BLACK;
import static primitives.Util.alignZero;

/**
 * A basic ray tracer that calculates local and global illumination,
 * supports transparency, shadows, reflection, and super sampling.
 *
 * @author Nethanel hasid and Ori Meged
 */
public class SimpleRayTracer extends RayTracerBase {

    // Small offset to avoid self-intersection
    public static final double DELTA = 0.1;
    // Maximum recursion depth for global effects
    private static final int MAX_CALC_COLOR_LEVEL = 10;
    // Minimum contribution threshold for recursion
    private static final double MIN_CALC_COLOR_K = 0.001;
    // Initial transparency/reflection coefficient
    private static final Double3 INITIAL_K = Double3.ONE;

    // Number of points for soft shadow sampling
    private int numberOfPoints = 80;

    // Constructor initializing the scene
    public SimpleRayTracer(Scene scene) {
        super(scene);
    }

    // Sets the number of points for soft shadow calculation
    public SimpleRayTracer setNumberOfPoints(int numberOfPoints) {
        this.numberOfPoints = numberOfPoints;
        return this;
    }

    // Traces a single ray and returns the resulting color
    @Override
    public Color traceRay(Ray ray) {
        GeoPoint closestPoint = findClosestIntersection(ray);
        return closestPoint == null ? scene.background : calcColor(closestPoint, ray);
    }

    // Traces a list of rays (for super sampling) and averages the color
    @Override
    public Color traceRays(List<Ray> rays) {
        Color color = new Color(BLACK);
        for (Ray ray : rays) {
            GeoPoint gp = findClosestIntersection(ray);
            color = color.add(gp == null ? scene.background : calcColor(gp, ray));
        }
        return color.reduce(rays.size());
    }

    // Finds the closest intersection point of a ray with the scene geometries
    private GeoPoint findClosestIntersection(Ray ray) {
        List<GeoPoint> intersections = scene.geometries.findGeoIntersections(ray);
        return intersections == null ? null : ray.findClosestGeoPoint(intersections);
    }

    // Calculates the color at a given intersection point (including ambient light)
    private Color calcColor(GeoPoint gp, Ray ray) {
        return calcColor(gp, ray, MAX_CALC_COLOR_LEVEL, INITIAL_K).add(scene.ambientLight.getIntensity());
    }

    // Recursively calculates the color at a point, including local and global effects
    private Color calcColor(GeoPoint gp, Ray ray, int level, Double3 k) {
        Color local = calcLocalEffects(gp, ray, k);
        return level == 1 ? local : local.add(calcGlobalEffects(gp, ray, level, k));
    }

    // Calculates local lighting effects (diffuse, specular, emission, shadows)
    private Color calcLocalEffects(GeoPoint gp, Ray ray, Double3 k) {
        Color color = gp.geometry.getEmission();
        Vector v = ray.getDirection(), n = gp.geometry.getNormal(gp.point);
        double nv = alignZero(n.dotProduct(v));
        if (nv == 0) return color;

        Material mat = gp.geometry.getMaterial();
        for (LightSource light : scene.lights) {
            Vector l = light.getL(gp.point).normalize();
            double nl = alignZero(n.dotProduct(l));
            if (nl * nv > 0) {
                Double3 ktr = hitPercentageColor(gp, light, n, l);
                if (!ktr.product(k).lowerThan(MIN_CALC_COLOR_K)) {
                    Color Li = light.getIntensity(gp.point).scale(ktr);
                    color = color.add(
                            Li.scale(calcDiffusive(mat, nl)),
                            Li.scale(calcSpecular(mat, n, l, nl, v))
                    );
                }
            }
        }
        return color;
    }

    // Calculates global lighting effects (reflection and refraction)
    private Color calcGlobalEffects(GeoPoint gp, Ray ray, int level, Double3 k) {
        Material mat = gp.geometry.getMaterial();
        Vector v = ray.getDirection(), n = gp.geometry.getNormal(gp.point);
        return calcGlobalEffect(constructRefractedRay(gp, v, n), mat.getKT(), level, k)
                .add(calcGlobalEffect(constructReflectedRay(gp, v, n), mat.getKR(), level, k));
    }

    // Calculates the color contribution from a single global effect (reflection/refraction)
    private Color calcGlobalEffect(Ray ray, Double3 kx, int level, Double3 k) {
        Double3 kkx = k.product(kx);
        if (kkx.lowerThan(MIN_CALC_COLOR_K)) return Color.BLACK;
        GeoPoint gp = findClosestIntersection(ray);
        return gp == null ? scene.background.scale(kx)
                : calcColor(gp, ray, level - 1, kkx).scale(kx);
    }

    // Constructs a reflected ray from a point
    private Ray constructReflectedRay(GeoPoint gp, Vector v, Vector n) {
        double nv = n.dotProduct(v);
        if (nv == 0) return null;
        Vector r = v.subtract(n.scale(2 * nv));
        return r.length() == 0 ? null : new Ray(gp.point, r, n);
    }

    // Constructs a refracted ray from a point
    private Ray constructRefractedRay(GeoPoint gp, Vector v, Vector n) {
        return v.length() == 0 ? null : new Ray(gp.point, v, n);
    }

    // Calculates the specular reflection component
    private Double3 calcSpecular(Material mat, Vector n, Vector l, double nl, Vector v) {
        Vector r = l.subtract(n.scale(2 * nl));
        double vr = alignZero(-v.dotProduct(r));
        return vr <= 0 ? Double3.ZERO : mat.getKS().scale(Math.pow(vr, mat.getShininess()));
    }

    // Calculates the diffusive reflection component
    private Double3 calcDiffusive(Material mat, double nl) {
        return mat.getKD().scale(Math.abs(nl));
    }

    // Calculates the transparency factor for a point with respect to a light source
    private Double3 transparency(GeoPoint gp, LightSource light, Vector l, Vector n) {
        Vector lDir = l.scale(-1);
        if (lDir.length() == 0) return Double3.ZERO;
        Ray ray = new Ray(gp.point, lDir, n);
        List<GeoPoint> intersections = scene.geometries.findGeoIntersections(ray);
        if (intersections == null) return Double3.ONE;

        Double3 ktr = INITIAL_K;
        double distToLight = light.getDistance(gp.point);
        for (GeoPoint inter : intersections) {
            if (alignZero(inter.point.distance(gp.point) - distToLight) <= 0) {
                ktr = ktr.product(inter.geometry.getMaterial().getKT());
                if (ktr.equals(Double3.ZERO)) break;
            }
        }
        return ktr;
    }

    // Calculates the average transparency for soft shadows using multiple sample points
    private Double3 hitPercentageColor(GeoPoint gp, LightSource light, Vector n, Vector l) {
        Point[] samplePoints = light.getPoints(gp.point, numberOfPoints);
        if (samplePoints == null) return transparency(gp, light, l, n);

        Double3 average = Double3.ZERO;
        for (Point pt : samplePoints) {
            Vector dir = gp.point.subtract(pt);
            if (dir.length() == 0) continue;
            average = average.add(transparency(gp, light, dir.normalize(), n).reduce(samplePoints.length));
        }
        return average;
    }

    // Checks if a point is in a list of points
    private boolean isInList(List<Point> list, Point point) {
        return list.contains(point);
    }

    // Performs adaptive super sampling recursively for anti-aliasing
    @Override
    public Color AdaptiveSuperSamplingRec(Point center, double w, double h, double minW, double minH,
                                          Point camLoc, Vector right, Vector up, List<Point> prev) {
        if (w < minW * 2 || h < minH * 2) {
            Vector dir = center.subtract(camLoc);
            return dir.length() == 0 ? scene.background : traceRay(new Ray(camLoc, dir));
        }

        List<Point> corners = new LinkedList<>();
        List<Color> colors = new LinkedList<>();
        List<Point> subCenters = new LinkedList<>();

        // Sample the four corners of the current region
        for (int i = -1; i <= 1; i += 2)
            for (int j = -1; j <= 1; j += 2) {
                Point corner = center.add(right.scale(i * w / 2)).add(up.scale(j * h / 2));
                corners.add(corner);
                if (prev == null || !isInList(prev, corner)) {
                    Vector dir = corner.subtract(camLoc);
                    colors.add(dir.length() == 0 ? scene.background : traceRay(new Ray(camLoc, dir)));
                    subCenters.add(center.add(right.scale(i * w / 4)).add(up.scale(j * h / 4)));
                }
            }

        if (subCenters.isEmpty() || colors.isEmpty()) return Color.BLACK;
        // If all corner colors are almost equal, return the color
        if (colors.stream().allMatch(c -> c.isAlmostEquals(colors.getFirst()))) return colors.getFirst();

        // Otherwise, recursively subdivide and sample subregions
        Color sum = Color.BLACK;
        for (Point sub : subCenters) {
            sum = sum.add(AdaptiveSuperSamplingRec(sub, w / 2, h / 2, minW, minH, camLoc, right, up, corners));
        }
        return sum.reduce(subCenters.size());
    }

    // Performs regular super sampling for anti-aliasing
    public Color RegularSuperSampling(Point center, double w, double h, double minW, double minH,
                                      Point camLoc, Vector right, Vector up, List<Point> prev) {
        List<Color> colors = new ArrayList<>();
        int numX = (int) Math.ceil(w / minW);
        int numY = (int) Math.ceil(h / minH);
        Random rand = new Random();

        // Generate random sample points within the region
        for (int i = 0; i < numY; i++) {
            for (int j = 0; j < numX; j++) {
                double offsetX = minW * j + rand.nextDouble() * minW;
                double offsetY = minH * i + rand.nextDouble() * minH;
                Point p = center.add(right.scale(offsetX - w / 2)).add(up.scale(offsetY - h / 2));
                if (prev == null || !isInList(prev, p)) {
                    Vector dir = p.subtract(camLoc);
                    colors.add(dir.length() == 0 ? scene.background : traceRay(new Ray(camLoc, dir)));
                }
            }
        }
        return colors.isEmpty() ? Color.BLACK : colors.stream().reduce(Color.BLACK, Color::add).reduce(colors.size());
    }
}