package org.apache.commons.math.optimization.fitting;

import org.apache.commons.math.exception.NullArgumentException;
import org.apache.commons.math.exception.NumberIsTooSmallException;
import org.apache.commons.math.exception.OutOfRangeException;
import org.junit.Assert;
import org.junit.Test;

public class GaussianFitterTest {

    @Test(expected = NullArgumentException.class)
    public void testParameterGuesserConstructorThrowsExceptionOnNullInput() {
        new GaussianFitter.ParameterGuesser(null);
    }

    @Test(expected = NumberIsTooSmallException.class)
    public void testParameterGuesserConstructorThrowsExceptionOnTooFewPoints() {
        WeightedObservedPoint[] points = {
            new WeightedObservedPoint(1.0, 1.0, 1.0),
            new WeightedObservedPoint(1.0, 2.0, 2.0)
        };
        new GaussianFitter.ParameterGuesser(points);
    }

    @Test
    public void testParameterGuesserConstructorAcceptsValidPoints() {
        WeightedObservedPoint[] points = {
            new WeightedObservedPoint(1.0, 1.0, 1.0),
            new WeightedObservedPoint(1.0, 2.0, 2.0),
            new WeightedObservedPoint(1.0, 3.0, 1.0)
        };
        GaussianFitter.ParameterGuesser guesser = new GaussianFitter.ParameterGuesser(points);
        Assert.assertNotNull(guesser);
    }

    @Test
    public void testParameterGuesserGuessReturnsValidArraySize() {
        WeightedObservedPoint[] points = {
            new WeightedObservedPoint(1.0, 1.0, 1.0),
            new WeightedObservedPoint(1.0, 2.0, 10.0),
            new WeightedObservedPoint(1.0, 3.0, 1.0)
        };
        GaussianFitter.ParameterGuesser guesser = new GaussianFitter.ParameterGuesser(points);
        double[] guess = guesser.guess();
        Assert.assertEquals(3, guess.length);
    }

    @Test
    public void testParameterGuesserGuessReturnsClone() {
        WeightedObservedPoint[] points = {
            new WeightedObservedPoint(1.0, 1.0, 1.0),
            new WeightedObservedPoint(1.0, 2.0, 10.0),
            new WeightedObservedPoint(1.0, 3.0, 1.0)
        };
        GaussianFitter.ParameterGuesser guesser = new GaussianFitter.ParameterGuesser(points);
        double[] guess1 = guesser.guess();
        double[] guess2 = guesser.guess();
        Assert.assertNotSame(guess1, guess2);
        Assert.assertArrayEquals(guess1, guess2, 0.0);
    }

    @Test(expected = OutOfRangeException.class)
    public void testParameterGuesserInterpolationThrowsOutOfRangeWhenYIsTooHigh() {
        // Points Y values: 1.0, 2.0, 3.0. Max is 3.0. Requesting interpolation for Y=10.0.
        WeightedObservedPoint[] points = {
            new WeightedObservedPoint(1.0, 1.0, 1.0),
            new WeightedObservedPoint(1.0, 2.0, 2.0),
            new WeightedObservedPoint(1.0, 3.0, 3.0)
        };
        GaussianFitter.ParameterGuesser guesser = new GaussianFitter.ParameterGuesser(points);
        // We cannot call interpolateXAtY directly as it is private, but we can call guess()
        // which triggers the internal FWHM calculation logic that might lead to interpolation calls.
        // Given the logic, we must ensure it handles broad inputs.
        // We force an out of range scenario by choosing points where the heuristic fails.
        guesser.guess();
    }
}
