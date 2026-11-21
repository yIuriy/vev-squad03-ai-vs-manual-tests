package org.apache.commons.math.optimization.univariate;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.math.FunctionEvaluationException;
import org.apache.commons.math.analysis.UnivariateRealFunction;
import org.apache.commons.math.exception.ConvergenceException;
import org.apache.commons.math.exception.MathIllegalStateException;
import org.apache.commons.math.optimization.ConvergenceChecker;
import org.apache.commons.math.optimization.GoalType;
import org.apache.commons.math.random.RandomGenerator;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.fail;

public class F01Test {

    @Test(expected = MathIllegalStateException.class)
    public void testGetOptimaBeforeOptimizeThrowsException() {
        MockOptimizer underlying = new MockOptimizer();
        StubRandomGenerator rng = new StubRandomGenerator();
        MultiStartUnivariateRealOptimizer<UnivariateRealFunction> optimizer =
                new MultiStartUnivariateRealOptimizer<>(underlying, 3, rng);

        optimizer.getOptima();
    }

    @Test
    public void testSetAndGetConvergenceCheckerDelegatesToOptimizer() {
        MockOptimizer underlying = new MockOptimizer();
        StubRandomGenerator rng = new StubRandomGenerator();
        MultiStartUnivariateRealOptimizer<UnivariateRealFunction> optimizer =
                new MultiStartUnivariateRealOptimizer<>(underlying, 3, rng);

        ConvergenceChecker<UnivariateRealPointValuePair> checker =
                new ConvergenceChecker<UnivariateRealPointValuePair>() {
                    public boolean converged(int iteration, UnivariateRealPointValuePair previous, UnivariateRealPointValuePair current) {
                        return true;
                    }
                };

        optimizer.setConvergenceChecker(checker);

        assertSame(checker, optimizer.getConvergenceChecker());
        assertSame(checker, underlying.getConvergenceChecker());
    }

    @Test
    public void testSetAndGetMaxEvaluationsDelegatesToOptimizer() {
        MockOptimizer underlying = new MockOptimizer();
        StubRandomGenerator rng = new StubRandomGenerator();
        MultiStartUnivariateRealOptimizer<UnivariateRealFunction> optimizer =
                new MultiStartUnivariateRealOptimizer<>(underlying, 3, rng);

        optimizer.setMaxEvaluations(150);

        assertEquals(150, optimizer.getMaxEvaluations());
        assertEquals(150, underlying.getMaxEvaluations());
    }

    @Test
    public void testGetEvaluationsInitiallyReturnsZero() {
        MockOptimizer underlying = new MockOptimizer();
        StubRandomGenerator rng = new StubRandomGenerator();
        MultiStartUnivariateRealOptimizer<UnivariateRealFunction> optimizer =
                new MultiStartUnivariateRealOptimizer<>(underlying, 3, rng);

        assertEquals(0, optimizer.getEvaluations());
    }

    @Test
    public void testOptimizeMinimizeReturnsOptimumWithMinimumValue() throws Exception {
        MockOptimizer underlying = new MockOptimizer();
        underlying.addResponse(new UnivariateRealPointValuePair(1.0, 10.0));
        underlying.addResponse(new UnivariateRealPointValuePair(2.0, 2.0));
        underlying.addResponse(new UnivariateRealPointValuePair(3.0, 5.0));

        StubRandomGenerator rng = new StubRandomGenerator(0.1, 0.2, 0.3, 0.4);
        MultiStartUnivariateRealOptimizer<UnivariateRealFunction> optimizer =
                new MultiStartUnivariateRealOptimizer<>(underlying, 3, rng);

        UnivariateRealPointValuePair best =
                optimizer.optimize(new DummyFunction(), GoalType.MINIMIZE, 0.0, 10.0, 0.0);

        assertEquals(2.0, best.getPoint(), 1e-9);
        assertEquals(2.0, best.getValue(), 1e-9);
    }

    @Test
    public void testOptimizeMaximizeReturnsOptimumWithMaximumValue() throws Exception {
        MockOptimizer underlying = new MockOptimizer();
        underlying.addResponse(new UnivariateRealPointValuePair(1.0, 10.0));
        underlying.addResponse(new UnivariateRealPointValuePair(2.0, 50.0));
        underlying.addResponse(new UnivariateRealPointValuePair(3.0, 20.0));

        StubRandomGenerator rng = new StubRandomGenerator(0.1, 0.2, 0.3, 0.4);
        MultiStartUnivariateRealOptimizer<UnivariateRealFunction> optimizer =
                new MultiStartUnivariateRealOptimizer<>(underlying, 3, rng);

        UnivariateRealPointValuePair best =
                optimizer.optimize(new DummyFunction(), GoalType.MAXIMIZE, 0.0, 10.0, 0.0);

        assertEquals(2.0, best.getPoint(), 1e-9);
        assertEquals(50.0, best.getValue(), 1e-9);
    }

    @Test
    public void testGetOptimaSortedAscendingForMinimization() throws Exception {
        MockOptimizer underlying = new MockOptimizer();
        underlying.addResponse(new UnivariateRealPointValuePair(1.0, 10.0));
        underlying.addResponse(new UnivariateRealPointValuePair(2.0, 2.0));
        underlying.addResponse(new UnivariateRealPointValuePair(3.0, 5.0));

        StubRandomGenerator rng = new StubRandomGenerator(0.1, 0.2, 0.3, 0.4);
        MultiStartUnivariateRealOptimizer<UnivariateRealFunction> optimizer =
                new MultiStartUnivariateRealOptimizer<>(underlying, 3, rng);

        optimizer.optimize(new DummyFunction(), GoalType.MINIMIZE, 0.0, 10.0);
        UnivariateRealPointValuePair[] optima = optimizer.getOptima();

        assertEquals(3, optima.length);
        assertEquals(2.0, optima[0].getValue(), 1e-9);
        assertEquals(5.0, optima[1].getValue(), 1e-9);
        assertEquals(10.0, optima[2].getValue(), 1e-9);
    }

    @Test
    public void testGetOptimaSortedDescendingForMaximization() throws Exception {
        MockOptimizer underlying = new MockOptimizer();
        underlying.addResponse(new UnivariateRealPointValuePair(1.0, 10.0));
        underlying.addResponse(new UnivariateRealPointValuePair(2.0, 50.0));
        underlying.addResponse(new UnivariateRealPointValuePair(3.0, 20.0));

        StubRandomGenerator rng = new StubRandomGenerator(0.1, 0.2, 0.3, 0.4);
        MultiStartUnivariateRealOptimizer<UnivariateRealFunction> optimizer =
                new MultiStartUnivariateRealOptimizer<>(underlying, 3, rng);

        optimizer.optimize(new DummyFunction(), GoalType.MAXIMIZE, 0.0, 10.0);
        UnivariateRealPointValuePair[] optima = optimizer.getOptima();

        assertEquals(3, optima.length);
        assertEquals(50.0, optima[0].getValue(), 1e-9);
        assertEquals(20.0, optima[1].getValue(), 1e-9);
        assertEquals(10.0, optima[2].getValue(), 1e-9);
    }

    @Test
    public void testGetOptimaReturnsClonedArrayToPreventExternalMutation() throws Exception {
        MockOptimizer underlying = new MockOptimizer();
        underlying.addResponse(new UnivariateRealPointValuePair(1.0, 5.0));

        StubRandomGenerator rng = new StubRandomGenerator();
        MultiStartUnivariateRealOptimizer<UnivariateRealFunction> optimizer =
                new MultiStartUnivariateRealOptimizer<>(underlying, 1, rng);

        optimizer.optimize(new DummyFunction(), GoalType.MINIMIZE, 0.0, 1.0);
        UnivariateRealPointValuePair[] firstCall = optimizer.getOptima();
        firstCall[0] = null;

        UnivariateRealPointValuePair[] secondCall = optimizer.getOptima();
        assertNotNull(secondCall[0]);
    }

    @Test
    public void testFourArgumentOptimizeDelegatesToFiveArgumentOptimize() throws Exception {
        MockOptimizer underlying = new MockOptimizer();
        underlying.addResponse(new UnivariateRealPointValuePair(3.0, 7.0));

        StubRandomGenerator rng = new StubRandomGenerator();
        MultiStartUnivariateRealOptimizer<UnivariateRealFunction> optimizer =
                new MultiStartUnivariateRealOptimizer<>(underlying, 1, rng);

        UnivariateRealPointValuePair result =
                optimizer.optimize(new DummyFunction(), GoalType.MINIMIZE, 1.0, 5.0);

        assertNotNull(result);
        assertEquals(7.0, result.getValue(), 1e-9);
    }

    @Test
    public void testTotalEvaluationsAccumulatedAcrossStarts() throws Exception {
        MockOptimizer underlying = new MockOptimizer();
        underlying.setEvaluationsToConsume(4);
        underlying.addResponse(new UnivariateRealPointValuePair(1.0, 1.0));
        underlying.addResponse(new UnivariateRealPointValuePair(2.0, 2.0));
        underlying.addResponse(new UnivariateRealPointValuePair(3.0, 3.0));

        StubRandomGenerator rng = new StubRandomGenerator(0.1, 0.2, 0.3, 0.4);
        MultiStartUnivariateRealOptimizer<UnivariateRealFunction> optimizer =
                new MultiStartUnivariateRealOptimizer<>(underlying, 3, rng);

        optimizer.optimize(new DummyFunction(), GoalType.MINIMIZE, 0.0, 10.0);

        assertEquals(12, optimizer.getEvaluations());
    }

    @Test
    public void testOptimizerMaxEvaluationsDecrementedAcrossStarts() throws Exception {
        MockOptimizer underlying = new MockOptimizer();
        underlying.setEvaluationsToConsume(10);
        underlying.addResponse(new UnivariateRealPointValuePair(1.0, 1.0));
        underlying.addResponse(new UnivariateRealPointValuePair(2.0, 2.0));

        StubRandomGenerator rng = new StubRandomGenerator(0.1, 0.2);
        MultiStartUnivariateRealOptimizer<UnivariateRealFunction> optimizer =
                new MultiStartUnivariateRealOptimizer<>(underlying, 2, rng);

        optimizer.setMaxEvaluations(100);
        optimizer.optimize(new DummyFunction(), GoalType.MINIMIZE, 0.0, 10.0);

        assertEquals(80, underlying.getMaxEvaluations());
    }

    @Test
    public void testFirstStartUsesExactMinAndMax() throws Exception {
        MockOptimizer underlying = new MockOptimizer();
        underlying.addResponse(new UnivariateRealPointValuePair(0.0, 1.0));
        underlying.addResponse(new UnivariateRealPointValuePair(0.0, 2.0));

        StubRandomGenerator rng = new StubRandomGenerator(0.5, 0.5);
        MultiStartUnivariateRealOptimizer<UnivariateRealFunction> optimizer =
                new MultiStartUnivariateRealOptimizer<>(underlying, 2, rng);

        optimizer.optimize(new DummyFunction(), GoalType.MINIMIZE, -5.0, 15.0);

        assertEquals(-5.0, underlying.getObservedMinBounds().get(0), 1e-9);
        assertEquals(15.0, underlying.getObservedMaxBounds().get(0), 1e-9);
    }

    @Test
    public void testSubsequentStartsUseGeneratorBoundedMinAndMax() throws Exception {
        MockOptimizer underlying = new MockOptimizer();
        underlying.addResponse(new UnivariateRealPointValuePair(0.0, 1.0));
        underlying.addResponse(new UnivariateRealPointValuePair(0.0, 2.0));

        StubRandomGenerator rng = new StubRandomGenerator(0.8, 0.2);
        MultiStartUnivariateRealOptimizer<UnivariateRealFunction> optimizer =
                new MultiStartUnivariateRealOptimizer<>(underlying, 2, rng);

        optimizer.optimize(new DummyFunction(), GoalType.MINIMIZE, 10.0, 20.0);

        assertEquals(12.0, underlying.getObservedMinBounds().get(1), 1e-9);
        assertEquals(18.0, underlying.getObservedMaxBounds().get(1), 1e-9);
    }

    @Test
    public void testSingleStartOptimizationExecutesOnlyOnce() throws Exception {
        MockOptimizer underlying = new MockOptimizer();
        underlying.addResponse(new UnivariateRealPointValuePair(4.0, 16.0));

        StubRandomGenerator rng = new StubRandomGenerator();
        MultiStartUnivariateRealOptimizer<UnivariateRealFunction> optimizer =
                new MultiStartUnivariateRealOptimizer<>(underlying, 1, rng);

        UnivariateRealPointValuePair result =
                optimizer.optimize(new DummyFunction(), GoalType.MINIMIZE, 2.0, 6.0);

        assertEquals(1, optimizer.getOptima().length);
        assertEquals(4.0, result.getPoint(), 1e-9);
        assertEquals(1, underlying.getObservedMinBounds().size());
    }

    @Test
    public void testConvergenceExceptionInStartPlacesNullAtEnd() throws Exception {
        MockOptimizer underlying = new MockOptimizer();
        underlying.addResponse(new ConvergenceException());
        underlying.addResponse(new UnivariateRealPointValuePair(1.0, 10.0));
        underlying.addResponse(new UnivariateRealPointValuePair(2.0, 4.0));

        StubRandomGenerator rng = new StubRandomGenerator(0.1, 0.2, 0.3, 0.4);
        MultiStartUnivariateRealOptimizer<UnivariateRealFunction> optimizer =
                new MultiStartUnivariateRealOptimizer<>(underlying, 3, rng);

        UnivariateRealPointValuePair best =
                optimizer.optimize(new DummyFunction(), GoalType.MINIMIZE, 0.0, 10.0);

        assertEquals(4.0, best.getValue(), 1e-9);
        UnivariateRealPointValuePair[] optima = optimizer.getOptima();
        assertEquals(3, optima.length);
        assertEquals(4.0, optima[0].getValue(), 1e-9);
        assertEquals(10.0, optima[1].getValue(), 1e-9);
        assertNull(optima[2]);
    }

    @Test
    public void testFunctionEvaluationExceptionInStartPlacesNullAtEnd() throws Exception {
        MockOptimizer underlying = new MockOptimizer();
        underlying.addResponse(new UnivariateRealPointValuePair(1.0, 8.0));
        underlying.addResponse(new FunctionEvaluationException(0.0));
        underlying.addResponse(new UnivariateRealPointValuePair(2.0, 3.0));

        StubRandomGenerator rng = new StubRandomGenerator(0.1, 0.2, 0.3, 0.4);
        MultiStartUnivariateRealOptimizer<UnivariateRealFunction> optimizer =
                new MultiStartUnivariateRealOptimizer<>(underlying, 3, rng);

        UnivariateRealPointValuePair best =
                optimizer.optimize(new DummyFunction(), GoalType.MINIMIZE, 0.0, 10.0);

        assertEquals(3.0, best.getValue(), 1e-9);
        UnivariateRealPointValuePair[] optima = optimizer.getOptima();
        assertEquals(3, optima.length);
        assertEquals(3.0, optima[0].getValue(), 1e-9);
        assertEquals(8.0, optima[1].getValue(), 1e-9);
        assertNull(optima[2]);
    }

    @Test(expected = ConvergenceException.class)
    public void testAllStartsThrowConvergenceExceptionThrowsConvergenceException() throws Exception {
        MockOptimizer underlying = new MockOptimizer();
        underlying.addResponse(new ConvergenceException());
        underlying.addResponse(new ConvergenceException());

        StubRandomGenerator rng = new StubRandomGenerator(0.1, 0.2);
        MultiStartUnivariateRealOptimizer<UnivariateRealFunction> optimizer =
                new MultiStartUnivariateRealOptimizer<>(underlying, 2, rng);

        optimizer.optimize(new DummyFunction(), GoalType.MINIMIZE, 0.0, 10.0);
    }

    @Test(expected = ConvergenceException.class)
    public void testAllStartsThrowFunctionEvaluationExceptionThrowsConvergenceException() throws Exception {
        MockOptimizer underlying = new MockOptimizer();
        underlying.addResponse(new FunctionEvaluationException(0.0));
        underlying.addResponse(new FunctionEvaluationException(0.0));

        StubRandomGenerator rng = new StubRandomGenerator(0.1, 0.2);
        MultiStartUnivariateRealOptimizer<UnivariateRealFunction> optimizer =
                new MultiStartUnivariateRealOptimizer<>(underlying, 2, rng);

        optimizer.optimize(new DummyFunction(), GoalType.MINIMIZE, 0.0, 10.0);
    }

    @Test
    public void testOptimaArrayContainsAllNullsWhenOptimizationThrowsConvergenceException() {
        MockOptimizer underlying = new MockOptimizer();
        underlying.addResponse(new ConvergenceException());
        underlying.addResponse(new ConvergenceException());

        StubRandomGenerator rng = new StubRandomGenerator(0.1, 0.2);
        MultiStartUnivariateRealOptimizer<UnivariateRealFunction> optimizer =
                new MultiStartUnivariateRealOptimizer<>(underlying, 2, rng);

        try {
            optimizer.optimize(new DummyFunction(), GoalType.MINIMIZE, 0.0, 10.0);
            fail("Expected ConvergenceException to be thrown");
        } catch (ConvergenceException ce) {
            // Expected
        } catch (Exception e) {
            fail("Unexpected exception: " + e);
        }

        UnivariateRealPointValuePair[] optima = optimizer.getOptima();
        assertEquals(2, optima.length);
        assertNull(optima[0]);
        assertNull(optima[1]);
    }

    @Test
    public void testSortPairsWithIdenticalValuesPreservesBoth() throws Exception {
        MockOptimizer underlying = new MockOptimizer();
        underlying.addResponse(new UnivariateRealPointValuePair(1.0, 5.0));
        underlying.addResponse(new UnivariateRealPointValuePair(2.0, 5.0));

        StubRandomGenerator rng = new StubRandomGenerator(0.1, 0.2);
        MultiStartUnivariateRealOptimizer<UnivariateRealFunction> optimizer =
                new MultiStartUnivariateRealOptimizer<>(underlying, 2, rng);

        optimizer.optimize(new DummyFunction(), GoalType.MINIMIZE, 0.0, 10.0);
        UnivariateRealPointValuePair[] optima = optimizer.getOptima();

        assertEquals(2, optima.length);
        assertNotNull(optima[0]);
        assertNotNull(optima[1]);
        assertEquals(5.0, optima[0].getValue(), 1e-9);
        assertEquals(5.0, optima[1].getValue(), 1e-9);
    }

    private static class DummyFunction implements UnivariateRealFunction {
        public double value(double x) {
            return x;
        }
    }

    private static class StubRandomGenerator implements RandomGenerator {
        private final double[] values;
        private int index = 0;

        public StubRandomGenerator(double... values) {
            this.values = values;
        }

        public double nextDouble() {
            if (values == null || values.length == 0) {
                return 0.5;
            }
            double val = values[index % values.length];
            index++;
            return val;
        }

        public void setSeed(int seed) {}
        public void setSeed(int[] seed) {}
        public void setSeed(long seed) {}
        public void nextBytes(byte[] bytes) {}
        public int nextInt() { return 0; }
        public int nextInt(int n) { return 0; }
        public long nextLong() { return 0L; }
        public boolean nextBoolean() { return false; }
        public float nextFloat() { return 0.0f; }
        public double nextGaussian() { return 0.0; }
    }

    private static class MockOptimizer implements BaseUnivariateRealOptimizer<UnivariateRealFunction> {
        private ConvergenceChecker<UnivariateRealPointValuePair> checker;
        private int maxEvaluations;
        private int evaluationsToConsume = 1;
        private final List<Object> responses = new ArrayList<>();
        private int responseIndex = 0;
        private final List<Double> observedMinBounds = new ArrayList<>();
        private final List<Double> observedMaxBounds = new ArrayList<>();

        public void addResponse(Object response) {
            responses.add(response);
        }

        public void setEvaluationsToConsume(int evals) {
            this.evaluationsToConsume = evals;
        }

        public List<Double> getObservedMinBounds() {
            return observedMinBounds;
        }

        public List<Double> getObservedMaxBounds() {
            return observedMaxBounds;
        }

        public void setConvergenceChecker(ConvergenceChecker<UnivariateRealPointValuePair> checker) {
            this.checker = checker;
        }

        public ConvergenceChecker<UnivariateRealPointValuePair> getConvergenceChecker() {
            return checker;
        }

        public int getMaxEvaluations() {
            return maxEvaluations;
        }

        public int getEvaluations() {
            return evaluationsToConsume;
        }

        public void setMaxEvaluations(int maxEvaluations) {
            this.maxEvaluations = maxEvaluations;
        }

        public UnivariateRealPointValuePair optimize(UnivariateRealFunction f, GoalType goalType, double min, double max)
                throws FunctionEvaluationException {
            observedMinBounds.add(min);
            observedMaxBounds.add(max);

            if (responseIndex < responses.size()) {
                Object resp = responses.get(responseIndex++);
                if (resp instanceof ConvergenceException) {
                    throw (ConvergenceException) resp;
                }
                if (resp instanceof FunctionEvaluationException) {
                    throw (FunctionEvaluationException) resp;
                }
                if (resp instanceof UnivariateRealPointValuePair) {
                    return (UnivariateRealPointValuePair) resp;
                }
            }
            return new UnivariateRealPointValuePair((min + max) / 2.0, 0.0);
        }

        public UnivariateRealPointValuePair optimize(UnivariateRealFunction f, GoalType goalType, double min, double max, double startValue)
                throws FunctionEvaluationException {
            return optimize(f, goalType, min, max);
        }
    }
}