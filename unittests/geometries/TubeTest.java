package geometries;

import org.junit.jupiter.api.Test;
import primitives.Point;
import primitives.Ray;
import primitives.Vector;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertTrue;

class TubeTest {

    @Test
    void getNormal() {
        Tube tube = new Tube(new Ray(new Point(0, 0, -2), new Vector(0, 0, 2)), 1);
        Vector normal = tube.getNormal(new Point(1,0,0));

        // ================ Equivalence Partitions Tests ==============

        //TC01: test that normal is correct
        assertTrue(normal.equals(new Vector(1,0,0).normalize()) || normal.equals(new Vector(-1,0,0).normalize()),"ERROR: tube normal is not correct");

        //TC02: test that normal is in the right length
        assertEquals(1, normal.length(), "ERROR: tube normal is not in the right length ");

        // =============== Boundary Values Tests ==================

        normal = tube.getNormal(new Point(1,0,-2));
        //TC011: test that normal is correct When connecting the point to the top of the beam
        //of the axis of the cylinder makes a right angle with the axis
        assertTrue(normal.equals(new Vector(1,0,0).normalize()) || normal.equals(new Vector(-1,0,0).normalize()),"ERROR: tube normal is not correct");

        //TC12: test that normal is in the right length
        assertEquals(1, normal.length(), "ERROR: tube normal is not in the right length");
    }
}
