// inicio: 16:00  fim: 16:30
package org.apache.commons.math.stat.clustering;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import org.junit.Test;

public class F06Test {

    KMeansPlusPlusClusterer<EuclideanIntegerPoint> agrupador = new KMeansPlusPlusClusterer<>(new Random(25));

    @Test
    public void testeUmCluster() {
        List<EuclideanIntegerPoint> pontos = Arrays.asList(
                new EuclideanIntegerPoint(new int[] { 1, 1 }),
                new EuclideanIntegerPoint(new int[] { 2, 2 }));

        assertEquals(1, agrupador.cluster(pontos, 1, 10).size());
    }

    @Test
    public void testeUmPontoPorCluster() {
        List<EuclideanIntegerPoint> pontos = Arrays.asList(
                new EuclideanIntegerPoint(new int[] { 1, 1 }),
                new EuclideanIntegerPoint(new int[] { 10, 10 }));

        List<Cluster<EuclideanIntegerPoint>> resultado = agrupador.cluster(pontos, 2, 10);

        assertEquals(1, resultado.get(0).getPoints().size());
        assertEquals(1, resultado.get(1).getPoints().size());
    }

    @Test
    public void testePontosAgrupados() {
        List<EuclideanIntegerPoint> pontos = Arrays.asList(
                new EuclideanIntegerPoint(new int[] { 1, 1 }),
                new EuclideanIntegerPoint(new int[] { 10, 10 }));

        assertEquals(2, agrupador.cluster(pontos, 1, 10).get(0).getPoints().size());
    }

    @Test
    public void testePontosNegativos() {
        List<EuclideanIntegerPoint> pontos = Arrays.asList(
                new EuclideanIntegerPoint(new int[] { -1, -1 }),
                new EuclideanIntegerPoint(new int[] { -5, -5 }));

        assertEquals(2, agrupador.cluster(pontos, 2, 10).size());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testeListaVazia() {
        List<EuclideanIntegerPoint> pontos = new ArrayList<>();

        agrupador.cluster(pontos, 1, 10);
    }

}
