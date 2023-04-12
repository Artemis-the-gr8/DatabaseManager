package com.artemis.the.gr8.databasemanager;

import com.artemis.the.gr8.databasemanager.datamodels.MyStatType;
import com.artemis.the.gr8.databasemanager.datamodels.MyStatistic;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class DatabaseTest {

    private static Database database;

    @BeforeAll
    static void setup() {
        File file = new File(System.getProperty("user.dir"));

        String URL = "jdbc:sqlite:" +
                file.getPath() +
                "/test.db";

        database = new Database(URL, null, null);
        database.setUp();
    }

    @Test
    void filterOutExistingStats() {
        assertEquals(
                database.filterOutExistingStats(
                        getMockListForProvidedStats(),
                        getMockListForExistingStats()),
                getExpectedResult(),
                "filtered map does not equal the expected map");
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

    private @NotNull List<MyStatistic> getExpectedResult() {
        List<MyStatistic> result = new ArrayList<>();
        result.add(new MyStatistic("animals_bred", MyStatType.CUSTOM));
        return result;
    }
}
