package com.artemis.the.gr8.databasemanager.sql;

import com.artemis.the.gr8.databasemanager.models.MyStatistic;
import com.artemis.the.gr8.databasemanager.testutils.TestDatabase;
import com.artemis.the.gr8.databasemanager.utils.Timer;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class StatTableTest extends TestDatabase {

    @Test
    @Order(1)
    void retrieveTestDataFromSpigot() {
        Timer timer = Timer.start();
        List<MyStatistic> stats = testDataProvider.getAllStatsFromSpigot();
        assertFalse(stats.isEmpty(),
                "no stats were retrieved from Spigot!");

        System.out.println("1. Got " + stats.size() + " stats from Spigot in " + timer.reset() + "ms");
    }

    @Test
    @Order(2)
    void checkIfTableIsEmpty() {
        assertEquals(0, getCountForTable(SQL.StatTable.NAME),
                "statistics should be empty after creation of fresh db!");

        System.out.println("2. Confirmed db is empty");
    }

    @Test
    @Order(3)
    void insertSpigotData() {
        StatTable table = new StatTable();
        List<MyStatistic> stats = testDataProvider.getAllStatsFromSpigot();

        Timer timer = Timer.start();
        table.update(stats, connection);
        System.out.println("3. Inserted " + stats.size() + " stats into db in " + timer.reset() + "ms");

        int expectedCount = stats.size();
        int actualCount = getCountForTable(SQL.StatTable.NAME);
        assertEquals(expectedCount, actualCount,
                "db contents should equal the Spigot list!");
    }

    @Test
    @Order(4)
    void insertNewData() {
        StatTable table = new StatTable();
        List<MyStatistic> stats = testDataProvider.getSomeFakeStats();
        int oldTableSize = getCountForTable(SQL.StatTable.NAME);

        Timer timer = Timer.start();
        table.update(stats, connection);
        System.out.println("4. Inserted " + stats.size() + " new stats in " + timer.reset() + "ms");

        int expectedTableSize = oldTableSize + stats.size();
        int actualTableSize = getCountForTable(SQL.StatTable.NAME);
        assertEquals(expectedTableSize, actualTableSize,
                "stats should contain " + stats.size() + " new entries!");
    }
}