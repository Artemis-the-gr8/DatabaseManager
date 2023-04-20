package com.artemis.the.gr8.databasemanager.testutils;

import com.artemis.the.gr8.databasemanager.Database;
import org.junit.jupiter.api.*;

import java.io.File;
import java.sql.*;

import static com.artemis.the.gr8.databasemanager.sql.SQL.*;

public class TestDatabase {

    private static String URL;
    protected static TestDataProvider testDataProvider;
    protected static Database database;
    protected Connection connection;

    @BeforeAll
    static void setup() {
        testDataProvider = new TestDataProvider();

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

        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage());
        }
    }
}