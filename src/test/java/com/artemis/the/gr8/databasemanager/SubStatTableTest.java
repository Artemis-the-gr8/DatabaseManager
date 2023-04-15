package com.artemis.the.gr8.databasemanager;

import com.artemis.the.gr8.databasemanager.datamodels.MyStatType;
import com.artemis.the.gr8.databasemanager.datamodels.MySubStatistic;
import com.artemis.the.gr8.databasemanager.datamodels.Timer;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.junit.jupiter.api.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public class SubStatTableTest extends DatabaseTest {

    @BeforeAll
    static void setup() {
        DatabaseTest.setup();
    }

    @BeforeEach
    void openConnection() {
        super.openConnection();
    }

    @AfterEach
    void closeConnection() {
        super.closeConnection();
    }

    @AfterAll
    static void tearDown() {
        DatabaseTest.tearDown();
    }

    @Test
    void updateSubStatTable() {
        Timer timer = new Timer();
        timer.startTimer();
        List<MySubStatistic> subStats = getAllSubStatsFromSpigot();
        System.out.println("Made list of all subStats in " + timer.stopTimer() + "ms");

        timer.startTimer();
        database.updateSubStatTable(subStats, connection);
        System.out.println("Inserted subStats 1: " + timer.stopTimer() + "ms");

        timer.startTimer();
        database.updateSubStatTable(subStats, connection);
        System.out.println("Inserted subStats 1 again: " + timer.stopTimer() + "ms");

        timer.startTimer();
        resetDatabase();
        System.out.println("Reset database: " + timer.stopTimer() + "ms");

        timer.startTimer();
        database.updateSubStatTable2(subStats, connection);
        System.out.println("Inserted subStats 2: " + timer.stopTimer() + "ms");

        timer.startTimer();
        database.updateSubStatTable2(subStats, connection);
        System.out.println("Inserted subStats 2 again: " + timer.stopTimer() + "ms");
    }

    private List<MySubStatistic> getAllSubStatsFromSpigot() {
        List<MySubStatistic> subStats = new ArrayList<>();
        subStats.addAll(
                Arrays.stream(Material.values())
                        .filter(Material::isBlock)
                        .map(material -> new MySubStatistic(material.toString().toLowerCase(Locale.ENGLISH), MyStatType.BLOCK))
                        .toList());
        subStats.addAll(
                Arrays.stream(Material.values())
                        .filter(Material::isItem)
                        .map(material -> new MySubStatistic(material.toString().toLowerCase(Locale.ENGLISH), MyStatType.ITEM))
                        .toList());
        subStats.addAll(
                Arrays.stream(EntityType.values())
                        .map(entity -> new MySubStatistic(entity.toString().toLowerCase(Locale.ENGLISH), MyStatType.ENTITY))
                        .toList());
        return subStats;
    }
}