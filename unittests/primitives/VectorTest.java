package primitives;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static primitives.Util.isZero;

class VectorTest {
    Vector v1         = new Vector(1, 2, 3);
    Vector v1Opposite = new Vector(-1, -2, -3);
    Vector v2         = new Vector(-2, -4, -6);
    Vector v3         = new Vector(0, 3, -2);
    Vector v4         = new Vector(1, 2, 2);
    /**
     * Method to test the lengthSquared function.
     */
    @Test
    void lengthSquared() {
        //============== Equivalence Partitions Tests ==============
        //TC01: test that check that the func can calculate the squared length of the vector and return the correct value
        assertEquals(v4.lengthSquared(),9 ,"ERROR: lengthSquared() wrong value");
    }

    /**
     * length() method test case.
     */
    @Test
    void length() {
        //============== Equivalence Partitions Tests ==============
        //TC01: test that check that the func can calculate the  length of the vector and return the correct
        assertEquals(v4.length(),3 ,"ERROR: length() wrong value");

    }

    /**
     * A description of the entire Java function.
     */
    @Test
    void add() {
        //============== Equivalence Partitions Tests ==============
        //TC01: test that check that this func can add two vectors and return new vector correctly
        assertEquals(v1Opposite,v1.add(v2),"ERROR: Vector + Vector does not work correctly");
        // =============== Boundary Values Tests ==================
        //TC11: test that check that this func can add vector and itself and return new vector correctly
        assertThrows(IllegalArgumentException.class, () -> v1.add(v1Opposite), "ERROR: Vector + itself throws wrong exception");
    }

    /**
     * Test method to check the scaling of vectors.
     */
    @Test
    void scale() {

        // ============ Equivalence Partitions Tests ==============
        // TC01: test that two vector are scaled
        assertEquals(new Vector(3, 6, 9), v1.scale(3), "scale() wrong result");
        // =============== Boundary Values Tests ==================
        // TC11: test that vector is scaled to zero
        assertThrows(IllegalArgumentException.class, () -> v1.scale(0), "scale() throws an exception for zero");
    }

    @Test
    void dotProduct() {

        // ============ Equivalence Partitions Tests ==============
        // TC01: Test that the dot product of a vector
        assertEquals(-28, v1.dotProduct(v2), "ERROR: dotProduct() wrong value");
        // =============== Boundary Values Tests ==================
        // TC11: Test that the dot product of the zero result
        assertEquals(0, v1.dotProduct(v3),"ERROR: dotProduct() for orthogonal vectors is not zero");
        // TC12: Test that the dot product of himself
        assertEquals(56,v2.dotProduct(v2), "dotProduct() returned wrong result");
    }



    /**
     * Test the cross product of two vectors and their properties.
     */
    @Test
    void crossProduct() {
        // ============ Equivalence Partitions Tests ==============
        // TC01: Test that the cross product of two vectors
        Vector vr = v1.crossProduct(v3);
        assertTrue(isZero(vr.length() - v1.length() * v3.length()), "crossProduct() returned wrong result");
        // TC02: Test that the cross product of two orthogonal vectors
        assertFalse(!isZero(vr.dotProduct(v1)) || !isZero(vr.dotProduct(v3)), "ERROR: crossProduct() result is not orthogonal to its operands");

        // =============== Boundary Values Tests ==================
        // TC11: Test that the cross product of two orthogonal vectors
        assertThrows(IllegalArgumentException.class, () -> v1.crossProduct(v2), "ERROR: crossProduct() for parallel vectors does not throw an exception");
    }



    /**
     * normalize() method test
     */
        @Test
    void normalize() {
        Vector v = new Vector(1, 2, 3);
        Vector u = v.normalize();
    // ============ Equivalence Partitions Tests ==============
    // TC01: Test that the length of the normalized vector is 1
        assertTrue(isZero(u.length() - 1), "ERROR: the normalized vector is not a unit vector");
        // TC02:    Test that the normalized vector is opposite to the original one
            assertFalse(v.dotProduct(u) < 0,"ERROR: the normalized vector is opposite to the original one");
    }
}
