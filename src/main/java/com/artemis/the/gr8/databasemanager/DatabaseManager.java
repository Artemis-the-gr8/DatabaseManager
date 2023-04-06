package com.artemis.the.gr8.databasemanager;

import com.artemis.the.gr8.databasemanager.datamodels.MyPlayer;
import com.artemis.the.gr8.databasemanager.datamodels.MyStatistic;
import com.artemis.the.gr8.databasemanager.datamodels.MySubStatistic;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.List;

public interface DatabaseManager {

    //local MySQL URL for testing: jdbc:mysql://localhost:3306/minecraftstatdb
    @Contract("_, _, _ -> new")
    static @NotNull Main getMySQLManager(String URL, String username, String password) {
        return new Main(URL, username, password);
    }

    static @NotNull Main getSQLiteManager(@NotNull File pluginDataFolder) {
        String URL = "jdbc:sqlite:" +
                pluginDataFolder.getPath() +
                "/stats.db";
        return new Main(URL, null, null);
    }

    void updateStatistics(List<MyStatistic> statistics, List<MySubStatistic> subStatistics);

    void updatePlayers(List<MyPlayer> players);
}