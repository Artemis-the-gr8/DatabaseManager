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

public class StatCombinationRepository {

    public StatCombinationRepository() {
    }

    public void update(HashMap<MyStatistic, Integer> stats, HashMap<MySubStatistic, Integer> subStats, @NotNull Connection connection) {
        ArrayList<int[]> currentlyStored = getAllStoredCombinations(connection);
        ArrayList<int[]> combinations = findAllValidCombinations(stats, subStats);
        ArrayList<int[]> newValues = combinations.stream()
                .filter(entry -> currentlyStored.stream()
                        .noneMatch(
                                storedEntry -> Arrays.equals(storedEntry, entry)))
                .collect(Collectors.toCollection(ArrayList::new));

        insert(newValues, connection);
    }

    protected @NotNull ArrayList<int[]> getAllStoredCombinations(@NotNull Connection connection) {
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

    protected @NotNull ArrayList<int[]> findAllValidCombinations(@NotNull HashMap<MyStatistic, Integer> stats, @NotNull HashMap<MySubStatistic, Integer> subStats) {
        ArrayList<int[]> combinations = new ArrayList<>();
        HashMap<MyStatistic, Integer> copyOfStats = new HashMap<>(stats);

        stats.forEach((stat, statID) -> {
            if (stat.statType() == MyStatType.CUSTOM) {
                combinations.add(new int[]{statID, 0});
                copyOfStats.remove(stat);
            }
        });

       subStats.forEach((subStat, subStatID) -> {
           switch (subStat.subStatType()) {
               case ENTITY -> copyOfStats.forEach((stat, statID) -> {
                   if (stat.statType() == MyStatType.ENTITY) {
                       combinations.add(new int[]{statID, subStatID});
                   }
               });
               case ITEM -> copyOfStats.forEach((stat, statID) -> {
                   if (stat.statType() == MyStatType.ITEM) {
                       combinations.add(new int[]{statID, subStatID});
                   }
               });
               case BLOCK -> copyOfStats.forEach((stat, statID) -> {
                   if (stat.statType() == MyStatType.BLOCK) {
                       combinations.add(new int[]{statID, subStatID});
                   }
               });
           }
       });
       return combinations;
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