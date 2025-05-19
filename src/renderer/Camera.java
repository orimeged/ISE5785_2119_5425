package renderer;

import primitives.*;
import scene.Scene;

import java.util.MissingResourceException;

import static primitives.Util.isZero;

/**
 * The Camera class represents a virtual camera in a 3D scene.
 * It is responsible for constructing rays through pixels, rendering images,
 * and managing the camera's position, orientation, and view plane.
 *
 * @author Ori meged and Natanel hasid
 */
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
     * Constructs a ray through a pixel in the view plane.
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
            if (i % interval == 0) {
                for (int j = 0; j < nX; j++) {
                    imageWriter.writePixel(j, i, color);
                }
            }
        }

        for (int j = 0; j < nX; j++) {
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

    /**
     * Builder class for constructing a Camera object with custom parameters.
     */
    public static class Builder {
        private final Camera camera;

        public Builder() {
            camera = new Camera();
        }

        public Builder setLocation(Point location) {
            if (location == null) {
                throw new IllegalArgumentException("Camera location cannot be null");
            }
            camera.location = location;
            return this;
        }

        public Builder setDirection(Vector vTo, Vector vUp) {
            if (vTo == null || vUp == null) {
                throw new IllegalArgumentException("Camera vectors cannot be null");
            }

            if (vTo.dotProduct(vUp) != 0) {
                throw new IllegalArgumentException("Camera vectors vTo and vUp must be orthogonal");
            }

            camera.vTo = vTo.normalize();
            camera.vUp = vUp.normalize();
            camera.vRight = vTo.crossProduct(vUp).normalize();

            return this;
        }

        public Builder setDirection(Point target, Vector vUp) {
            if (target == null || vUp == null) {
                throw new IllegalArgumentException("Target point or up vector cannot be null");
            }

            Vector vTo = target.subtract(camera.location).normalize();
            Vector vRight = vTo.crossProduct(vUp).normalize();
            Vector vUpExact = vRight.crossProduct(vTo).normalize();

            camera.vTo = vTo;
            camera.vUp = vUpExact;
            camera.vRight = vRight;

            return this;
        }

        public Builder setDirection(Point target) {
            if (target == null) {
                throw new IllegalArgumentException("Target point cannot be null");
            }

            return setDirection(target, new Vector(0, 1, 0));
        }

        public Builder setVpSize(double width, double height) {
            if (width <= 0 || height <= 0) {
                throw new IllegalArgumentException("View plane dimensions must be positive");
            }

            camera.vpWidth = width;
            camera.vpHeight = height;

            return this;
        }

        public Builder setVpDistance(double distance) {
            if (distance <= 0) {
                throw new IllegalArgumentException("Distance must be positive");
            }

            camera.vpDistance = distance;

            return this;
        }

        public Builder setResolution(int nX, int nY) {
            camera.nX = nX;
            camera.nY = nY;
            return this;
        }

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

            if (camera.nX <= 0 || camera.nY <= 0) {
                throw new IllegalArgumentException("Image resolution must be positive");
            }

            camera.imageWriter = new ImageWriter(camera.nX, camera.nY);

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