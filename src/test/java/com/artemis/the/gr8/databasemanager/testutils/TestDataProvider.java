package com.artemis.the.gr8.databasemanager.testutils;

import com.artemis.the.gr8.databasemanager.models.MyPlayer;
import com.artemis.the.gr8.databasemanager.models.MyStatType;
import com.artemis.the.gr8.databasemanager.models.MyStatistic;
import com.artemis.the.gr8.databasemanager.models.MySubStatistic;
import org.bukkit.Material;
import org.bukkit.Statistic;
import org.bukkit.entity.EntityType;
import org.jetbrains.annotations.NotNull;

import java.util.*;

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

    public @NotNull MyStatistic getEntityTypeStatFromSpigot() {
        return new MyStatistic(Statistic.KILL_ENTITY.toString().toLowerCase(Locale.ENGLISH), MyStatType.ENTITY);
    }

    public @NotNull List<MyPlayer> getSomeFakePlayers() {
        List<MyPlayer> players = new ArrayList<>();
        players.add(getArtemis());
        players.add(getCyber());
        players.add(getRidi());
        players.add(getTz());
        return players;
    }

    public @NotNull List<MyPlayer> getSomeFakePlayersWithOldTzName() {
        List<MyPlayer> players = new ArrayList<>();
        players.add(getArtemis());
        players.add(getCyber());
        players.add(getRidi());
        players.add(getTzWithOldName());
        return players;
    }

    public @NotNull MyPlayer getNewRandomlyGeneratedPlayer() {
        Random random = new Random();
        String name = "test" + random.nextInt(999999999);
        UUID uuid = UUID.randomUUID();
        return new MyPlayer(name, uuid, false);
    }

    public @NotNull MyPlayer getArtemis() {
        return new MyPlayer(
                "Artemis_the_gr8",
                UUID.fromString("46dd0c5a-2b51-4ee6-80e8-29deca6dedc1"),
                false);
    }

    public @NotNull MyPlayer getCyber() {
        return new MyPlayer(
                "CyberDrain",
                UUID.fromString("e4c5dfef-bbcc-4012-9f74-879d28fff431"),
                false);
    }

    public @NotNull MyPlayer getRidi() {
        return new MyPlayer(
                "Ridimas",
                UUID.fromString("8fb811dc-2ceb-4528-9951-cf803e0550a1"),
                false);
    }

    public @NotNull MyPlayer getTz() {
        return new MyPlayer(
                "Tzvi_",
                UUID.fromString("29c0911d-695a-4c31-817f-3a065a7144b7"),
                false);
    }

    private @NotNull MyPlayer getTzWithOldName() {
        return new MyPlayer("daboss42", getTz().uuid(), false);
    }
}