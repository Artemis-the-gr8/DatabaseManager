package com.artemis.the.gr8.databasemanager;

import com.artemis.the.gr8.databasemanager.models.MyStatType;
import com.artemis.the.gr8.databasemanager.models.MyStatistic;
import com.artemis.the.gr8.databasemanager.models.MySubStatistic;
import com.artemis.the.gr8.databasemanager.sql.SQL;
import org.jetbrains.annotations.NotNull;

import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.stream.Collectors;

public class StatCombinationDAO {

    public StatCombinationDAO() {
    }

    public void update(@NotNull Connection connection) {
        ArrayList<int[]> combinations = getAllValidCombinations(connection);
        ArrayList<int[]> currentlyStored = getCurrentContentsOfCombinationTable(connection);

        ArrayList<int[]> newValues =
                combinations.stream()
                        .filter(entry -> currentlyStored.stream()
                        .noneMatch(
                                storedEntry -> Arrays.equals(storedEntry, entry)))
                        .collect(Collectors.toCollection(ArrayList::new));

        insert(newValues, connection);
    }



    protected @NotNull ArrayList<int[]> getCurrentContentsOfCombinationTable(@NotNull Connection connection) {
        ArrayList<int[]> combinations = new ArrayList<>();
        try (Statement statement = connection.createStatement()) {
            ResultSet resultSet = statement.executeQuery(SQL.StatCombinationTable.selectAll());

            while (resultSet.next()) {
                combinations.add(
                        new int[]{resultSet.getInt(SQL.StatCombinationTable.STAT_ID_COLUMN),
                                resultSet.getInt(SQL.StatCombinationTable.SUB_STAT_ID_COLUMN)});
            }
            resultSet.close();
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        return combinations;
    }

    protected @NotNull ArrayList<int[]> getAllValidCombinations(@NotNull Connection connection) {
        HashMap<MyStatistic, Integer> storedStats = getAllStoredStats(connection);
        HashMap<MySubStatistic, Integer> entitySubStats = getEntitySubStats(connection);
        HashMap<MySubStatistic, Integer> itemSubStats = getItemSubStats(connection);
        HashMap<MySubStatistic, Integer> blockSubStats = getBlockSubStats(connection);
        ArrayList<int[]> combinations = new ArrayList<>();

        storedStats.forEach((stat, statID) -> {
            switch (stat.type()) {
                case CUSTOM -> combinations.add(new int[]{statID, 0});
                case ENTITY ->
                        entitySubStats.forEach((subStat, subStatID) ->
                                combinations.add(new int[]{statID, subStatID}));
                case ITEM ->
                        itemSubStats.forEach((subStat, subStatID) ->
                                combinations.add(new int[]{statID, subStatID}));
                case BLOCK ->
                        blockSubStats.forEach((subStat, subStatID) ->
                                combinations.add(new int[]{statID, subStatID}));
            }
        });

       return combinations;
    }

    private @NotNull HashMap<MyStatistic, Integer> getAllStoredStats(@NotNull Connection connection) {
        HashMap<MyStatistic, Integer> stats = new HashMap<>();
        try (Statement statement = connection.createStatement()) {
            ResultSet resultSet = statement.executeQuery(SQL.StatTable.selectAll());

            while (resultSet.next()) {
                stats.put(
                        new MyStatistic(
                                resultSet.getString(SQL.StatTable.NAME_COLUMN),
                                MyStatType.fromString(resultSet.getString(SQL.StatTable.TYPE_COLUMN))),
                        resultSet.getInt(SQL.UNIVERSAL_ID_COLUMN));
            }
            resultSet.close();
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        return stats;
    }

    private @NotNull HashMap<MySubStatistic, Integer> getEntitySubStats(@NotNull Connection connection) {
        return getSubStats(connection, SQL.SubStatTable.selectEntityType());
    }

    private @NotNull HashMap<MySubStatistic, Integer> getItemSubStats(@NotNull Connection connection) {
        return getSubStats(connection, SQL.SubStatTable.selectItemType());
    }

    private @NotNull HashMap<MySubStatistic, Integer> getBlockSubStats(@NotNull Connection connection) {
        return getSubStats(connection, SQL.SubStatTable.selectBlockType());
    }

    private @NotNull HashMap<MySubStatistic, Integer> getSubStats(@NotNull Connection connection, String selectStatement) {
        HashMap<MySubStatistic, Integer> subStats = new HashMap<>();
        try (Statement statement = connection.createStatement()) {
            ResultSet resultSet = statement.executeQuery(selectStatement);

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

    private void insert(@NotNull ArrayList<int[]> combinations, @NotNull Connection connection) {
        try (PreparedStatement statement = connection.prepareStatement(SQL.StatCombinationTable.insert())) {
            for (int[] combination : combinations) {
                statement.setInt(1, combination[0]);

                if (combination[1] == 0) {
                    statement.setNull(2, Types.NULL);
                } else {
                    statement.setInt(2, combination[1]);
                }
                statement.addBatch();
            }
            statement.executeBatch();
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
    }
}