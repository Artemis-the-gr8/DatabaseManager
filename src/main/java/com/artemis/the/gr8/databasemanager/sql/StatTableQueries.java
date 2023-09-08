package com.artemis.the.gr8.databasemanager.sql;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public abstract class StatTableQueries {

    public static final String TABLE_NAME = "statistics";
    public static final String ID_COLUMN = "id";
    public static final String NAME_COLUMN = "name";
    public static final String TYPE_COLUMN = "type";

    public static final String ID_COLUMN_WITH_TABLE_NAME = TABLE_NAME + "." + ID_COLUMN;
    public static final String NAME_COLUMN_WITH_TABLE_NAME = TABLE_NAME + "." + NAME_COLUMN;
    public static final String TYPE_COLUMN_WITH_TABLE_NAME = TABLE_NAME + "." + TYPE_COLUMN;

    public abstract @NotNull String createTable();

    public @NotNull String selectIdFromName(String statName) {
        return "SELECT " + ID_COLUMN + " FROM " + TABLE_NAME +
                " WHERE " + NAME_COLUMN + " = '" + statName + "';";
    }

    public @NotNull String selectCount() {
        return "SELECT COUNT(*) FROM " + TABLE_NAME + ";";
    }

    public @NotNull String selectAll() {
        return "SELECT * FROM " + TABLE_NAME + ";";
    }

    @Contract(pure = true)
    public @NotNull String insert() {
        return "INSERT INTO " + TABLE_NAME + " (" +
                NAME_COLUMN + ", " +
                TYPE_COLUMN + ") VALUES (?, ?);";
    }
}
