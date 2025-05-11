package primitives;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
/**
 * Test class for {@link Ray}.
 * Contains test methods to validate the behavior of the {@code GetPoint} method in the Ray class.
 */
class RayTests {
final Ray  ray=new Ray(new Point(1,0,2),new Vector(0,0,-1));
    /**
     * Test method to run all GetPoint tests.
     * Invokes individual test methods for various parameter values.
     */
@Test
    void testGetPoint()
    {
        testGetPoint1();
        testGetPoint2();
        testGetPoint3();
    }
    // ============================ Equivalence Partitions Tests ================================
    /**
     * Tests the scenario where the parameter t is negative.
     * <br>TC01: Parameter t is negative
     */
    @Test
    void testGetPoint1()
    {
        assertEquals(new Point (1,0,4),ray.getPoint(-2),"ERROR:Point was not computed correctly");
    }
    /**
     * Tests the scenario where the parameter t is positive.
     * <br>TC02: Parameter t is positive
     */
    @Test
    void testGetPoint2()
    {
        assertEquals(new Point (1,0,0),ray.getPoint(2),"ERROR:Point was not computed correctly");
    }
    // ============================= Boundary Value Tests =================================
    /**
     * Tests the scenario where the parameter t is zero.
     * <br>TC03: Parameter t is zero
     */
    @Test
    void testGetPoint3()
    {
        assertEquals(new Point (1,0,2),ray.getPoint(0),"ERROR:Point was not computed correctly");
    }
}