package org.apache.commons.lang.time;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class F03Test {

    @Test
    public void testInitialStateGetTimeReturnsZero() {
        StopWatch watch = new StopWatch();
        assertEquals(0L, watch.getTime());
    }

    @Test
    public void testInitialStateToString() {
        StopWatch watch = new StopWatch();
        assertEquals(DurationFormatUtils.formatDurationHMS(0L), watch.toString());
    }

    @Test
    public void testStartWhenUnstartedChangesRunningTime() throws InterruptedException {
        StopWatch watch = new StopWatch();
        watch.start();
        Thread.sleep(20);
        assertTrue(watch.getTime() >= 15);
    }

    @Test(expected = IllegalStateException.class)
    public void testStartWhenAlreadyRunningThrowsIllegalStateException() {
        StopWatch watch = new StopWatch();
        watch.start();
        watch.start();
    }

    @Test(expected = IllegalStateException.class)
    public void testStartWhenStoppedWithoutResetThrowsIllegalStateException() {
        StopWatch watch = new StopWatch();
        watch.start();
        watch.stop();
        watch.start();
    }

    @Test(expected = IllegalStateException.class)
    public void testStopWhenUnstartedThrowsIllegalStateException() {
        StopWatch watch = new StopWatch();
        watch.stop();
    }

    @Test
    public void testStopWhenRunningFreezesTime() throws InterruptedException {
        StopWatch watch = new StopWatch();
        watch.start();
        Thread.sleep(20);
        watch.stop();
        long stoppedTime = watch.getTime();
        Thread.sleep(20);
        assertEquals(stoppedTime, watch.getTime());
    }

    @Test(expected = IllegalStateException.class)
    public void testStopWhenAlreadyStoppedThrowsIllegalStateException() {
        StopWatch watch = new StopWatch();
        watch.start();
        watch.stop();
        watch.stop();
    }

    @Test
    public void testStopWhenSuspendedTransitionsToStopped() {
        StopWatch watch = new StopWatch();
        watch.start();
        watch.suspend();
        watch.stop();
        assertTrue(watch.getTime() >= 0);
    }

    @Test
    public void testResetWhenUnstarted() {
        StopWatch watch = new StopWatch();
        watch.reset();
        assertEquals(0L, watch.getTime());
    }

    @Test
    public void testResetWhenRunningResetsState() throws InterruptedException {
        StopWatch watch = new StopWatch();
        watch.start();
        Thread.sleep(20);
        watch.reset();
        assertEquals(0L, watch.getTime());
    }

    @Test
    public void testResetWhenStoppedResetsState() {
        StopWatch watch = new StopWatch();
        watch.start();
        watch.stop();
        watch.reset();
        assertEquals(0L, watch.getTime());
    }

    @Test
    public void testResetAllowsRestartAfterStop() {
        StopWatch watch = new StopWatch();
        watch.start();
        watch.stop();
        watch.reset();
        watch.start();
        assertTrue(watch.getTime() >= 0);
    }

    @Test(expected = IllegalStateException.class)
    public void testSplitWhenUnstartedThrowsIllegalStateException() {
        StopWatch watch = new StopWatch();
        watch.split();
    }

    @Test(expected = IllegalStateException.class)
    public void testSplitWhenStoppedThrowsIllegalStateException() {
        StopWatch watch = new StopWatch();
        watch.start();
        watch.stop();
        watch.split();
    }

    @Test(expected = IllegalStateException.class)
    public void testSplitWhenSuspendedThrowsIllegalStateException() {
        StopWatch watch = new StopWatch();
        watch.start();
        watch.suspend();
        watch.split();
    }

    @Test
    public void testSplitRecordsSplitTime() throws InterruptedException {
        StopWatch watch = new StopWatch();
        watch.start();
        Thread.sleep(20);
        watch.split();
        long splitTime1 = watch.getSplitTime();
        Thread.sleep(20);
        assertEquals(splitTime1, watch.getSplitTime());
    }

    @Test
    public void testSplitDoesNotHaltWatchRunning() throws InterruptedException {
        StopWatch watch = new StopWatch();
        watch.start();
        Thread.sleep(20);
        watch.split();
        long splitTime = watch.getSplitTime();
        Thread.sleep(20);
        assertTrue(watch.getTime() >= splitTime);
    }

    @Test(expected = IllegalStateException.class)
    public void testGetSplitTimeWhenUnsplitThrowsIllegalStateException() {
        StopWatch watch = new StopWatch();
        watch.start();
        watch.getSplitTime();
    }

    @Test(expected = IllegalStateException.class)
    public void testUnsplitWhenNotSplitThrowsIllegalStateException() {
        StopWatch watch = new StopWatch();
        watch.start();
        watch.unsplit();
    }

    @Test(expected = IllegalStateException.class)
    public void testUnsplitClearsSplitState() {
        StopWatch watch = new StopWatch();
        watch.start();
        watch.split();
        watch.unsplit();
        watch.getSplitTime();
    }

    @Test(expected = IllegalStateException.class)
    public void testToSplitStringWhenNotSplitThrowsIllegalStateException() {
        StopWatch watch = new StopWatch();
        watch.start();
        watch.toSplitString();
    }

    @Test
    public void testToSplitStringReturnsFormattedTime() throws InterruptedException {
        StopWatch watch = new StopWatch();
        watch.start();
        Thread.sleep(20);
        watch.split();
        assertEquals(DurationFormatUtils.formatDurationHMS(watch.getSplitTime()), watch.toSplitString());
    }

    @Test(expected = IllegalStateException.class)
    public void testSuspendWhenUnstartedThrowsIllegalStateException() {
        StopWatch watch = new StopWatch();
        watch.suspend();
    }

    @Test(expected = IllegalStateException.class)
    public void testSuspendWhenStoppedThrowsIllegalStateException() {
        StopWatch watch = new StopWatch();
        watch.start();
        watch.stop();
        watch.suspend();
    }

    @Test(expected = IllegalStateException.class)
    public void testSuspendWhenAlreadySuspendedThrowsIllegalStateException() {
        StopWatch watch = new StopWatch();
        watch.start();
        watch.suspend();
        watch.suspend();
    }

    @Test
    public void testSuspendFreezesTime() throws InterruptedException {
        StopWatch watch = new StopWatch();
        watch.start();
        Thread.sleep(20);
        watch.suspend();
        long suspendedTime = watch.getTime();
        Thread.sleep(20);
        assertEquals(suspendedTime, watch.getTime());
    }

    @Test(expected = IllegalStateException.class)
    public void testResumeWhenUnstartedThrowsIllegalStateException() {
        StopWatch watch = new StopWatch();
        watch.resume();
    }

    @Test(expected = IllegalStateException.class)
    public void testResumeWhenRunningThrowsIllegalStateException() {
        StopWatch watch = new StopWatch();
        watch.start();
        watch.resume();
    }

    @Test(expected = IllegalStateException.class)
    public void testResumeWhenStoppedThrowsIllegalStateException() {
        StopWatch watch = new StopWatch();
        watch.start();
        watch.stop();
        watch.resume();
    }

    @Test
    public void testResumeExcludesSuspendedDurationFromTotalTime() throws InterruptedException {
        StopWatch watch = new StopWatch();
        watch.start();
        Thread.sleep(30);
        watch.suspend();
        long timeAtSuspend = watch.getTime();
        Thread.sleep(60);
        watch.resume();
        long timeImmediatelyAfterResume = watch.getTime();
        assertTrue(timeImmediatelyAfterResume >= timeAtSuspend);
        assertTrue(timeImmediatelyAfterResume < timeAtSuspend + 50);
    }

    @Test
    public void testToStringWhenRunningReturnsNonNullString() {
        StopWatch watch = new StopWatch();
        watch.start();
        String output = watch.toString();
        assertNotNull(output);
        assertFalse(output.isEmpty());
    }

    @Test
    public void testToStringWhenStoppedMatchesFormat() {
        StopWatch watch = new StopWatch();
        watch.start();
        watch.stop();
        assertEquals(DurationFormatUtils.formatDurationHMS(watch.getTime()), watch.toString());
    }
}