package org.apache.commons.math3.optimization.univariate;

import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.exception.TooManyEvaluationsException;
import org.apache.commons.math3.optimization.ConvergenceChecker;
import org.apache.commons.math3.optimization.GoalType;
import org.apache.commons.math3.util.FastMath;
import org.junit.Assert;
import org.junit.Test;

/**
 * Unit test suite for {@link BrentOptimizer}.
 */
public class F04Test {

//     @Test(expected = NumberIsTooSmallException.class)
//     public void testConstructorThrowsWhenRelativeToleranceIsStrictlyLessThanMinimum() {
//         double belowMinRelativeTolerance = FastMath.nextDown(2 * FastMath.ulp(1.0));
//         new BrentOptimizer(belowMinRelativeTolerance, 1e-8);
//     }

    @Test
    public void testConstructorAcceptsRelativeToleranceAtExactMinimumBoundary() {
        double minRelativeTolerance = 2 * FastMath.ulp(1.0);
        BrentOptimizer optimizer = new BrentOptimizer(minRelativeTolerance, 1e-8);
        Assert.assertNotNull(optimizer);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testConstructorThrowsWhenAbsoluteToleranceIsZero() {
        new BrentOptimizer(1e-8, 0.0);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testConstructorThrowsWhenAbsoluteToleranceIsNegative() {
        new BrentOptimizer(1e-8, -1e-6);
    }

    @Test
    public void testConstructorWithCheckerRetainsCheckerInstance() {
        ConvergenceChecker<UnivariatePointValuePair> customChecker =
            new ConvergenceChecker<UnivariatePointValuePair>() {
                @Override
                public boolean converged(int iteration,
                                         UnivariatePointValuePair previous,
                                         UnivariatePointValuePair current) {
                    return false;
                }
            };

        BrentOptimizer optimizer = new BrentOptimizer(1e-8, 1e-8, customChecker);
        Assert.assertSame(customChecker, optimizer.getConvergenceChecker());
    }

    @Test
    public void testOptimizeFindsParabolaMinimumCorrectly() {
        BrentOptimizer optimizer = new BrentOptimizer(1e-10, 1e-14);
        UnivariateFunction parabola = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return (x - 2.5) * (x - 2.5) + 3.0;
            }
        };

        UnivariatePointValuePair result = optimizer.optimize(100, parabola, GoalType.MINIMIZE, 0.0, 5.0, 1.0);

        Assert.assertEquals(2.5, result.getPoint(), 1e-6);
        Assert.assertEquals(3.0, result.getValue(), 1e-6);
    }

    @Test
    public void testOptimizeFindsParabolaMaximumCorrectly() {
        BrentOptimizer optimizer = new BrentOptimizer(1e-10, 1e-14);
        UnivariateFunction invertedParabola = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return -(x + 1.25) * (x + 1.25) + 7.5;
            }
        };

        UnivariatePointValuePair result = optimizer.optimize(100, invertedParabola, GoalType.MAXIMIZE, -5.0, 2.0, 0.0);

        Assert.assertEquals(-1.25, result.getPoint(), 1e-6);
        Assert.assertEquals(7.5, result.getValue(), 1e-6);
    }

    @Test
    public void testOptimizeHandlesReversedIntervalEndpoints() {
        BrentOptimizer optimizer = new BrentOptimizer(1e-10, 1e-14);
        UnivariateFunction parabola = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return (x - 4.0) * (x - 4.0);
            }
        };

        UnivariatePointValuePair result = optimizer.optimize(100, parabola, GoalType.MINIMIZE, 10.0, 0.0, 2.0);

        Assert.assertEquals(4.0, result.getPoint(), 1e-6);
        Assert.assertEquals(0.0, result.getValue(), 1e-6);
    }

    @Test
    public void testOptimizeWithStartValueAtLowerBoundary() {
        BrentOptimizer optimizer = new BrentOptimizer(1e-10, 1e-14);
        UnivariateFunction parabola = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return (x - 3.0) * (x - 3.0);
            }
        };

        UnivariatePointValuePair result = optimizer.optimize(100, parabola, GoalType.MINIMIZE, 1.0, 5.0, 1.0);

        Assert.assertEquals(3.0, result.getPoint(), 1e-6);
        Assert.assertEquals(0.0, result.getValue(), 1e-6);
    }

    @Test
    public void testOptimizeWithStartValueAtUpperBoundary() {
        BrentOptimizer optimizer = new BrentOptimizer(1e-10, 1e-14);
        UnivariateFunction parabola = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return (x - 3.0) * (x - 3.0);
            }
        };

        UnivariatePointValuePair result = optimizer.optimize(100, parabola, GoalType.MINIMIZE, 1.0, 5.0, 5.0);

        Assert.assertEquals(3.0, result.getPoint(), 1e-6);
        Assert.assertEquals(0.0, result.getValue(), 1e-6);
    }

    @Test
    public void testOptimizeWithoutExplicitStartValueUsesDefault() {
        BrentOptimizer optimizer = new BrentOptimizer(1e-10, 1e-14);
        UnivariateFunction parabola = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return (x - 2.0) * (x - 2.0) + 1.0;
            }
        };

        UnivariatePointValuePair result = optimizer.optimize(100, parabola, GoalType.MINIMIZE, 0.0, 6.0);

        Assert.assertEquals(2.0, result.getPoint(), 1e-6);
        Assert.assertEquals(1.0, result.getValue(), 1e-6);
    }

    @Test
    public void testOptimizeConstantFunctionTerminatesNormally() {
        BrentOptimizer optimizer = new BrentOptimizer(1e-8, 1e-10);
        final double constantValue = 42.0;
        UnivariateFunction constantFunc = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return constantValue;
            }
        };

        UnivariatePointValuePair result = optimizer.optimize(50, constantFunc, GoalType.MINIMIZE, -2.0, 2.0, 0.0);

        Assert.assertEquals(constantValue, result.getValue(), 1e-12);
        Assert.assertTrue(result.getPoint() >= -2.0 && result.getPoint() <= 2.0);
    }

    @Test
    public void testOptimizeStopsImmediatelyWhenInitialIntervalIsWithinTolerance() {
        double absTol = 1e-2;
        double relTol = 1e-4;
        BrentOptimizer optimizer = new BrentOptimizer(relTol, absTol);
        UnivariateFunction func = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return x * x;
            }
        };

        double min = 1.0;
        double max = 1.0 + 1e-5;
        double start = 1.0 + 0.5e-5;
        UnivariatePointValuePair result = optimizer.optimize(10, func, GoalType.MINIMIZE, min, max, start);

        Assert.assertEquals(start, result.getPoint(), 1e-9);
        Assert.assertEquals(func.value(start), result.getValue(), 1e-9);
    }

    @Test
    public void testOptimizeTracksEvaluationsCountAccurately() {
        BrentOptimizer optimizer = new BrentOptimizer(1e-10, 1e-14);
        UnivariateFunction parabola = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return x * x;
            }
        };

        optimizer.optimize(100, parabola, GoalType.MINIMIZE, -1.0, 1.0, 0.5);

        Assert.assertTrue(optimizer.getEvaluations() > 0);
        Assert.assertTrue(optimizer.getEvaluations() <= 100);
    }

    @Test(expected = TooManyEvaluationsException.class)
    public void testOptimizeThrowsTooManyEvaluationsExceptionWhenLimitIsExceeded() {
        BrentOptimizer optimizer = new BrentOptimizer(1e-12, 1e-14);
        UnivariateFunction parabola = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return (x - 1.5) * (x - 1.5);
            }
        };

        optimizer.optimize(2, parabola, GoalType.MINIMIZE, -10.0, 10.0, 8.0);
    }

    @Test
    public void testCheckerEarlyStopSelectsBestPointWhenLatestEvaluationIsWorseInMinimization() {
        ConvergenceChecker<UnivariatePointValuePair> earlyStopChecker =
            new ConvergenceChecker<UnivariatePointValuePair>() {
                @Override
                public boolean converged(int iteration,
                                         UnivariatePointValuePair previous,
                                         UnivariatePointValuePair current) {
                    return iteration >= 0;
                }
            };

        BrentOptimizer optimizer = new BrentOptimizer(1e-8, 1e-8, earlyStopChecker);
        UnivariateFunction parabola = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return (x - 2.0) * (x - 2.0);
            }
        };

        UnivariatePointValuePair result = optimizer.optimize(50, parabola, GoalType.MINIMIZE, 0.0, 4.0, 2.0);

        Assert.assertEquals(2.0, result.getPoint(), 1e-12);
        Assert.assertEquals(0.0, result.getValue(), 1e-12);
    }

    @Test
    public void testCheckerEarlyStopSelectsBestPointWhenLatestEvaluationIsWorseInMaximization() {
        ConvergenceChecker<UnivariatePointValuePair> earlyStopChecker =
            new ConvergenceChecker<UnivariatePointValuePair>() {
                @Override
                public boolean converged(int iteration,
                                         UnivariatePointValuePair previous,
                                         UnivariatePointValuePair current) {
                    return iteration >= 0;
                }
            };

        BrentOptimizer optimizer = new BrentOptimizer(1e-8, 1e-8, earlyStopChecker);
        UnivariateFunction invertedParabola = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return -(x - 3.0) * (x - 3.0) + 10.0;
            }
        };

        UnivariatePointValuePair result = optimizer.optimize(50, invertedParabola, GoalType.MAXIMIZE, 1.0, 5.0, 3.0);

        Assert.assertEquals(3.0, result.getPoint(), 1e-12);
        Assert.assertEquals(10.0, result.getValue(), 1e-12);
    }

    @Test
    public void testOptimizeTrigonometricFunctionFindsLocalMinimum() {
        BrentOptimizer optimizer = new BrentOptimizer(1e-10, 1e-14);
        UnivariateFunction cosFunc = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return FastMath.cos(x);
            }
        };

        UnivariatePointValuePair result = optimizer.optimize(100, cosFunc, GoalType.MINIMIZE, 1.0, 5.0, 2.0);

        Assert.assertEquals(FastMath.PI, result.getPoint(), 1e-6);
        Assert.assertEquals(-1.0, result.getValue(), 1e-6);
    }
}