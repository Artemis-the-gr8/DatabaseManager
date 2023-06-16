package com.artemis.the.gr8.databasemanager;

import com.artemis.the.gr8.databasemanager.models.MyPlayer;
import com.artemis.the.gr8.databasemanager.models.MyStatistic;
import com.artemis.the.gr8.databasemanager.models.MySubStatistic;
import com.artemis.the.gr8.databasemanager.sql.StatValueTableQueries;
import org.jetbrains.annotations.NotNull;

import java.sql.*;
import java.util.*;

public class StatValueDAO {

    private final PlayerDAO playerDAO;
    private final StatCombinationDAO statCombinationDAO;
    private final StatValueTableQueries sqlQueries;

    public StatValueDAO(PlayerDAO playerDAO, StatCombinationDAO statCombinationDAO, StatValueTableQueries statValueTableQueries) {
        this.playerDAO = playerDAO;
        this.statCombinationDAO = statCombinationDAO;
        sqlQueries = statValueTableQueries;
    }

    protected void create(@NotNull Connection connection) {
        try (Statement statement = connection.createStatement()) {
            statement.execute(sqlQueries.createTable());
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateStatsForPlayer(int playerID, @NotNull HashMap<Integer, Integer> valuesWithCombinationID, Connection connection) {
        ArrayList<Integer> currentlyStored = getAllEntryIds(playerID, connection);
        HashMap<Integer, Integer> valuesToInsert = new HashMap<>();
        HashMap<Integer, Integer> valuesToUpdate = new HashMap<>();

        valuesWithCombinationID.forEach((combinationId, value) -> {
            if (!currentlyStored.contains(combinationId)) {
                valuesToInsert.put(combinationId, value);
            } else {
                valuesToUpdate.put(combinationId, value);
            }
        });

        insert(playerID, valuesToInsert, connection);
        update(playerID, valuesToUpdate, connection);
    }

    public void updateStatWithSubStatForPlayer(MyPlayer player, MyStatistic statistic, @NotNull HashMap<MySubStatistic, Integer> values, Connection connection) {
        int playerID = getPlayerId(player, connection);
        ArrayList<Integer> currentlyStored = getAllEntryIds(playerID, connection);
        HashMap<Integer, Integer> valuesToInsert = new HashMap<>();
        HashMap<Integer, Integer> valuesToUpdate = new HashMap<>();

        values.forEach((subStat, value) -> {
                int combinationId = statCombinationDAO.getOrGenerateCombinationID(statistic, subStat, connection);
                if (!currentlyStored.contains(combinationId)) {
                    valuesToInsert.put(combinationId, value);
                } else {
                    valuesToUpdate.put(combinationId, value);
                }
        });

        insert(playerID, valuesToInsert, connection);
        update(playerID, valuesToUpdate, connection);
    }

    private int getPlayerId(@NotNull MyPlayer player, Connection connection) {
        int playerID = playerDAO.getOrGeneratePlayerID(player, connection);
        if (playerID == 0) {
            playerDAO.insert(player, connection);
            playerID = playerDAO.getOrGeneratePlayerID(player, connection);
        }
        return playerID;
    }

    private @NotNull ArrayList<Integer> getAllEntryIds(int playerId, @NotNull Connection connection) {
        ArrayList<Integer> entries = new ArrayList<>();
        try (Statement statement = connection.createStatement()) {
            ResultSet resultSet = statement.executeQuery(sqlQueries.selectAllEntryIds(playerId));

            while (resultSet.next()) {
                entries.add(resultSet.getInt(1));
            }
            resultSet.close();
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        return entries;
    }

    private void insert(int playerId, @NotNull HashMap<Integer, Integer> values, @NotNull Connection connection) {
        try (PreparedStatement statement = connection.prepareStatement(sqlQueries.insert(playerId))) {
            for (Map.Entry<Integer, Integer> entry : values.entrySet()) {
                statement.setInt(1, entry.getKey());
                statement.setInt(2, entry.getValue());
                statement.addBatch();
            }
            statement.executeBatch();
        }
        catch (SQLException e) {
            e.printStackTrace();
            System.out.println("SQLState: " + e.getSQLState());
        }
    }

    private void update(int playerId, @NotNull HashMap<Integer, Integer> values, @NotNull Connection connection) {
        try (PreparedStatement statement = connection.prepareStatement(sqlQueries.updateForPlayer(playerId))) {
            for (Map.Entry<Integer, Integer> entry : values.entrySet()) {
                statement.setInt(1, entry.getValue());
                statement.setInt(2, entry.getKey());
                statement.addBatch();
            }
            statement.executeBatch();
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
    }
}