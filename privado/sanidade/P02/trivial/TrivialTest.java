import static org.junit.Assert.assertNotNull;
import org.junit.Test;
import org.apache.commons.math.optimization.fitting.GaussianFitter;
import org.apache.commons.math.optimization.general.LevenbergMarquardtOptimizer;

public class TrivialTest {
    @Test
    public void constroi() {
        assertNotNull(new GaussianFitter(new LevenbergMarquardtOptimizer()));
    }
}
