package com.softarum.svsa.util;

public class CronometroUtil {
	
    private final long nanoSecondsPerMillisecond = 1000000;
    private final long nanoSecondsPerSecond = 1000000000;
    private final long nanoSecondsPerMinute = 60000000000L;
    private final long nanoSecondsPerHour = 3600000000000L;

    private long stopWatchStartTime = 0;
    private long stopWatchStopTime = 0;
    private boolean stopWatchRunning = false;


    public void start() {
        this.stopWatchStartTime = System.nanoTime();
        this.stopWatchRunning = true;
    }


    public void stop() {
        this.stopWatchStopTime = System.nanoTime();
        this.stopWatchRunning = false;
    }


    public long getElapsedMilliseconds() {
        long elapsedTime;

        if (stopWatchRunning)
            elapsedTime = (System.nanoTime() - stopWatchStartTime);
        else
            elapsedTime = (stopWatchStopTime - stopWatchStartTime);

        return elapsedTime / nanoSecondsPerMillisecond;
    }


    public long getElapsedSeconds() {
        long elapsedTime;

        if (stopWatchRunning)
            elapsedTime = (System.nanoTime() - stopWatchStartTime);
        else
            elapsedTime = (stopWatchStopTime - stopWatchStartTime);

        return elapsedTime / nanoSecondsPerSecond;
    }


    public long getElapsedMinutes() {
        long elapsedTime;
        if (stopWatchRunning)
            elapsedTime = (System.nanoTime() - stopWatchStartTime);
        else
            elapsedTime = (stopWatchStopTime - stopWatchStartTime);

        return elapsedTime / nanoSecondsPerMinute;
    }


    public long getElapsedHours() {
        long elapsedTime;
        if (stopWatchRunning)
            elapsedTime = (System.nanoTime() - stopWatchStartTime);
        else
            elapsedTime = (stopWatchStopTime - stopWatchStartTime);

        return elapsedTime / nanoSecondsPerHour;
    }


}

/*
public class Stopwatch {

    public static void main(String[] args) {

        Stopwatch1 stopwatch1 = new Stopwatch1();
        stopwatch1.start();
        Fibonacci(45);
        stopwatch1.stop();


        System.out.println("Elapsed time in milliseconds: "
                + stopwatch1.getElapsedMilliseconds());

        System.out.println("Elapsed time in seconds: "
                + stopwatch1.getElapsedSeconds());

        System.out.println("Elapsed time in minutes: "
                + stopwatch1.getElapsedMinutes());

        System.out.println("Elapsed time in hours: "
                + stopwatch1.getElapsedHours());

    }

    private static BigInteger Fibonacci(int n) {
        if (n < 2)
            return BigInteger.ONE;
        else
            return Fibonacci(n - 1).add(Fibonacci(n - 2)); 
    }
}
    */
