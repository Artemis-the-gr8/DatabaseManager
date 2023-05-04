package com.artemis.the.gr8.databasemanager;

import com.artemis.the.gr8.databasemanager.models.MyStatistic;
import com.artemis.the.gr8.databasemanager.models.MySubStatistic;
import com.artemis.the.gr8.databasemanager.sql.SQL;
import com.artemis.the.gr8.databasemanager.testutils.TestDatabase;
import com.artemis.the.gr8.databasemanager.utils.Timer;
import org.junit.jupiter.api.*;

import java.util.HashMap;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class StatCombinationRepositoryTest extends TestDatabase {

    @Disabled
    @Test
    @Order(1)
    void checkIfTableIsEmpty() {
        Assertions.assertEquals(0, getCountForTable(SQL.StatCombinationTable.NAME),
                "stat_combinations should be empty after creating fresh db!");

        System.out.println("1. Confirmed stat_combination_table is empty");
    }

    @Test
    @Order(2)
    void insertCombinations() {
        StatCombinationRepository combiTable = new StatCombinationRepository();
        StatRepository statTable = new StatRepository();
        SubStatRepository subStatTable = new SubStatRepository();

        Timer timer = Timer.start();
        HashMap<MyStatistic, Integer> stats = statTable.getAllStatisticsWithID(connection);
        System.out.println("1. Retrieved " + stats.size() + " stats in " + timer.reset() + "ms");

        HashMap<MySubStatistic, Integer> subStats = subStatTable.getAllSubStatisticsWithID(connection);
        System.out.println("2. Retrieved " + subStats.size() + " subStats in " + timer.reset() + "ms");

        int before = getCountForTable(SQL.StatCombinationTable.NAME);
        System.out.println("3.1 Currently there are " + before + " combinations in table (" + timer.reset() + "ms)");

        combiTable.update(stats, subStats, connection);
        System.out.println("3.2 Inserted combinations in db in " + timer.reset() + "ms");

        int after = getCountForTable(SQL.StatCombinationTable.NAME);
        System.out.println("3.3 Now there are " + after + " combinations in table (" + timer.reset() + "ms)");
    }
}
