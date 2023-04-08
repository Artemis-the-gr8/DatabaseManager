package com.artemis.the.gr8.databasemanager;

import com.artemis.the.gr8.databasemanager.datamodels.MyStatType;
import com.artemis.the.gr8.databasemanager.datamodels.MyStatistic;
import com.artemis.the.gr8.databasemanager.datamodels.MySubStatistic;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

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
            List<MyStatistic> newStats = updateStatTable(statistics, connection);
            List<MySubStatistic> newSubStats = updateSubStatTable(subStatistics, connection);
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private @NotNull List<MyStatistic> updateStatTable(List<MyStatistic> statistics, Connection connection) {
        if (statistics != null) {
            try (Statement statement = connection.createStatement()) {
                ResultSet resultSet = statement.executeQuery(Query.SELECT_ALL_FROM_STAT_TABLE);
                List<MyStatistic> newStatistics = filterOutExistingStatistics(statistics, resultSet);
                resultSet.close();

                insertIntoStatTable(newStatistics, connection);
                return newStatistics;
            }
            catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return new ArrayList<>();
    }

    @Contract("null, _ -> new")
    private @NotNull List<MySubStatistic> updateSubStatTable(List<MySubStatistic> subStatistics, @NotNull Connection connection) {
        if (subStatistics != null) {
            try (Statement statement = connection.createStatement()) {
                ResultSet resultSet = statement.executeQuery(Query.SELECT_ALL_FROM_SUB_STAT_TABLE);
                List<MySubStatistic> newSubStatistics = filterOutExistingSubStatistics(subStatistics, resultSet);
                resultSet.close();

                insertIntoSubStatTable(newSubStatistics, connection);
                return newSubStatistics;
            }
            catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return new ArrayList<>();
    }

    private @NotNull List<MyStatistic> filterOutExistingStatistics(List<MyStatistic> providedStatistics, @NotNull ResultSet storedStatistics) throws SQLException {
        ArrayList<MyStatistic> newStatistics = new ArrayList<>(providedStatistics);
        while (storedStatistics.next()) {
            MyStatistic currentRow = new MyStatistic(
                    storedStatistics.getString("name"),
                    MyStatType.fromString(storedStatistics.getString("type")));

            newStatistics.remove(currentRow);
        }
        return newStatistics;
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