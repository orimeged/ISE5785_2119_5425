package level8;

import geometries.*;
import lighting.*;
import org.junit.jupiter.api.Test;
import primitives.*;
import renderer.Camera;
import renderer.ImageWriter;
import renderer.SimpleRayTracer;
import scene.Scene;

public class Before_SuperSampling {

    private final Scene scene = new Scene("Cool Missile Scene");

    private final Camera.Builder camera = Camera.getBuilder()
            .setDirection(new Vector(0, 0, -1), new Vector(0, 1, 0))
            .setLocation(new Point(0, 0, 1000))
            .setVpDistance(1000)
            .setVpSize(200, 200)
            .setRayTracer(new SimpleRayTracer(scene).setNumberOfPoints(36));
    private void createScene() {
        Material matte = new Material().setKD(0.6).setKS(0.5).setShininess(80); // Shinier for sharper highlights
        Material shiny = new Material().setKD(0.2).setKS(0.9).setShininess(250); // Very metallic nose
        Material reflectiveGround = new Material().setKD(0.3).setKS(0.8).setShininess(150).setKR(0.4); // More reflective ground

        // Missile nose
        scene.geometries.add(new Triangle(
                new Point(0, 0, -330),
                new Point(-8, 10, -310),
                new Point(8, 10, -310)
        ).setEmission(new Color(150, 0, 0)).setMaterial(shiny));

        // Missile body
        scene.geometries.add(new Polygon(
                new Point(-8, 10, -310),
                new Point(8, 10, -310),
                new Point(8, 40, -310),
                new Point(-8, 40, -310)
        ).setEmission(new Color(90, 90, 200)).setMaterial(matte));

        // Wings
        scene.geometries.add(new Triangle(
                new Point(-8, 20, -310),
                new Point(-35, 30, -320),
                new Point(-8, 30, -310)
        ).setEmission(new Color(60, 120, 250)).setMaterial(matte));

        scene.geometries.add(new Triangle(
                new Point(8, 20, -310),
                new Point(35, 30, -320),
                new Point(8, 30, -310)
        ).setEmission(new Color(60, 120, 250)).setMaterial(matte));

        // Tail
        scene.geometries.add(new Triangle(
                new Point(-8, 40, -310),
                new Point(-16, 48, -320),
                new Point(-8, 48, -310)
        ).setEmission(new Color(180, 180, 180)).setMaterial(matte));

        scene.geometries.add(new Triangle(
                new Point(8, 40, -310),
                new Point(16, 48, -320),
                new Point(8, 48, -310)
        ).setEmission(new Color(180, 180, 180)).setMaterial(matte));

        // Shining sun (visible sphere)
        scene.geometries.add(new Sphere(new Point(-90, 90, -150), 15)
                .setEmission(new Color(255, 255, 100)) // Bright yellow sun
                .setMaterial(new Material().setKD(0.1).setKS(0.9).setShininess(100)));

        // Reflective ground plane
        scene.geometries.add(new Plane(
                new Point(0, -20, -300),
                new Vector(0, 1, 0)
        ).setEmission(new Color(20, 20, 50)).setMaterial(reflectiveGround));

        // Sky background
        scene.geometries.add(new Plane(
                new Point(0, 0, -400),
                new Vector(0, 0, 1)
        ).setEmission(new Color(100, 150, 255)).setMaterial(new Material().setKD(0.2).setKS(0.1)));

        // Lighting setup (5 light sources)
        // 1. Primary sunlight (SpotLight, aligned with sun sphere)
        scene.lights.add(new SpotLight(new Color(255, 255, 200), new Point(-90, 90, -150), new Vector(1, -1, -0.5))
                .setKL(0.00003).setKQ(0.000003).setNarrowBeam(8).setSize(12)); // Stronger, focused beam

        // 2. Ambient light
        scene.setAmbientLight(new AmbientLight(new Color(150, 150, 255), 0.08)); // Slightly dimmer for sun dominance

        // 3. Point light for subtle glow near missile
        scene.lights.add(new PointLight(new Color(200, 200, 255), new Point(0, 20, -300))
                .setKL(0.0001).setKQ(0.00001).setSize(5));

        // 4. Secondary SpotLight for highlights from opposite side

        // 5. Directional light for soft fill
        scene.lights.add(new DirectionalLight(new Color(80, 80, 80), new Vector(0, -1, -1)));
    }

    @Test
    public void testMissileSceneBeautiful() {
        createScene();
        camera.setImageWriter(new ImageWriter("Before", 2000, 2000))
                .build()
                .renderImage()
                .writeToImage();
    }
}