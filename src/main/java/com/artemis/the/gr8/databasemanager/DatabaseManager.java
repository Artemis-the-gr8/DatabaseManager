package com.artemis.the.gr8.databasemanager;

import com.artemis.the.gr8.databasemanager.models.MyPlayer;
import com.artemis.the.gr8.databasemanager.models.MyStatistic;
import com.artemis.the.gr8.databasemanager.models.MySubStatistic;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.List;

public interface DatabaseManager {

    @Contract("_, _, _ -> new")
    static @NotNull Database getMySQLManager(String URL, String username, String password) {
        return Database.getMySQLDatabase(URL, username, password);
    }

    static @NotNull Database getSQLiteManager(@NotNull File pluginDataFolder) {
        String URL = "jdbc:sqlite:" +
                pluginDataFolder.getPath() +
                "/stats.db";
        return Database.getSQLiteDatabase(URL);
    }

    void updateStatistics(List<MyStatistic> statistics, List<MySubStatistic> subStatistics);

    void updatePlayers(List<MyPlayer> players);
}