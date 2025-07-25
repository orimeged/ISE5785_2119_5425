package lighting;

import static java.awt.Color.*;

import org.junit.jupiter.api.Test;

import geometries.*;
import lighting.*;
import primitives.*;
import renderer.*;
import scene.Scene;

/**
 * Test rendering a basic image
 * @author Dan
 */
public class LightTests {
    /** First scene for some of tests */
    private final Scene          scene1                  = new Scene("Test scene");
    /** Second scene for some of tests */
    private final Scene          scene2                  = new Scene("Test scene")
            .setAmbientLight(new AmbientLight(new Color(WHITE), new Double3(0.15)));

    /** First camera builder for some of tests */
    private final Camera.Builder camera1                 = Camera.getBuilder()
            .setRayTracer(new SimpleRayTracer(scene1))
            .setLocation(new Point(0, 0, 1000))
            .setDirection(new Vector(0,0,-1), new Vector(0,1,0))
            .setVpSize(150, 150).setVpDistance(1000);
    /** Second camera builder for some of tests */
    private final Camera.Builder camera2                 = Camera.getBuilder()
            .setRayTracer(new SimpleRayTracer(scene2))
            .setLocation(new Point(0, 0, 1000))
            .setDirection(new Vector(0,0,-1), new Vector(0,1,0))
            .setVpSize(200, 200).setVpDistance(1000);

    /** Shininess value for most of the geometries in the tests */
    private static final int     SHININESS               = 301;
    /** Diffusion attenuation factor for some of the geometries in the tests */
    private static final double  KD                      = 0.5;
    /** Diffusion attenuation factor for some of the geometries in the tests */
    private static final Double3 KD3                     = new Double3(0.2, 0.6, 0.4);

    /** Specular attenuation factor for some of the geometries in the tests */
    private static final double  KS                      = 0.5;
    /** Specular attenuation factor for some of the geometries in the tests */
    private static final Double3 KS3                     = new Double3(0.2, 0.4, 0.3);

    /** Material for some of the geometries in the tests */
    private final Material       material                = new Material().setKD(KD3).setKS(KS3).setShininess(SHININESS);
    /** Light color for tests with triangles */
    private final Color          trianglesLightColor     = new Color(800, 500, 250);
    /** Light color for tests with sphere */
    private final Color          sphereLightColor        = new Color(800, 500, 0);
    /** Color of the sphere */
    private final Color          sphereColor             = new Color(BLUE).reduce(2);

    /** Center of the sphere */
    private final Point          sphereCenter            = new Point(0, 0, -50);
    /** Radius of the sphere */
    private static final double  SPHERE_RADIUS           = 50d;

    /** The triangles' vertices for the tests with triangles */
    private final Point[]        vertices                =
            {
                    // the shared left-bottom:
                    new Point(-110, -110, -150),
                    // the shared right-top:
                    new Point(95, 100, -150),
                    // the right-bottom
                    new Point(110, -110, -150),
                    // the left-top
                    new Point(-75, 78, 100)
            };
    /** Position of the light in tests with sphere */
    private final Point          sphereLightPosition     = new Point(-50, -50, 25);
    /** Light direction (directional and spot) in tests with sphere */
    private final Vector         sphereLightDirection    = new Vector(1, 1, -0.5);
    /** Position of the light in tests with triangles */
    private final Point          trianglesLightPosition  = new Point(30, 10, -100);
    /** Light direction (directional and spot) in tests with triangles */
    private final Vector         trianglesLightDirection = new Vector(-2, -2, -2);

    /** The sphere in appropriate tests */
    private final Geometry       sphere                  = new Sphere(sphereCenter, SPHERE_RADIUS)
            .setEmission(sphereColor).setMaterial(new Material().setKD(KD).setKS(KS).setShininess(SHININESS));
    /** The first triangle in appropriate tests */
    private final Geometry       triangle1               = new Triangle(vertices[0], vertices[1], vertices[2])
            .setMaterial(material);
    /** The first triangle in appropriate tests */
    private final Geometry       triangle2               = new Triangle(vertices[0], vertices[1], vertices[3])
            .setMaterial(material);

    /** Produce a picture of a sphere lighted by a directional light */
    @Test
    public void sphereDirectional() {
        scene1.geometries.add(sphere);
        scene1.lights.add(new DirectionalLight(sphereLightColor, sphereLightDirection));

        camera1.setImageWriter(new ImageWriter("lightSphereDirectional", 500, 500))
                .build()
                .renderImage()
                .writeToImage();
    }

    /** Produce a picture of a sphere lighted by a point light */
    @Test
    public void spherePoint() {
        scene1.geometries.add(sphere);
        scene1.lights.add(new PointLight(sphereLightColor, sphereLightPosition)
                .setKL(0.001).setKQ(0.0002));

        camera1.setImageWriter(new ImageWriter("lightSpherePoint", 500, 500))
                .build()
                .renderImage()
                .writeToImage();
    }

    /** Produce a picture of a sphere lighted by a spotlight */
    @Test
    public void sphereSpot() {
        scene1.geometries.add(sphere);
        scene1.lights.add(new SpotLight(sphereLightColor, sphereLightPosition, sphereLightDirection)
                .setKL(0.001).setKQ(0.0001));

        camera1.setImageWriter(new ImageWriter("lightSphereSpot", 500, 500))
                .build()
                .renderImage()
                .writeToImage();
    }

    /** Produce a picture of two triangles lighted by a directional light */
    @Test
    public void trianglesDirectional() {
        scene2.geometries.add(triangle1, triangle2);
        scene2.lights.add(new DirectionalLight(trianglesLightColor, trianglesLightDirection));

        camera2.setImageWriter(new ImageWriter("lightTrianglesDirectional", 500, 500)) //
                .build()
                .renderImage()
                .writeToImage();
    }

    /** Produce a picture of two triangles lighted by a point light */
    @Test
    public void trianglesPoint() {
        scene2.geometries.add(triangle1, triangle2);
        scene2.lights.add(new PointLight(trianglesLightColor, trianglesLightPosition)
                .setKL(0.001).setKQ(0.0002));

        camera2.setImageWriter(new ImageWriter("lightTrianglesPoint", 500, 500)) //
                .build() //
                .renderImage() //
                .writeToImage(); //
    }

    /** Produce a picture of two triangles lighted by a spotlight */
    @Test
    public void trianglesSpot() {
        scene2.geometries.add(triangle1, triangle2);
        scene2.lights.add(new SpotLight(trianglesLightColor, trianglesLightPosition, trianglesLightDirection)
                .setKL(0.001).setKQ(0.0001));

        camera2.setImageWriter(new ImageWriter("lightTrianglesSpot", 500, 500))
                .build()
                .renderImage()
                .writeToImage();
    }


    /** Produce a picture of a sphere lighted by a narrow spotlight */
    @Test
    public void sphereSpotSharp() {
        scene1.geometries.add(sphere);
        scene1.lights
                .add(new SpotLight(sphereLightColor, sphereLightPosition, new Vector(1, 1, -0.5))
                        .setNarrowBeam(10).setKL(0.001).setKQ(0.00004));

        camera1.setImageWriter(new ImageWriter("lightSphereSpotSharp", 500, 500))
                .build()
                .renderImage()
                .writeToImage();
    }

    /** Produce a picture of two triangles lighted by a narrow spotlight */
    @Test
    public void trianglesSpotSharp() {
        scene2.geometries.add(triangle1, triangle2);
        scene2.lights.add(new SpotLight(trianglesLightColor, trianglesLightPosition, trianglesLightDirection)
                .setNarrowBeam(10).setKL(0.001).setKL(0.00004));

        camera2.setImageWriter(new ImageWriter("lightTrianglesSpotSharp", 500, 500))
                .build()
                .renderImage()
                .writeToImage();
    }

    /**
     * Produce a picture of a one sphere lighted by a directional light, PointLight and spotlight.
     */
    @Test
    public void sphereIntegrationTest() {
        scene1.geometries.add(sphere);
        DirectionalLight directionalLight = new DirectionalLight(new Color(0, 255, 0), new Vector(1, 1, -1));
        PointLight pointLight = new PointLight(new Color(0, 0, 255), new Point(-300, 200, 100))//
                .setKL(0.00001).setKQ(0.000001);
        SpotLight spotLight = new SpotLight(new Color(255, 0, 0), new Point(250, 200, 100), new Vector(-1, -1, -1));

        scene1.lights.add(directionalLight);
        scene1.lights.add(pointLight);
        scene1.lights.add(spotLight);

        ImageWriter imageWriter = new ImageWriter("sphereIntegrationTest", 500, 500);
        camera1.setImageWriter(imageWriter) //
                .setRayTracer(new SimpleRayTracer(scene1))
                .build()//
                .renderImage() //
                .writeToImage(); //
    }

    /** Produce a picture of a scene with ten bodies (five spheres and five triangles) lighted by multiple lights */
    @Test
    public void bonusSceneWithTenBodies() {
        Scene bonusScene = new Scene("Bonus Scene")
                .setAmbientLight(new AmbientLight(new Color(50, 50, 50), new Double3(0.1)));

        // Define materials for variety
        Material shinyMaterial = new Material().setKD(new Double3(0.4, 0.4, 0.4)).setKS(new Double3(0.6, 0.6, 0.6)).setShininess(400);
        Material matteMaterial = new Material().setKD(new Double3(0.6, 0.5, 0.5)).setKS(new Double3(0.2, 0.2, 0.2)).setShininess(100);

        // Add five spheres with varying sizes and colors
        bonusScene.geometries.add(
                new Sphere(new Point(0, 0, -100), 40)
                        .setEmission(new Color(100, 20, 20)).setMaterial(shinyMaterial),
                new Sphere(new Point(80, 50, -150), 30)
                        .setEmission(new Color(20, 100, 20)).setMaterial(matteMaterial),
                new Sphere(new Point(-80, -50, -80), 25)
                        .setEmission(new Color(20, 20, 100)).setMaterial(shinyMaterial),
                new Sphere(new Point(50, -70, -50), 20)
                        .setEmission(new Color(100, 100, 20)).setMaterial(matteMaterial),
                new Sphere(new Point(-50, 70, -120), 15)
                        .setEmission(new Color(100, 20, 100)).setMaterial(shinyMaterial)
        );

        // Add five triangles forming a partial pyramid-like structure
        Point base1 = new Point(-100, -100, -200);
        Point base2 = new Point(100, -100, -200);
        Point base3 = new Point(0, -100, -250);
        Point apex1 = new Point(0, 50, -200);
        Point apex2 = new Point(0, 0, -225);

        bonusScene.geometries.add(
                new Triangle(base1, base2, apex1).setEmission(new Color(80, 80, 80)).setMaterial(matteMaterial),
                new Triangle(base2, base3, apex1).setEmission(new Color(80, 80, 80)).setMaterial(matteMaterial),
                new Triangle(base3, base1, apex1).setEmission(new Color(80, 80, 80)).setMaterial(matteMaterial),
                new Triangle(base1, base2, apex2).setEmission(new Color(60, 60, 60)).setMaterial(shinyMaterial),
                new Triangle(base2, base3, apex2).setEmission(new Color(60, 60, 60)).setMaterial(shinyMaterial)
        );

        // Add multiple lights for realism
        bonusScene.lights.add(
                new DirectionalLight(new Color(200, 200, 200), new Vector(1, -1, -1)) // Soft white directional light
                // Warm point light
                // Cool spotlight
        );

        // Configure camera
        Camera.Builder bonusCamera = Camera.getBuilder()
                .setRayTracer(new SimpleRayTracer(bonusScene))
                .setLocation(new Point(0, 0, 1000))
                .setDirection(new Vector(0, 0, -1), new Vector(0, 1, 0))
                .setVpSize(200, 200).setVpDistance(1000)
                .setImageWriter(new ImageWriter("bonusSceneWithTenBodies", 500, 500));

        // Render and write image
        bonusCamera.build().renderImage().writeToImage();
    }


}

