// inicio: 16:15  fim: 16:45
package org.apache.commons.math3.optimization.univariate;

import static org.junit.Assert.*;

import java.beans.Transient;

import org.junit.Test;
import org.junit.Before;

public class F04Test {
    private BrentOptimizer obj;

    @Before                               
    public void prepara() {
        obj = new BrentOptimizer(3, 4);
    }

    // não consegui entender como os métodos das classes funcionam, as variáveis estão com os nomes muito simplificados
}
