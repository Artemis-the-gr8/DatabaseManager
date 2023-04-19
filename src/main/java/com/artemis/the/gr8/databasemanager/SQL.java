package com.artemis.the.gr8.databasemanager;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public class SQL {

    @Contract(pure = true)
    private static @NotNull String selectAllFrom(String tableName) {
        return "SELECT * FROM " + tableName + ";";
    }

    public static class PlayerTable {

        public static final String NAME = "players";
        public static final String NAME_COLUMN = "name";
        public static final String UUID_COLUMN = "uuid";
        public static final String IS_EXCLUDED_COLUMN = "is_excluded";

        @Contract(pure = true)
        public static @NotNull String createTable() {
            return "CREATE TABLE IF NOT EXISTS " + NAME +
                    " (" +
                    "id INT AUTO_INCREMENT PRIMARY KEY," +
                    NAME_COLUMN + " VARCHAR(16)," +
                    UUID_COLUMN + " VARCHAR(255)," +
                    IS_EXCLUDED_COLUMN + " BOOLEAN DEFAULT 0," +
                    "CONSTRAINT unique_uuid UNIQUE (" + UUID_COLUMN + ")" +
                    ");";
        }

        @Contract(pure = true)
        public static @NotNull String selectAll() {
            return selectAllFrom(NAME);
        }
    }

    public static class StatTable {

        public static final String NAME = "statistics";
        public static final String NAME_COLUMN = "name";
        public static final String TYPE_COLUMN = "type";

        @Contract(pure = true)
        public static @NotNull String createTable() {
            return "CREATE TABLE IF NOT EXISTS " + StatTable.NAME +
                    "( " +
                    "id INT AUTO_INCREMENT PRIMARY KEY," +
                    StatTable.NAME_COLUMN + " VARCHAR(255)," +
                    StatTable.TYPE_COLUMN + " VARCHAR(255) NOT NULL," +
                    "CONSTRAINT unique_stat_name UNIQUE (" + StatTable.NAME_COLUMN + ")," +
                    "CONSTRAINT allowed_stat_types CHECK (type IN ('CUSTOM', 'ENTITY', 'BLOCK', 'ITEM'))" +
                    ");";
        }

        @Contract(pure = true)
        public static @NotNull String selectAll() {
            return selectAllFrom(NAME);
        }
    }

    public static class SubStatTable {

        public static final String NAME = "sub_statistics";
        public static final String NAME_COLUMN = "name";
        public static final String TYPE_COLUMN = "type";

        @Contract(pure = true)
        public static @NotNull String createTable() {
            return "CREATE TABLE IF NOT EXISTS " + SubStatTable.NAME +
                    "( " +
                    "id INT AUTO_INCREMENT PRIMARY KEY," +
                    SubStatTable.NAME_COLUMN + " VARCHAR(255)," +
                    SubStatTable.TYPE_COLUMN + " VARCHAR(255) NOT NULL," +
                    "CONSTRAINT unique_sub_stat UNIQUE (" +
                    SubStatTable.NAME_COLUMN + ", " +
                    SubStatTable.TYPE_COLUMN +
                    ")," +
                    "CONSTRAINT allowed_sub_stat_types CHECK (type IN ('ENTITY', 'BLOCK', 'ITEM'))" +
                    ");";
        }

        @Contract(pure = true)
        public static @NotNull String selectAll() {
            return selectAllFrom(NAME);
        }
    }

    public static class StatCombinationTable {

        public static final String NAME = "stat_combinations";
        public static final String STAT_ID_COLUMN = "stat_id";
        public static final String SUB_STAT_ID_COLUMN = "sub_stat_id";

        @Contract(pure = true)
        public static @NotNull String createTable() {
            return "CREATE TABLE IF NOT EXISTS " + NAME  +
                    "( " +
                    "id INT AUTO_INCREMENT PRIMARY KEY," +
                    STAT_ID_COLUMN + " INT," +
                    SUB_STAT_ID_COLUMN + " INT," +
                    "FOREIGN KEY (" + STAT_ID_COLUMN + ") REFERENCES " +
                            StatTable.NAME + "(id)," +
                    "FOREIGN KEY (" + SUB_STAT_ID_COLUMN + ") REFERENCES " +
                            SubStatTable.NAME + "(id)," +
                    "CONSTRAINT unique_combination UNIQUE (" +
                            STAT_ID_COLUMN + ", " +
                            SUB_STAT_ID_COLUMN +
                            ")" +
                    ");";
        }

        @Contract(pure = true)
        public static @NotNull String selectAll() {
            return selectAllFrom(NAME);
        }
    }

    public static class StatValueTable {

        public static final String NAME = "stat_values";
        public static final String PLAYER_ID_COLUMN = "player_id";
        public static final String STAT_COMBINATION_ID_COLUMN = "stat_combination_id";
        public static final String VALUE_COLUMN = "value";

        @Contract(pure = true)
        public static @NotNull String createTable() {
            return "CREATE TABLE IF NOT EXISTS " + NAME +
                    "( " +
                    PLAYER_ID_COLUMN + " INT," +
                    STAT_COMBINATION_ID_COLUMN + " INT," +
                    VALUE_COLUMN + " INT," +
                    "FOREIGN KEY (" + PLAYER_ID_COLUMN + ") REFERENCES " +
                            PlayerTable.NAME + "(id)," +
                    "FOREIGN KEY (" + STAT_COMBINATION_ID_COLUMN + ") REFERENCES " +
                            StatCombinationTable.NAME + "(id)" +
                    ");";
        }

        @Contract(pure = true)
        public static @NotNull String selectAll() {
            return selectAllFrom(NAME);
        }
    }

    public static final String INSERT_STATISTIC =
            "INSERT INTO " + StatTable.NAME +
                    " (name, type) VALUES (?, ?);";

    public static final String INSERT_SUB_STATISTIC =
            "INSERT INTO " + SubStatTable.NAME +
                    " (name, type) VALUES (?, ?);";

    public static final String INSERT_ALL_STATS_AND_SUB_STATS_INTO_COMBINED_TABLE =
            """
            INSERT INTO stat_combinations (stat_id, sub_stat_id)
            SELECT statistics.id, sub_statistics.id FROM statistics
            LEFT JOIN sub_statistics
            ON statistics.type = sub_statistics.type;
            """;

    public static final String INSERT_PLAYER =
            "INSERT INTO " + PlayerTable.NAME +
                    " (name, uuid, is_excluded) VALUES(?, ?, ?);";

    public static final String SELECT_COUNT_FROM_STAT_TABLE =
            "SELECT COUNT(*) FROM " + StatTable.NAME + ";";

    public static final String SELECT_ALL_STAT_COMBINATIONS_WITH_IDs =
            """
            SELECT * FROM stat_combinations
            LEFT JOIN sub_statistics
            ON stat_combinations.sub_stat_id = sub_statistics.id
            LEFT JOIN statistics
            ON stat_combinations.stat_id = statistics.id;
            """;

    public static final String SELECT_PLAYER_BY_UUID =
            "SELECT * FROM " + PlayerTable.NAME +
                    " WHERE uuid = ?;";

    public static final String UPDATE_PLAYER_NAME =
            "UPDATE " + PlayerTable.NAME +
                    " SET name = ? WHERE uuid = ?;";
}