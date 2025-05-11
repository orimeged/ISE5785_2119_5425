package geometries;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import primitives.Point;
import primitives.Ray;
import primitives.Vector;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class TriangleTest {


    @Test
    void getNormal() {
        Point p1 = new Point(1, 0, 0);
        Point p2 = new Point(0, 1, 0);
        Point p3 = new Point(0, 0, 1);
        Triangle triangle = new  Triangle(p1, p2, p3);
        // ============ Equivalence Partitions Tests ==============
        // TC01: There is a simple single test here
        Vector normal = triangle.getNormal(p1);
        assertTrue(normal.equals(new Vector(1, 1, 1).normalize()) || normal.equals(new Vector(1, 1, 1).normalize().scale(-1)), "ERROR: Triangle normal is not correct");
        // TC02: check that normal is normalized
        assertEquals(1,normal.length(),"Error: Triangle's normal is not a unit vector");

    }
    }



