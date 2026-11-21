/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.commons.math.stat.clustering;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import org.apache.commons.math.exception.ConvergenceException;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * Unit test suite for {@link KMeansPlusPlusClusterer}.
 */
public class F06Test {

    /**
     * Test implementation of {@link Clusterable} representing a 2D point.
     */
    private static class TestPoint implements Clusterable<TestPoint> {
        private final double x;
        private final double y;

        public TestPoint(final double x, final double y) {
            this.x = x;
            this.y = y;
        }

        @Override
        public double distanceFrom(final TestPoint other) {
            final double dx = this.x - other.x;
            final double dy = this.y - other.y;
            return Math.sqrt(dx * dx + dy * dy);
        }

        @Override
        public TestPoint centroidOf(final Collection<TestPoint> points) {
            if (points == null || points.isEmpty()) {
                return new TestPoint(0.0, 0.0);
            }
            double sumX = 0.0;
            double sumY = 0.0;
            for (final TestPoint p : points) {
                sumX += p.x;
                sumY += p.y;
            }
            return new TestPoint(sumX / points.size(), sumY / points.size());
        }

        @Override
        public boolean equals(final Object obj) {
            if (this == obj) {
                return true;
            }
            if (!(obj instanceof TestPoint)) {
                return false;
            }
            final TestPoint other = (TestPoint) obj;
            return Double.doubleToLongBits(this.x) == Double.doubleToLongBits(other.x) &&
                   Double.doubleToLongBits(this.y) == Double.doubleToLongBits(other.y);
        }

        @Override
        public int hashCode() {
            int result = Double.hashCode(x);
            result = 31 * result + Double.hashCode(y);
            return result;
        }

        @Override
        public String toString() {
            return "(" + x + ", " + y + ")";
        }
    }

    /**
     * Controlled {@link Random} implementation for deterministic branching in tests.
     */
    private static class ControlledRandom extends Random {
        private static final long serialVersionUID = 1L;
        private final int[] intSequence;
        private final double[] doubleSequence;
        private int intIndex = 0;
        private int doubleIndex = 0;

        public ControlledRandom(final int[] intSequence, final double[] doubleSequence) {
            this.intSequence = intSequence;
            this.doubleSequence = doubleSequence;
        }

        @Override
        public int nextInt(final int bound) {
            if (intSequence != null && intIndex < intSequence.length) {
                return intSequence[intIndex++];
            }
            return 0;
        }

        @Override
        public double nextDouble() {
            if (doubleSequence != null && doubleIndex < doubleSequence.length) {
                return doubleSequence[doubleIndex++];
            }
            return 0.0;
        }
    }

    @Test
    public void testConstructorWithDefaultStrategySetsLargestVariance() {
        final List<TestPoint> points = Arrays.asList(
            new TestPoint(0.0, 0.0),
            new TestPoint(0.0, 0.0),
            new TestPoint(20.0, 0.0),
            new TestPoint(100.0, 100.0),
            new TestPoint(100.0, 100.0)
        );
        final ControlledRandom random = new ControlledRandom(new int[]{0, 2}, new double[]{0.99, 0.0});
        final KMeansPlusPlusClusterer<TestPoint> clusterer = new KMeansPlusPlusClusterer<TestPoint>(random);

        final List<Cluster<TestPoint>> clusters = clusterer.cluster(points, 3, 10);

        assertEquals(3, clusters.size());
        int totalPoints = 0;
        for (final Cluster<TestPoint> cluster : clusters) {
            totalPoints += cluster.getPoints().size();
        }
        assertEquals(points.size(), totalPoints);
    }

    @Test
    public void testConstructorWithCustomStrategyCreatesInstance() {
        final KMeansPlusPlusClusterer<TestPoint> clusterer =
            new KMeansPlusPlusClusterer<TestPoint>(new Random(42L), KMeansPlusPlusClusterer.EmptyClusterStrategy.ERROR);

        assertNotNull(clusterer);
    }

    @Test
    public void testClusterSinglePointSingleCluster() {
        final List<TestPoint> points = Collections.singletonList(new TestPoint(5.0, 5.0));
        final KMeansPlusPlusClusterer<TestPoint> clusterer =
            new KMeansPlusPlusClusterer<TestPoint>(new Random(42L));

        final List<Cluster<TestPoint>> clusters = clusterer.cluster(points, 1, 10);

        assertEquals(1, clusters.size());
        final Cluster<TestPoint> cluster = clusters.get(0);
        assertEquals(1, cluster.getPoints().size());
        assertEquals(new TestPoint(5.0, 5.0), cluster.getCenter());
        assertEquals(new TestPoint(5.0, 5.0), cluster.getPoints().get(0));
    }

    @Test
    public void testClusterMultiplePointsSingleClusterCentroid() {
        final List<TestPoint> points = Arrays.asList(
            new TestPoint(0.0, 0.0),
            new TestPoint(2.0, 0.0),
            new TestPoint(0.0, 2.0),
            new TestPoint(2.0, 2.0)
        );
        final KMeansPlusPlusClusterer<TestPoint> clusterer =
            new KMeansPlusPlusClusterer<TestPoint>(new Random(42L));

        final List<Cluster<TestPoint>> clusters = clusterer.cluster(points, 1, 10);

        assertEquals(1, clusters.size());
        final Cluster<TestPoint> cluster = clusters.get(0);
        assertEquals(4, cluster.getPoints().size());
        assertEquals(new TestPoint(1.0, 1.0), cluster.getCenter());
    }

    @Test
    public void testClusterTwoDistinctClustersPartition() {
        final TestPoint p1 = new TestPoint(0.0, 0.0);
        final TestPoint p2 = new TestPoint(1.0, 0.0);
        final TestPoint p3 = new TestPoint(0.0, 1.0);
        final TestPoint p4 = new TestPoint(100.0, 100.0);
        final TestPoint p5 = new TestPoint(101.0, 100.0);
        final TestPoint p6 = new TestPoint(100.0, 101.0);
        final List<TestPoint> points = Arrays.asList(p1, p2, p3, p4, p5, p6);

        final KMeansPlusPlusClusterer<TestPoint> clusterer =
            new KMeansPlusPlusClusterer<TestPoint>(new Random(42L));

        final List<Cluster<TestPoint>> clusters = clusterer.cluster(points, 2, 20);

        assertEquals(2, clusters.size());
        final Cluster<TestPoint> c1 = clusters.get(0);
        final Cluster<TestPoint> c2 = clusters.get(1);
        assertEquals(3, c1.getPoints().size());
        assertEquals(3, c2.getPoints().size());

        if (c1.getPoints().contains(p1)) {
            assertTrue(c1.getPoints().contains(p2));
            assertTrue(c1.getPoints().contains(p3));
            assertTrue(c2.getPoints().contains(p4));
            assertTrue(c2.getPoints().contains(p5));
            assertTrue(c2.getPoints().contains(p6));
        } else {
            assertTrue(c2.getPoints().contains(p1));
            assertTrue(c2.getPoints().contains(p2));
            assertTrue(c2.getPoints().contains(p3));
            assertTrue(c1.getPoints().contains(p4));
            assertTrue(c1.getPoints().contains(p5));
            assertTrue(c1.getPoints().contains(p6));
        }
    }

    @Test
    public void testClusterWithKEqualsPointsCount() {
        final List<TestPoint> points = Arrays.asList(
            new TestPoint(0.0, 0.0),
            new TestPoint(10.0, 10.0),
            new TestPoint(20.0, 20.0)
        );
        final KMeansPlusPlusClusterer<TestPoint> clusterer =
            new KMeansPlusPlusClusterer<TestPoint>(new Random(42L));

        final List<Cluster<TestPoint>> clusters = clusterer.cluster(points, 3, 10);

        assertEquals(3, clusters.size());
        for (final Cluster<TestPoint> cluster : clusters) {
            assertEquals(1, cluster.getPoints().size());
        }
    }

    @Test
    public void testClusterNegativeMaxIterationsTreatedAsUnlimited() {
        final List<TestPoint> points = Arrays.asList(
            new TestPoint(0.0, 0.0),
            new TestPoint(1.0, 1.0),
            new TestPoint(100.0, 100.0),
            new TestPoint(101.0, 101.0)
        );
        final KMeansPlusPlusClusterer<TestPoint> clusterer =
            new KMeansPlusPlusClusterer<TestPoint>(new Random(42L));

        final List<Cluster<TestPoint>> clusters = clusterer.cluster(points, 2, -1);

        assertEquals(2, clusters.size());
        int totalPoints = 0;
        for (final Cluster<TestPoint> cluster : clusters) {
            totalPoints += cluster.getPoints().size();
        }
        assertEquals(points.size(), totalPoints);
    }

    @Test
    public void testClusterMaxIterationsZeroReturnsInitialAssignment() {
        final List<TestPoint> points = Arrays.asList(
            new TestPoint(0.0, 0.0),
            new TestPoint(10.0, 10.0)
        );
        final KMeansPlusPlusClusterer<TestPoint> clusterer =
            new KMeansPlusPlusClusterer<TestPoint>(new Random(42L));

        final List<Cluster<TestPoint>> clusters = clusterer.cluster(points, 2, 0);

        assertEquals(2, clusters.size());
        int totalPoints = 0;
        for (final Cluster<TestPoint> cluster : clusters) {
            totalPoints += cluster.getPoints().size();
        }
        assertEquals(points.size(), totalPoints);
    }

    @Test
    public void testClusterMaxIterationsOnePerformsSingleIteration() {
        final List<TestPoint> points = Arrays.asList(
            new TestPoint(0.0, 0.0),
            new TestPoint(2.0, 0.0),
            new TestPoint(10.0, 0.0),
            new TestPoint(12.0, 0.0)
        );
        final KMeansPlusPlusClusterer<TestPoint> clusterer =
            new KMeansPlusPlusClusterer<TestPoint>(new Random(42L));

        final List<Cluster<TestPoint>> clusters = clusterer.cluster(points, 2, 1);

        assertEquals(2, clusters.size());
        int totalPoints = 0;
        for (final Cluster<TestPoint> cluster : clusters) {
            totalPoints += cluster.getPoints().size();
        }
        assertEquals(points.size(), totalPoints);
    }

    @Test
    public void testClusterPreservesAllPointsWithoutLoss() {
        final TestPoint p1 = new TestPoint(10.0, 10.0);
        final TestPoint p2 = new TestPoint(20.0, 20.0);
        final TestPoint p3 = new TestPoint(100.0, 100.0);
        final TestPoint p4 = new TestPoint(110.0, 110.0);
        final List<TestPoint> points = Arrays.asList(p1, p2, p3, p4);

        final KMeansPlusPlusClusterer<TestPoint> clusterer =
            new KMeansPlusPlusClusterer<TestPoint>(new Random(42L));

        final List<Cluster<TestPoint>> clusters = clusterer.cluster(points, 2, 10);

        final List<TestPoint> collectedPoints = new ArrayList<TestPoint>();
        for (final Cluster<TestPoint> cluster : clusters) {
            collectedPoints.addAll(cluster.getPoints());
        }

        assertEquals(points.size(), collectedPoints.size());
        assertTrue(collectedPoints.contains(p1));
        assertTrue(collectedPoints.contains(p2));
        assertTrue(collectedPoints.contains(p3));
        assertTrue(collectedPoints.contains(p4));
    }

    @Test
    public void testClusterCentersAreNotNull() {
        final List<TestPoint> points = Arrays.asList(
            new TestPoint(1.0, 2.0),
            new TestPoint(3.0, 4.0),
            new TestPoint(5.0, 6.0)
        );
        final KMeansPlusPlusClusterer<TestPoint> clusterer =
            new KMeansPlusPlusClusterer<TestPoint>(new Random(42L));

        final List<Cluster<TestPoint>> clusters = clusterer.cluster(points, 2, 10);

        assertEquals(2, clusters.size());
        for (final Cluster<TestPoint> cluster : clusters) {
            assertNotNull(cluster.getCenter());
        }
    }

    @Test
    public void testClusterDuplicatePointsStayTogether() {
        final List<TestPoint> points = Arrays.asList(
            new TestPoint(5.0, 5.0),
            new TestPoint(5.0, 5.0),
            new TestPoint(5.0, 5.0)
        );
        final KMeansPlusPlusClusterer<TestPoint> clusterer =
            new KMeansPlusPlusClusterer<TestPoint>(new Random(42L));

        final List<Cluster<TestPoint>> clusters = clusterer.cluster(points, 1, 10);

        assertEquals(1, clusters.size());
        final Cluster<TestPoint> cluster = clusters.get(0);
        assertEquals(3, cluster.getPoints().size());
        assertEquals(new TestPoint(5.0, 5.0), cluster.getCenter());
    }

    @Test(expected = ConvergenceException.class)
    public void testEmptyClusterStrategyErrorThrowsException() {
        final List<TestPoint> points = Arrays.asList(
            new TestPoint(0.0, 0.0),
            new TestPoint(0.0, 0.0),
            new TestPoint(20.0, 0.0),
            new TestPoint(100.0, 100.0),
            new TestPoint(100.0, 100.0)
        );
        final ControlledRandom random = new ControlledRandom(new int[]{0}, new double[]{0.99, 0.0});
        final KMeansPlusPlusClusterer<TestPoint> clusterer =
            new KMeansPlusPlusClusterer<TestPoint>(random, KMeansPlusPlusClusterer.EmptyClusterStrategy.ERROR);

        clusterer.cluster(points, 3, 10);
    }

    @Test
    public void testEmptyClusterStrategyFarthestPointResolvesEmptyCluster() {
        final List<TestPoint> points = Arrays.asList(
            new TestPoint(0.0, 0.0),
            new TestPoint(0.0, 0.0),
            new TestPoint(20.0, 0.0),
            new TestPoint(100.0, 100.0),
            new TestPoint(100.0, 100.0)
        );
        final ControlledRandom random = new ControlledRandom(new int[]{0}, new double[]{0.99, 0.0});
        final KMeansPlusPlusClusterer<TestPoint> clusterer =
            new KMeansPlusPlusClusterer<TestPoint>(random, KMeansPlusPlusClusterer.EmptyClusterStrategy.FARTHEST_POINT);

        final List<Cluster<TestPoint>> clusters = clusterer.cluster(points, 3, 10);

        assertEquals(3, clusters.size());
        int totalPoints = 0;
        for (final Cluster<TestPoint> cluster : clusters) {
            assertFalse(cluster.getPoints().isEmpty());
            totalPoints += cluster.getPoints().size();
        }
        assertEquals(points.size(), totalPoints);
    }

    @Test
    public void testEmptyClusterStrategyLargestPointsNumberResolvesEmptyCluster() {
        final List<TestPoint> points = Arrays.asList(
            new TestPoint(0.0, 0.0),
            new TestPoint(0.0, 0.0),
            new TestPoint(20.0, 0.0),
            new TestPoint(100.0, 100.0),
            new TestPoint(100.0, 100.0)
        );
        final ControlledRandom random = new ControlledRandom(new int[]{0, 2}, new double[]{0.99, 0.0});
        final KMeansPlusPlusClusterer<TestPoint> clusterer =
            new KMeansPlusPlusClusterer<TestPoint>(random, KMeansPlusPlusClusterer.EmptyClusterStrategy.LARGEST_POINTS_NUMBER);

        final List<Cluster<TestPoint>> clusters = clusterer.cluster(points, 3, 10);

        assertEquals(3, clusters.size());
        int totalPoints = 0;
        for (final Cluster<TestPoint> cluster : clusters) {
            assertFalse(cluster.getPoints().isEmpty());
            totalPoints += cluster.getPoints().size();
        }
        assertEquals(points.size(), totalPoints);
    }

    @Test
    public void testEmptyClusterStrategyLargestVarianceResolvesEmptyCluster() {
        final List<TestPoint> points = Arrays.asList(
            new TestPoint(0.0, 0.0),
            new TestPoint(0.0, 0.0),
            new TestPoint(20.0, 0.0),
            new TestPoint(100.0, 100.0),
            new TestPoint(100.0, 100.0)
        );
        final ControlledRandom random = new ControlledRandom(new int[]{0, 2}, new double[]{0.99, 0.0});
        final KMeansPlusPlusClusterer<TestPoint> clusterer =
            new KMeansPlusPlusClusterer<TestPoint>(random, KMeansPlusPlusClusterer.EmptyClusterStrategy.LARGEST_VARIANCE);

        final List<Cluster<TestPoint>> clusters = clusterer.cluster(points, 3, 10);

        assertEquals(3, clusters.size());
        int totalPoints = 0;
        for (final Cluster<TestPoint> cluster : clusters) {
            assertFalse(cluster.getPoints().isEmpty());
            totalPoints += cluster.getPoints().size();
        }
        assertEquals(points.size(), totalPoints);
    }

    @Test
    public void testClusterModerateCoordinateValues() {
        final List<TestPoint> points = Arrays.asList(
            new TestPoint(1000.0, 1000.0),
            new TestPoint(1002.0, 1000.0),
            new TestPoint(-1000.0, -1000.0),
            new TestPoint(-1002.0, -1000.0)
        );
        final KMeansPlusPlusClusterer<TestPoint> clusterer =
            new KMeansPlusPlusClusterer<TestPoint>(new Random(42L));

        final List<Cluster<TestPoint>> clusters = clusterer.cluster(points, 2, 10);

        assertEquals(2, clusters.size());
        for (final Cluster<TestPoint> cluster : clusters) {
            assertEquals(2, cluster.getPoints().size());
        }
    }
}