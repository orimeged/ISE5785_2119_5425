package primitives;

import static org.junit.jupiter.api.Assertions.*;
import static primitives.Util.isZero;

import org.junit.jupiter.api.Test;
/**
 * Testing Points
 * This class contains tests for the `Point` class, specifically for various methods like `subtract`, `add`, `distanceSquared`, and `distance`.
 */
class PointTests {

    /**
     * Tests the subtract method.
     */
    @org.junit.jupiter.api.Test
    void testSubtract() {
        testSubtract1();
        testSubtract2();
        testSubtract3();
        testSubtract4();
        testSubtract5();

    }

    //=============================Boundary Value Tests================================


    /**
     * Test method for {@link primitives.Point#subtract(Point)}.
     * Tests subtracting a point from itself.
     * <br>TC01: Subtracting a point from itself (exception expected)
     */
    @org.junit.jupiter.api.Test
void testSubtract1() {
        Point p1=new Point(1,2,3);
        assertThrows(IllegalArgumentException.class,()->p1.subtract(p1),"ERROR: Point-Itself does not throws exception");
        }



    //============================Equivalence Partitions Tests================================
    /**
     * Test method for {@link primitives.Point#subtract(Point)}.
     * Tests subtracting one point from another.
     * <br>TC02: Subtracting one point from another
     */
    @org.junit.jupiter.api.Test
    void testSubtract2() {
        Point p1=new Point(2,2,2);
        Point p2=new Point(-1,-1,-1);
        assertEquals(new Point(3,3,3),p1.subtract(p2),"ERROR: Point-Point does not work correctly");
    }
    /**
     * Test method for {@link primitives.Point#subtract(Point)}.
     * Tests subtracting one point from another.
     * <br>TC03: Subtracting one point from another (negative coordinates)
     */
    @org.junit.jupiter.api.Test
    void testSubtract3() {
        Point p1=new Point(-1,-1,-1);
        Point p2=new Point(1,1,1);
        assertEquals(new Point(-2,-2,-2),p1.subtract(p2),"ERROR: Point-Point does not work correctly");
    }
    /**
     * Test method for {@link primitives.Point#subtract(Point)}.
     * Tests subtracting one point from another.
     * <br>TC04: Subtracting one point from another (mixed coordinates)
     */
    @org.junit.jupiter.api.Test
    void testSubtract4() {
        Point  p1 = new Point(2, 4, 6);
        Point  p2= new Point(1, 2, 3);

        assertEquals(new Vector(1,2,3),p1.subtract(p2),"ERROR: Point-Point does not work correctly");
    }
    /**
     * Test method for {@link primitives.Point#subtract(Point)}.
     * Tests subtracting one point from another.
     * <br>TC05: Subtracting one point from another (negative coordinates)
     */
    @org.junit.jupiter.api.Test
    void testSubtract5() {
        Point p1=new Point(-1,-2,-3);
        Point p2=new Point(-2,-1,-1);
        assertEquals(new Point(1,-1,-2),p1.subtract(p2),"ERROR: Point-Point does not work correctly");
    }

    /**
     * Tests the add method.
     */
    @org.junit.jupiter.api.Test
    void testAdd() {
    }

    //=============================Boundary Value Tests================================
    /**
     * Test method for {@link primitives.Point#add(Vector)}.
     * Tests adding a vector to a point.
     * <br>TC01: Adding a negative vector to a point
     */
    @org.junit.jupiter.api.Test
    void testAdd1() {
       Point p1=new Point(1,2,3);
            Vector v=new Vector(-1,-2,-3);
            assertEquals(new Point(0,0,0),p1.add(v),"ERROR: Point+Vector does not work correctly");
    }

    //============================Equivalence Partitions Tests================================
    /**
     * Test method for {@link primitives.Point#add(Vector)}.
     * Tests adding a vector to a point.
     * <br>TC02: Adding a vector to a point (center of coordinates)
     */
    @org.junit.jupiter.api.Test
    void testAdd2() {
        Point p1=new Point(1,2,3);
        Vector v=new Vector(1,2,3)  ;
        assertEquals(new Point(2,4,6),p1.add(v),"ERROR: Point+Vector=center of coordinates does not work correctly");
    }
    /**
     * Test method for {@link primitives.Point#add(Vector)}.
     * Tests adding a vector to a point.
     * <br>TC03: Adding a vector to a point (negative coordinates)
     */
    @org.junit.jupiter.api.Test
    void testAdd3() {
        Point p1=new Point(-1,-1,-1);
        Vector v=new Vector(1,3,2);
        assertEquals(new Point(0,2,1),p1.add(v),"ERROR: Point+Vector does not work correctly");
    }
    /**
     * Test method for {@link primitives.Point#add(Vector)}.
     * Tests adding a vector to a point.
     * <br>TC04: Adding a vector to a point (negative vector)
     */
    @org.junit.jupiter.api.Test
    void testAdd4() {
        Point p1=new Point(-1,-1,-1);
        Vector v=new Vector(-1,-3,-2);
        assertEquals(new Point(-2,-4,-3),p1.add(v),"ERROR: Point+Vector does not work correctly");
    }


    /**
     * Tests the distanceSquared method.
     */
    @org.junit.jupiter.api.Test
    void testDistanceSquared() {
        testDistanceSquared1();
        testDistanceSquared2();
        testDistanceSquared3();

    }
    //=============================Boundary Value Tests================================
    /**
     * Test method for {@link primitives.Point#distanceSquared(Point)}.
     * Tests the squared distance of a point to itself.
     * <br>TC01: Squared distance of a point to itself
     */
    @org.junit.jupiter.api.Test
    void testDistanceSquared1() {
        Point p1=new Point(1,2,3);
        assertEquals(0,p1.distanceSquared(p1),"ERROR: Point squared distance to itself is not zero");
    }

    //============================Equivalence Partitions Tests================================
    /**
     * Test method for {@link primitives.Point#distanceSquared(Point)}.
     * Tests the squared distance between two points.
     * <br>TC02: Squared distance between two points
     */
    @org.junit.jupiter.api.Test
    void testDistanceSquared2() {
        Point  p1  = new Point(1, 2, 3);
        Point  p2 = new Point(2, 4, 5);
        assertEquals(9,p1.distanceSquared(p2),"ERROR: squared distance between points is wrong");
    }
    /**
     * Test method for {@link primitives.Point#distanceSquared(Point)}.
     * Tests the squared distance between two points.
     * <br>TC03: Squared distance between two points (order independent)
     */
    @org.junit.jupiter.api.Test
    void testDistanceSquared3() {
        Point  p1  = new Point(1, 2, 3);
        Point  p2 = new Point(2, 4, 5);
        assertEquals(9,p2.distanceSquared(p1),"ERROR: squared distance between points is wrong");
    }
    /**
     * Tests the distance method.
     */
    @org.junit.jupiter.api.Test
    void testDistance() {
        testDistance1();
        testDistance2();
        testDistance3();

    }

    //=============================Boundary Value Tests================================
    /**
     * Test method for {@link primitives.Point#distance(Point)}.
     * Tests the distance of a point to itself.
     * <br>TC01: Distance of a point to itself
     */
    @org.junit.jupiter.api.Test
    void testDistance1() {
        Point  p1  = new Point(1, 2, 3);
        assertEquals(0,p1.distance(p1),"ERROR: Point squared distance to itself is not zero");
    }

    //============================Equivalence Partitions Tests================================
    /**
     * Test method for {@link primitives.Point#distance(Point)}.
     * Tests the distance between two points.
     * <br>TC02: Distance between two points
     */
    @org.junit.jupiter.api.Test
    void testDistance2() {
        Point  p1  = new Point(1, 2, 3);
        Point  p2 = new Point(2, 4, 5);
        assertEquals(3,p1.distance(p2),"ERROR: distance between points is wrong");
    }
    /**
     * Test method for {@link primitives.Point#distance(Point)}.
     * Tests the distance between two points.
     * <br>TC03: Distance between two points (order independent)
     */
    @org.junit.jupiter.api.Test
    void testDistance3() {
        Point  p1  = new Point(1, 2, 3);
        Point  p2 = new Point(2, 4, 5);
        assertEquals(3,p2.distance(p1),"ERROR: distance between points is wrong");
    }
}