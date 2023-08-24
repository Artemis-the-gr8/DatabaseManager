package com.artemis.the.gr8.databasemanager;

import com.artemis.the.gr8.databasemanager.models.MyPlayer;
import com.artemis.the.gr8.databasemanager.models.MyStatistic;
import com.artemis.the.gr8.databasemanager.models.MySubStatistic;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.HashMap;
import java.util.List;

/**
 The outgoing API
 */
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
     * for all provided statistics. The statistic must be of
     * type CUSTOM (sometimes called UNTYPED).
     *
     * @param player the player whose statistics should be updated
     * @param values a <code>HashMap</code> with statistic-value pairs
     *               for all statistics that should be updated
     *               (for example "animals_bred, 20)
     */
    void updateStatsForPlayer(MyPlayer player, @NotNull HashMap<MyStatistic, Integer> values);

    /**
     * For the given player and the given statistic, update the
     * values in the database for all provided sub-statistics.
     * This method is only for statistics that have a sub-statistic,
     * so statistics of type ENTITY, BLOCK or ITEM.
     *
     * @param player the player whose statistics should be updated
     * @param statistic a statistic of type ENTITY, BLOCK or ITEM
     *                       (for example "kill_entity")
     * @param values a <code>HashMap</code> with substatistic-value pairs for all
     *               substatistics that should be updated
     *               (for example "zombie, 15")
     */
    void updateStatWithSubStatsForPlayer(MyPlayer player, @NotNull MyStatistic statistic, @NotNull HashMap<MySubStatistic, Integer> values);
}