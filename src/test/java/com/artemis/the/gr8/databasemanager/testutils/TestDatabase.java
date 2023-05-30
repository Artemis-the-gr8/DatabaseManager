package com.artemis.the.gr8.databasemanager.testutils;

import com.artemis.the.gr8.databasemanager.Database;
import com.artemis.the.gr8.databasemanager.StatDAO;
import com.artemis.the.gr8.databasemanager.SubStatDAO;
import com.artemis.the.gr8.databasemanager.sql.SQL;
import org.junit.jupiter.api.*;

import java.io.File;
import java.sql.*;

import static com.artemis.the.gr8.databasemanager.sql.SQL.*;

public class TestDatabase {

    protected static final boolean useSQLite = false;

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
        }
        else {
            URL = "jdbc:mysql://localhost:3306/minecraftstatdb";
            USERNAME = "myuser";
            PASSWORD = "myuser";
        }

        database = new Database(URL, USERNAME, PASSWORD);
        database.setUp();
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

    protected int getCountForTable(String tableName) {
        String query = switch (tableName) {
            case StatTable.NAME -> StatTable.selectCount();
            case SubStatTable.NAME -> SubStatTable.selectCount();
            case StatCombinationTable.NAME -> StatCombinationTable.selectCount();
            case PlayerTable.NAME -> PlayerTable.selectCount();
            case StatValueTable.NAME -> StatValueTable.selectCount();
            default -> throw new IllegalArgumentException("Not a valid tableName!");
        };

        try (Statement statement = connection.createStatement()) {
            ResultSet resultSet = statement.executeQuery(query);
            resultSet.next();
            int count = resultSet.getInt(1);
            resultSet.close();

            return count;
        }
        catch (SQLException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    protected void fillStatTableWithSpigotData() {
        StatDAO statDAO = new StatDAO();
        statDAO.update(testDataProvider.getAllStatsFromSpigot(), connection);
    }

    protected void fillSubStatTableWithSpigotData() {
        SubStatDAO subStatDAO = new SubStatDAO();
        subStatDAO.update(testDataProvider.getAllSubStatsFromSpigot(), connection);
    }

    protected String getNameOfTzvi_FromPlayerTable() {
        try (Statement statement = connection.createStatement()) {
            ResultSet resultSet = statement.executeQuery(
                    SQL.PlayerTable.selectNameFromUUID(
                            testDataProvider.getUUIDForTzvi_()));

            resultSet.next();
            String name = resultSet.getString(PlayerTable.NAME_COLUMN);
            resultSet.close();

            return name;
        }
        catch (SQLException e) {
            throw new RuntimeException(e.getMessage());
        }
    }
}