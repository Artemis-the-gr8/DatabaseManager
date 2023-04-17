package com.artemis.the.gr8.databasemanager;

import com.artemis.the.gr8.databasemanager.datamodels.MyStatType;
import com.artemis.the.gr8.databasemanager.datamodels.MySubStatistic;
import org.jetbrains.annotations.NotNull;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class SubStatTable {

    private final String tableName;

    public SubStatTable() {
        tableName = Query.subStatTable;
    }

    protected void update(List<MySubStatistic> subStatistics, @NotNull Connection connection) {
        if (subStatistics != null) {
            List<MySubStatistic> currentlyStored = getAllSubStatistics(connection);
            currentlyStored.forEach(subStatistics::remove);
            insertIntoSubStatTable(subStatistics, connection);
        }
    }

    private @NotNull List<MySubStatistic> getAllSubStatistics(@NotNull Connection connection) {
        ArrayList<MySubStatistic> allStats = new ArrayList<>();
        try (Statement statement = connection.createStatement()) {
            ResultSet resultSet = statement.executeQuery(Query.selectAll(tableName));

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