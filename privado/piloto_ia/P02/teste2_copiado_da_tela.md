package org.apache.commons.math.optimization.fitting;
import org.junit.Test;
import static org.junit.Assert.*;
import org.apache.commons.math.exception.NullArgumentException;
import org.apache.commons.math.exception.NumberIsTooSmallException;
public class GaussianFitterTest {
Code

```
@Test(expected = NullArgumentException.class)
public void testParameterGuesserConstructorThrowsExceptionOnNullInput() {
    new GaussianFitter.ParameterGuesser(null);
}

@Test(expected = NumberIsTooSmallException.class)
public void testParameterGuesserConstructorThrowsExceptionOnInsufficientPoints() {
    WeightedObservedPoint[] points = new WeightedObservedPoint[] {
        new WeightedObservedPoint(1.0, 0.0, 1.0),
        new WeightedObservedPoint(1.0, 1.0, 2.0)
    };
    new GaussianFitter.ParameterGuesser(points);
}

@Test
public void testParameterGuesserGuessCalculatesCorrectParameters() {
    WeightedObservedPoint[] points = new WeightedObservedPoint[] {
        new WeightedObservedPoint(1.0, -1.0, 0.0),
        new WeightedObservedPoint(1.0, 0.0, 10.0),
        new WeightedObservedPoint(1.0, 1.0, 0.0)
    };
    
    GaussianFitter.ParameterGuesser guesser = new GaussianFitter.ParameterGuesser(points);
    double[] parameters = guesser.guess();
    
    // Asserting expected behavior based on the implementation of basicGuess
    // Norm is the max Y
    assertEquals(10.0, parameters[0], 1e-10);
    // Mean is the X at max Y
    assertEquals(0.0, parameters[1], 1e-10);
    // Sigma: With range fallback (points[2].x - points[0].x = 2.0)
    // fwhmApprox = 2.0
    // sigma = 2.0 / (2.0 * sqrt(2 * ln(2)))
    double expectedSigma = 2.0 / (2.0 * Math.sqrt(2.0 * Math.log(2.0)));
    assertEquals(expectedSigma, parameters[2], 1e-10);
}

@Test
public void testParameterGuesserGuessIsIdempotent() {
    WeightedObservedPoint[] points = new WeightedObservedPoint[] {
        new WeightedObservedPoint(1.0, -1.0, 0.0),
        new WeightedObservedPoint(1.0, 0.0, 10.0),
        new WeightedObservedPoint(1.0, 1.0, 0.0)
    };
    
    GaussianFitter.ParameterGuesser guesser = new GaussianFitter.ParameterGuesser(points);
    double[] firstGuess = guesser.guess();
    double[] secondGuess = guesser.guess();
    
    assertArrayEquals(firstGuess, secondGuess, 1e-10);
    assertNotSame(firstGuess, secondGuess);
}

@Test
public void testParameterGuesserHandlesUnsortedPoints() {
    // Unsorted input, should be sorted internally by ParameterGuesser
    WeightedObservedPoint[] points = new WeightedObservedPoint[] {
        new WeightedObservedPoint(1.0, 1.0, 0.0),
        new WeightedObservedPoint(1.0, -1.0, 0.0),
        new WeightedObservedPoint(1.0, 0.0, 10.0)
    };
    
    GaussianFitter.ParameterGuesser guesser = new GaussianFitter.ParameterGuesser(points);
    double[] parameters = guesser.guess();
    
    assertEquals(10.0, parameters[0], 1e-10);
    assertEquals(0.0, parameters[1], 1e-10);
}
```

}
