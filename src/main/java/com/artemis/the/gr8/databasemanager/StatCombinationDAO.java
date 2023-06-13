package com.artemis.the.gr8.databasemanager;

import com.artemis.the.gr8.databasemanager.models.MyStatistic;
import com.artemis.the.gr8.databasemanager.models.MySubStatistic;
import com.artemis.the.gr8.databasemanager.sql.StatCombinationTableQueries;
import org.jetbrains.annotations.NotNull;

import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.stream.Collectors;

public class StatCombinationDAO {

    private final StatDAO statDAO;
    private final SubStatDAO subStatDAO;
    private final StatCombinationTableQueries sqlQueries;

    public StatCombinationDAO(StatDAO statDAO, SubStatDAO subStatDAO, StatCombinationTableQueries subStatTableQueries) {
        this.statDAO = statDAO;
        this.subStatDAO = subStatDAO;
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

    public int getStatCombinationID(@NotNull MyStatistic statistic, MySubStatistic subStatistic, Connection connection) {
        int statId = statDAO.getStatisticID(statistic.name(), connection);
        int subStatId = subStatistic == null ? 0 : subStatDAO.getSubStatId(subStatistic, connection);
        int statCombinationId = 0;

        try (Statement statement = connection.createStatement()) {
            ResultSet resultSet =
                    statement.executeQuery(
                            sqlQueries.selectIdFromStatAndSubStatId(statId, subStatId));
            if (resultSet.next()) {
                statCombinationId = resultSet.getInt(1);
            }
            resultSet.close();
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        if (statCombinationId == 0) {
            if (subStatistic == null) {
                System.out.println("Couldn't find combination-id for " + statistic.name() + "!");
            } else {
                System.out.println("Couldn't find combination-id for " + statistic.name() + " (id: " + statId + ") and " + subStatistic.name() + "(id: " + subStatId + ")!");
            }
        }
        return statCombinationId;
    }

    protected int getStatCombinationCount(@NotNull Connection connection) {
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

    private @NotNull ArrayList<int[]> getCurrentContentsOfCombinationTable(@NotNull Connection connection) {
        ArrayList<int[]> combinations = new ArrayList<>();
        try (Statement statement = connection.createStatement()) {
            ResultSet resultSet = statement.executeQuery(sqlQueries.selectAll());

            while (resultSet.next()) {
                combinations.add(
                        new int[]{resultSet.getInt(sqlQueries.STAT_ID_COLUMN),
                                resultSet.getInt(sqlQueries.SUB_STAT_ID_COLUMN)});
            }
            resultSet.close();
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        return combinations;
    }

    private @NotNull ArrayList<int[]> getAllValidCombinations(@NotNull Connection connection) {
        HashMap<Integer, MyStatistic> storedStats = statDAO.getAllStatistics(connection);
        HashMap<Integer, MySubStatistic> entitySubStats = subStatDAO.getEntitySubStats(connection);
        HashMap<Integer, MySubStatistic> itemSubStats = subStatDAO.getItemSubStats(connection);
        HashMap<Integer, MySubStatistic> blockSubStats = subStatDAO.getBlockSubStats(connection);

        ArrayList<int[]> combinations = new ArrayList<>();

        storedStats.forEach((statID, stat) -> {
            switch (stat.type()) {
                case CUSTOM -> combinations.add(new int[]{statID, 0});
                case ENTITY ->
                        entitySubStats.forEach((subStatID, subStat) ->
                                combinations.add(new int[]{statID, subStatID}));
                case ITEM ->
                        itemSubStats.forEach((subStatID, subStat) ->
                                combinations.add(new int[]{statID, subStatID}));
                case BLOCK ->
                        blockSubStats.forEach((subStatID, subStat) ->
                                combinations.add(new int[]{statID, subStatID}));
            }
        });

       return combinations;
    }

    private void insert(@NotNull ArrayList<int[]> combinations, @NotNull Connection connection) {
        try (PreparedStatement statement = connection.prepareStatement(sqlQueries.insert())) {
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