package com.artemis.the.gr8.databasemanager;

import com.artemis.the.gr8.databasemanager.models.MyPlayer;
import com.artemis.the.gr8.databasemanager.models.MyStatistic;
import com.artemis.the.gr8.databasemanager.models.MySubStatistic;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.HashMap;
import java.util.List;

public interface DatabaseManager {

    @Contract("_, _, _ -> new")
    static @NotNull DatabaseManager getMySQLManager(String URL, String username, String password) {
        return Database.getMySQLDatabase(URL, username, password);
    }

    static @NotNull DatabaseManager getSQLiteManager(@NotNull File pluginDataFolder) {
        String URL = "jdbc:sqlite:" +
                pluginDataFolder.getPath() +
                "/stats.db";
        return Database.getSQLiteDatabase(URL);
    }

    void updateStatistics(List<MyStatistic> statistics, List<MySubStatistic> subStatistics);

    void updatePlayers(List<MyPlayer> players);

    /** For the given player, update the values in the database
     * for all provided statistics.
     * @param player the player whose statistics should be updated
     * @param values a <code>HashMap</code> with statistic-value pairs
     *               for all statistics that should be updated
     *               (for example "animals_bred, 20)
     */
    void updateStatsForPlayer(MyPlayer player, @NotNull HashMap<MyStatistic, Integer> values);

    /**
     * For the given player and the given statistic (of type ENTITY),
     * update the values in the database for all provided entities.
     * @param player the player whose statistics should be updated
     * @param statistic a statistic of type ENTITY
     *                       (for example "kill_entity")
     * @param values a <code>HashMap</code> with substatistic-value pairs for all
     *               substatistics that should be updated
     *               (for example "zombie, 15")
     */
    void updateEntityStatForPlayer(MyPlayer player, @NotNull MyStatistic statistic, @NotNull HashMap<MySubStatistic, Integer> values);

    /**
     * For the given player and the given statistic (of type ITEM),
     * update the values in the database for all provided items.
     * @param player the player whose statistics should be updated
     * @param statistic a statistic of type ITEM
     *                       (for example "craft_item")
     * @param values a <code>HashMap</code> with substatistic-value pairs for all
     *               substatistics that should be updated
     *               (for example "birch_boat, 15")
     */
    void updateItemStatForPlayer(MyPlayer player, @NotNull MyStatistic statistic, @NotNull HashMap<MySubStatistic, Integer> values);

    /**
     * For the given player and the given statistic (of type BLOCK),
     * update the values in the database for all provided blocks.
     * @param player the player whose statistics should be updated
     * @param statistic a statistic of type BLOCK
     *                       (for example "mine_block")
     * @param values a <code>HashMap</code> with substatistic-value pairs for all
     *               substatistics that should be updated
     *               (for example "stone, 15")
     */
    void updateBlockStatForPlayer(MyPlayer player, @NotNull MyStatistic statistic, @NotNull HashMap<MySubStatistic, Integer> values);
}