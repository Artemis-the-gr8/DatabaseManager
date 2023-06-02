package com.artemis.the.gr8.databasemanager.sql.mysql;

import com.artemis.the.gr8.databasemanager.sql.SubStatTableQueries;
import org.jetbrains.annotations.NotNull;

public class MySQLSubStatTableQueries extends SubStatTableQueries {

    @Override
    public @NotNull String createTable() {
        return "CREATE TABLE IF NOT EXISTS " + TABLE_NAME +
                "( " +
                "id INT AUTO_INCREMENT PRIMARY KEY," +
                NAME_COLUMN + " VARCHAR(255)," +
                TYPE_COLUMN + " VARCHAR(255) NOT NULL," +
                "CONSTRAINT unique_sub_stat UNIQUE (" +
                NAME_COLUMN + ", " +
                TYPE_COLUMN +
                ")," +
                "CONSTRAINT allowed_sub_stat_types CHECK (type IN ('ENTITY', 'BLOCK', 'ITEM'))" +
                ");";
    }
}
