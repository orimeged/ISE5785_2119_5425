package level9;

import geometries.*;
import lighting.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import primitives.*;
import renderer.Camera;
import renderer.ImageWriter;
import renderer.SimpleRayTracer;
import scene.Scene;

public class Ragular_with_adaptiv {

    private final Scene scene = new Scene("Realistic Earth and Missile Scene with Twinkling Stars and Moon");
    private Camera.Builder cameraBase;

    @BeforeEach
    void setUp() {
        // Camera setup to focus on upper starry sky, Earth, and moon
        cameraBase = Camera.getBuilder()
                .setDirection(new Vector(0, 0, -1), new Vector(0, 1, 0))
                .setLocation(new Point(0, 50, 900)) // Closer for composition
                .setVpDistance(900)
                .setVpSize(200, 200)
                .setRayTracer(new SimpleRayTracer(scene).setNumberOfPoints(64)) // Reduced for speed
                .setMultithreading(-2); // Automatic threading

        createScene();
    }

    private void createScene() {
        // Materials for enhanced realism
        Material earthBlueMaterial = new Material().setKD(0.5).setKS(0.7).setShininess(120).setKR(0.4); // Reflective oceans
        Material earthGreenMaterial = new Material().setKD(0.5).setKS(0.6).setShininess(100).setKR(0.3); // Reflective land
        Material missileShiny = new Material().setKD(0.2).setKS(0.95).setShininess(350).setKR(0.5); // Polished metal
        Material missileMatte = new Material().setKD(0.6).setKS(0.3).setShininess(80);
        Material starMaterial = new Material().setKD(0.05).setKS(0.9).setShininess(150).setKT(0.3); // Twinkling star glow
        Material moonMaterial = new Material().setKD(0.2).setKS(0.95).setShininess(350).setKR(0.4); // Reflective moon
        Material flameMaterial = new Material().setKD(0.1).setKS(0.9).setShininess(200).setKT(0.6); // Glowing flames

        // Earth - blue ocean sphere (slightly offset for partial visibility)
        scene.geometries.add(new Sphere(new Point(20, -180, -400), 150)
                .setEmission(new Color(20, 60, 120)) // Deep blue for oceans
                .setMaterial(earthBlueMaterial));

        // Earth - green land sphere (overlapping for land effect)
        scene.geometries.add(new Sphere(new Point(-20, -220, -400), 150)
                .setEmission(new Color(40, 120, 60)) // Green for land
                .setMaterial(earthGreenMaterial));

        // Starry night sky (darker plane)
        scene.geometries.add(new Plane(
                new Point(0, 0, -600),
                new Vector(0, 0, 1))
                .setEmission(new Color(0, 0, 3)) // Darker for contrast
                .setMaterial(new Material().setKD(0.03)));

//         Smaller, twinkling stars in the upper half only (commented out as in original)
         for (int i = 0; i < 30; i++) {
             double x = -150 + Math.random() * 300;
             double y = 50 + Math.random() * 150;
             double z = -595 + Math.random() * 5;
             double brightness = 180 + Math.random() * 75;
             double size = 0.3 + Math.random() * 0.5;
             scene.geometries.add(new Sphere(new Point(x, y, z), size)
                     .setEmission(new Color(brightness, brightness, brightness))
                     .setMaterial(starMaterial));
             if (i % 10 == 0) {
                 scene.lights.add(new PointLight(new Color(brightness, brightness, brightness), new Point(x, y, z))
                         .setKL(0.0005).setKQ(0.0001).setSize(9));
             }
         }

        // Moon with grayish tint and strong reflections
        scene.geometries.add(new Sphere(new Point(100, 100, -500), 20)
                .setEmission(new Color(180, 180, 200)) // Gray-blue moon
                .setMaterial(moonMaterial));

        // Missile - nose (triangular pyramid-like shape using triangles)
        scene.geometries.add(new Triangle(
                new Point(0, 20, -350),
                new Point(-8, 40, -350),
                new Point(8, 40, -350))
                .setEmission(new Color(200, 50, 50)) // Reddish nose
                .setMaterial(missileShiny));
        scene.geometries.add(new Triangle(
                new Point(0, 20, -350),
                new Point(-8, 40, -350),
                new Point(0, 40, -342))
                .setEmission(new Color(200, 50, 50))
                .setMaterial(missileShiny));
        scene.geometries.add(new Triangle(
                new Point(0, 20, -350),
                new Point(8, 40, -350),
                new Point(0, 40, -342))
                .setEmission(new Color(200, 50, 50))
                .setMaterial(missileShiny));

        // Missile - body (rectangular prism using polygons)
        scene.geometries.add(new Polygon(
                new Point(-6, 40, -350),
                new Point(6, 40, -350),
                new Point(6, 90, -350),
                new Point(-6, 90, -350))
                .setEmission(new Color(100, 100, 160)) // Metallic blue
                .setMaterial(missileShiny));
        scene.geometries.add(new Polygon(
                new Point(-6, 40, -344),
                new Point(6, 40, -344),
                new Point(6, 90, -344),
                new Point(-6, 90, -344))
                .setEmission(new Color(100, 100, 160))
                .setMaterial(missileShiny));
        scene.geometries.add(new Polygon(
                new Point(-6, 40, -350),
                new Point(-6, 40, -344),
                new Point(-6, 90, -344),
                new Point(-6, 90, -350))
                .setEmission(new Color(100, 100, 160))
                .setMaterial(missileShiny));
        scene.geometries.add(new Polygon(
                new Point(6, 40, -350),
                new Point(6, 40, -344),
                new Point(6, 90, -344),
                new Point(6, 90, -350))
                .setEmission(new Color(100, 100, 160))
                .setMaterial(missileShiny));

        // Missile - fins (4 triangular fins for realism)
        scene.geometries.add(new Triangle(
                new Point(6, 60, -350),
                new Point(20, 70, -360),
                new Point(6, 70, -350))
                .setEmission(new Color(80, 80, 120))
                .setMaterial(missileMatte));
        scene.geometries.add(new Triangle(
                new Point(-6, 60, -350),
                new Point(-20, 70, -360),
                new Point(-6, 70, -350))
                .setEmission(new Color(80, 80, 120))
                .setMaterial(missileMatte));
        scene.geometries.add(new Triangle(
                new Point(0, 60, -344),
                new Point(0, 70, -330),
                new Point(0, 70, -350))
                .setEmission(new Color(80, 80, 120))
                .setMaterial(missileMatte));
        scene.geometries.add(new Triangle(
                new Point(0, 60, -356),
                new Point(0, 70, -370),
                new Point(0, 70, -350))
                .setEmission(new Color(80, 80, 120))
                .setMaterial(missileMatte));


        // Space debris for added realism
        scene.geometries.add(new Sphere(new Point(-80, 0, -380), 5)
                .setEmission(new Color(150, 100, 100))
                .setMaterial(missileShiny));
        scene.geometries.add(new Sphere(new Point(60, 30, -360), 3)
                .setEmission(new Color(100, 150, 100))
                .setMaterial(missileMatte));

        // Optimized lighting
        scene.setAmbientLight(new AmbientLight(new Color(25, 25, 50), 0.2)); // Soft ambient light
        scene.lights.add(new SpotLight(new Color(255, 0, 0), new Point(250, 200, 100), new Vector(-1, -1, -1))
                .setKL(0.00001).setKQ(0.000001).setNarrowBeam(8).setSize(9)); // Red spotlight
        scene.lights.add(new PointLight(new Color(0, 0, 255), new Point(-300, 200, 100))
                .setKL(0.00001).setKQ(0.000001).setSize(9)); // Blue point light
        scene.lights.add(new DirectionalLight(new Color(0, 255, 0), new Vector(1, 1, -1))); // Green directional light
        scene.lights.add(new SpotLight(new Color(230, 230, 255), new Point(100, 100, -500), new Vector(-1, -1, 1))
                .setKL(0.000008).setKQ(0.0000004).setNarrowBeam(8).setSize(9)); // Moonlight
    }

    @Test
    public void testMissileSceneComparison() {
        System.out.println("Rendering with Ragular_with_adaptiv...");
        cameraBase
                .setNumberOfRays(1) // Fast rendering
                .setAdaptive(true) // Disabled for speed
                .setImageWriter(new ImageWriter("Ragular_with_adaptiv", 1000, 1000))
                .build()
                .renderImage()
                .writeToImage();
    }
}