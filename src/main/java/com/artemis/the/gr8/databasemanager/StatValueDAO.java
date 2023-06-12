package com.artemis.the.gr8.databasemanager;

import com.artemis.the.gr8.databasemanager.models.MyStatistic;
import com.artemis.the.gr8.databasemanager.models.MySubStatistic;
import com.artemis.the.gr8.databasemanager.sql.StatValueTableQueries;
import org.jetbrains.annotations.NotNull;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

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

    /** For the given player, update the values in the database
     * for all provided statistics.
     * @param playerUUID the UUID of the player whose statistics should be updated
     * @param values a <code>HashMap</code> with statistic-value pairs
     *               for all statistics that should be updated
     *               (for example "animals_bred, 20)
     */
    public void updateStatsForPlayer(UUID playerUUID, @NotNull HashMap<MyStatistic, Integer> values, Connection connection) {
        int playerID = playerDAO.getPlayerID(playerUUID, connection);
        HashMap<Integer, Integer> valuesWithID = new HashMap<>();

        values.forEach((stat, value) ->
                valuesWithID.put(
                        statCombinationDAO.getStatCombinationID(stat, null, connection),
                        value));
        insert(playerID, valuesWithID, connection);
    }

    /**
     * For the given player and the given statistic (of type ENTITY),
     * update the values in the database for all provided entities.
     * @param playerUUID the UUID of the player whose statistics should be updated
     * @param entityTypeStat a statistic of type ENTITY
     *                       (for example "kill_entity")
     * @param values a <code>HashMap</code> with substatistic-value pairs for all
     *               substatistics that should be updated
     *               (for example "zombie, 15")
     */
    public void updateEntityStatForPlayer(UUID playerUUID, MyStatistic entityTypeStat, @NotNull HashMap<MySubStatistic, Integer> values, Connection connection) {
        updateStatWithSubStatForPlayer(playerUUID, entityTypeStat, values, connection);
    }

    /**
     * For the given player and the given statistic (of type BLOCK),
     * update the values in the database for all provided blocks.
     * @param playerUUID the UUID of the player whose statistics should be updated
     * @param blockTypeStat a statistic of type BLOCK
     *                       (for example "mine_block")
     * @param values a <code>HashMap</code> with substatistic-value pairs for all
     *               substatistics that should be updated
     *               (for example "stone, 15")
     */
    public void updateBlockStatForPlayer(UUID playerUUID, MyStatistic blockTypeStat, @NotNull HashMap<MySubStatistic, Integer> values, Connection connection) {
        updateStatWithSubStatForPlayer(playerUUID, blockTypeStat, values, connection);
    }

    /**
     * For the given player and the given statistic (of type ITEM),
     * update the values in the database for all provided items.
     * @param playerUUID the UUID of the player whose statistics should be updated
     * @param itemTypeStat a statistic of type ITEM
     *                       (for example "craft_item")
     * @param values a <code>HashMap</code> with substatistic-value pairs for all
     *               substatistics that should be updated
     *               (for example "birch_boat, 15")
     */
    public void updateItemStatForPlayer(UUID playerUUID, MyStatistic itemTypeStat, @NotNull HashMap<MySubStatistic, Integer> values, Connection connection) {
        updateStatWithSubStatForPlayer(playerUUID, itemTypeStat, values, connection);
    }

    private void updateStatWithSubStatForPlayer(UUID uuid, MyStatistic statistic, @NotNull HashMap<MySubStatistic, Integer> values, Connection connection) {
        int playerID = playerDAO.getPlayerID(uuid, connection);
        HashMap<Integer, Integer> valuesWithID = new HashMap<>();

        values.forEach((subStat, value) ->
                valuesWithID.put(
                        statCombinationDAO.getStatCombinationID(statistic, subStat, connection),
                        value));
        insert(playerID, valuesWithID, connection);
    }

    private void insert(int playerId, @NotNull HashMap<Integer, Integer> values, @NotNull Connection connection) {
        try (PreparedStatement statement = connection.prepareStatement(sqlQueries.insert())) {
            for (Map.Entry<Integer, Integer> entry : values.entrySet()) {
                statement.setInt(1, playerId);
                statement.setInt(2, entry.getKey());
                statement.setInt(3, entry.getValue());
                statement.addBatch();
            }
            statement.executeBatch();
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
    }
}