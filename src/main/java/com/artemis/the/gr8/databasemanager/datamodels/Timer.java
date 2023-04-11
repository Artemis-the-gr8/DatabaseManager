package com.artemis.the.gr8.databasemanager.datamodels;

public class Timer {

    private long startTime;

    public void startTimer() {
        startTime = System.currentTimeMillis();
    }

    public long stopTimer() {
        return System.currentTimeMillis() - startTime;
    }
}
