package com.artemis.the.gr8.databasemanager;

import com.artemis.the.gr8.databasemanager.models.MyPlayer;
import com.artemis.the.gr8.databasemanager.models.MyStatistic;
import com.artemis.the.gr8.databasemanager.models.MySubStatistic;
import com.artemis.the.gr8.databasemanager.testutils.TestDataProvider;
import org.junit.jupiter.api.*;

import java.io.File;
import java.sql.*;

public class TestDatabaseHandler {

    protected static final boolean useSQLite = false;
    protected static final boolean useSpigot = false;

    private static String URL;
    private static String USERNAME;
    private static String PASSWORD;

    protected static TestDataProvider testDataProvider;
    protected static Database database;
    protected Connection connection;

    //local MySQL URL for testing: jdbc:mysql://localhost:3306/minecraftstatdb
    @BeforeAll
    static void setup() {
        testDataProvider = new TestDataProvider();

        if (useSQLite) {
            File file = new File(System.getProperty("user.dir"));
            URL = "jdbc:sqlite:" + file.getPath() + "/test.db";
            USERNAME = null;
            PASSWORD = null;

            database = Database.getSQLiteDatabase(URL);
        }
        else {
            URL = "jdbc:mysql://localhost:3306/minecraftstatdb";
            USERNAME = "myuser";
            PASSWORD = "myuser";

            database = Database.getMySQLDatabase(URL, USERNAME, PASSWORD);
        }
    }

    @BeforeEach
    void openConnection() {
        try {
            connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
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
        Assumptions.assumeTrue(useSQLite);
        File file = new File(System.getProperty("user.dir") + "/test.db");
        if (file.exists()) {
            file.delete();
        }
    }

    protected int insertOrGetPlayerID(MyPlayer player) {
        return database.playerDAO.getOrGeneratePlayerID(player, connection);
    }

    protected int insertOrGetStatCombinationID(MyStatistic statistic, MySubStatistic subStatistic) {
        return database.statCombinationDAO.getOrGenerateCombinationID(statistic, subStatistic, connection);
    }

    protected void fillStatTableWithSpigotData() {
        database.statDAO.update(testDataProvider.getAllStatsFromSpigot().stream().toList(), connection);
    }

    protected void fillSubStatTableWithSpigotData() {
        database.subStatDAO.update(testDataProvider.getAllSubStatsFromSpigot(), connection);
    }
}