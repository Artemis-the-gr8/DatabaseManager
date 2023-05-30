package com.artemis.the.gr8.databasemanager;

import com.artemis.the.gr8.databasemanager.models.MyStatType;
import com.artemis.the.gr8.databasemanager.models.MySubStatistic;
import com.artemis.the.gr8.databasemanager.sql.SQL;
import org.jetbrains.annotations.NotNull;

import java.sql.*;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.function.Predicate;

public class SubStatDAO {

    public SubStatDAO() {
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

    public @NotNull HashMap<Integer, MySubStatistic> getAllSubStats(@NotNull Connection connection) {
        return getSubStats(connection, SQL.SubStatTable.selectAll());
    }

    public @NotNull HashMap<Integer, MySubStatistic> getEntitySubStats(@NotNull Connection connection) {
        return getSubStats(connection, SQL.SubStatTable.selectEntityType());
    }

    public @NotNull HashMap<Integer, MySubStatistic> getItemSubStats(@NotNull Connection connection) {
        return getSubStats(connection, SQL.SubStatTable.selectItemType());
    }

    public @NotNull HashMap<Integer, MySubStatistic> getBlockSubStats(@NotNull Connection connection) {
        return getSubStats(connection, SQL.SubStatTable.selectBlockType());
    }

    private @NotNull HashMap<Integer, MySubStatistic> getSubStats(@NotNull Connection connection, String selectStatement) {
        HashMap<Integer, MySubStatistic> allStats = new HashMap<>();
        try (Statement statement = connection.createStatement()) {
            ResultSet resultSet = statement.executeQuery(selectStatement);

            while (resultSet.next()) {
                allStats.put(
                        resultSet.getInt(SQL.UNIVERSAL_ID_COLUMN),
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