package geometries;

import org.junit.jupiter.api.Test;
import primitives.Point;
import primitives.Vector;
import static org.junit.jupiter.api.Assertions.*;

class PlaneTest {
    Point p1 = new Point(1, 0, 0);
    Point p2 = new Point(0, 1, 0);
    Point p3 = new Point(0, 0, 1);
    Point p1SameLine = new Point(8, 0, 0);
    Point p2SameLine = new Point(4, 0, 0);
    Plane plane = new Plane(p1, p2, p3);

    @Test
    void  testConstructor() {

        //=============== Boundary Values Tests ==================
        //TC01: The first and second points are connected
        assertThrows(IllegalArgumentException.class, () -> new Plane(p1,p1,p2),"ERROR: two or more point are collided and it's not allowed");
        //TC02:The points are on the same line
        assertThrows(IllegalArgumentException.class, () -> new Plane(p1,p1SameLine,p2SameLine),"ERROR: 3 Points on the same line and it's not allowed");
    }

    @Test
    void getNormal() {

        // ============ Equivalence Partitions Tests ==============
        // TC01: There is a simple single test here
        Vector normal = plane.getNormal(p1);
        assertTrue(normal.equals(new Vector(1, 1, 1).normalize()) || normal.equals(new Vector(1, 1, 1).normalize().scale(-1)), "ERROR: plane normal is not correct");
        // TC02: check that normal is normalized
        assertEquals(1,normal.length(),"ERROR: plane's normal is not a unit vector");

    }
}

