package org.apache.commons.math.optimization.fitting;

import org.junit.Test;
import org.junit.Assert;
import org.apache.commons.math.exception.NullArgumentException;
import org.apache.commons.math.exception.NumberIsTooSmallException;

public class GaussianFitterTest {

    @Test(expected = NullArgumentException.class)
    public void testParameterGuesserConstructor_NullObservations_ThrowsException() {
        new GaussianFitter.ParameterGuesser(null);
    }

    @Test(expected = NumberIsTooSmallException.class)
    public void testParameterGuesserConstructor_InsufficientPoints_ThrowsException() {
        WeightedObservedPoint[] points = new WeightedObservedPoint[] {
            new WeightedObservedPoint(1.0, 0.0, 1.0),
            new WeightedObservedPoint(1.0, 1.0, 2.0)
        };
        new GaussianFitter.ParameterGuesser(points);
    }

    @Test
    public void testParameterGuesserGuess_ValidPoints_ReturnsCorrectParameters() {
        // Gaussian parameters: norm=10.0, mean=5.0, sigma=1.0
        // Expected curve points: f(x) = 10 * exp(- (x-5)^2 / 2)
        WeightedObservedPoint[] points = new WeightedObservedPoint[] {
            new WeightedObservedPoint(1.0, 3.0, 1.35335),
            new WeightedObservedPoint(1.0, 4.0, 6.06531),
            new WeightedObservedPoint(1.0, 5.0, 10.0),
            new WeightedObservedPoint(1.0, 6.0, 6.06531),
            new WeightedObservedPoint(1.0, 7.0, 1.35335)
        };
        GaussianFitter.ParameterGuesser guesser = new GaussianFitter.ParameterGuesser(points);
        double[] parameters = guesser.guess();

        Assert.assertNotNull(parameters);
        Assert.assertEquals(3, parameters.length);
        Assert.assertEquals(10.0, parameters[0], 1e-3);
        Assert.assertEquals(5.0, parameters[1], 1e-3);
        Assert.assertEquals(1.0, parameters[2], 1e-1);
    }

    @Test
    public void testParameterGuesserGuess_ReturnsIndependentArray() {
        WeightedObservedPoint[] points = new WeightedObservedPoint[] {
            new WeightedObservedPoint(1.0, 0.0, 1.0),
            new WeightedObservedPoint(1.0, 1.0, 5.0),
            new WeightedObservedPoint(1.0, 2.0, 1.0)
        };
        GaussianFitter.ParameterGuesser guesser = new GaussianFitter.ParameterGuesser(points);
        double[] p1 = guesser.guess();
        double[] p2 = guesser.guess();

        Assert.assertNotSame(p1, p2);
        Assert.assertArrayEquals(p1, p2, 1e-15);
    }
}
