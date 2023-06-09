package com.artemis.the.gr8.databasemanager.sql.sqlite;

import com.artemis.the.gr8.databasemanager.sql.PlayerTableQueries;
import com.artemis.the.gr8.databasemanager.sql.StatCombinationTableQueries;
import com.artemis.the.gr8.databasemanager.sql.StatValueTableQueries;
import org.jetbrains.annotations.NotNull;

public class SQLiteStatValueTableQueries extends StatValueTableQueries {

    @Override
    public @NotNull String createTable() {
        return "CREATE TABLE IF NOT EXISTS " + TABLE_NAME +
                "( " +
                PLAYER_ID_COLUMN + " INT," +
                STAT_COMBINATION_ID_COLUMN + " INT," +
                VALUE_COLUMN + " INT," +
                "FOREIGN KEY (" + PLAYER_ID_COLUMN + ") REFERENCES " +
                PlayerTableQueries.TABLE_NAME + "(id)," +
                "FOREIGN KEY (" + STAT_COMBINATION_ID_COLUMN + ") REFERENCES " +
                StatCombinationTableQueries.TABLE_NAME + "(id)," +
                "CONSTRAINT unique_player_value_combination UNIQUE (" +
                PLAYER_ID_COLUMN + ", " +
                STAT_COMBINATION_ID_COLUMN +
                ")" +
                ");";
    }
}
