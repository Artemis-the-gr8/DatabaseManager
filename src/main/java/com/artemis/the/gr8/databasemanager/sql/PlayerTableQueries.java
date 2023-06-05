package com.artemis.the.gr8.databasemanager.sql;

import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public abstract class PlayerTableQueries {

    public static final String TABLE_NAME = "players";
    public final String ID_COLUMN = "id";
    public final String NAME_COLUMN = "name";
    public final String UUID_COLUMN = "uuid";
    public final String IS_EXCLUDED_COLUMN = "is_excluded";

    public abstract @NotNull String createTable();

    public @NotNull String selectIdFromUUID(@NotNull UUID uuid) {
        return "SELECT " + ID_COLUMN + " FROM " + TABLE_NAME +
                " WHERE " + UUID_COLUMN + " = '" + uuid + "';";
    }

    public @NotNull String selectPlayerFromUUID(@NotNull UUID uuid) {
        return "SELECT * FROM " + TABLE_NAME +
                " WHERE " + UUID_COLUMN + " = '" + uuid + "';";
    }

    public @NotNull String selectCount() {
        return "SELECT COUNT(*) FROM " + TABLE_NAME + ";";
    }

    public @NotNull String selectAll() {
        return "SELECT * FROM " + TABLE_NAME + ";";
    }

    public @NotNull String insert() {
        return "INSERT INTO " + TABLE_NAME + " (" +
                NAME_COLUMN + ", " +
                UUID_COLUMN + ", " +
                IS_EXCLUDED_COLUMN + ") VALUES (?, ?, ?);";
    }

    public @NotNull String updateWhereMatchingUUID() {
        return "UPDATE " + TABLE_NAME + " SET " +
                NAME_COLUMN + " = ?, " +
                IS_EXCLUDED_COLUMN + " = ?" +
                " WHERE " + UUID_COLUMN + " = ?;";
    }
}