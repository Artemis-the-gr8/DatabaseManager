package com.artemis.the.gr8.databasemanager.datamodels;

public class Timer {

    private long startTime;

    public void startTimer() {
        startTime = System.currentTimeMillis();
    }

    public long stopTimer() {
        long time = System.currentTimeMillis() - startTime;
        startTime = System.currentTimeMillis();
        return time;
    }

    public void stopTimerAndPrintTime() {
        System.out.println("Time taken: " + (System.currentTimeMillis() - startTime));
    }

    public void restartTimerAndPrintTime() {
        System.out.println("Time taken: " + (System.currentTimeMillis() - startTime));
        startTime = System.currentTimeMillis();
    }
}
