package com.artemis.the.gr8.databasemanager;

import com.artemis.the.gr8.databasemanager.datamodels.MyPlayer;
import com.artemis.the.gr8.databasemanager.datamodels.MyStatistic;
import com.artemis.the.gr8.databasemanager.datamodels.MySubStatistic;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.List;

public class DatabaseManager {

    //local MySQL URL for testing: jdbc:mysql://localhost:3306/minecraftstatdb
    private final Database database;

    private DatabaseManager(String URL, String username, String password) {
        database = new Database(URL, username, password);
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

    public void setUp(List<MyStatistic> statistics, List<MySubStatistic> subStatistics) {
        database.updateStatistics(statistics, subStatistics);
    }

    public void updateAllTables(List<MyPlayer> players, List<MyStatistic> statistics, List<MySubStatistic> subStatistics) {

    }

    public void updatePlayerTable(List<MyPlayer> players) {

    }

    public void updateStatTable(List<MyStatistic> statistics) {

    }

    public void updateSubStatTable(List<MySubStatistic> subStatistics) {

    }

    private void setup() {

    }
}