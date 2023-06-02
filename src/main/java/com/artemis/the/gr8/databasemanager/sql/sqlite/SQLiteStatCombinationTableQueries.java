package com.artemis.the.gr8.databasemanager.sql.sqlite;

import com.artemis.the.gr8.databasemanager.sql.StatCombinationTableQueries;
import com.artemis.the.gr8.databasemanager.sql.StatTableQueries;
import com.artemis.the.gr8.databasemanager.sql.SubStatTableQueries;
import org.jetbrains.annotations.NotNull;

public class SQLiteStatCombinationTableQueries extends StatCombinationTableQueries {

    @Override
    public @NotNull String createTable() {
        return "CREATE TABLE IF NOT EXISTS " + TABLE_NAME +
                "( " +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                STAT_ID_COLUMN + " INT," +
                SUB_STAT_ID_COLUMN + " INT," +
                "FOREIGN KEY (" + STAT_ID_COLUMN + ") REFERENCES " +
                StatTableQueries.TABLE_NAME + "(id)," +
                "FOREIGN KEY (" + SUB_STAT_ID_COLUMN + ") REFERENCES " +
                SubStatTableQueries.TABLE_NAME + "(id)," +
                "CONSTRAINT unique_combination UNIQUE (" +
                STAT_ID_COLUMN + ", " +
                SUB_STAT_ID_COLUMN +
                ")" +
                ");";
    }
}
