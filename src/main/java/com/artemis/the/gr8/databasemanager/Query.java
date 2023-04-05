package com.artemis.the.gr8.databasemanager;

public class Query {

    private static final String playerTable = "players";
    private static final String statTable = "statistics";
    private static final String subStatTable = "substatistics";
    private static final String statCombinationTable = "statcombinations";
    private static final String statValueTable = "statvalues";

    public static final String CREATE_PLAYER_TABLE =
            """
            CREATE TABLE IF NOT EXISTS players (
            ID INT AUTO_INCREMENT PRIMARY KEY,
            playerName VARCHAR(16),
            UUID VARCHAR(255),
            isExcluded BOOLEAN DEFAULT 0,
            CONSTRAINT unique_UUID UNIQUE (UUID));
            """;

    public static final String CREATE_STAT_TABLE =
            """
            CREATE TABLE IF NOT EXISTS statistics (
            ID INT AUTO_INCREMENT PRIMARY KEY,
            statName VARCHAR(255),
            statType VARCHAR (255) NOT NULL,
            CONSTRAINT unique_stat_name UNIQUE (statName),
            CONSTRAINT allowed_stat_types CHECK (statType IN ('CUSTOM', 'ENTITY', 'BLOCK', 'ITEM')));
            """;

    public static final String CREATE_SUB_STAT_TABLE =
            """
            CREATE TABLE IF NOT EXISTS substatistics (
            ID INT AUTO_INCREMENT PRIMARY KEY,
            subStatName VARCHAR(255),
            subStatType VARCHAR(255) NOT NULL,
            CONSTRAINT unique_sub_stat UNIQUE (subStatName, subStatType),
            CONSTRAINT allowed_substat_types CHECK (subStatType IN ('ENTITY', 'BLOCK', 'ITEM')));
            """;

    public static final String CREATE_STAT_COMBINATION_TABLE =
            """
            CREATE TABLE IF NOT EXISTS statcombinations (
            ID INT AUTO_INCREMENT PRIMARY KEY,
            statisticID INT,
            FOREIGN KEY (statisticID) REFERENCES statistics(ID));
            """;

    public static final String CREATE_STAT_VALUE_TABLE =
            """
            CREATE TABLE IF NOT EXISTS statvalues (
            playerID INT,
            statCombinationID INT,
            value INT,
            FOREIGN KEY (playerID) REFERENCES players(ID),
            FOREIGN KEY (statCombinationID) REFERENCES statcombinations(ID));
            """;

    public static final String SELECT_ALL_FROM_STAT_TABLE =
            "SELECT * FROM " + statTable + ";";

    public static final String SELECT_ALL_FROM_SUB_STAT_TABLE =
            "SELECT * FROM " + subStatTable + ";";

    public static final String SELECT_PLAYER_BY_UUID =
            "SELECT * FROM " + playerTable +
                    " WHERE UUID = ?;";

    public static final String INSERT_STATISTIC =
            "INSERT INTO " + statTable +
                    " (statName, statType) VALUES (?, ?);";

    public static final String INSERT_SUB_STATISTIC =
            "INSERT INTO " + subStatTable +
                    " (subStatName, subStatType) VALUES (?, ?);";

    public static final String INSERT_NEW_PLAYER =
            "INSERT INTO " + playerTable +
                    " (playerName, UUID, isExcluded) VALUES(?, ?, ?);";

    public static final String UPDATE_PLAYER_NAME =
            "UPDATE " + playerTable +
                    " SET playerName = ? WHERE UUID = ?;";
}