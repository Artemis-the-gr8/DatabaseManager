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

    /**
     * For the given player and the given statistic (of type ENTITY),
     * update the values in the database for all provided entities.
     * @param player the player whose statistics should be updated
     * @param entityTypeStat a statistic of type ENTITY
     *                       (for example "kill_entity")
     * @param values a <code>HashMap</code> with substatistic-value pairs for all
     *               substatistics that should be updated
     *               (for example "zombie, 15")
     */
    public void updateEntityStatForPlayer(MyPlayer player, MyStatistic entityTypeStat, @NotNull HashMap<MySubStatistic, Integer> values, Connection connection) {
        updateStatWithSubStatForPlayer(player, entityTypeStat, values, connection);
    }

    /**
     * For the given player and the given statistic (of type BLOCK),
     * update the values in the database for all provided blocks.
     * @param player the player whose statistics should be updated
     * @param blockTypeStat a statistic of type BLOCK
     *                       (for example "mine_block")
     * @param values a <code>HashMap</code> with substatistic-value pairs for all
     *               substatistics that should be updated
     *               (for example "stone, 15")
     */
    public void updateBlockStatForPlayer(MyPlayer player, MyStatistic blockTypeStat, @NotNull HashMap<MySubStatistic, Integer> values, Connection connection) {
        updateStatWithSubStatForPlayer(player, blockTypeStat, values, connection);
    }

    /**
     * For the given player and the given statistic (of type ITEM),
     * update the values in the database for all provided items.
     * @param player the player whose statistics should be updated
     * @param itemTypeStat a statistic of type ITEM
     *                       (for example "craft_item")
     * @param values a <code>HashMap</code> with substatistic-value pairs for all
     *               substatistics that should be updated
     *               (for example "birch_boat, 15")
     */
    public void updateItemStatForPlayer(MyPlayer player, MyStatistic itemTypeStat, @NotNull HashMap<MySubStatistic, Integer> values, Connection connection) {
        updateStatWithSubStatForPlayer(player, itemTypeStat, values, connection);
    }

    /** For the given player, update the values in the database
     * for all provided statistics.
     * @param player the player whose statistics should be updated
     * @param values a <code>HashMap</code> with statistic-value pairs
     *               for all statistics that should be updated
     *               (for example "animals_bred, 20)
     */
    public void updateStatsForPlayer(MyPlayer player, @NotNull HashMap<MyStatistic, Integer> values, Connection connection) {
        int playerID = getPlayerId(player, connection);
        ArrayList<Integer> currentlyStored = getAllEntryIds(playerID, connection);
        HashMap<Integer, Integer> valuesToInsert = new HashMap<>();
        HashMap<Integer, Integer> valuesToUpdate = new HashMap<>();

        values.forEach((stat, value) -> {
            int combinationId = statCombinationDAO.getStatCombinationID(stat, null, connection);
            if (!currentlyStored.contains(combinationId)) {
                valuesToInsert.put(combinationId, value);
            } else {
                valuesToUpdate.put(combinationId, value);
            }
        });

        insert(playerID, valuesToInsert, connection);
        update(playerID, valuesToUpdate, connection);
    }

    private void updateStatWithSubStatForPlayer(MyPlayer player, MyStatistic statistic, @NotNull HashMap<MySubStatistic, Integer> values, Connection connection) {
        int playerID = getPlayerId(player, connection);
        ArrayList<Integer> currentlyStored = getAllEntryIds(playerID, connection);
        HashMap<Integer, Integer> valuesToInsert = new HashMap<>();
        HashMap<Integer, Integer> valuesToUpdate = new HashMap<>();

        values.forEach((subStat, value) -> {
                int combinationId = statCombinationDAO.getStatCombinationID(statistic, subStat, connection);
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
        int playerID = playerDAO.getPlayerID(player.uuid(), connection);
        if (playerID == 0) {
            playerDAO.insert(player, connection);
            playerID = playerDAO.getPlayerID(player.uuid(), connection);
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