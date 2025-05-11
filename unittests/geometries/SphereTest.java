package geometries;

import org.junit.jupiter.api.Test;
import primitives.Point;

import primitives.Vector;


import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

class SphereTest {


    @Test
    void getNormal() {
        Sphere sphere = new Sphere(new Point(0, 0, 0), 1);
        Point pointOnSphere = new Point(1, 0, 0);
        // ============ Equivalence Partitions Tests ==============
        // TC01: Creating a sphere and checking the getNormal function - (Is the normal vector equals to the program's output?)
        Vector sphereNormal = sphere.getNormal(pointOnSphere);
        assertTrue(sphereNormal.equals(new Vector(1, 0, 0)) || sphereNormal.equals(new Vector(-1, 0, 0)), "ERROR: Sphere's normal is not correct");
        // TC02: check that normal is normalized
        assertEquals(1,sphereNormal.length(),"ERROR: Sphere's normal is not a unit vector");
    }


}
