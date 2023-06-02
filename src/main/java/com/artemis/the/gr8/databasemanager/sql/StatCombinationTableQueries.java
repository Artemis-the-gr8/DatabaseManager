package com.artemis.the.gr8.databasemanager.sql;

import org.jetbrains.annotations.NotNull;

public abstract class StatCombinationTableQueries {

    public static final String TABLE_NAME = "stat_combinations";
    public final String ID_COLUMN = "id";
    public final String STAT_ID_COLUMN = "stat_id";
    public final String SUB_STAT_ID_COLUMN = "sub_stat_id";

    public abstract @NotNull String createTable();

    public @NotNull String insert() {
        return "INSERT INTO " + TABLE_NAME + " (" +
                STAT_ID_COLUMN + ", " +
                SUB_STAT_ID_COLUMN + ") VALUES (?, ?);";
    }

    public @NotNull String selectAll() {
        return "SELECT * FROM " + TABLE_NAME + ";";
    }

    public @NotNull String selectCount() {
        return "SELECT COUNT(*) FROM " + TABLE_NAME + ";";
    }
}
