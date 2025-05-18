package renderer;

import primitives.*;
import scene.Scene;

import java.util.MissingResourceException;

import static primitives.Util.isZero;

public class Camera implements Cloneable {
    private Point location;
    private Vector vTo;
    private Vector vUp;
    private Vector vRight;
    private double vpDistance;
    private double vpWidth;
    private double vpHeight;
    private int nX = 1;
    private int nY = 1;
    private ImageWriter imageWriter;
    private RayTracerBase rayTracer;

    private Camera() {
        this.location = new Point(0, 0, 0);
        this.vTo = new Vector(0, 0, -1);
        this.vUp = new Vector(0, 1, 0);
        this.vRight = new Vector(1, 0, 0);
        this.vpDistance = 1;
        this.vpWidth = 1;
        this.vpHeight = 1;
    }

    public static Builder getBuilder() {
        return new Builder();
    }

    /**
     * Constructs a ray through a pixel in the view plane
     *
     * @param nX Number of pixels in the horizontal dimension
     * @param nY Number of pixels in the vertical dimension
     * @param j  Pixel's column (from 0)
     * @param i  Pixel's row (from 0)
     * @return Ray from camera through the pixel
     */
    public Ray constructRay(int nX, int nY, int j, int i) {
        Point pIJ = this.location.add(this.vTo.scale(this.vpDistance));
        double yI = ((double) (nY - 1) / 2 - i) * this.vpHeight / nY;
        double xJ = (j - ((double) (nX - 1) / 2)) * this.vpWidth / nX;
        if (!isZero(xJ)) pIJ = pIJ.add(this.vRight.scale(xJ));
        if (!isZero(yI)) pIJ = pIJ.add(this.vUp.scale(yI));
        Vector vIJ = pIJ.subtract(this.location);
        return new Ray(this.location, vIJ);
    }

    /**
     * Renders the image.
     *
     * @return this camera object for method chaining
     */
    public Camera renderImage() {
        if (imageWriter == null) {
            throw new MissingResourceException("Missing image writer", ImageWriter.class.getName(), "");
        }
        if (rayTracer == null) {
            throw new MissingResourceException("Missing ray tracer", RayTracerBase.class.getName(), "");
        }

        // For each pixel in the view plane, cast a ray and compute its color
        for (int i = 0; i < nY; i++) {
            for (int j = 0; j < nX; j++) {
                castRay(j, i);
            }
        }

        return this;
    }

    /**
     * Casts a ray through a pixel and colors it accordingly.
     *
     * @param j the pixel's column index (X coordinate)
     * @param i the pixel's row index (Y coordinate)
     */
    private void castRay(int j, int i) {
        Ray ray = constructRay(nX, nY, j, i);
        Color color = rayTracer.traceRay(ray);
        imageWriter.writePixel(j, i, color);
    }

    /**
     * Prints a grid on the image.
     *
     * @param interval the interval between grid lines
     * @param color    the color of the grid lines
     * @return this camera object for method chaining
     */
    public Camera printGrid(int interval, Color color) {
        if (imageWriter == null) {
            throw new MissingResourceException("Missing image writer", ImageWriter.class.getName(), "");
        }

        for (int i = 0; i < nY; i++) {
            // Draw horizontal lines
            if (i % interval == 0) {
                for (int j = 0; j < nX; j++) {
                    imageWriter.writePixel(j, i, color);
                }
            }
        }

        for (int j = 0; j < nX; j++) {
            // Draw vertical lines
            if (j % interval == 0) {
                for (int i = 0; i < nY; i++) {
                    imageWriter.writePixel(j, i, color);
                }
            }
        }

        return this;
    }

    /**
     * Writes the rendered image to a file.
     *
     * @param imageName the name of the image file (without extension)
     * @return this camera object for method chaining
     */
    public Camera writeToImage(String imageName) {
        if (imageWriter == null) {
            throw new MissingResourceException("Missing image writer", ImageWriter.class.getName(), "");
        }

        imageWriter.writeToImage(imageName);
        return this;
    }

    public static class Builder {
        private final Camera camera;

        /**
         * Default constructor for Builder
         */
        public Builder() {
            camera = new Camera();
        }

        /**
         * Sets the location of the camera
         *
         * @param location The point representing camera location
         * @return The builder object for method chaining
         */
        public Builder setLocation(Point location) {
            if (location == null) {
                throw new IllegalArgumentException("Camera location cannot be null");
            }
            camera.location = location;
            return this;
        }

        /**
         * Sets the orientation of the camera using direction vectors
         *
         * @param vTo The vector pointing in the direction the camera is facing
         * @param vUp The vector pointing upward relative to the camera
         * @return The builder object for method chaining
         */
        public Builder setDirection(Vector vTo, Vector vUp) {
            if (vTo == null || vUp == null) {
                throw new IllegalArgumentException("Camera vectors cannot be null");
            }

            // Check if vectors are orthogonal (perpendicular to each other)
            if (vTo.dotProduct(vUp) != 0) {
                throw new IllegalArgumentException("Camera vectors vTo and vUp must be orthogonal");
            }

            // Normalize vectors
            camera.vTo = vTo.normalize();
            camera.vUp = vUp.normalize();

            // Calculate the right vector using cross product
            camera.vRight = vTo.crossProduct(vUp).normalize();

            return this;
        }

        /**
         * Sets the orientation of the camera using target point and up vector
         *
         * @param target The point the camera is looking at
         * @param vUp    The approximate up vector (doesn't need to be exact)
         * @return The builder object for method chaining
         */
        public Builder setDirection(Point target, Vector vUp) {
            if (target == null || vUp == null) {
                throw new IllegalArgumentException("Target point or up vector cannot be null");
            }

            // Calculate vTo from camera location to target
            Vector vTo = target.subtract(camera.location).normalize();

            // Calculate vRight using cross product of vTo and the approximate vUp
            Vector vRight = vTo.crossProduct(vUp).normalize();

            // Calculate exact vUp using cross product of vRight and vTo
            Vector vUpExact = vRight.crossProduct(vTo).normalize();

            // Set the camera vectors
            camera.vTo = vTo;
            camera.vUp = vUpExact;
            camera.vRight = vRight;

            return this;
        }

        /**
         * Sets the orientation of the camera using only target point (assuming Y-axis as default up)
         *
         * @param target The point the camera is looking at
         * @return The builder object for method chaining
         */
        public Builder setDirection(Point target) {
            if (target == null) {
                throw new IllegalArgumentException("Target point cannot be null");
            }

            // Use Y-axis as default "up" vector
            return setDirection(target, new Vector(0, 1, 0));
        }

        /**
         * Sets the size of the view plane
         *
         * @param width  The width of the view plane
         * @param height The height of the view plane
         * @return The builder object for method chaining
         */
        public Builder setVpSize(double width, double height) {
            if (width <= 0 || height <= 0) {
                throw new IllegalArgumentException("View plane dimensions must be positive");
            }

            camera.vpWidth = width;
            camera.vpHeight = height;

            return this;
        }

        /**
         * Sets the distance from camera to view plane
         *
         * @param distance The distance between camera and view plane
         * @return The builder object for method chaining
         */
        public Builder setVpDistance(double distance) {
            if (distance <= 0) {
                throw new IllegalArgumentException("Distance must be positive");
            }

            camera.vpDistance = distance;

            return this;
        }

        /**
         * Sets the resolution of the view plane in pixels
         *
         * @param nX Number of pixels in X dimension (width)
         * @param nY Number of pixels in Y dimension (height)
         * @return The builder object for method chaining
         */
        public Builder setResolution(int nX, int nY) {
            camera.nX = nX;
            camera.nY = nY;
            return this;
        }

        /**
         * Sets the ray tracer for the camera.
         *
         * @param scene the scene to be rendered
         * @param type  the type of ray tracer to use
         * @return this builder object for method chaining
         */
        public Builder setRayTracer(Scene scene, RayTracerType type) {
            switch (type) {
                case SIMPLE:
                    camera.rayTracer = new SimpleRayTracer(scene);
                    break;
                default:
                    camera.rayTracer = null;
            }
            return this;
        }

        /**
         * Builds the camera with the configured parameters
         *
         * @return A fully constructed Camera object
         * @throws MissingResourceException if any required parameter is missing
         */
        public Camera build() {
            final String className = "Camera";
            final String description = "missing render data";

            if (camera.location == null)
                throw new MissingResourceException(description, className, "p0");
            if (camera.vUp == null)
                throw new MissingResourceException(description, className, "vUp");
            if (camera.vTo == null)
                throw new MissingResourceException(description, className, "vTo");
            if (camera.vpWidth == 0d)
                throw new MissingResourceException(description, className, "width");
            if (camera.vpHeight == 0d)
                throw new MissingResourceException(description, className, "height");
            if (camera.vpDistance == 0d)
                throw new MissingResourceException(description, className, "distance");

            camera.vRight = camera.vTo.crossProduct(camera.vUp).normalize();

            if (!Util.isZero(camera.vTo.dotProduct(camera.vRight)) ||
                    !Util.isZero(camera.vTo.dotProduct(camera.vUp)) ||
                    !Util.isZero(camera.vRight.dotProduct(camera.vUp)))
                throw new IllegalArgumentException("vTo, vUp and vRight must be orthogonal");

            if ((Util.alignZero(camera.vTo.length() - 1)) != 0 || (Util.alignZero(camera.vUp.length() - 1)) != 0 || (Util.alignZero(camera.vRight.length() - 1)) != 0)
                throw new IllegalArgumentException("vTo, vUp and vRight must be normalized");

            if (camera.vpWidth <= 0 || camera.vpHeight <= 0)
                throw new IllegalArgumentException("width and height must be positive");

            if (camera.vpDistance <= 0)
                throw new IllegalArgumentException("distance from camera to view must be positive");

            // Validate resolution
            if (camera.nX <= 0 || camera.nY <= 0) {
                throw new IllegalArgumentException("Image resolution must be positive");
            }
            // Initialize image writer
            camera.imageWriter = new ImageWriter(camera.nX, camera.nY);

            // Initialize ray tracer with empty scene if not already set
            if (camera.rayTracer == null) {
                camera.rayTracer = new SimpleRayTracer(null);
            }

            try {
                return (Camera) camera.clone();
            } catch (CloneNotSupportedException e) {
                throw new RuntimeException(e);
            }
        }
    }


}