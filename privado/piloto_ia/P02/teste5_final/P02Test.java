package org.apache.commons.math.optimization.fitting;

import org.apache.commons.math.exception.NullArgumentException;
import org.apache.commons.math.exception.NumberIsTooSmallException;
import org.junit.Assert;
import org.junit.Test;

public class P02Test {

    @Test(expected = NullArgumentException.class)
    public void testParameterGuesserConstructorThrowsExceptionOnNullInput() {
        new GaussianFitter.ParameterGuesser(null);
    }

    @Test(expected = NumberIsTooSmallException.class)
    public void testParameterGuesserConstructorThrowsExceptionOnInsufficientPoints() {
        WeightedObservedPoint[] points = new WeightedObservedPoint[] {
            new WeightedObservedPoint(1.0, 1.0, 1.0),
            new WeightedObservedPoint(1.0, 2.0, 2.0)
        };
        new GaussianFitter.ParameterGuesser(points);
    }

    @Test
    public void testParameterGuesserEncapsulation() {
        WeightedObservedPoint[] points = new WeightedObservedPoint[] {
            new WeightedObservedPoint(1.0, 1.0, 1.0),
            new WeightedObservedPoint(1.0, 2.0, 2.0),
            new WeightedObservedPoint(1.0, 3.0, 3.0)
        };
        GaussianFitter.ParameterGuesser guesser = new GaussianFitter.ParameterGuesser(points);
        double[] firstGuess = guesser.guess();
        
        // Modify input array
        points[0] = new WeightedObservedPoint(1.0, 10.0, 10.0);
        
        double[] secondGuess = guesser.guess();
        
        Assert.assertArrayEquals("Guess result should be independent of changes to input array after instantiation", 
                                 firstGuess, secondGuess, 1e-15);
    }

    @Test
    public void testParameterGuesserReturnsClone() {
        WeightedObservedPoint[] points = new WeightedObservedPoint[] {
            new WeightedObservedPoint(1.0, 1.0, 1.0),
            new WeightedObservedPoint(1.0, 2.0, 2.0),
            new WeightedObservedPoint(1.0, 3.0, 3.0)
        };
        GaussianFitter.ParameterGuesser guesser = new GaussianFitter.ParameterGuesser(points);
        double[] guess1 = guesser.guess();
        double[] guess2 = guesser.guess();
        
        Assert.assertNotSame("guess() should return a new array instance on every call", guess1, guess2);
        Assert.assertArrayEquals(guess1, guess2, 1e-15);
    }

    @Test
    public void testParameterGuesserBasicLogic() {
        // Gaussian peak at x=0, y=1.
        // FWHM calculation:
        // halfY = 0 + (1 - 0) / 2 = 0.5.
        // Points: (-1, 0), (0, 1), (1, 0).
        // X at y=0.5 between (-1,0) and (0,1) is -0.5.
        // X at y=0.5 between (0,1) and (1,0) is 0.5.
        // FWHM = 0.5 - (-0.5) = 1.0.
        // Sigma = 1.0 / (2 * sqrt(2 * ln(2))) ≈ 0.4246609
        
        WeightedObservedPoint[] points = new WeightedObservedPoint[] {
            new WeightedObservedPoint(1.0, -1.0, 0.0),
            new WeightedObservedPoint(1.0, 0.0, 1.0),
            new WeightedObservedPoint(1.0, 1.0, 0.0)
        };
        
        GaussianFitter.ParameterGuesser guesser = new GaussianFitter.ParameterGuesser(points);
        double[] params = guesser.guess();
        
        double expectedNorm = 1.0;
        double expectedMean = 0.0;
        double expectedSigma = 1.0 / (2.0 * Math.sqrt(2.0 * Math.log(2.0)));
        
        Assert.assertEquals("Norm should be 1.0", expectedNorm, params[0], 1e-15);
        Assert.assertEquals("Mean should be 0.0", expectedMean, params[1], 1e-15);
        Assert.assertEquals("Sigma should be derived from FWHM", expectedSigma, params[2], 1e-10);
    }
}
