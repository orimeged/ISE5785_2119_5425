package renderer;

import primitives.Color;
import primitives.Point;
import primitives.Ray;
import primitives.Vector;

import java.util.LinkedList;
import java.util.List;
import java.util.MissingResourceException;

import static primitives.Util.alignZero;
import static primitives.Util.isZero;

/**
 * Camera class for rendering scenes using ray tracing.
 * Supports super sampling, adaptive anti-aliasing, and multithreading.
 * Implements the Builder design pattern.
 *
 * @author Nethanel Hasid and Ori Meged
 */
public class Camera implements Cloneable {
    // Camera position in 3D space
    private Point place;
    // Camera orientation vectors
    private Vector vUp, vTo, vRight;
    // View plane dimensions and distance from camera
    private double height = 0, width = 0, distance = 0;
    // Image writer for output
    private ImageWriter imageWriter;
    // Ray tracer for rendering
    private RayTracerBase rayTracer;
    // Adaptive anti-aliasing flag
    private boolean adaptive = false;
    // Number of threads for rendering
    private int threadsCount = 1;
    // Number of rays per pixel for super sampling
    private int numberOfRays = 1;
    // Number of spare threads to leave unused
    private static final int SPARE_THREADS = 2;

    // Private constructor for Builder pattern
    private Camera() {}

    // Returns a new Builder instance for Camera
    public static Builder getBuilder() {
        return new Builder();
    }

    // Returns the camera's position
    public Point getPlace() {
        return place;
    }

    // Constructs a single ray through a specific pixel (j, i) on the view plane
    public Ray constructRay(int nX, int nY, int j, int i) {
        Point pc = place.add(vTo.scale(distance));
        double rx = width / nX;
        double ry = height / nY;
        double xJ = (j - (nX - 1) / 2.0) * rx;
        double yI = -1 * (i - (nY - 1) / 2.0) * ry;

        Point pij = pc;
        if (!isZero(xJ)) pij = pij.add(vRight.scale(xJ));
        if (!isZero(yI)) pij = pij.add(vUp.scale(yI));

        return new Ray(place, pij.subtract(place));
    }

    // Constructs multiple rays for super sampling through a pixel
    public List<Ray> constructRays(int nX, int nY, int j, int i) {
        Point center = getCenterOfPixel(nX, nY, j, i);
        List<Ray> rays = new LinkedList<>();
        double rx = width / nX, ry = height / nY;
        double subRx = rx / numberOfRays, subRy = ry / numberOfRays;

        // Generate rays through sub-pixels for super sampling
        for (int si = 0; si < numberOfRays; si++) {
            for (int sj = 0; sj < numberOfRays; sj++) {
                Point subP = center;
                double x = (sj - (numberOfRays - 1) / 2d) * subRx;
                double y = -(si - (numberOfRays - 1) / 2d) * subRy;
                if (!isZero(x)) subP = subP.add(vRight.scale(x));
                if (!isZero(y)) subP = subP.add(vUp.scale(y));
                rays.add(new Ray(place, subP.subtract(place)));
            }
        }
        return rays;
    }

    // Returns the center point of a specific pixel on the view plane
    private Point getCenterOfPixel(int nX, int nY, double j, double i) {
        double rx = alignZero(width / nX);
        double ry = alignZero(height / nY);
        double x = alignZero((j - (nX - 1d) / 2d) * rx);
        double y = alignZero(-(i - (nY - 1d) / 2d) * ry);

        Point p = place.add(vTo.scale(distance));
        if (!isZero(x)) p = p.add(vRight.scale(x));
        if (!isZero(y)) p = p.add(vUp.scale(y));
        return p;
    }

    // Renders the image using the current camera configuration
    public Camera renderImage() {
        // Validate camera configuration
        if (place == null || vRight == null || vUp == null || vTo == null ||
                distance == 0 || width == 0 || height == 0 ||
                imageWriter == null || rayTracer == null) {
            throw new MissingResourceException("Missing camera data", Camera.class.getName(), null);
        }

        long start = System.nanoTime();
        // Initialize pixel management for multithreading
        Pixel.initialize(imageWriter.getNy(), imageWriter.getNx(), 1);

        // Rendering logic for each thread
        Runnable renderer = () -> {
            for (Pixel pixel = new Pixel(); pixel.nextPixel(); Pixel.pixelDone()) {
                Color color = (numberOfRays == 1)
                        ? rayTracer.traceRays(constructRays(imageWriter.getNx(), imageWriter.getNy(), pixel.col, pixel.row))
                        : SuperSampling(imageWriter.getNx(), imageWriter.getNy(), pixel.col, pixel.row, numberOfRays, adaptive);
                imageWriter.writePixel(pixel.col, pixel.row, color);
            }
        };

        // Create and start threads for rendering
        List<Thread> threads = new LinkedList<>();
        for (int i = 0; i < threadsCount; i++) {
            threads.add(new Thread(renderer));
        }
        threads.forEach(Thread::start);
        // Wait for all threads to finish
        Pixel.waitToFinish();

        double ms = (System.nanoTime() - start) / 1_000_000.0;
        System.out.println("Render time: " + ms + " ms");
        return this;
    }

    // Performs super sampling or adaptive anti-aliasing for a pixel
    private Color SuperSampling(int nX, int nY, int j, int i, int rays, boolean adaptiveAliasing) {
        Point center = getCenterOfPixel(nX, nY, j, i);
        double rx = alignZero(width / nX);
        double ry = alignZero(height / nY);
        double minRx = rx / Math.floor(Math.sqrt(rays));
        double minRy = ry / Math.floor(Math.sqrt(rays));

        return adaptiveAliasing
                ? rayTracer.AdaptiveSuperSamplingRec(center, rx, ry, minRx, minRy, place, vRight, vUp, null)
                : rayTracer.RegularSuperSampling(center, rx, ry, minRx, minRy, place, vRight, vUp, null);
    }

    // Writes the rendered image to disk
    public void writeToImage() {
        imageWriter.writeToImage();
    }

    // Draws a grid on the image for debugging or visualization
    public Camera printGrid(int interval, Color color) {
        for (int j = 0; j < imageWriter.getNx(); j++)
            for (int i = 0; i < imageWriter.getNy(); i++)
                if (j % interval == 0 || i % interval == 0)
                    imageWriter.writePixel(j, i, color);
        return this;
    }

    // Builder class for constructing Camera instances
    public static class Builder {
        private final Camera camera = new Camera();

        // Sets the camera's location
        public Builder setLocation(Point p) {
            camera.place = p;
            return this;
        }

        // Sets the camera's direction vectors (vTo and vUp)
        public Builder setDirection(Vector to, Vector up) {
            if (!isZero(to.dotProduct(up)))
                throw new IllegalArgumentException("vTo and vUp must be orthogonal");
            camera.vTo = to.normalize();
            camera.vUp = up.normalize();
            camera.vRight = camera.vTo.crossProduct(camera.vUp).normalize();
            return this;
        }

        // Sets the view plane size
        public Builder setVpSize(double w, double h) {
            if (w <= 0 || h <= 0)
                throw new IllegalArgumentException("Width and height must be positive");
            camera.width = w;
            camera.height = h;
            return this;
        }

        // Sets the distance from the camera to the view plane
        public Builder setVpDistance(double d) {
            if (d <= 0)
                throw new IllegalArgumentException("Distance must be positive");
            camera.distance = d;
            return this;
        }

        // Sets the image writer for output
        public Builder setImageWriter(ImageWriter iw) {
            camera.imageWriter = iw;
            return this;
        }

        // Sets the ray tracer for rendering
        public Builder setRayTracer(RayTracerBase rt) {
            camera.rayTracer = rt;
            return this;
        }

        // Sets the number of rays per pixel for super sampling
        public Builder setNumberOfRays(int rays) {
            if (rays < 1)
                throw new IllegalArgumentException("Number of rays must be >= 1");
            camera.numberOfRays = rays;
            return this;
        }

        // Enables or disables adaptive anti-aliasing
        public Builder setAdaptive(boolean adaptive) {
            camera.adaptive = adaptive;
            return this;
        }

        // Sets the number of threads for rendering
        public Builder setMultithreading(int threads) {
            if (threads < -2)
                throw new IllegalArgumentException("Invalid thread count");
            if (threads == -2) {
                int cores = Runtime.getRuntime().availableProcessors();
                camera.threadsCount = Math.max(1, cores - SPARE_THREADS);
            } else {
                camera.threadsCount = Math.max(1, threads);
            }
            return this;
        }

        // Builds and returns the Camera instance
        public Camera build() {
            if (camera.place == null || camera.vTo == null || camera.vUp == null ||
                    camera.width == 0 || camera.height == 0 || camera.distance == 0 ||
                    camera.imageWriter == null || camera.rayTracer == null)
                throw new MissingResourceException("Camera is missing required data", Camera.class.getName(), "build");

            camera.vRight = camera.vTo.crossProduct(camera.vUp).normalize();
            return camera;
        }
    }
}