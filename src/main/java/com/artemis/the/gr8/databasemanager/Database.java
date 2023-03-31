package com.artemis.the.gr8.databasemanager;

import com.artemis.the.gr8.databasemanager.models.MyStatistic;
import com.artemis.the.gr8.databasemanager.models.MySubStatistic;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.sql.*;
import java.util.List;

public class Database {

    private final String URL;
    private final String USER;
    private final String PASSWORD;

    private Connection connection;

    public Database(String URL, String username, String password) {
        this.URL = URL;
        this.USER = username;
        this.PASSWORD = password;
        setup();
    }

    private void setup() {
        try {
            connection = DriverManager.getConnection(URL, USER, PASSWORD);
            createTablesIfNotExisting();
            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void fillStatistics(List<MyStatistic> statistics, List<MySubStatistic> subStatistics) {
        try {
            if (connection == null || connection.isClosed()) {
                connection = DriverManager.getConnection(URL, USER, PASSWORD);
            }
            fillStatTable(statistics);
            connection.close();
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void fillStatTable(List<MyStatistic> statistics) {
        try {
            PreparedStatement statement = connection.prepareStatement(Query.INSERT_STATISTIC);
            statistics.forEach(stat ->
            {
                try {
                    statement.setString(1, stat.statName());
                    statement.setString(2, stat.statType().toString());
                    statement.addBatch();
                }
                catch (SQLException e) {
                    e.printStackTrace();
                }
            });
            statement.executeBatch();
            statement.close();
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void createTablesIfNotExisting() {
        try (Statement statement = connection.createStatement()){
            statement.addBatch(createPlayerTable());
            statement.addBatch(createStatTable());
            statement.addBatch(createSubStatTable());
            statement.addBatch(createStatCombinationTable());
            statement.addBatch(createStatValueTable());
            statement.executeBatch();
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Contract(pure = true)
    private @NotNull String createPlayerTable() {
        return """
                CREATE TABLE IF NOT EXISTS players (
                ID INT AUTO_INCREMENT PRIMARY KEY,
                playerName VARCHAR(16),
                UUID VARCHAR(255),
                isExcluded BOOLEAN DEFAULT 0,
                CONSTRAINT unique_UUID UNIQUE (UUID));""";
    }

    @Contract(pure = true)
    private @NotNull String createStatTable() {
        return """
                CREATE TABLE IF NOT EXISTS statistics (
                ID INT AUTO_INCREMENT PRIMARY KEY,
                statName VARCHAR(255),
                statType VARCHAR (255) NOT NULL,
                CONSTRAINT unique_stat_name UNIQUE (statName),
                CONSTRAINT allowed_stat_types CHECK (statType IN ('CUSTOM', 'ENTITY', 'BLOCK', 'ITEM')));
                """;
    }

    @Contract(pure = true)
    private @NotNull String createSubStatTable() {
        return """
                CREATE TABLE IF NOT EXISTS substatistics (
                ID INT AUTO_INCREMENT PRIMARY KEY,
                subStatName VARCHAR(255),
                subStatType VARCHAR(255) NOT NULL,
                CONSTRAINT unique_sub_stat UNIQUE (subStatName, subStatType),
                CONSTRAINT allowed_substat_types CHECK (subStatType IN ('ENTITY', 'BLOCK', 'ITEM')));
                """;
    }

    @Contract(pure = true)
    private @NotNull String createStatCombinationTable() {
        return """
                CREATE TABLE IF NOT EXISTS statcombinations (
                ID INT AUTO_INCREMENT PRIMARY KEY,
                statisticID INT,
                FOREIGN KEY (statisticID) REFERENCES statistics(ID));
                """;
    }

    @Contract(pure = true)
    private @NotNull String createStatValueTable() {
        return """
                CREATE TABLE IF NOT EXISTS statvalues (
                playerID INT,
                statCombinationID INT,
                value INT,
                FOREIGN KEY (playerID) REFERENCES players(ID),
                FOREIGN KEY (statCombinationID) REFERENCES statcombinations(ID));
                """;
    }
}