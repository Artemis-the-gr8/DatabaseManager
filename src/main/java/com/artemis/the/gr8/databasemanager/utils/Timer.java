package com.artemis.the.gr8.databasemanager.utils;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public class Timer {

    private long startTime;

    private Timer() {
        startTime = System.currentTimeMillis();
    }

    @Contract(" -> new")
    public static @NotNull Timer start() {
        return new Timer();
    }

    public long reset() {
        long recordedTime = System.currentTimeMillis() - startTime;
        startTime = System.currentTimeMillis();

        return recordedTime;
    }
}