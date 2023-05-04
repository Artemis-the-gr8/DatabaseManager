package com.artemis.the.gr8.databasemanager;

import com.artemis.the.gr8.databasemanager.models.MyStatistic;
import com.artemis.the.gr8.databasemanager.models.MySubStatistic;
import com.artemis.the.gr8.databasemanager.sql.SQL;

import java.sql.*;
import java.util.*;

/**
 * Responsible for all connections with databases
 */
public class Database {

    private final String URL;
    private final String USER;
    private final String PASSWORD;

    public Database(String URL, String username, String password) {
        this.URL = URL;
        this.USER = username;
        this.PASSWORD = password;
    }

    public void setUp() {
        createTablesIfNotExisting();
    }

    public void updateStatistics(List<MyStatistic> statistics, List<MySubStatistic> subStatistics) {
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD)){
            StatDAO statDAO = new StatDAO();
            statDAO.update(statistics, connection);

            SubStatDAO subStatDAO = new SubStatDAO();
            subStatDAO.update(subStatistics, connection);
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
    }


    private void createTablesIfNotExisting() {
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD)){
            Statement statement = connection.createStatement();

            statement.addBatch(SQL.PlayerTable.createTable());
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