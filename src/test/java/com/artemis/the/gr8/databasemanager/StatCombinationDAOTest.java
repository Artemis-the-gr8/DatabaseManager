package com.artemis.the.gr8.databasemanager;

import com.artemis.the.gr8.databasemanager.sql.SQL;
import com.artemis.the.gr8.databasemanager.testutils.TestDatabase;
import com.artemis.the.gr8.databasemanager.utils.Timer;
import org.junit.jupiter.api.*;


@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class StatCombinationDAOTest extends TestDatabase {

    @Test
    @Order(1)
    void checkIfTableIsEmpty() {
        Timer timer = Timer.start();
        super.fillStatTableWithSpigotData();
        System.out.println("1.1) Filled stat_table in " + timer.reset() + "ms");

        super.fillSubStatTableWithSpigotData();
        System.out.println("1.2) Filled sub_stat_table in " + timer.reset() + "ms");

        Assertions.assertEquals(0, getCountForTable(SQL.StatCombinationTable.NAME),
                "stat_combinations should be empty after creating fresh db!");

        System.out.println("1.3) Confirmed stat_combination_table is empty");
    }

    @Test
    @Order(2)
    void insertCombinations() {
        StatCombinationDAO combinationDAO = new StatCombinationDAO();

        Timer timer = Timer.start();
        int before = getCountForTable(SQL.StatCombinationTable.NAME);

        combinationDAO.update(connection);
        int after = getCountForTable(SQL.StatCombinationTable.NAME);
        System.out.println("2. Inserted " + (after - before) + " combinations in db in " + timer.reset() + "ms");

        Assertions.assertTrue(after > 0, "there should be data in stat_combinations!");
    }

    @Test
    @Order(3)
    void updateWithSameData() {
        StatCombinationDAO combinationDAO = new StatCombinationDAO();

        Timer timer = Timer.start();
        int before = getCountForTable(SQL.StatCombinationTable.NAME);
        System.out.println("3.1) stat_combinations contains " + before + " entries (" + timer.reset() + "ms)");

        combinationDAO.update(connection);
        int after = getCountForTable(SQL.StatCombinationTable.NAME);
        System.out.println("3.2) " + (after - before) + " entries added (" + timer.reset() + "ms)");

        Assertions.assertEquals(0, (after - before), "stat_combinations should have no new entries!");
    }
}
