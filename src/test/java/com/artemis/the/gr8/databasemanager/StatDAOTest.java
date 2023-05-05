package com.artemis.the.gr8.databasemanager;

import com.artemis.the.gr8.databasemanager.models.MyStatistic;
import com.artemis.the.gr8.databasemanager.sql.SQL;
import com.artemis.the.gr8.databasemanager.testutils.TestDatabase;
import com.artemis.the.gr8.databasemanager.utils.Timer;
import org.junit.jupiter.api.*;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class StatDAOTest extends TestDatabase {

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

        System.out.println("2. Confirmed stat_table is empty");
    }

    @Test
    @Order(3)
    void insertSpigotData() {
        StatDAO table = new StatDAO();
        List<MyStatistic> stats = testDataProvider.getAllStatsFromSpigot();

        Timer timer = Timer.start();
        int before = getCountForTable(SQL.StatTable.NAME);

        table.update(stats, connection);
        int expectedCount = stats.size();
        int actualCount = getCountForTable(SQL.StatTable.NAME);

        assertEquals(expectedCount, actualCount,
                "db contents should equal the Spigot list!");

        System.out.println("3. Inserted " + (actualCount - before) + " stats into db in " + timer.reset() + "ms" +
                "\n" + "   Table now contains " + actualCount + " entries");
    }

    @Test
    @Order(4)
    void insertNewData() {
        StatDAO table = new StatDAO();
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