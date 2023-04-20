package com.artemis.the.gr8.databasemanager.sql;

import com.artemis.the.gr8.databasemanager.models.MyStatType;
import com.artemis.the.gr8.databasemanager.models.MyStatistic;
import org.jetbrains.annotations.NotNull;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class StatTable {

    public StatTable() {
    }

    public void update(List<MyStatistic> statistics, Connection connection) {
        if (statistics != null) {
            List<MyStatistic> currentlyStored = getAllStatistics(connection);
            currentlyStored.forEach(statistics::remove);
            insert(statistics, connection);
        }
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

    private void insert(@NotNull List<MyStatistic> statistics, @NotNull Connection connection) {
        try (PreparedStatement statement = connection.prepareStatement(SQL.StatTable.insert())){
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
}
