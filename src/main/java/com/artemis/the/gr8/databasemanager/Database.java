package com.artemis.the.gr8.databasemanager;

import com.artemis.the.gr8.databasemanager.models.MyPlayer;
import com.artemis.the.gr8.databasemanager.models.MyStatistic;
import com.artemis.the.gr8.databasemanager.models.MySubStatistic;
import com.artemis.the.gr8.databasemanager.sql.mysql.*;
import com.artemis.the.gr8.databasemanager.sql.sqlite.*;
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
    protected StatDAO statDAO;
    protected SubStatDAO subStatDAO;
    protected StatCombinationDAO statCombinationDAO;
    protected StatValueDAO statValueDAO;

    private Database(String URL, String username, String password) {
        this.URL = URL;
        this.USER = username;
        this.PASSWORD = password;
    }

    public static @NotNull Database getMySQLDatabase(String URL, String userName, String password) {
        Database database = new Database(URL, userName, password);
        database.setUpMySQL();
        return database;
    }

    public static @NotNull Database getSQLiteDatabase(String URL) {
        Database database = new Database(URL, null, null);
        database.setUpSQLite();
        return database;
    }

    public void updateStatistics(List<MyStatistic> statistics, List<MySubStatistic> subStatistics) {
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD)) {
            statDAO.update(statistics, connection);
            subStatDAO.update(subStatistics, connection);
            statCombinationDAO.update(connection);
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
    }

    protected HashMap<Integer, MyStatistic> getAllStatistics() {
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD)) {
            return statDAO.getAllStatistics(connection);
        }
        catch (SQLException e) {
            e.printStackTrace();
            throw new NullPointerException("Getting all statistics has failed!");
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

    private void setUpMySQL() {
        playerDAO = new PlayerDAO(new MySQLPlayerTableQueries());
        statDAO = new StatDAO(new MySQLStatTableQueries());
        subStatDAO = new SubStatDAO(new MySQLSubStatTableQueries());
        statCombinationDAO = new StatCombinationDAO(statDAO, subStatDAO, new MySQLStatCombinationTableQueries());
        statValueDAO = new StatValueDAO(playerDAO, statCombinationDAO, new MySQLStatValueTableQueries());

        createTablesIfNotExisting();
    }

    private void setUpSQLite() {
        playerDAO = new PlayerDAO(new SQLitePlayerTableQueries());
        statDAO = new StatDAO(new SQLiteStatTableQueries());
        subStatDAO = new SubStatDAO(new SQLiteSubStatTableQueries());
        statCombinationDAO = new StatCombinationDAO(statDAO, subStatDAO, new SQLiteStatCombinationTableQueries());
        statValueDAO = new StatValueDAO(playerDAO, statCombinationDAO, new SQLiteStatValueTableQueries());

        createTablesIfNotExisting();
    }

    private void createTablesIfNotExisting() {
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD)) {
            playerDAO.create(connection);
            statDAO.create(connection);
            subStatDAO.create(connection);
            statCombinationDAO.create(connection);
            statValueDAO.create(connection);
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
    }
}