package com.artemis.the.gr8.databasemanager.sql.sqlite;

import com.artemis.the.gr8.databasemanager.sql.StatTableQueries;
import org.jetbrains.annotations.NotNull;

public class SQLiteStatTableQueries extends StatTableQueries {

    @Override
    public @NotNull String createTable() {
        return "CREATE TABLE IF NOT EXISTS " + TABLE_NAME +
                "( " +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                NAME_COLUMN + " VARCHAR(255)," +
                TYPE_COLUMN + " VARCHAR(255) NOT NULL," +
                "CONSTRAINT unique_stat_name UNIQUE (" + NAME_COLUMN + ")," +
                "CONSTRAINT allowed_stat_types CHECK (type IN ('CUSTOM', 'ENTITY', 'BLOCK', 'ITEM'))" +
                ");";
    }
}
