package com.artemis.the.gr8.databasemanager;

public class Query {

    protected static final String playerTable = "players";
    protected static final String statTable = "statistics";
    protected static final String subStatTable = "sub_statistics";
    protected static final String statCombinationTable = "stat_combinations";
    protected static final String statValueTable = "stat_values";

    public static String selectAll(String tableName) {
        return "SELECT * FROM " + tableName + ";";
    }

    public static final String CREATE_PLAYER_TABLE =
            """
            CREATE TABLE IF NOT EXISTS players (
            id INT AUTO_INCREMENT PRIMARY KEY,
            name VARCHAR(16),
            uuid VARCHAR(255),
            is_excluded BOOLEAN DEFAULT 0,
            CONSTRAINT unique_uuid UNIQUE (uuid));
            """;

    public static final String CREATE_STAT_TABLE =
            """
            CREATE TABLE IF NOT EXISTS statistics (
            id INT AUTO_INCREMENT PRIMARY KEY,
            name VARCHAR(255),
            type VARCHAR (255) NOT NULL,
            CONSTRAINT unique_stat_name UNIQUE (name),
            CONSTRAINT allowed_stat_types CHECK (type IN ('CUSTOM', 'ENTITY', 'BLOCK', 'ITEM')));
            """;

    public static final String CREATE_SUB_STAT_TABLE =
            """
            CREATE TABLE IF NOT EXISTS sub_statistics (
            id INT AUTO_INCREMENT PRIMARY KEY,
            name VARCHAR(255),
            type VARCHAR(255) NOT NULL,
            CONSTRAINT unique_sub_stat UNIQUE (name, type),
            CONSTRAINT allowed_sub_stat_types CHECK (type IN ('ENTITY', 'BLOCK', 'ITEM')));
            """;

    public static final String CREATE_STAT_COMBINATION_TABLE =
            """
            CREATE TABLE IF NOT EXISTS stat_combinations (
            id INT AUTO_INCREMENT PRIMARY KEY,
            stat_id INT,
            sub_stat_id INT,
            FOREIGN KEY (stat_id) REFERENCES statistics(id),
            FOREIGN KEY (sub_stat_id) REFERENCES sub_statistics(id),
            CONSTRAINT unique_combination UNIQUE (stat_id, sub_stat_id));
            """;

    public static final String CREATE_STAT_VALUE_TABLE =
            """
            CREATE TABLE IF NOT EXISTS stat_values (
            player_id INT,
            stat_combination_id INT,
            value INT,
            FOREIGN KEY (player_id) REFERENCES players(id),
            FOREIGN KEY (stat_combination_id) REFERENCES stat_combinations(id));
            """;

    public static final String INSERT_STATISTIC =
            "INSERT INTO " + statTable +
                    " (name, type) VALUES (?, ?);";

    public static final String INSERT_SUB_STATISTIC =
            "INSERT INTO " + subStatTable +
                    " (name, type) VALUES (?, ?);";

    public static final String INSERT_ALL_STATS_AND_SUB_STATS_INTO_COMBINED_TABLE =
            """
            INSERT INTO stat_combinations (stat_id, sub_stat_id)
            SELECT statistics.id, sub_statistics.id FROM statistics
            LEFT JOIN sub_statistics
            ON statistics.type = sub_statistics.type;
            """;

    public static final String INSERT_PLAYER =
            "INSERT INTO " + playerTable +
                    " (name, uuid, is_excluded) VALUES(?, ?, ?);";

    public static final String SELECT_ALL_FROM_STAT_TABLE =
            "SELECT * FROM " + statTable + ";";

    public static final String SELECT_COUNT_FROM_STAT_TABLE =
            "SELECT COUNT(*) FROM " + statTable + ";";

    public static final String SELECT_ALL_FROM_SUB_STAT_TABLE =
            "SELECT * FROM " + subStatTable + ";";

    public static final String SELECT_ALL_STAT_COMBINATIONS_WITH_IDs =
            """
            SELECT * FROM stat_combinations
            LEFT JOIN sub_statistics
            ON stat_combinations.sub_stat_id = sub_statistics.id
            LEFT JOIN statistics
            ON stat_combinations.stat_id = statistics.id;
            """;

    public static final String SELECT_PLAYER_BY_UUID =
            "SELECT * FROM " + playerTable +
                    " WHERE uuid = ?;";

    public static final String UPDATE_PLAYER_NAME =
            "UPDATE " + playerTable +
                    " SET name = ? WHERE uuid = ?;";
}