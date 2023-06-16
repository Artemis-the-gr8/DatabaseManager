package com.artemis.the.gr8.databasemanager;

import com.artemis.the.gr8.databasemanager.models.MyStatType;
import com.artemis.the.gr8.databasemanager.models.MyStatistic;
import com.artemis.the.gr8.databasemanager.models.MySubStatistic;
import com.artemis.the.gr8.databasemanager.utils.Timer;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import java.util.HashMap;
import java.util.List;
import java.util.Random;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class StatValueDAOTest extends TestDatabaseHandler {

    @Test
    @Order(1)
    void insertSomeValues() {
        Random randomizer = new Random();
        HashMap<Integer, Integer> values = new HashMap<>();
        int playerID = super.insertOrGetPlayerID(testDataProvider.getArtemis());
//        super.fillStatTableWithSpigotData();

        List<MyStatistic> fakeStats = testDataProvider.getAllStatsFromSpigot();
        for (int i = 0; i <5; i++) {
            if (fakeStats.get(i).type() == MyStatType.CUSTOM) {
                values.put(
                        super.insertOrGetStatCombinationID(fakeStats.get(i), null),
                        Math.abs(randomizer.nextInt(1000)));
            }
        }

        Timer timer = Timer.start();
        database.statValueDAO.updateStatsForPlayer(playerID, values, connection);
        System.out.println("1. Inserted some fake values in " + timer.reset() + "ms");
    }

    @Test
    @Order(2)
    void insertValuesForAllCustomTypeStats() {
        Random randomizer = new Random();
        HashMap<Integer, Integer> values = new HashMap<>();
        int playerID = super.insertOrGetPlayerID(testDataProvider.getArtemis());

        List<MyStatistic> fakeStats = testDataProvider.getAllStatsFromSpigot();
        fakeStats.forEach(statistic -> {
            if (statistic.type() == MyStatType.CUSTOM) {
                values.put(
                        super.insertOrGetStatCombinationID(
                                statistic,
                                null),
                        Math.abs(randomizer.nextInt(1000)));
            }
        });

        Timer timer = Timer.start();
        database.statValueDAO.updateStatsForPlayer(playerID, values, connection);
        System.out.println("2. Inserted data for all CUSTOM stats in " + timer.reset() + "ms");
    }

    @Test
    @Order(3)
    void insertValuesForTypeEntity() {
        Random random = new Random();
        int playerID = super.insertOrGetPlayerID(testDataProvider.getArtemis());
        List<MySubStatistic> fakeSubStats = testDataProvider.getAllSubStatsFromSpigot()
                .stream()
                .filter(subStatistic -> subStatistic.type() == MyStatType.ENTITY)
                .toList();

        HashMap<Integer, Integer> values = new HashMap<>();
        fakeSubStats.forEach(subStatistic ->
                values.put(
                        super.insertOrGetStatCombinationID(
                                testDataProvider.getEntityTypeStatFromSpigot(),
                                subStatistic),
                        Math.abs(random.nextInt(10000))));

        Timer timer = Timer.start();
        database.statValueDAO.updateStatsForPlayer(playerID, values, connection);
        System.out.println("3. Inserted entity-type-stat-values in " + timer.reset() + "ms");
    }

    @Test
    @Order(4)
    void insertWrongValuesForTypeBlock() {
        Random random = new Random();
        int playerID = super.insertOrGetPlayerID(testDataProvider.getArtemis());
        List<MySubStatistic> fakeSubStats = testDataProvider.getAllSubStatsFromSpigot()
                .stream()
                .filter(subStatistic -> subStatistic.type() == MyStatType.ENTITY)
                .toList();

        HashMap<Integer, Integer> values = new HashMap<>();
        fakeSubStats.forEach(subStatistic ->
                values.put(
                        super.insertOrGetStatCombinationID(
                                testDataProvider.getBlockTypeStatFromSpigot(),
                                subStatistic),
                        Math.abs(random.nextInt(10000))));

        Timer timer = Timer.start();
        database.statValueDAO.updateStatsForPlayer(playerID, values, connection);
        System.out.println("4. Inserted entity-type-stat-values for block-type-stat in " + timer.reset() + "ms");
    }
}