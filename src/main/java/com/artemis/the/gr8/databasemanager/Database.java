package com.artemis.the.gr8.databasemanager;

import com.artemis.the.gr8.databasemanager.models.MyStatistic;
import com.artemis.the.gr8.databasemanager.models.MySubStatistic;
import com.artemis.the.gr8.databasemanager.sql.SQL;
import com.artemis.the.gr8.databasemanager.sql.StatTable;
import com.artemis.the.gr8.databasemanager.sql.SubStatTable;
import com.artemis.the.gr8.databasemanager.utils.Timer;
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

    public Database(String URL, String username, String password) {
        this.URL = URL;
        this.USER = username;
        this.PASSWORD = password;
    }

    public void setUp() {
        createTablesIfNotExisting();
    }

    public void updateStatistics(List<MyStatistic> statistics, List<MySubStatistic> subStatistics) {
        Timer timer = Timer.start();
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD)){
            System.out.println("Connection made in " + timer.reset() + "ms");

            updateStatTable(statistics, connection);
            System.out.println("StatTable updated in " + timer.reset() + "ms");

            updateSubStatTable(subStatistics, connection);
            System.out.println("SubStatTable updated in " + timer.reset() + "ms");
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
    }

    protected void updateStatTable(List<MyStatistic> statistics, Connection connection) {
        StatTable table = new StatTable();
        table.update(statistics, connection);
    }

    protected void updateSubStatTable(List<MySubStatistic> subStatistics, @NotNull Connection connection) {
        SubStatTable table = new SubStatTable();
        table.update(subStatistics, connection);
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