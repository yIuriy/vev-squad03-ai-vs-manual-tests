package org.apache.commons.math.optimization.fitting;

import org.apache.commons.math.exception.NullArgumentException;
import org.apache.commons.math.exception.NumberIsTooSmallException;
import org.apache.commons.math.optimization.general.LevenbergMarquardtOptimizer;
import org.junit.Test;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

public class P02Test {

    private static final double FWHM_FACTOR = 2.0 * Math.sqrt(2.0 * Math.log(2.0));

    @Test
    public void testParameterGuesserConstructorNullObservationsThrowsNullArgumentException() {
        try {
            new GaussianFitter.ParameterGuesser(null);
            fail("Expected NullArgumentException for null observations");
        } catch (NullArgumentException e) {
            assertNotNull(e.getMessage());
        }
    }

    @Test
    public void testParameterGuesserConstructorEmptyArrayThrowsNumberIsTooSmallException() {
        try {
            new GaussianFitter.ParameterGuesser(new WeightedObservedPoint[0]);
            fail("Expected NumberIsTooSmallException for 0 observations");
        } catch (NumberIsTooSmallException e) {
            assertEquals(3, e.getMin().intValue());
            assertEquals(0, e.getArgument().intValue());
        }
    }

    @Test
    public void testParameterGuesserConstructorOneObservationThrowsNumberIsTooSmallException() {
        WeightedObservedPoint[] points = new WeightedObservedPoint[] {
            new WeightedObservedPoint(1.0, 0.0, 1.0)
        };
        try {
            new GaussianFitter.ParameterGuesser(points);
            fail("Expected NumberIsTooSmallException for 1 observation");
        } catch (NumberIsTooSmallException e) {
            assertEquals(3, e.getMin().intValue());
            assertEquals(1, e.getArgument().intValue());
        }
    }

    @Test
    public void testParameterGuesserConstructorTwoObservationsThrowsNumberIsTooSmallException() {
        WeightedObservedPoint[] points = new WeightedObservedPoint[] {
            new WeightedObservedPoint(1.0, 0.0, 1.0),
            new WeightedObservedPoint(1.0, 1.0, 2.0)
        };
        try {
            new GaussianFitter.ParameterGuesser(points);
            fail("Expected NumberIsTooSmallException for 2 observations");
        } catch (NumberIsTooSmallException e) {
            assertEquals(3, e.getMin().intValue());
            assertEquals(2, e.getArgument().intValue());
        }
    }

    @Test
    public void testParameterGuesserConstructorThreeObservationsSucceeds() {
        WeightedObservedPoint[] points = new WeightedObservedPoint[] {
            new WeightedObservedPoint(1.0, -1.0, 2.0),
            new WeightedObservedPoint(1.0, 0.0, 10.0),
            new WeightedObservedPoint(1.0, 1.0, 2.0)
        };
        GaussianFitter.ParameterGuesser guesser = new GaussianFitter.ParameterGuesser(points);
        double[] parameters = guesser.guess();
        assertNotNull(parameters);
        assertEquals(3, parameters.length);
    }

    @Test
    public void testParameterGuesserDoesNotMutateCallerObservationsArray() {
        WeightedObservedPoint p1 = new WeightedObservedPoint(1.0, 2.0, 3.0);
        WeightedObservedPoint p2 = new WeightedObservedPoint(1.0, -1.0, 5.0);
        WeightedObservedPoint p3 = new WeightedObservedPoint(1.0, 0.5, 10.0);
        WeightedObservedPoint[] points = new WeightedObservedPoint[] { p1, p2, p3 };

        new GaussianFitter.ParameterGuesser(points).guess();

        assertSame(p1, points[0]);
        assertSame(p2, points[1]);
        assertSame(p3, points[2]);
    }

    @Test
    public void testParameterGuesserIsIsolatedFromSubsequentChangesToCallerArray() {
        WeightedObservedPoint[] points = new WeightedObservedPoint[] {
            new WeightedObservedPoint(1.0, -1.0, 2.0),
            new WeightedObservedPoint(1.0, 0.0, 10.0),
            new WeightedObservedPoint(1.0, 1.0, 2.0)
        };
        GaussianFitter.ParameterGuesser guesser = new GaussianFitter.ParameterGuesser(points);

        points[1] = new WeightedObservedPoint(1.0, 0.0, 999.0);
        double[] parameters = guesser.guess();

        assertEquals(10.0, parameters[0], 1e-9);
    }

    @Test
    public void testParameterGuesserGuessReturnsClonedParameters() {
        WeightedObservedPoint[] points = new WeightedObservedPoint[] {
            new WeightedObservedPoint(1.0, -1.0, 2.0),
            new WeightedObservedPoint(1.0, 0.0, 10.0),
            new WeightedObservedPoint(1.0, 1.0, 2.0)
        };
        GaussianFitter.ParameterGuesser guesser = new GaussianFitter.ParameterGuesser(points);

        double[] guess1 = guesser.guess();
        guess1[0] = -123.45;
        double[] guess2 = guesser.guess();

        assertNotEquals(-123.45, guess2[0], 1e-9);
        assertEquals(10.0, guess2[0], 1e-9);
    }

    @Test
    public void testParameterGuesserGuessMultipleInvocationsReturnIdenticalValues() {
        WeightedObservedPoint[] points = new WeightedObservedPoint[] {
            new WeightedObservedPoint(1.0, -2.0, 2.0),
            new WeightedObservedPoint(1.0, 0.0, 10.0),
            new WeightedObservedPoint(1.0, 2.0, 2.0)
        };
        GaussianFitter.ParameterGuesser guesser = new GaussianFitter.ParameterGuesser(points);

        double[] first = guesser.guess();
        double[] second = guesser.guess();

        assertNotSame(first, second);
        assertArrayEquals(first, second, 1e-9);
    }

    @Test
    public void testParameterGuesserInterpolationBetweenPointsNominalSymmetricPeak() {
        WeightedObservedPoint[] points = new WeightedObservedPoint[] {
            new WeightedObservedPoint(1.0, -2.0, 2.0),
            new WeightedObservedPoint(1.0, 0.0, 10.0),
            new WeightedObservedPoint(1.0, 2.0, 2.0)
        };
        GaussianFitter.ParameterGuesser guesser = new GaussianFitter.ParameterGuesser(points);
        double[] parameters = guesser.guess();

        assertEquals(10.0, parameters[0], 1e-9);
        assertEquals(0.0, parameters[1], 1e-9);

        double expectedFwhm = 2.5;
        double expectedSigma = expectedFwhm / FWHM_FACTOR;
        assertEquals(expectedSigma, parameters[2], 1e-9);
    }

    @Test
    public void testParameterGuesserInterpolationExactHalfYValues() {
        WeightedObservedPoint[] points = new WeightedObservedPoint[] {
            new WeightedObservedPoint(1.0, -1.0, 5.0),
            new WeightedObservedPoint(1.0, 0.0, 10.0),
            new WeightedObservedPoint(1.0, 1.0, 5.0)
        };
        GaussianFitter.ParameterGuesser guesser = new GaussianFitter.ParameterGuesser(points);
        double[] parameters = guesser.guess();

        assertEquals(10.0, parameters[0], 1e-9);
        assertEquals(0.0, parameters[1], 1e-9);

        double expectedFwhm = 2.0;
        double expectedSigma = expectedFwhm / FWHM_FACTOR;
        assertEquals(expectedSigma, parameters[2], 1e-9);
    }

    @Test
    public void testParameterGuesserMultiStepInterpolationAlongSlope() {
        WeightedObservedPoint[] points = new WeightedObservedPoint[] {
            new WeightedObservedPoint(1.0, -3.0, 1.0),
            new WeightedObservedPoint(1.0, -2.0, 3.0),
            new WeightedObservedPoint(1.0, -1.0, 7.0),
            new WeightedObservedPoint(1.0, 0.0, 10.0),
            new WeightedObservedPoint(1.0, 1.0, 7.0),
            new WeightedObservedPoint(1.0, 2.0, 3.0),
            new WeightedObservedPoint(1.0, 3.0, 1.0)
        };
        GaussianFitter.ParameterGuesser guesser = new GaussianFitter.ParameterGuesser(points);
        double[] parameters = guesser.guess();

        assertEquals(10.0, parameters[0], 1e-9);
        assertEquals(0.0, parameters[1], 1e-9);

        double expectedFwhm = 3.0;
        double expectedSigma = expectedFwhm / FWHM_FACTOR;
        assertEquals(expectedSigma, parameters[2], 1e-9);
    }

    @Test
    public void testParameterGuesserFallbackWhenMaxYAtFirstIndex() {
        WeightedObservedPoint[] points = new WeightedObservedPoint[] {
            new WeightedObservedPoint(1.0, 0.0, 10.0),
            new WeightedObservedPoint(1.0, 1.0, 5.0),
            new WeightedObservedPoint(1.0, 2.0, 2.0)
        };
        GaussianFitter.ParameterGuesser guesser = new GaussianFitter.ParameterGuesser(points);
        double[] parameters = guesser.guess();

        assertEquals(10.0, parameters[0], 1e-9);
        assertEquals(0.0, parameters[1], 1e-9);

        double expectedFwhm = 2.0;
        double expectedSigma = expectedFwhm / FWHM_FACTOR;
        assertEquals(expectedSigma, parameters[2], 1e-9);
    }

    @Test
    public void testParameterGuesserFallbackWhenMaxYAtLastIndex() {
        WeightedObservedPoint[] points = new WeightedObservedPoint[] {
            new WeightedObservedPoint(1.0, 0.0, 2.0),
            new WeightedObservedPoint(1.0, 1.0, 5.0),
            new WeightedObservedPoint(1.0, 2.0, 10.0)
        };
        GaussianFitter.ParameterGuesser guesser = new GaussianFitter.ParameterGuesser(points);
        double[] parameters = guesser.guess();

        assertEquals(10.0, parameters[0], 1e-9);
        assertEquals(2.0, parameters[1], 1e-9);

        double expectedFwhm = 2.0;
        double expectedSigma = expectedFwhm / FWHM_FACTOR;
        assertEquals(expectedSigma, parameters[2], 1e-9);
    }

    @Test
    public void testParameterGuesserFallbackWhenHalfYExceedsMaxY() {
        WeightedObservedPoint[] points = new WeightedObservedPoint[] {
            new WeightedObservedPoint(1.0, 10.0, 2.0),
            new WeightedObservedPoint(1.0, 20.0, 10.0),
            new WeightedObservedPoint(1.0, 30.0, 2.0)
        };
        GaussianFitter.ParameterGuesser guesser = new GaussianFitter.ParameterGuesser(points);
        double[] parameters = guesser.guess();

        assertEquals(10.0, parameters[0], 1e-9);
        assertEquals(20.0, parameters[1], 1e-9);

        double expectedFwhm = 20.0;
        double expectedSigma = expectedFwhm / FWHM_FACTOR;
        assertEquals(expectedSigma, parameters[2], 1e-9);
    }

    @Test
    public void testParameterGuesserFallbackWhenAllYValuesAreEqual() {
        WeightedObservedPoint[] points = new WeightedObservedPoint[] {
            new WeightedObservedPoint(1.0, 1.0, 5.0),
            new WeightedObservedPoint(1.0, 2.0, 5.0),
            new WeightedObservedPoint(1.0, 3.0, 5.0)
        };
        GaussianFitter.ParameterGuesser guesser = new GaussianFitter.ParameterGuesser(points);
        double[] parameters = guesser.guess();

        assertEquals(5.0, parameters[0], 1e-9);
        assertEquals(1.0, parameters[1], 1e-9);

        double expectedFwhm = 2.0;
        double expectedSigma = expectedFwhm / FWHM_FACTOR;
        assertEquals(expectedSigma, parameters[2], 1e-9);
    }

    @Test
    public void testParameterGuesserIdenticalPointsYieldsZeroSigma() {
        WeightedObservedPoint[] points = new WeightedObservedPoint[] {
            new WeightedObservedPoint(1.0, 1.0, 5.0),
            new WeightedObservedPoint(1.0, 1.0, 5.0),
            new WeightedObservedPoint(1.0, 1.0, 5.0)
        };
        GaussianFitter.ParameterGuesser guesser = new GaussianFitter.ParameterGuesser(points);
        double[] parameters = guesser.guess();

        assertEquals(5.0, parameters[0], 1e-9);
        assertEquals(1.0, parameters[1], 1e-9);
        assertEquals(0.0, parameters[2], 1e-9);
    }

    @Test
    public void testParameterGuesserPointsSortedByXWhenProvidedOutOfOrder() {
        WeightedObservedPoint[] points = new WeightedObservedPoint[] {
            new WeightedObservedPoint(1.0, 2.0, 2.0),
            new WeightedObservedPoint(1.0, -2.0, 2.0),
            new WeightedObservedPoint(1.0, 0.0, 10.0)
        };
        GaussianFitter.ParameterGuesser guesser = new GaussianFitter.ParameterGuesser(points);
        double[] parameters = guesser.guess();

        assertEquals(10.0, parameters[0], 1e-9);
        assertEquals(0.0, parameters[1], 1e-9);
        double expectedSigma = 2.5 / FWHM_FACTOR;
        assertEquals(expectedSigma, parameters[2], 1e-9);
    }

    @Test
    public void testParameterGuesserSortingPointsWithEqualXSortsByY() {
        WeightedObservedPoint[] points = new WeightedObservedPoint[] {
            new WeightedObservedPoint(1.0, 0.0, 10.0),
            new WeightedObservedPoint(1.0, 0.0, 5.0),
            new WeightedObservedPoint(1.0, -1.0, 2.0),
            new WeightedObservedPoint(1.0, 1.0, 2.0)
        };
        GaussianFitter.ParameterGuesser guesser = new GaussianFitter.ParameterGuesser(points);
        double[] parameters = guesser.guess();

        assertEquals(10.0, parameters[0], 1e-9);
        assertEquals(0.0, parameters[1], 1e-9);
        assertTrue(parameters[2] > 0.0);
    }

    @Test
    public void testParameterGuesserSortingPointsWithEqualXAndYSortsByWeight() {
        WeightedObservedPoint[] points = new WeightedObservedPoint[] {
            new WeightedObservedPoint(2.0, 0.0, 10.0),
            new WeightedObservedPoint(1.0, 0.0, 10.0),
            new WeightedObservedPoint(1.0, -1.0, 2.0),
            new WeightedObservedPoint(1.0, 1.0, 2.0)
        };
        GaussianFitter.ParameterGuesser guesser = new GaussianFitter.ParameterGuesser(points);
        double[] parameters = guesser.guess();

        assertEquals(10.0, parameters[0], 1e-9);
        assertEquals(0.0, parameters[1], 1e-9);
        assertTrue(parameters[2] > 0.0);
    }

    @Test(expected = NumberIsTooSmallException.class)
    public void testGaussianFitterFitWithoutObservationsThrowsNumberIsTooSmallException() {
        GaussianFitter fitter = new GaussianFitter(new LevenbergMarquardtOptimizer());
        fitter.fit();
    }

    @Test(expected = NumberIsTooSmallException.class)
    public void testGaussianFitterFitWithTwoObservationsThrowsNumberIsTooSmallException() {
        GaussianFitter fitter = new GaussianFitter(new LevenbergMarquardtOptimizer());
        fitter.addObservedPoint(1.0, 2.0);
        fitter.addObservedPoint(2.0, 3.0);
        fitter.fit();
    }

    @Test
    public void testGaussianFitterAddObservedPointXYDefaultsWeightToOne() {
        GaussianFitter fitter = new GaussianFitter(new LevenbergMarquardtOptimizer());
        fitter.addObservedPoint(2.5, 4.0);

        WeightedObservedPoint[] observations = fitter.getObservations();
        assertEquals(1, observations.length);
        assertEquals(1.0, observations[0].getWeight(), 1e-9);
        assertEquals(2.5, observations[0].getX(), 1e-9);
        assertEquals(4.0, observations[0].getY(), 1e-9);
    }

    @Test
    public void testGaussianFitterAddObservedPointWithWeightXY() {
        GaussianFitter fitter = new GaussianFitter(new LevenbergMarquardtOptimizer());
        fitter.addObservedPoint(0.75, 3.5, 9.0);

        WeightedObservedPoint[] observations = fitter.getObservations();
        assertEquals(1, observations.length);
        assertEquals(0.75, observations[0].getWeight(), 1e-9);
        assertEquals(3.5, observations[0].getX(), 1e-9);
        assertEquals(9.0, observations[0].getY(), 1e-9);
    }

    @Test
    public void testGaussianFitterAddObservedPointInstance() {
        GaussianFitter fitter = new GaussianFitter(new LevenbergMarquardtOptimizer());
        WeightedObservedPoint point = new WeightedObservedPoint(0.5, 1.2, 3.4);
        fitter.addObservedPoint(point);

        WeightedObservedPoint[] observations = fitter.getObservations();
        assertEquals(1, observations.length);
        assertEquals(0.5, observations[0].getWeight(), 1e-9);
        assertEquals(1.2, observations[0].getX(), 1e-9);
        assertEquals(3.4, observations[0].getY(), 1e-9);
    }

    @Test
    public void testGaussianFitterClearObservationsEmptiesObservationList() {
        GaussianFitter fitter = new GaussianFitter(new LevenbergMarquardtOptimizer());
        fitter.addObservedPoint(1.0, 2.0);
        fitter.addObservedPoint(2.0, 4.0);
        fitter.addObservedPoint(3.0, 2.0);
        assertEquals(3, fitter.getObservations().length);

        fitter.clearObservations();
        assertEquals(0, fitter.getObservations().length);

        try {
            fitter.fit();
            fail("Expected NumberIsTooSmallException after clearing observations");
        } catch (NumberIsTooSmallException e) {
            assertEquals(3, e.getMin().intValue());
            assertEquals(0, e.getArgument().intValue());
        }
    }

    @Test
    public void testGaussianFitterFitJavadocDatasetWithoutInitialGuess() {
        GaussianFitter fitter = new GaussianFitter(new LevenbergMarquardtOptimizer());
        fitter.addObservedPoint(4.0254623,  531026.0);
        fitter.addObservedPoint(4.03128248, 984167.0);
        fitter.addObservedPoint(4.03839603, 1887233.0);
        fitter.addObservedPoint(4.04421621, 2687152.0);
        fitter.addObservedPoint(4.05132976, 3461228.0);
        fitter.addObservedPoint(4.05326982, 3580526.0);
        fitter.addObservedPoint(4.05779662, 3439750.0);
        fitter.addObservedPoint(4.0636168,  2877648.0);
        fitter.addObservedPoint(4.06943698, 2175960.0);
        fitter.addObservedPoint(4.07525716, 1447024.0);
        fitter.addObservedPoint(4.08237071, 717104.0);
        fitter.addObservedPoint(4.08366408, 620014.0);

        double[] parameters = fitter.fit();

        assertEquals(3588789.7, parameters[0], 1.0);
        assertEquals(4.05432, parameters[1], 1e-4);
        assertEquals(0.01524, parameters[2], 1e-4);
    }

    @Test
    public void testGaussianFitterFitJavadocDatasetWithInitialGuess() {
        GaussianFitter fitter = new GaussianFitter(new LevenbergMarquardtOptimizer());
        fitter.addObservedPoint(4.0254623,  531026.0);
        fitter.addObservedPoint(4.03128248, 984167.0);
        fitter.addObservedPoint(4.03839603, 1887233.0);
        fitter.addObservedPoint(4.04421621, 2687152.0);
        fitter.addObservedPoint(4.05132976, 3461228.0);
        fitter.addObservedPoint(4.05326982, 3580526.0);
        fitter.addObservedPoint(4.05779662, 3439750.0);
        fitter.addObservedPoint(4.0636168,  2877648.0);
        fitter.addObservedPoint(4.06943698, 2175960.0);
        fitter.addObservedPoint(4.07525716, 1447024.0);
        fitter.addObservedPoint(4.08237071, 717104.0);
        fitter.addObservedPoint(4.08366408, 620014.0);

        double[] initialGuess = new double[] { 3500000.0, 4.05, 0.02 };
        double[] parameters = fitter.fit(initialGuess);

        assertEquals(3588789.7, parameters[0], 1.0);
        assertEquals(4.05432, parameters[1], 1e-4);
        assertEquals(0.01524, parameters[2], 1e-4);
    }

    @Test
    public void testGaussianFitterFitSyntheticGaussianDataWithoutNoise() {
        final double expectedNorm = 4.0;
        final double expectedMean = 2.0;
        final double expectedSigma = 1.0;

        GaussianFitter fitter = new GaussianFitter(new LevenbergMarquardtOptimizer());
        for (double x = -1.0; x <= 5.0; x += 0.5) {
            double y = expectedNorm * Math.exp(-Math.pow(x - expectedMean, 2.0) / (2.0 * expectedSigma * expectedSigma));
            fitter.addObservedPoint(x, y);
        }

        double[] parameters = fitter.fit();

        assertEquals(expectedNorm, parameters[0], 1e-3);
        assertEquals(expectedMean, parameters[1], 1e-3);
        assertEquals(expectedSigma, parameters[2], 1e-3);
    }

    @Test
    public void testGaussianFitterFitUnsortedObservationsYieldsConsistentResult() {
        final double expectedNorm = 5.0;
        final double expectedMean = 1.0;
        final double expectedSigma = 0.8;

        GaussianFitter fitter = new GaussianFitter(new LevenbergMarquardtOptimizer());
        for (double x = 4.0; x >= -2.0; x -= 0.5) {
            double y = expectedNorm * Math.exp(-Math.pow(x - expectedMean, 2.0) / (2.0 * expectedSigma * expectedSigma));
            fitter.addObservedPoint(x, y);
        }

        double[] parameters = fitter.fit();

        assertEquals(expectedNorm, parameters[0], 1e-3);
        assertEquals(expectedMean, parameters[1], 1e-3);
        assertEquals(expectedSigma, parameters[2], 1e-3);
    }
}
