package com.artemis.the.gr8.databasemanager.testutils;

import com.artemis.the.gr8.databasemanager.models.MyStatType;
import com.artemis.the.gr8.databasemanager.models.MyStatistic;
import com.artemis.the.gr8.databasemanager.models.MySubStatistic;
import org.bukkit.Material;
import org.bukkit.Statistic;
import org.bukkit.entity.EntityType;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public class TestDataProvider {

    public @NotNull List<MyStatistic> getAllStatsFromSpigot() {
        return Arrays.stream(Statistic.values())
                .map(statistic -> new MyStatistic(
                        statistic.toString().toLowerCase(Locale.ENGLISH),
                        MyStatType.fromString(statistic.getType().toString())))
                .toList();
    }

    public @NotNull List<MyStatistic> getSomeFakeStats() {
        List<MyStatistic> statistics = new ArrayList<>();
        statistics.add(new MyStatistic("chat_messages", MyStatType.CUSTOM));
        statistics.add(new MyStatistic("throw", MyStatType.ITEM));
        statistics.add(new MyStatistic("punch_block", MyStatType.BLOCK));
        statistics.add(new MyStatistic("entity_seen", MyStatType.ENTITY));
        statistics.add(new MyStatistic("animals_pet", MyStatType.ENTITY));
        return statistics;
    }

    public @NotNull List<MySubStatistic> getAllSubStatsFromSpigot() {
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