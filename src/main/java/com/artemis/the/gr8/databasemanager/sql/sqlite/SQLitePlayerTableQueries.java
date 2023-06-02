package com.artemis.the.gr8.databasemanager.sql.sqlite;

import com.artemis.the.gr8.databasemanager.sql.PlayerTableQueries;
import org.jetbrains.annotations.NotNull;

public class SQLitePlayerTableQueries extends PlayerTableQueries {

    @Override
    public @NotNull String createTable() {
        return "CREATE TABLE IF NOT EXISTS " + TABLE_NAME +
                " (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                NAME_COLUMN + " VARCHAR(16)," +
                UUID_COLUMN + " VARCHAR(255)," +
                IS_EXCLUDED_COLUMN + " BOOLEAN DEFAULT 0," +
                "CONSTRAINT unique_uuid UNIQUE (" + UUID_COLUMN + ")" +
                ");";
    }
}
