package com.artemis.the.gr8.databasemanager;

import com.artemis.the.gr8.databasemanager.models.MySubStatistic;
import com.artemis.the.gr8.databasemanager.utils.Timer;
import org.junit.jupiter.api.*;

import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class SubStatDAOTest extends TestDatabase {

    @Test
    @Order(1)
    void retrieveTestDataFromSpigot() {
        Timer timer = Timer.start();
        List<MySubStatistic> subStatistics = testDataProvider.getAllSubStatsFromSpigot();
        assertFalse(subStatistics.isEmpty(),
                "no sub_stats were retrieved from Spigot!");

        System.out.println("1. Got " + subStatistics.size() + " sub_stats from Spigot in " + timer.reset() + "ms");
    }

    @Test
    @Order(2)
    void checkIfTableIsEmpty() {
        Assumptions.assumeTrue(useSQLite);
        assertEquals(0, database.subStatDAO.getAllSubStatsCount(connection),
                "sub_statistics should be empty after creation of fresh db!");

        System.out.println("2. Confirmed sub_stat_table is empty");
    }

    @Test
    @Order(3)
    void insertSpigotData() {
        List<MySubStatistic> subStats = testDataProvider.getAllSubStatsFromSpigot();
        Timer timer = Timer.start();

        int initialCount = database.subStatDAO.getAllSubStatsCount(connection);
        database.subStatDAO.update(subStats, connection);
        int expectedCount = subStats.size();
        int actualCount = database.subStatDAO.getAllSubStatsCount(connection);

        System.out.println("3. Inserted " + (actualCount - initialCount) + " of " + subStats.size() +
                " total sub_stats list into db in " + timer.reset() + "ms");

        assertEquals(expectedCount, actualCount,
                "db contents should equal the Spigot list!");
    }

    @Test
    @Order(4)
    void updateWithSameSpigotData() {
        List<MySubStatistic> subStats = testDataProvider.getAllSubStatsFromSpigot();
        int oldTableSize = database.subStatDAO.getAllSubStatsCount(connection);

        Timer timer = Timer.start();
        database.subStatDAO.update(subStats, connection);
        System.out.println("4. Checked for new values in " + timer.reset() + "ms");

        int newTableSize = database.subStatDAO.getAllSubStatsCount(connection);
        assertEquals(oldTableSize, newTableSize,
                "sub_statistics should have no new entries!");
    }
}