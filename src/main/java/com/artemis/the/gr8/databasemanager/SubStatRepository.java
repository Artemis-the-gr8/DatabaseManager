package com.artemis.the.gr8.databasemanager;

import com.artemis.the.gr8.databasemanager.models.MyStatType;
import com.artemis.the.gr8.databasemanager.models.MySubStatistic;
import com.artemis.the.gr8.databasemanager.sql.SQL;
import org.jetbrains.annotations.NotNull;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.function.Predicate;

public class SubStatRepository {

    public SubStatRepository() {
    }

    public void update(List<MySubStatistic> subStatistics, @NotNull Connection connection) {
        if (subStatistics != null) {
            List<MySubStatistic> currentlyStored = getAllSubStatistics(connection);
            List<MySubStatistic> newValues = subStatistics.stream()
                            .filter(Predicate.not(currentlyStored::contains))
                                    .toList();

            insert(newValues, connection);
        }
    }

    protected @NotNull HashMap<MySubStatistic, Integer> getAllSubStatisticsWithID(@NotNull Connection connection) {
        HashMap<MySubStatistic, Integer> subStats = new HashMap<>();
        try (Statement statement = connection.createStatement()) {
            ResultSet resultSet = statement.executeQuery(
                    SQL.SubStatTable.selectAll());

            while (resultSet.next()) {
                subStats.put(
                        new MySubStatistic(
                                resultSet.getString(SQL.SubStatTable.NAME_COLUMN),
                                MyStatType.fromString(resultSet.getString(SQL.SubStatTable.TYPE_COLUMN))),
                        resultSet.getInt(SQL.UNIVERSAL_ID_COLUMN));
            }
            resultSet.close();
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        return subStats;
    }

    protected @NotNull List<MySubStatistic> getAllSubStatistics(@NotNull Connection connection) {
        ArrayList<MySubStatistic> allStats = new ArrayList<>();
        try (Statement statement = connection.createStatement()) {
            ResultSet resultSet = statement.executeQuery(
                    SQL.SubStatTable.selectAll());

            while (resultSet.next()) {
                allStats.add(
                        new MySubStatistic(
                                resultSet.getString(SQL.SubStatTable.NAME_COLUMN),
                                MyStatType.fromString(resultSet.getString(SQL.SubStatTable.TYPE_COLUMN))));
            }
            resultSet.close();
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        return allStats;
    }

    private void insert(@NotNull List<MySubStatistic> subStats, @NotNull Connection connection) {
        try (PreparedStatement statement = connection.prepareStatement(SQL.SubStatTable.insert())) {
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