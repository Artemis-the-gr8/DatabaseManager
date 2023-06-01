package com.artemis.the.gr8.databasemanager;

import com.artemis.the.gr8.databasemanager.models.MyPlayer;
import com.artemis.the.gr8.databasemanager.models.MyStatistic;
import com.artemis.the.gr8.databasemanager.models.MySubStatistic;
import com.artemis.the.gr8.databasemanager.sql.SQL;
import com.artemis.the.gr8.databasemanager.sql.mysql.MySQLPlayerTableQueries;
import com.artemis.the.gr8.databasemanager.sql.sqlite.SQLitePlayerTableQueries;
import org.jetbrains.annotations.NotNull;

import java.sql.*;
import java.util.*;

/**
 * Responsible for all connections with databases
 */
public class Database {

    private final String URL;
    private final String USER;
    private final String PASSWORD;

    protected PlayerDAO playerDAO;

    private Database(String URL, String username, String password) {
        this.URL = URL;
        this.USER = username;
        this.PASSWORD = password;
    }

    public static @NotNull Database getMySQLDatabase(String URL, String userName, String password) {
        Database database = new Database(URL, userName, password);
        database.playerDAO = new PlayerDAO(new MySQLPlayerTableQueries());

        return database;
    }

    public static @NotNull Database getSQLiteDatabase(String URL) {
        Database database = new Database(URL, null, null);
        database.playerDAO = new PlayerDAO(new SQLitePlayerTableQueries());

        return database;
    }

    public void setUp() {
        createTablesIfNotExisting();
    }

    public void updateStatistics(List<MyStatistic> statistics, List<MySubStatistic> subStatistics) {
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD)) {
            StatDAO statDAO = new StatDAO();
            statDAO.update(statistics, connection);

            SubStatDAO subStatDAO = new SubStatDAO();
            subStatDAO.update(subStatistics, connection);

            StatCombinationDAO combinationDAO = new StatCombinationDAO();
            combinationDAO.update(connection);
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updatePlayers(List<MyPlayer> players) {
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD)) {
            playerDAO.update(players, connection);
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void createTablesIfNotExisting() {
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD)) {
            playerDAO.create(connection);

            Statement statement = connection.createStatement();
            statement.addBatch(SQL.StatTable.createTable());
            statement.addBatch(SQL.SubStatTable.createTable());
            statement.addBatch(SQL.StatCombinationTable.createTable());
            statement.addBatch(SQL.StatValueTable.createTable());

            statement.executeBatch();
            statement.close();
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
    }
}