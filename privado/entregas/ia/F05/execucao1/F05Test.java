package org.apache.commons.math.geometry;

import org.apache.commons.math.exception.MathArithmeticException;
import org.apache.commons.math.util.FastMath;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class F05Test {

    private static final double EPSILON = 1e-12;

    @Test
    public void testConstants() {
        assertEquals(0.0, Vector3D.ZERO.getX(), EPSILON);
        assertEquals(0.0, Vector3D.ZERO.getY(), EPSILON);
        assertEquals(0.0, Vector3D.ZERO.getZ(), EPSILON);

        assertEquals(1.0, Vector3D.PLUS_I.getX(), EPSILON);
        assertEquals(0.0, Vector3D.PLUS_I.getY(), EPSILON);
        assertEquals(0.0, Vector3D.PLUS_I.getZ(), EPSILON);

        assertEquals(-1.0, Vector3D.MINUS_I.getX(), EPSILON);
        assertEquals(0.0, Vector3D.MINUS_I.getY(), EPSILON);
        assertEquals(0.0, Vector3D.MINUS_I.getZ(), EPSILON);

        assertEquals(0.0, Vector3D.PLUS_J.getX(), EPSILON);
        assertEquals(1.0, Vector3D.PLUS_J.getY(), EPSILON);
        assertEquals(0.0, Vector3D.PLUS_J.getZ(), EPSILON);

        assertEquals(0.0, Vector3D.MINUS_J.getX(), EPSILON);
        assertEquals(-1.0, Vector3D.MINUS_J.getY(), EPSILON);
        assertEquals(0.0, Vector3D.MINUS_J.getZ(), EPSILON);

        assertEquals(0.0, Vector3D.PLUS_K.getX(), EPSILON);
        assertEquals(0.0, Vector3D.PLUS_K.getY(), EPSILON);
        assertEquals(1.0, Vector3D.PLUS_K.getZ(), EPSILON);

        assertEquals(0.0, Vector3D.MINUS_K.getX(), EPSILON);
        assertEquals(0.0, Vector3D.MINUS_K.getY(), EPSILON);
        assertEquals(-1.0, Vector3D.MINUS_K.getZ(), EPSILON);

        assertTrue(Vector3D.NaN.isNaN());
        assertTrue(Vector3D.POSITIVE_INFINITY.isInfinite());
        assertTrue(Vector3D.NEGATIVE_INFINITY.isInfinite());
    }

    @Test
    public void testCartesianConstructor() {
        Vector3D v = new Vector3D(1.5, -2.5, 3.5);
        assertEquals(1.5, v.getX(), EPSILON);
        assertEquals(-2.5, v.getY(), EPSILON);
        assertEquals(3.5, v.getZ(), EPSILON);
    }

    @Test
    public void testAzimuthalConstructorEquator() {
        Vector3D v = new Vector3D(FastMath.PI / 2, 0.0);
        assertEquals(0.0, v.getX(), EPSILON);
        assertEquals(1.0, v.getY(), EPSILON);
        assertEquals(0.0, v.getZ(), EPSILON);
    }

    @Test
    public void testAzimuthalConstructorNorthPole() {
        Vector3D v = new Vector3D(0.0, FastMath.PI / 2);
        assertEquals(0.0, v.getX(), EPSILON);
        assertEquals(0.0, v.getY(), EPSILON);
        assertEquals(1.0, v.getZ(), EPSILON);
    }

    @Test
    public void testLinearConstructor1() {
        Vector3D base = new Vector3D(1.0, 2.0, 3.0);
        Vector3D scaled = new Vector3D(2.5, base);
        assertEquals(2.5, scaled.getX(), EPSILON);
        assertEquals(5.0, scaled.getY(), EPSILON);
        assertEquals(7.5, scaled.getZ(), EPSILON);
    }

    @Test
    public void testLinearConstructor2() {
        Vector3D u1 = new Vector3D(1.0, 0.0, 0.0);
        Vector3D u2 = new Vector3D(0.0, 2.0, -1.0);
        Vector3D v = new Vector3D(2.0, u1, 3.0, u2);
        assertEquals(2.0, v.getX(), EPSILON);
        assertEquals(6.0, v.getY(), EPSILON);
        assertEquals(-3.0, v.getZ(), EPSILON);
    }

    @Test
    public void testLinearConstructor3() {
        Vector3D u1 = new Vector3D(1.0, 0.0, 0.0);
        Vector3D u2 = new Vector3D(0.0, 1.0, 0.0);
        Vector3D u3 = new Vector3D(0.0, 0.0, 1.0);
        Vector3D v = new Vector3D(2.0, u1, -3.0, u2, 4.0, u3);
        assertEquals(2.0, v.getX(), EPSILON);
        assertEquals(-3.0, v.getY(), EPSILON);
        assertEquals(4.0, v.getZ(), EPSILON);
    }

    @Test
    public void testLinearConstructor4() {
        Vector3D u1 = new Vector3D(1.0, 0.0, 0.0);
        Vector3D u2 = new Vector3D(0.0, 1.0, 0.0);
        Vector3D u3 = new Vector3D(0.0, 0.0, 1.0);
        Vector3D u4 = new Vector3D(1.0, 1.0, 1.0);
        Vector3D v = new Vector3D(1.0, u1, 2.0, u2, 3.0, u3, -1.0, u4);
        assertEquals(0.0, v.getX(), EPSILON);
        assertEquals(1.0, v.getY(), EPSILON);
        assertEquals(2.0, v.getZ(), EPSILON);
    }

    @Test
    public void testGetNorm1() {
        Vector3D v = new Vector3D(-2.0, 3.0, -4.0);
        assertEquals(9.0, v.getNorm1(), EPSILON);
    }

    @Test
    public void testGetNorm() {
        Vector3D v = new Vector3D(1.0, 2.0, 2.0);
        assertEquals(3.0, v.getNorm(), EPSILON);
    }

    @Test
    public void testGetNormSq() {
        Vector3D v = new Vector3D(2.0, 3.0, 4.0);
        assertEquals(29.0, v.getNormSq(), EPSILON);
    }

    @Test
    public void testGetNormInf() {
        Vector3D v = new Vector3D(-5.0, 2.0, -3.0);
        assertEquals(5.0, v.getNormInf(), EPSILON);
    }

    @Test
    public void testGetAlpha() {
        Vector3D v = new Vector3D(1.0, 1.0, 5.0);
        assertEquals(FastMath.PI / 4, v.getAlpha(), EPSILON);
    }

    @Test
    public void testGetDelta() {
        Vector3D v = new Vector3D(0.0, 0.0, 5.0);
        assertEquals(FastMath.PI / 2, v.getDelta(), EPSILON);
    }

    @Test
    public void testAddVector() {
        Vector3D v1 = new Vector3D(1.0, 2.0, 3.0);
        Vector3D v2 = new Vector3D(-2.0, 3.0, 1.0);
        Vector3D sum = v1.add(v2);
        assertEquals(-1.0, sum.getX(), EPSILON);
        assertEquals(5.0, sum.getY(), EPSILON);
        assertEquals(4.0, sum.getZ(), EPSILON);
    }

    @Test
    public void testAddScaledVector() {
        Vector3D v1 = new Vector3D(1.0, 2.0, 3.0);
        Vector3D v2 = new Vector3D(2.0, -1.0, 4.0);
        Vector3D result = v1.add(3.0, v2);
        assertEquals(7.0, result.getX(), EPSILON);
        assertEquals(-1.0, result.getY(), EPSILON);
        assertEquals(15.0, result.getZ(), EPSILON);
    }

    @Test
    public void testSubtractVector() {
        Vector3D v1 = new Vector3D(5.0, 2.0, 1.0);
        Vector3D v2 = new Vector3D(2.0, -1.0, 4.0);
        Vector3D diff = v1.subtract(v2);
        assertEquals(3.0, diff.getX(), EPSILON);
        assertEquals(3.0, diff.getY(), EPSILON);
        assertEquals(-3.0, diff.getZ(), EPSILON);
    }

    @Test
    public void testSubtractScaledVector() {
        Vector3D v1 = new Vector3D(5.0, 2.0, 1.0);
        Vector3D v2 = new Vector3D(2.0, 1.0, -2.0);
        Vector3D result = v1.subtract(2.0, v2);
        assertEquals(1.0, result.getX(), EPSILON);
        assertEquals(0.0, result.getY(), EPSILON);
        assertEquals(5.0, result.getZ(), EPSILON);
    }

    @Test
    public void testNormalizeValidVector() {
        Vector3D v = new Vector3D(0.0, 3.0, 4.0);
        Vector3D normalized = v.normalize();
        assertEquals(1.0, normalized.getNorm(), EPSILON);
        assertEquals(0.0, normalized.getX(), EPSILON);
        assertEquals(0.6, normalized.getY(), EPSILON);
        assertEquals(0.8, normalized.getZ(), EPSILON);
    }

    @Test(expected = MathArithmeticException.class)
    public void testNormalizeZeroVectorThrowsException() {
        Vector3D.ZERO.normalize();
    }

    @Test
    public void testOrthogonalBranchXWithinThreshold() {
        Vector3D v = new Vector3D(0.0, 3.0, 4.0);
        Vector3D ortho = v.orthogonal();
        assertEquals(1.0, ortho.getNorm(), EPSILON);
        assertEquals(0.0, Vector3D.dotProduct(v, ortho), EPSILON);
    }

    @Test
    public void testOrthogonalBranchYWithinThreshold() {
        Vector3D v = new Vector3D(4.0, 0.0, 3.0);
        Vector3D ortho = v.orthogonal();
        assertEquals(1.0, ortho.getNorm(), EPSILON);
        assertEquals(0.0, Vector3D.dotProduct(v, ortho), EPSILON);
    }

    @Test
    public void testOrthogonalBranchNeitherWithinThreshold() {
        Vector3D v = new Vector3D(3.0, 3.0, 1.0);
        Vector3D ortho = v.orthogonal();
        assertEquals(1.0, ortho.getNorm(), EPSILON);
        assertEquals(0.0, Vector3D.dotProduct(v, ortho), EPSILON);
    }

    @Test(expected = MathArithmeticException.class)
    public void testOrthogonalZeroVectorThrowsException() {
        Vector3D.ZERO.orthogonal();
    }

    @Test
    public void testAngleSeparatedVectors() {
        Vector3D v1 = new Vector3D(1.0, 0.0, 0.0);
        Vector3D v2 = new Vector3D(0.0, 1.0, 0.0);
        assertEquals(FastMath.PI / 2, Vector3D.angle(v1, v2), EPSILON);
    }

    @Test
    public void testAngleAlmostParallelVectors() {
        Vector3D v1 = new Vector3D(1.0, 0.0, 0.0);
        Vector3D v2 = new Vector3D(1.0, 1e-6, 0.0);
        double angle = Vector3D.angle(v1, v2);
        assertTrue(angle > 0.0);
        assertTrue(angle < 1e-4);
    }

    @Test
    public void testAngleAlmostOppositeVectors() {
        Vector3D v1 = new Vector3D(1.0, 0.0, 0.0);
        Vector3D v2 = new Vector3D(-1.0, 1e-6, 0.0);
        double angle = Vector3D.angle(v1, v2);
        assertEquals(FastMath.PI, angle, 1e-4);
    }

    @Test(expected = MathArithmeticException.class)
    public void testAngleWithZeroVectorThrowsException() {
        Vector3D.angle(Vector3D.ZERO, new Vector3D(1.0, 0.0, 0.0));
    }

    @Test
    public void testNegate() {
        Vector3D v = new Vector3D(1.0, -2.0, 3.0);
        Vector3D negated = v.negate();
        assertEquals(-1.0, negated.getX(), EPSILON);
        assertEquals(2.0, negated.getY(), EPSILON);
        assertEquals(-3.0, negated.getZ(), EPSILON);
    }

    @Test
    public void testScalarMultiply() {
        Vector3D v = new Vector3D(1.5, -2.0, 3.0);
        Vector3D scaled = v.scalarMultiply(2.0);
        assertEquals(3.0, scaled.getX(), EPSILON);
        assertEquals(-4.0, scaled.getY(), EPSILON);
        assertEquals(6.0, scaled.getZ(), EPSILON);
    }

    @Test
    public void testIsNaN() {
        assertFalse(new Vector3D(1.0, 2.0, 3.0).isNaN());
        assertTrue(new Vector3D(Double.NaN, 2.0, 3.0).isNaN());
        assertTrue(new Vector3D(1.0, Double.NaN, 3.0).isNaN());
        assertTrue(new Vector3D(1.0, 2.0, Double.NaN).isNaN());
    }

    @Test
    public void testIsInfinite() {
        assertFalse(new Vector3D(1.0, 2.0, 3.0).isInfinite());
        assertTrue(new Vector3D(Double.POSITIVE_INFINITY, 2.0, 3.0).isInfinite());
        assertTrue(new Vector3D(1.0, Double.NEGATIVE_INFINITY, 3.0).isInfinite());
        assertTrue(new Vector3D(1.0, 2.0, Double.POSITIVE_INFINITY).isInfinite());
        assertFalse(new Vector3D(Double.POSITIVE_INFINITY, Double.NaN, 3.0).isInfinite());
    }

    @Test
    public void testEqualsSameInstance() {
        Vector3D v = new Vector3D(1.0, 2.0, 3.0);
        assertTrue(v.equals(v));
    }

    @Test
    public void testEqualsNull() {
        Vector3D v = new Vector3D(1.0, 2.0, 3.0);
        assertFalse(v.equals(null));
    }

    @Test
    public void testEqualsDifferentType() {
        Vector3D v = new Vector3D(1.0, 2.0, 3.0);
        assertFalse(v.equals(new Object()));
    }

    @Test
    public void testEqualsEqualVectors() {
        Vector3D v1 = new Vector3D(1.0, 2.0, 3.0);
        Vector3D v2 = new Vector3D(1.0, 2.0, 3.0);
        assertTrue(v1.equals(v2));
        assertEquals(v1.hashCode(), v2.hashCode());
    }

    @Test
    public void testEqualsDifferentCoordinates() {
        Vector3D v1 = new Vector3D(1.0, 2.0, 3.0);
        assertFalse(v1.equals(new Vector3D(1.1, 2.0, 3.0)));
        assertFalse(v1.equals(new Vector3D(1.0, 2.1, 3.0)));
        assertFalse(v1.equals(new Vector3D(1.0, 2.0, 3.1)));
    }

    @Test
    public void testEqualsNaNRule() {
        Vector3D v1 = new Vector3D(Double.NaN, 0.0, 0.0);
        Vector3D v2 = new Vector3D(0.0, Double.NaN, 0.0);
        Vector3D nonNan = new Vector3D(1.0, 2.0, 3.0);

        assertTrue(v1.equals(v2));
        assertTrue(v1.equals(Vector3D.NaN));
        assertFalse(v1.equals(nonNan));
        assertFalse(nonNan.equals(v1));
    }

    @Test
    public void testHashCodeNaN() {
        Vector3D nan1 = new Vector3D(Double.NaN, 0.0, 0.0);
        Vector3D nan2 = new Vector3D(0.0, 0.0, Double.NaN);
        assertEquals(8, nan1.hashCode());
        assertEquals(nan1.hashCode(), nan2.hashCode());
    }

    @Test
    public void testHashCodeConsistency() {
        Vector3D v1 = new Vector3D(1.0, -2.0, 3.0);
        Vector3D v2 = new Vector3D(1.0, -2.0, 3.0);
        assertEquals(v1.hashCode(), v2.hashCode());
    }

    @Test
    public void testDotProduct() {
        Vector3D v1 = new Vector3D(1.0, 2.0, 3.0);
        Vector3D v2 = new Vector3D(4.0, -5.0, 6.0);
        assertEquals(12.0, Vector3D.dotProduct(v1, v2), EPSILON);
    }

    @Test
    public void testCrossProduct() {
        Vector3D v1 = new Vector3D(1.0, 0.0, 0.0);
        Vector3D v2 = new Vector3D(0.0, 1.0, 0.0);
        Vector3D cross = Vector3D.crossProduct(v1, v2);
        assertEquals(0.0, cross.getX(), EPSILON);
        assertEquals(0.0, cross.getY(), EPSILON);
        assertEquals(1.0, cross.getZ(), EPSILON);
    }

    @Test
    public void testDistance1() {
        Vector3D v1 = new Vector3D(1.0, -2.0, 3.0);
        Vector3D v2 = new Vector3D(4.0, 2.0, -1.0);
        assertEquals(11.0, Vector3D.distance1(v1, v2), EPSILON);
    }

    @Test
    public void testDistance() {
        Vector3D v1 = new Vector3D(1.0, 2.0, 3.0);
        Vector3D v2 = new Vector3D(4.0, 6.0, 3.0);
        assertEquals(5.0, Vector3D.distance(v1, v2), EPSILON);
    }

    @Test
    public void testDistanceInf() {
        Vector3D v1 = new Vector3D(1.0, 2.0, 3.0);
        Vector3D v2 = new Vector3D(4.0, 8.0, 2.0);
        assertEquals(6.0, Vector3D.distanceInf(v1, v2), EPSILON);
    }

    @Test
    public void testDistanceSq() {
        Vector3D v1 = new Vector3D(1.0, 2.0, 3.0);
        Vector3D v2 = new Vector3D(3.0, 4.0, 5.0);
        assertEquals(12.0, Vector3D.distanceSq(v1, v2), EPSILON);
    }

    @Test
    public void testToString() {
        Vector3D v = new Vector3D(1.0, 2.0, 3.0);
        String str = v.toString();
        assertNotNull(str);
        assertFalse(str.isEmpty());
    }
}