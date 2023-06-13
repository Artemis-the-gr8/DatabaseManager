package com.artemis.the.gr8.databasemanager.sql;

import org.jetbrains.annotations.NotNull;

public abstract class StatValueTableQueries {

    public static final String TABLE_NAME = "stat_values";
    public final String PLAYER_ID_COLUMN = "player_id";
    public final String STAT_COMBINATION_ID_COLUMN = "stat_combination_id";
    public final String VALUE_COLUMN = "value";

    public abstract @NotNull String createTable();

    public @NotNull String insert(int playerId) {
        return "INSERT INTO " + TABLE_NAME + " (" +
                PLAYER_ID_COLUMN + ", " +
                STAT_COMBINATION_ID_COLUMN + ", " +
                VALUE_COLUMN + ") VALUES (" + playerId + ", ?, ?);";
    }

    public @NotNull String selectAllEntryIds(int playerId) {
        return "SELECT " + STAT_COMBINATION_ID_COLUMN + " FROM " + TABLE_NAME +
                " WHERE " + PLAYER_ID_COLUMN + " = " + playerId + ";";
    }

    public @NotNull String updateForPlayer(int playerId) {
        return "UPDATE " + TABLE_NAME +
                " SET " + VALUE_COLUMN + " = ?" +
                " WHERE " + PLAYER_ID_COLUMN + " = " + playerId +
                " AND " + STAT_COMBINATION_ID_COLUMN + " = ?;";
    }
}