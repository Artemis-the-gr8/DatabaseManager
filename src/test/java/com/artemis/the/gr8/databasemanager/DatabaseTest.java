package com.artemis.the.gr8.databasemanager;

import com.artemis.the.gr8.databasemanager.datamodels.MyStatType;
import com.artemis.the.gr8.databasemanager.datamodels.MyStatistic;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.*;

import java.io.File;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class DatabaseTest {

    private static String URL;
    protected static Database database;
    protected Connection connection;

    @BeforeAll
    static void setup() {
        File file = new File(System.getProperty("user.dir"));

        URL = "jdbc:sqlite:" + file.getPath() + "/test.db";
        database = new Database(URL, null, null);
        database.setUp();
    }

    @BeforeEach
    void openConnection() {
        try {
            connection = DriverManager.getConnection(URL);
        }
        catch (SQLException e) {
            throw new RuntimeException("Could not get database connection!");
        }
    }

    @AfterEach
    void closeConnection() {
        try {
            if (connection != null) {
                connection.close();
            }
        } catch (SQLException e) {
            throw new RuntimeException(
                    "Could not close database connection!");
        }
    }

    @AfterAll
    static void tearDown() {
        File file = new File(System.getProperty("user.dir") + "/test.db");
        if (file.exists()) {
            file.delete();
        }
    }

    protected void resetDatabase() {
        closeConnection();
        tearDown();
        setup();
        openConnection();
    }

    @Test
    void updateStatTable() {
        database.updateStatTable(getMockListForExistingStats(), connection);
        assertEquals(4, getCountForStatTable());

        database.updateStatTable(getMockListForUpdatedStats(), connection);
        assertEquals(5, getCountForStatTable());
    }

    private int getCountForStatTable() {
        try (Statement statement = connection.createStatement()) {
            ResultSet resultSet = statement.executeQuery(SQL.SELECT_COUNT_FROM_STAT_TABLE);
            resultSet.next();
            int count = resultSet.getInt(1);
            resultSet.close();

            return count;
        }
        catch (SQLException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    private @NotNull List<MyStatistic> getMockListForUpdatedStats() {
        List<MyStatistic> statistics = new ArrayList<>();
        statistics.add(new MyStatistic("damage_dealt", MyStatType.CUSTOM));
        statistics.add(new MyStatistic("animals_bred", MyStatType.CUSTOM));
        statistics.add(new MyStatistic("drop", MyStatType.ITEM));
        statistics.add(new MyStatistic("mine_block", MyStatType.BLOCK));
        statistics.add(new MyStatistic("kill_entity", MyStatType.ENTITY));
        return statistics;
    }

    private @NotNull List<MyStatistic> getMockListForExistingStats() {
        List<MyStatistic> statistics = new ArrayList<>();
        statistics.add(new MyStatistic("damage_dealt", MyStatType.CUSTOM));
        statistics.add(new MyStatistic("drop", MyStatType.ITEM));
        statistics.add(new MyStatistic("mine_block", MyStatType.BLOCK));
        statistics.add(new MyStatistic("kill_entity", MyStatType.ENTITY));
        return statistics;
    }
}