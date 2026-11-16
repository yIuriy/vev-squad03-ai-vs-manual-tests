// inicio: 15:25  fim: 15:55
package org.apache.commons.math.geometry;

import static org.junit.Assert.*;

import java.beans.Transient;
import java.util.Vector;

import org.junit.Test;

public class F05Test {

    @Test
    public void testeCoordenadas() {
        Vector3D v = new Vector3D(1, 2, 3);

        assertEquals(1.0, v.getX(), 0.0001);
        assertEquals(2.0, v.getY(), 0.0001);
        assertEquals(3.0, v.getZ(), 0.0001);

    }

    @Test
    public void testeSoma() {
        Vector3D v1 = new Vector3D(1, 2, 3);
        Vector3D v2 = new Vector3D(2, 3, 4);

        Vector3D resultado = v1.add(v2);

        assertEquals(3.0, resultado.getX(), 0.0001);
        assertEquals(5.0, resultado.getY(), 0.0001);
        assertEquals(7.0, resultado.getZ(), 0.0001);
    }

    @Test
    public void testeSomaNegativos() {
        Vector3D v1 = Vector3D(-1, -2, -3);
        Vector3D v2 = Vector3D(2, 3, 4);

        Vector3D resultado = v1.add(v2);

        assertEquals(1.0, resultado.getX(), 0.0001);
        assertEquals(1.0, resultado.getY(), 0.0001);
        assertEquals(1.0, resultado.getZ(), 0.0001);
    }

    @Test
    public void testeSubtracao() {
        Vector3D v1 = new Vector3D(5, 6, 7);
        Vector3D v2 = new Vector3D(1, 2, 3);

        Vector3D resultado = v1.subtract(v2);

        assertEquals(4.0, resultado.getX(), 0.0001);
        assertEquals(4.0, resultado.getY(), 0.0001);
        assertEquals(4.0, resultado.getZ(), 0.0001);
    }

    @Test
    public void testeMultiplicacao() {
        Vector3D v = new Vector3D(1, 2, 3);

        Vector3D resultado = v.scalarMultiply(2);

        assertEquals(2.0, resultado.getX(), 0.0001);
        assertEquals(4.0, resultado.getY(), 0.0001);
        assertEquals(6.0, resultado.getZ(), 0.0001);
    }

    @Test
    public void testeMultiplicacaoPorZero() {
        Vector3D v = new Vector3D(1, 2, 3);

        Vector3D resultado = v.scalarMultiply(0);

        assertEquals(0.0, resultado.getX(), 0.0001);
        assertEquals(0.0, resultado.getY(), 0.0001);
        assertEquals(0.0, resultado.getZ(), 0.0001);
    }

    @Test
    public void testeNorma() {
        Vector3D v = new Vector3D(3, 4, 0);

        assertEquals(5.0, v.getNorm(), 0.0001);
    }

    @Test
    public void testeVetorZero() {
        Vector3D v = new Vector3D(0, 0, 0);

        assertEquals(0.0, v.getNorm(), 0.0001);
    }

    @Test
    public void testeNormalizacao() {
        Vector3D v = new Vector3D(3, 4, 0);

        Vector3D resultado = v.normalize();

        assertEquals(1.0, resultado.getNorm(), 0.0001);
        assertEquals(0.6, resultado.getX(), 0.0001);
        assertEquals(0.8, resultado.getY(), 0.0001);
    }

    @Test(expected = ArithmeticException.class)
    public void testeNormalizacaoZero() {
        Vector3D v = new Vector3D(0, 0, 0);

        v.normalize();
    }

    @Test
    public void testeDistancia() {
        Vector3D v1 = new Vector3D(0, 0, 0);
        Vector3D v2 = new Vector3D(3, 4, 0);

        double resultado = Vector3D.distance(v1, v2);

        assertEquals(5.0, resultado, 0.0001);
    }

    @Test // testes do Not a Number
    public void testeNaN() {
        Vector3D v = new Vector3D(Double.NaN, 2, 3);

        assertTrue(v.isNaN());
    }

}
