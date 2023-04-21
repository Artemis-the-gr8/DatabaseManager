package com.artemis.the.gr8.databasemanager.sql;

import com.artemis.the.gr8.databasemanager.models.MySubStatistic;
import com.artemis.the.gr8.databasemanager.testutils.TestDatabase;
import com.artemis.the.gr8.databasemanager.utils.Timer;
import org.junit.jupiter.api.*;

import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class SubStatTableTest extends TestDatabase {

    @Test
    @Order(1)
    void retrieveTestDataFromSpigot() {
        Timer timer = Timer.start();
        List<MySubStatistic> subStatistics = testDataProvider.getAllSubStatsFromSpigot();
        assertFalse(subStatistics.isEmpty(),
                "no sub_stats were retrieved from Spigot!");

        System.out.println("1. Got " + subStatistics.size() + " sub_stats from Spigot in " + timer.reset() + "ms");
    }

    @Disabled
    @Test
    @Order(2)
    void checkIfTableIsEmpty() {
        assertEquals(0, getCountForTable(SQL.SubStatTable.NAME),
                "sub_statistics should be empty after creation of fresh db!");

        System.out.println("2. Confirmed db is empty");
    }

    @Test
    @Order(3)
    void insertSpigotData() {
        SubStatTable table = new SubStatTable();
        List<MySubStatistic> subStats = testDataProvider.getAllSubStatsFromSpigot();

        Timer timer = Timer.start();
        table.update(subStats, connection);
        System.out.println("3. Inserted " + subStats.size() + " sub_stats into db in " + timer.reset() + "ms");

        int expectedCount = subStats.size();
        int actualCount = getCountForTable(SQL.SubStatTable.NAME);
        assertEquals(expectedCount, actualCount,
                "db contents should equal the Spigot list!");
    }

    @Test
    @Order(4)
    void updateWithSameSpigotData() {
        SubStatTable table = new SubStatTable();
        List<MySubStatistic> subStats = testDataProvider.getAllSubStatsFromSpigot();
        int oldTableSize = getCountForTable(SQL.SubStatTable.NAME);

        Timer timer = Timer.start();
        table.update(subStats, connection);
        System.out.println("4. Checked for new values in " + timer.reset() + "ms");

        int newTableSize = getCountForTable(SQL.SubStatTable.NAME);
        assertEquals(oldTableSize, newTableSize,
                "sub_statistics should have no new entries!");
    }
}