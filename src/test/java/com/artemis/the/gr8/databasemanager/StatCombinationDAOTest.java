package com.artemis.the.gr8.databasemanager;

import com.artemis.the.gr8.databasemanager.utils.Timer;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;


@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class StatCombinationDAOTest extends TestDatabase {

    @Test
    @Order(1)
    void checkIfTableIsEmpty() {
        Assumptions.assumeTrue(useSQLite);

        Timer timer = Timer.start();
        super.fillStatTableWithSpigotData();
        System.out.println("1.1) Filled stat_table in " + timer.reset() + "ms");

        super.fillSubStatTableWithSpigotData();
        System.out.println("1.2) Filled sub_stat_table in " + timer.reset() + "ms");

        assertEquals(0, database.statCombinationDAO.getStatCombinationCount(connection),
                "stat_combinations should be empty after creating fresh db!");

        System.out.println("1.3) Confirmed stat_combination_table is empty");
    }

    @Test
    @Order(2)
    void insertCombinations() {
        Timer timer = Timer.start();
        int before = database.statCombinationDAO.getStatCombinationCount(connection);

        database.statCombinationDAO.update(connection);
        int after = database.statCombinationDAO.getStatCombinationCount(connection);
        System.out.println("2. Inserted " + (after - before) + " combinations in db in " + timer.reset() + "ms");

        assertTrue(after > 0, "there should be data in stat_combinations!");
    }

    @Test
    @Order(3)
    void updateWithSameData() {
        Timer timer = Timer.start();
        int before = database.statCombinationDAO.getStatCombinationCount(connection);
        System.out.println("3.1) stat_combinations contains " + before + " entries (" + timer.reset() + "ms)");

        database.statCombinationDAO.update(connection);
        int after = database.statCombinationDAO.getStatCombinationCount(connection);
        System.out.println("3.2) " + (after - before) + " entries added (" + timer.reset() + "ms)");

        assertEquals(0, (after - before), "stat_combinations should have no new entries!");
    }
}