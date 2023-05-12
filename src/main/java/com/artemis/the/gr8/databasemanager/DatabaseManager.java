package com.artemis.the.gr8.databasemanager;

import com.artemis.the.gr8.databasemanager.models.MyPlayer;
import com.artemis.the.gr8.databasemanager.models.MyStatistic;
import com.artemis.the.gr8.databasemanager.models.MySubStatistic;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.List;

public class DatabaseManager {

    private final Database database;

    private DatabaseManager(String URL, String username, String password) {
        database = new Database(URL, username, password);
        database.setUp();
    }

    @Contract("_, _, _ -> new")
    public static @NotNull DatabaseManager getMySQLManager(String URL, String username, String password) {
        return new DatabaseManager(URL, username, password);
    }

    public static @NotNull DatabaseManager getSQLiteManager(@NotNull File pluginDataFolder) {
        String URL = "jdbc:sqlite:" +
                pluginDataFolder.getPath() +
                "/stats.db";
        return new DatabaseManager(URL, null, null);
    }

    public void updateStatistics(List<MyStatistic> statistics, List<MySubStatistic> subStatistics) {
        database.updateStatistics(statistics, subStatistics);
    }

    public void updatePlayers(List<MyPlayer> players) {
        database.updatePlayers(players);
    }
}