package com.artemis.the.gr8.databasemanager;

import com.artemis.the.gr8.databasemanager.models.MyPlayer;
import com.artemis.the.gr8.databasemanager.models.MyStatistic;
import com.artemis.the.gr8.databasemanager.models.MySubStatistic;

import java.io.File;
import java.util.List;

public class DatabaseManager {

    private static volatile DatabaseManager instance;
    private Database database;

    public static void main(String[] args) {
        getSQLiteManager(new File(System.getProperty("user.dir")));
    }

    private DatabaseManager(String URL) {
        this(URL, null, null);
    }

    private DatabaseManager(String URL, String username, String password) {
        database = new Database(URL, username, password);
    }

    public static DatabaseManager getMySQLManager(String URL, String username, String password) {
        DatabaseManager localVar = instance;
        if (localVar != null) {
            return localVar;
        }

        synchronized (DatabaseManager.class) {
            if (instance == null) {
                instance = new DatabaseManager("jdbc:mysql://localhost:3306/minecraftstatdb", "myuser", "myuser");
            }
            return instance;
        }
    }

    public static DatabaseManager getSQLiteManager(File pluginDataFolder) {
        DatabaseManager localVar = instance;
        if (localVar != null) {
            return localVar;
        }

        synchronized (DatabaseManager.class) {
            if (instance == null) {
                String URL = "jdbc:sqlite:" +
                        pluginDataFolder.getPath() +
                        "/stats.db";
                instance = new DatabaseManager(URL);
            }
            return instance;
        }
    }

    public void setUp(List<MyStatistic> statistics, List<MySubStatistic> subStatistics) {
        database.fillStatistics(statistics, subStatistics);
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