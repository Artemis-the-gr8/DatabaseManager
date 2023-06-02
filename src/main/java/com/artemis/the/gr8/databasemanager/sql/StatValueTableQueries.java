package com.artemis.the.gr8.databasemanager.sql;

import org.jetbrains.annotations.NotNull;

public abstract class StatValueTableQueries {

    public static final String TABLE_NAME = "stat_values";
    public final String PLAYER_ID_COLUMN = "player_id";
    public final String STAT_COMBINATION_ID_COLUMN = "stat_combination_id";
    public final String VALUE_COLUMN = "value";

    public abstract @NotNull String createTable();

}
