package com.artemis.the.gr8.databasemanager;

import com.artemis.the.gr8.databasemanager.models.MyPlayer;
import com.artemis.the.gr8.databasemanager.models.MyStatistic;
import com.artemis.the.gr8.databasemanager.models.MySubStatistic;

import java.io.File;
import java.util.List;

public class DatabaseManager {

    //local MySQL URL for testing: jdbc:mysql://localhost:3306/minecraftstatdb
    private final Database database;

    private DatabaseManager(String URL, String username, String password) {
        database = new Database(URL, username, password);
    }

    public static DatabaseManager getMySQLManager(String URL, String username, String password) {
        return new DatabaseManager(URL, username, password);
    }

    public static DatabaseManager getSQLiteManager(File pluginDataFolder) {
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
}