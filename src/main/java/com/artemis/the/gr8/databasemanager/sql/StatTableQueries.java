package com.artemis.the.gr8.databasemanager.sql;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public abstract class StatTableQueries {

    public static final String TABLE_NAME = "statistics";
    public final String ID_COLUMN = "id";
    public final String NAME_COLUMN = "name";
    public final String TYPE_COLUMN = "type";

    public abstract @NotNull String createTable();

    public @NotNull String selectAll() {
        return "SELECT * FROM " + TABLE_NAME + ";";
    }

    public @NotNull String selectCount() {
        return "SELECT COUNT(*) FROM " + TABLE_NAME + ";";
    }

    @Contract(pure = true)
    public @NotNull String insert() {
        return "INSERT INTO " + TABLE_NAME + " (" +
                NAME_COLUMN + ", " +
                TYPE_COLUMN + ") VALUES (?, ?);";
    }
}
