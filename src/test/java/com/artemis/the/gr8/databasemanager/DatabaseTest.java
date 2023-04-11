package com.artemis.the.gr8.databasemanager;

import com.artemis.the.gr8.databasemanager.datamodels.MyStatType;
import com.artemis.the.gr8.databasemanager.datamodels.MyStatistic;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

public class DatabaseTest {

    private static Database database;

    @BeforeAll
    static void setup() {
        String URL = "jdbc:sqlite:" +
                System.getProperty("user.dir") +
                "/test.db";

        database = new Database(URL, null, null);
        database.setUp();
    }

    @Test
    void filterOutExistingStats() {

    }

    private @NotNull List<MyStatistic> getMockListForProvidedStats() {
        List<MyStatistic> statistics = new ArrayList<>();
        statistics.add(new MyStatistic("damage_dealt", MyStatType.CUSTOM));
        statistics.add(new MyStatistic("animals_bred", MyStatType.CUSTOM));
        statistics.add(new MyStatistic("drop", MyStatType.ITEM));
        statistics.add(new MyStatistic("mine_block", MyStatType.BLOCK));
        statistics.add(new MyStatistic("kill_entity", MyStatType.ENTITY));
        return statistics;
    }

    private @NotNull List<MyStatistic> getMockListForExistingStats() {
        List<MyStatistic> statistics = new ArrayList<>();
        statistics.add(new MyStatistic("damage_dealt", MyStatType.CUSTOM));
        statistics.add(new MyStatistic("drop", MyStatType.ITEM));
        statistics.add(new MyStatistic("mine_block", MyStatType.BLOCK));
        statistics.add(new MyStatistic("kill_entity", MyStatType.ENTITY));
        return statistics;
    }
}
