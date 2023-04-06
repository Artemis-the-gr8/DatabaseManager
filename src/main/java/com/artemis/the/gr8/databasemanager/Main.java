package com.artemis.the.gr8.databasemanager;

import com.artemis.the.gr8.databasemanager.datamodels.MyPlayer;
import com.artemis.the.gr8.databasemanager.datamodels.MyStatistic;
import com.artemis.the.gr8.databasemanager.datamodels.MySubStatistic;

import java.util.List;

public class Main implements DatabaseManager {

    private final Database database;

    public Main(String URL, String username, String password) {
        database = new Database(URL, username, password);
        database.setUp();
    }

    public void updateStatistics(List<MyStatistic> statistics, List<MySubStatistic> subStatistics) {
        database.updateStatistics(statistics, subStatistics);
    }

    public void updatePlayers(List<MyPlayer> players) {

    }
}