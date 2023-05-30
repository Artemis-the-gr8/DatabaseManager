package com.artemis.the.gr8.databasemanager.sql;

import com.artemis.the.gr8.databasemanager.models.MyStatType;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public class SQL {

    public static @NotNull String UNIVERSAL_ID_COLUMN = "id";

    @Contract(pure = true)
    private static @NotNull String selectAllFrom(String tableName) {
        return "SELECT * FROM " + tableName + ";";
    }

    @Contract(pure = true)
    private static @NotNull String selectCountFrom(String tableName) {
        return "SELECT COUNT(*) FROM " + tableName + ";";
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

        @Contract(pure = true)
        public static @NotNull String selectCount() {
            return selectCountFrom(NAME);
        }

        @Contract(pure = true)
        public static @NotNull String selectNameFromUUID(UUID uuid) {
            return "SELECT " + NAME_COLUMN + " FROM " + NAME +
                    " WHERE " + UUID_COLUMN + " = '" + uuid.toString() + "';";
        }

        @Contract(pure = true)
        public static @NotNull String selectIdFromUUID(UUID uuid) {
            return "SELECT " + UNIVERSAL_ID_COLUMN + " FROM " + NAME +
                    " WHERE " + UUID_COLUMN + " = '" + uuid.toString() + "';";
        }

        @Contract(pure = true)
        public static @NotNull String insert() {
            return "INSERT INTO " + NAME + " (" +
                    NAME_COLUMN + ", " +
                    UUID_COLUMN + ", " +
                    IS_EXCLUDED_COLUMN + ") VALUES (?, ?, ?);";
        }

        @Contract(pure = true)
        public static @NotNull String updateWhereMatchingUUID() {
            return "UPDATE " + NAME + " SET " +
                    NAME_COLUMN + " = ?, " +
                    IS_EXCLUDED_COLUMN + " = ?" +
                    " WHERE " + UUID_COLUMN + " = ?;";
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
        public static @NotNull String insert() {
            return "INSERT INTO " + NAME + " (" +
                    NAME_COLUMN + ", " +
                    TYPE_COLUMN + ") VALUES (?, ?);";
        }

        @Contract(pure = true)
        public static @NotNull String selectAll() {
            return selectAllFrom(NAME);
        }

        @Contract(pure = true)
        public static @NotNull String selectCount() {
            return selectCountFrom(NAME);
        }
    }

    public static class SubStatTable {

        public static final String NAME = "sub_statistics";
        public static final String NAME_COLUMN = "name";
        public static final String TYPE_COLUMN = "type";

        @Contract(pure = true)
        public static @NotNull String createTable() {
            //TODO SQLite doesn't support the AUTO_INCREMENT keyword. Make different create version for both database types
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

        @Contract(pure = true)
        public static @NotNull String selectCount() {
            return selectCountFrom(NAME);
        }

        public static @NotNull String selectEntityType() {
            return selectType(MyStatType.ENTITY.getName());
        }

        public static @NotNull String selectItemType() {
            return selectType(MyStatType.ITEM.getName());
        }

        public static @NotNull String selectBlockType() {
            return selectType(MyStatType.BLOCK.getName());
        }

        @Contract(pure = true)
        public static @NotNull String insert() {
            return "INSERT INTO " + NAME + " (" +
                    NAME_COLUMN + ", " +
                    TYPE_COLUMN + ") VALUES (?, ?);";
        }

        @Contract(pure = true)
        private static @NotNull String selectType(String type) {
            return "SELECT * FROM " + NAME +
                    " WHERE " + TYPE_COLUMN + " = '" + type + "';";
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

        public static @NotNull String selectCount() {
            return selectCountFrom(NAME);
        }

        public static @NotNull String insert() {
            return "INSERT INTO " + NAME + " (" +
                    STAT_ID_COLUMN + ", " +
                    SUB_STAT_ID_COLUMN + ") VALUES (?, ?);";
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

        public static @NotNull String selectCount() {
            return selectCountFrom(NAME);
        }
    }
}