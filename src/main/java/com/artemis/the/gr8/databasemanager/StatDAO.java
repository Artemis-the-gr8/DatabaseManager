package com.artemis.the.gr8.databasemanager;

import com.artemis.the.gr8.databasemanager.models.MyStatType;
import com.artemis.the.gr8.databasemanager.models.MyStatistic;
import com.artemis.the.gr8.databasemanager.sql.StatTableQueries;
import org.jetbrains.annotations.NotNull;

import java.sql.*;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.function.Predicate;

public class StatDAO {

    private final StatTableQueries sqlQueries;

    public StatDAO(StatTableQueries statTableQueries) {
        sqlQueries = statTableQueries;
    }

    protected void create(@NotNull Connection connection) {
        try (Statement statement = connection.createStatement()) {
            statement.execute(sqlQueries.createTable());
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void update(List<MyStatistic> statistics, Connection connection) {
        if (statistics != null) {
            List<MyStatistic> currentlyStored = getAllStatistics(connection).values().stream().toList();
            List<MyStatistic> newValues = statistics.stream()
                    .filter(Predicate.not(currentlyStored::contains)).toList();

            insert(newValues, connection);
        }
    }

    public int getStatisticCount(@NotNull Connection connection) {
        int count = 0;
        try (Statement statement = connection.createStatement()) {
            ResultSet resultSet = statement.executeQuery(sqlQueries.selectCount());
            resultSet.next();
            count = resultSet.getInt(1);
            resultSet.close();
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        return count;
    }

    public @NotNull HashMap<Integer, MyStatistic> getAllStatistics(@NotNull Connection connection) {
        HashMap<Integer, MyStatistic> allStats = new HashMap<>();
        try (Statement statement = connection.createStatement()) {
            ResultSet resultSet = statement.executeQuery(sqlQueries.selectAll());

            while (resultSet.next()) {
                allStats.put(
                        resultSet.getInt(sqlQueries.ID_COLUMN),
                        new MyStatistic(
                                resultSet.getString(sqlQueries.NAME_COLUMN),
                                MyStatType.fromString(resultSet.getString(sqlQueries.TYPE_COLUMN))));
            }
            resultSet.close();
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        return allStats;
    }

    private void insert(@NotNull List<MyStatistic> statistics, @NotNull Connection connection) {
        try (PreparedStatement statement = connection.prepareStatement(sqlQueries.insert())){
            for (MyStatistic stat : statistics) {
                statement.setString(1, stat.name());
                statement.setString(2, stat.type().toString().toUpperCase(Locale.ENGLISH));
                statement.addBatch();
            }
            statement.executeBatch();
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
    }
}