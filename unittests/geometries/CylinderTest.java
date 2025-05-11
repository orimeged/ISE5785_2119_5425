package geometries;

import org.junit.jupiter.api.Test;
import primitives.Point;
import primitives.Ray;
import primitives.Vector;

import static org.junit.jupiter.api.Assertions.*;

class CylinderTest {

    @Test
    void getNormal() {
        // ============ Equivalence Partitions Tests ==============
        // TC01: There is a simple single test here
        Cylinder cylinder = new Cylinder(1,new Ray(new Point(0,0,-1),new Vector(0,0,1)),5);
        Vector normalSide = cylinder.getNormal(new Point(1,0,0));
        Vector normalTop = cylinder.getNormal(new Point(  -0.5,-0.5,4));
        Vector normalBottom = cylinder.getNormal(new Point(0.5,-0.5,-1));
        Vector normalTopCenter = cylinder.getNormal(new Point(  0,0,4));
        Vector normalBottomCenter = cylinder.getNormal(new Point(0,0,-1));

        // ================ Equivalence Partitions Tests ==============

        //TC01: test that normal is correct
        assertTrue(normalSide.equals(new Vector(1,0,0).normalize()) || normalSide.equals(new Vector(-1,0,0).normalize()),"ERROR: cylinder normal is not correct");

        //TC02: test that normal is in the right length
        assertEquals(1, normalSide.length(), "ERROR: cylinder normal is not in the right length");

        //TC03: test that normal is correct
        assertTrue(normalTop.equals(new Vector(0,0,1).normalize()) || normalTop.equals(new Vector(0,0,-1).normalize()),"ERROR: cylinder normal is not correct");
        //TC04: test that normal is in the right length
        assertEquals(1, normalTop.length(), "ERROR: cylinder normal is not in the right length");

        //TC05: test that normal is correct
        assertTrue(normalBottom.equals(new Vector(0,0,1).normalize()) || normalBottom.equals(new Vector(0,0,-1).normalize()),"ERROR: cylinder normal is not correct");
        //TC06: test that normal is in the right length
        assertEquals(1, normalBottom.length(), "ERROR: cylinder normal is not in the right length");

        // =============== Boundary Values Tests ==================

        //TC11: test that normal is correct
        assertTrue(normalTopCenter.equals(new Vector(0, 0, 1).normalize()) || normalTopCenter.equals(new Vector(0, 0, -1).normalize()), "ERROR: cylinder normal is not correct");
        //TC12: test that normal is in the right length
        assertEquals(1, normalTopCenter.length(), "ERROR: cylinder normal is not in the right length");

        //TC13: test that normal is correct
        assertTrue(normalBottomCenter.equals(new Vector(0, 0, 1).normalize()) || normalBottomCenter.equals(new Vector(0, 0, -1).normalize()), "ERROR: cylinder normal is not correct");
        //TC14: test that normal is in the right length
        assertEquals(1, normalBottomCenter.length(), "ERROR: cylinder normal is not in the right length");
    }
}