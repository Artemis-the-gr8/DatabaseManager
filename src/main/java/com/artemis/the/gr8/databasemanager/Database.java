package com.artemis.the.gr8.databasemanager;

import com.artemis.the.gr8.databasemanager.datamodels.Timer;
import com.artemis.the.gr8.databasemanager.datamodels.MyStatType;
import com.artemis.the.gr8.databasemanager.datamodels.MyStatistic;
import com.artemis.the.gr8.databasemanager.datamodels.MySubStatistic;
import org.jetbrains.annotations.NotNull;

import java.sql.*;
import java.util.*;

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
        Timer timer = new Timer();
        timer.startTimer();
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD)){
            System.out.println("Connection made in " + timer.stopTimer() + "ms");

            timer.startTimer();
            updateStatTable(statistics, connection);
            System.out.println("StatTable updated in " + timer.stopTimer() + "ms");

            timer.startTimer();
            updateSubStatTable(subStatistics, connection);
            System.out.println("SubStatTable updated in " + timer.stopTimer() + "ms");
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
    }

    protected void updateStatTable(List<MyStatistic> statistics, Connection connection) {
        if (statistics != null) {
            List<MyStatistic> currentlyStored = getAllStatistics(connection);
            currentlyStored.forEach(statistics::remove);
            insertIntoStatTable(statistics, connection);
        }
    }

    protected void updateSubStatTable(List<MySubStatistic> subStatistics, @NotNull Connection connection) {
        SubStatTable table = new SubStatTable();
        table.update(subStatistics, connection);
    }

    private void updateStatCombinationTable(Connection connection) {

    }

    private @NotNull List<MyStatistic> getAllStatistics(@NotNull Connection connection) {
        ArrayList<MyStatistic> allStats = new ArrayList<>();
        try (Statement statement = connection.createStatement()) {
            ResultSet resultSet = statement.executeQuery(SQL.StatTable.selectAll());

            while (resultSet.next()) {
                allStats.add(
                        new MyStatistic(
                                resultSet.getString(SQL.StatTable.NAME_COLUMN),
                                MyStatType.fromString(resultSet.getString(SQL.StatTable.TYPE_COLUMN))));
            }
            resultSet.close();
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        return allStats;
    }

    private Map<Integer, Integer> findAllCombinations(@NotNull Connection connection) throws SQLException {
        return null;
    }

    private void insertIntoStatTable(@NotNull List<MyStatistic> statistics, @NotNull Connection connection) {
        try (PreparedStatement statement = connection.prepareStatement(SQL.INSERT_STATISTIC)){
            for (MyStatistic stat : statistics) {
                statement.setString(1, stat.statName());
                statement.setString(2, stat.statType().toString().toUpperCase(Locale.ENGLISH));
                statement.addBatch();
            }
            statement.executeBatch();
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