// inicio: 16:00  fim: 16:30
package org.apache.commons.math.optimization.univariate;
import org.apache.commons.math.analysis.UnivariateRealFunction;
import org.apache.commons.math.exception.MathIllegalStateException;
import org.apache.commons.math.random.JDKRandomGenerator;
import org.apache.commons.math.FunctionEvaluationException;
import org.apache.commons.math.optimization.GoalType;

import static org.junit.Assert.*;

import org.junit.Test;

public class F01Test {
//     @Test(expected = MathIllegalStateException.class)
//     public void getOptimizeThrows(){
//         JDKRandomGenerator generator = new JDKRandomGenerator();
//         generator.getSeed(42L);
//         MultiStartUnivariateRealOptimizer<UnivariateRealFunction> optimizer = 
//             new MultiStartUnivariateRealOptimizer<UnivariateRealFunction>(
//                 new BrentOptimizer(1e-10, 1e-14), 5, generator);
// 
//             optimizer.getOptima();
//     }

//     @Test
//     public void getOptimaAfterMinimizeIsSortedAscendingWithNullsLast() throws FunctionEvaluationException {
//         JDKRandomGenerator generator = new JKDRandomGenerator();
//         generator.setSeed(42L);
//         MultiStartUnivariateRealOptimizer<UnivariateRealFunction> optimizer = 
//             new MultiStartUnivariateRealOptimizer<UnivariateRealFunction>(
//                 new BrentOptimizer(1e-10, 1e-14), 5, generator);
//         optimizer.setMaxEvaluations(300);
// 
//         UnivariateRealPointValuePair best = optimizer.optimize(new UnivariateRealFunction() {
//             public double value(double x) {return Math.sin(x);}
//         }, GoalType.MINIMIZE, -20.0, 20.0);
//         UnivariateRealPointValuePair[] optima = optimizer.getOptima();
// 
//         assertEquals(5, optima.length);
//         assertEquals(best.getValue(), optima[0].getValue(), 0.0);
//         boolean nullSeen = false;
//         for (int i = 1; i < optima.length; i++){
//             if (optima[i] == null){
//                 nullSeen = true;
//             }else {
//                 assertFalse(nullSeen);
//                 assertTrue(optima[i - 1].getValue() <= optima[i].getValue());
//             }
//         }
//             
//     }

}
