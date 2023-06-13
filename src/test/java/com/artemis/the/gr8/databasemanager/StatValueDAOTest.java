package com.artemis.the.gr8.databasemanager;

import com.artemis.the.gr8.databasemanager.models.MyPlayer;
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
        MyPlayer artemis = testDataProvider.getArtemis();
        List<MyStatistic> fakeStats = testDataProvider.getAllStatsFromSpigot();
        HashMap<MyStatistic, Integer> values = new HashMap<>();

        for (int i = 0; i <5; i++) {
            if (fakeStats.get(i).type() == MyStatType.CUSTOM) {
                values.put(fakeStats.get(i), Math.abs(randomizer.nextInt(1000)));
            }
        }

        Timer timer = Timer.start();
        database.statValueDAO.updateStatsForPlayer(artemis, values, connection);
        System.out.println("1. Inserted some fake values in " + timer.reset() + "ms");
    }

    @Test
    @Order(2)
    void insertValuesForAllCustomTypeStats() {
        Random randomizer = new Random();
        MyPlayer artemis = testDataProvider.getArtemis();
        List<MyStatistic> fakeStats = testDataProvider.getAllStatsFromSpigot();
        HashMap<MyStatistic, Integer> values = new HashMap<>();

        fakeStats.forEach(statistic -> {
            if (statistic.type() == MyStatType.CUSTOM) {
                values.put(statistic, Math.abs(randomizer.nextInt(1000)));
            }
        });

        Timer timer = Timer.start();
        database.statValueDAO.updateStatsForPlayer(artemis, values, connection);
        System.out.println("2. Inserted data for all CUSTOM stats in " + timer.reset() + "ms");
    }

    @Test
    @Order(3)
    void insertValuesForTypeEntity() {
        Random random = new Random();
        MyPlayer artemis = testDataProvider.getArtemis();
        List<MySubStatistic> fakeSubStats = testDataProvider.getAllSubStatsFromSpigot()
                .stream()
                .filter(subStatistic -> subStatistic.type() == MyStatType.ENTITY)
                .toList();

        HashMap<MySubStatistic, Integer> values = new HashMap<>();
        fakeSubStats.forEach(subStatistic ->
                values.put(subStatistic, Math.abs(random.nextInt(10000))));

        Timer timer = Timer.start();
        database.statValueDAO.updateStatWithSubStatForPlayer(
                artemis, testDataProvider.getEntityTypeStatFromSpigot(), values, connection);
        System.out.println("3. Inserted entity-type-stat-values in " + timer.reset() + "ms");
    }
}
