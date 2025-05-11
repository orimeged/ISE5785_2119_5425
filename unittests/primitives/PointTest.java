package primitives;

import org.junit.jupiter.api.Test;

import static java.lang.System.out;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Unit tests for primitives.Point class
 */
public class PointTest {
    Point  p1         = new Point(1, 2, 3);
    Point  p2         = new Point(2, 4, 6);
    Point  p3         = new Point(2, 4, 5);
    Vector v1         = new Vector(1, 2, 3);
    Vector v1Opposite = new Vector(-1, -2, -3);

    /**
     * Test of add method, of class Point.
     */
    @Test
    public void testAdd() {
        //============== Equivalence Partitions Tests ==============
        //TC01: test that check that this func can add point and vector and return new point correctly
        assertEquals(p1.add(v1), p2, "ERROR: (point + vector) = other point does not work correctly");
        // =============== Boundary Values Tests ==================
        //TC11: test that check that point + his opposite vector equals zero
        assertEquals(p1.add(v1Opposite), Point.ZERO, "ERROR: (point + vector) = center of coordinates does not work correctly");
}

    /**
     * subtract function test case
     */
@Test
    public void subtract() {
        //============== Equivalence Partitions Tests ==============
        //TC01: test that check that this func can subtract point and vector and return new point correctly
        assertEquals(p2.subtract(p1), v1, "ERROR: (point2 - point1) does not work correctly");
    // =============== Boundary Values Tests ==================
    //TC: test that check that this func can subtract point and vector and return new point correctly
        assertThrows(IllegalArgumentException.class, () ->  p1.subtract(p1), "ERROR: (point - itself) throws wrong exception");    }

    /**
     * Calculates the distance between points and asserts the squared distance is correct.
     */
    @Test
    void distance() {
        //============== Equivalence Partitions Tests ==============
        //TC01: check that this func can calculate the distance between two points and return the correct value
        assertEquals(p1.distance(p3),3 ,"ERROR: squared distance between points is wrong");
        //TC02: check that this func can calculate the distance between point to himself
        assertEquals(p1.distance(p1),0 ,"ERROR: point squared distance to itself is not zero");
    }


    /**
     * Calculate the squared distance between two points and return the correct value.
     */
    @Test
    void distanceSquared() {
        //============== Equivalence Partitions Tests ==============
        //TC01: check that this func can calculate the distance between two points and return the correct value
        assertEquals(p1.distanceSquared(p3),9 ,"ERROR: squared distance between points is wrong");
        //TC02: check that this func can calculate the distance between point to himself
        assertEquals(p1.distanceSquared(p1),0 ,"ERROR: point squared distance to itself is not zero");
    }
}