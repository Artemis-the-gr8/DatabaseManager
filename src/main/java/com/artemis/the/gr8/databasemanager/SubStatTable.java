package com.artemis.the.gr8.databasemanager;

import com.artemis.the.gr8.databasemanager.datamodels.MyStatType;
import com.artemis.the.gr8.databasemanager.datamodels.MySubStatistic;
import org.jetbrains.annotations.NotNull;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class SubStatTable {

    protected void updateSubStatTable(List<MySubStatistic> subStatistics, @NotNull Connection connection) {
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

    protected void updateSubStatTable2(List<MySubStatistic> subStatistics, @NotNull Connection connection) {
        if (subStatistics != null) {
            List<MySubStatistic> currentlyStored = getAllSubStatistics(connection);
            currentlyStored.forEach(subStatistics::remove);
            insertIntoSubStatTable(subStatistics, connection);
        }
    }

    private @NotNull List<MySubStatistic> getAllSubStatistics(@NotNull Connection connection) {
        ArrayList<MySubStatistic> allStats = new ArrayList<>();
        try (Statement statement = connection.createStatement()) {
            ResultSet resultSet = statement.executeQuery(Query.SELECT_ALL_FROM_STAT_TABLE);

            while (resultSet.next()) {
                allStats.add(
                        new MySubStatistic(
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
}
