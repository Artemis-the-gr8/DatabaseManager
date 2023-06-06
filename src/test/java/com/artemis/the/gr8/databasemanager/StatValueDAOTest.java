package com.artemis.the.gr8.databasemanager;

import com.artemis.the.gr8.databasemanager.models.MyStatType;
import com.artemis.the.gr8.databasemanager.models.MyStatistic;
import com.artemis.the.gr8.databasemanager.utils.Timer;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.UUID;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class StatValueDAOTest extends TestDatabase {

    @Test
    @Order(1)
    void insertValues() {
        Random randomizer = new Random();
        UUID uuidArtemis = testDataProvider.getArtemis().uuid();
        List<MyStatistic> fakeStats = testDataProvider.getAllStatsFromSpigot();
        HashMap<MyStatistic, Integer> values = new HashMap<>();

        Timer timer = Timer.start();
        fakeStats.forEach(stat -> {
            if (stat.type() == MyStatType.CUSTOM) {
                values.put(stat, Math.abs(randomizer.nextInt( 10000)));
            }
        });
        System.out.println("1.1) Generated some fake values in " + timer.reset() + "ms");

        database.statValueDAO.updateCustomStatTypeForPlayer(uuidArtemis, values, connection);
        System.out.println("1.2) Inserted fake values in " + timer.reset() + "ms");
    }
}
