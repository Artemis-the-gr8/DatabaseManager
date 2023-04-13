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

    private void updateSubStatTable(List<MySubStatistic> subStatistics, @NotNull Connection connection) {
        if (subStatistics != null) {
            try (Statement statement = connection.createStatement()) {
                ResultSet resultSet = statement.executeQuery(Query.SELECT_ALL_FROM_SUB_STAT_TABLE);
                List<MySubStatistic> newSubStatistics = filterOutExistingSubStatistics(subStatistics, resultSet);
                resultSet.close();

                insertIntoSubStatTable(newSubStatistics, connection);
            }
            catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    private void updateStatCombinationTable(Connection connection) {


    }

    private @NotNull List<MyStatistic> getAllStatistics(@NotNull Connection connection) {
        ArrayList<MyStatistic> allStats = new ArrayList<>();
        try (Statement statement = connection.createStatement()) {
            ResultSet resultSet = statement.executeQuery(Query.SELECT_ALL_FROM_STAT_TABLE);

            while (resultSet.next()) {
                allStats.add(
                        new MyStatistic(
                                resultSet.getString("name"),
                                MyStatType.fromString(resultSet.getString("type"))));
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

    protected @NotNull List<MyStatistic> filterOutExistingStats(@NotNull List<MyStatistic> providedStats, @NotNull List<MyStatistic> storedStats) {
        storedStats.forEach(providedStats::remove);
        return providedStats;
    }

    private @NotNull List<MySubStatistic> filterOutExistingSubStatistics(List<MySubStatistic> providedSubStats, @NotNull ResultSet storedSubStats) throws SQLException {
        ArrayList<MySubStatistic> newSubStatistics = new ArrayList<>(providedSubStats);
        while (storedSubStats.next()) {
            MySubStatistic currentRow = new MySubStatistic(
                    storedSubStats.getString("name"),
                    MyStatType.fromString(storedSubStats.getString("type")));

            newSubStatistics.remove(currentRow);
        }
        return newSubStatistics;
    }


    private void insertIntoStatTable(@NotNull List<MyStatistic> statistics, @NotNull Connection connection) {
        try (PreparedStatement statement = connection.prepareStatement(Query.INSERT_STATISTIC)){
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

    private void insertIntoSubStatTable(@NotNull List<MySubStatistic> subStats, @NotNull Connection connection) {
        try (PreparedStatement statement = connection.prepareStatement(Query.INSERT_SUB_STATISTIC)) {
            for (MySubStatistic subStat : subStats) {
                statement.setString(1, subStat.subStatName());
                statement.setString(2, subStat.subStatType().toString().toUpperCase(Locale.ENGLISH));
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
            statement.addBatch(Query.CREATE_PLAYER_TABLE);
            statement.addBatch(Query.CREATE_STAT_TABLE);
            statement.addBatch(Query.CREATE_SUB_STAT_TABLE);
            statement.addBatch(Query.CREATE_STAT_COMBINATION_TABLE);
            statement.addBatch(Query.CREATE_STAT_VALUE_TABLE);
            statement.executeBatch();
            statement.close();
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
    }
}