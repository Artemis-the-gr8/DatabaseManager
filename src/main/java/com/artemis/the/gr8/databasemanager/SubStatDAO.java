package com.artemis.the.gr8.databasemanager;

import com.artemis.the.gr8.databasemanager.models.MyStatType;
import com.artemis.the.gr8.databasemanager.models.MySubStatistic;
import com.artemis.the.gr8.databasemanager.sql.SubStatTableQueries;
import org.jetbrains.annotations.NotNull;

import java.sql.*;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.function.Predicate;

public class SubStatDAO {

    private final SubStatTableQueries sqlQueries;

    public SubStatDAO(SubStatTableQueries subStatTableQueries) {
        sqlQueries = subStatTableQueries;
    }

    protected void create(@NotNull Connection connection) {
        try (Statement statement = connection.createStatement()) {
            statement.execute(sqlQueries.createTable());
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void update(List<MySubStatistic> subStatistics, @NotNull Connection connection) {
        if (subStatistics != null) {
            List<MySubStatistic> currentlyStored = getAllSubStats(connection).values().stream().toList();
            List<MySubStatistic> newValues = subStatistics.stream()
                            .filter(Predicate.not(currentlyStored::contains))
                                    .toList();

            insert(newValues, connection);
        }
    }

    public int getSubStatId(@NotNull MySubStatistic subStatistic, @NotNull Connection connection) {
        int id = 0;
        try (Statement statement = connection.createStatement()) {
            String query = switch (subStatistic.type()) {
                case BLOCK -> sqlQueries.selectBlockIdFromName(subStatistic.name());
                case ITEM -> sqlQueries.selectItemIdFromName(subStatistic.name());
                case ENTITY -> sqlQueries.selectEntityIdFromName(subStatistic.name());
                case CUSTOM -> null;
            };
            ResultSet resultSet = statement.executeQuery(query);
            if (resultSet.next()) {
                id = resultSet.getInt(1);
            }
            resultSet.close();
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        return id;
    }

    public int getAllSubStatsCount(@NotNull Connection connection) {
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

    public @NotNull HashMap<Integer, MySubStatistic> getAllSubStats(@NotNull Connection connection) {
        return getSubStats(connection, sqlQueries.selectAll());
    }

    public @NotNull HashMap<Integer, MySubStatistic> getEntitySubStats(@NotNull Connection connection) {
        return getSubStats(connection, sqlQueries.selectEntityType());
    }

    public @NotNull HashMap<Integer, MySubStatistic> getItemSubStats(@NotNull Connection connection) {
        return getSubStats(connection, sqlQueries.selectItemType());
    }

    public @NotNull HashMap<Integer, MySubStatistic> getBlockSubStats(@NotNull Connection connection) {
        return getSubStats(connection, sqlQueries.selectBlockType());
    }

    private @NotNull HashMap<Integer, MySubStatistic> getSubStats(@NotNull Connection connection, String selectStatement) {
        HashMap<Integer, MySubStatistic> allStats = new HashMap<>();
        try (Statement statement = connection.createStatement()) {
            ResultSet resultSet = statement.executeQuery(selectStatement);

            while (resultSet.next()) {
                allStats.put(
                        resultSet.getInt(sqlQueries.ID_COLUMN),
                        new MySubStatistic(
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

    private void insert(@NotNull List<MySubStatistic> subStats, @NotNull Connection connection) {
        try (PreparedStatement statement = connection.prepareStatement(sqlQueries.insert())) {
            for (MySubStatistic subStat : subStats) {
                statement.setString(1, subStat.name());
                statement.setString(2, subStat.type().toString().toUpperCase(Locale.ENGLISH));
                statement.addBatch();
            }
            statement.executeBatch();
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
    }
}