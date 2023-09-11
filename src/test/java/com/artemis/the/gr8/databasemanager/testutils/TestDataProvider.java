package com.artemis.the.gr8.databasemanager.testutils;

import com.artemis.the.gr8.databasemanager.models.MyPlayer;
import com.artemis.the.gr8.databasemanager.models.MyStatType;
import com.artemis.the.gr8.databasemanager.models.MyStatistic;
import com.artemis.the.gr8.databasemanager.models.MySubStatistic;
import com.artemis.the.gr8.statfilereader.StatFileReader;
import com.artemis.the.gr8.statfilereader.model.Stats;
import org.bukkit.Material;
import org.bukkit.Statistic;
import org.bukkit.entity.EntityType;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.*;
import java.util.stream.Collectors;

public class TestDataProvider {

    private static final String testFolderPath = "src/test/resources/stats";

    public @NotNull Set<MyStatistic> getAllStatsFromSpigot() {
        return Arrays.stream(Statistic.values())
                .map(statistic -> new MyStatistic(
                        statistic.toString().toLowerCase(Locale.ENGLISH),
                        MyStatType.fromString(statistic.getType().toString())))
                .collect(Collectors.toSet());
    }

    public Set<MyStatistic> getStatsFromTestFiles() throws NullPointerException {
        File statFolder = new File(testFolderPath);
        File[] statFiles = statFolder.listFiles();
        if (statFiles == null) {
            throw new NullPointerException("No test files found!");
        }

        StatFileReader reader = new StatFileReader();
        List<Stats> statFileContents = new ArrayList<>();
        Arrays.stream(statFiles).forEach(file ->
                statFileContents.add(reader.readFile(file.getPath())));

        Set<MyStatistic> statistics = getNonCustomStats();
        statFileContents.forEach(fileContent ->
                fileContent.custom.keySet().forEach(key ->
                        statistics.add(new MyStatistic(key, MyStatType.CUSTOM))));

        return statistics;
    }

    public Stats getValuesForArtemisFromFile() {
        String artemisFilePath = testFolderPath + File.separator + getArtemis().uuid().toString() + ".json";
        StatFileReader reader = new StatFileReader();
        return reader.readFile(artemisFilePath);
    }

    private Set<MyStatistic> getNonCustomStats() {
        Set<MyStatistic> statistics = new HashSet<>();
        statistics.add(new MyStatistic("mined", MyStatType.BLOCK));
        statistics.add(new MyStatistic("broken", MyStatType.ITEM));
        statistics.add(new MyStatistic("crafted", MyStatType.ITEM));
        statistics.add(new MyStatistic("used", MyStatType.ITEM));
        statistics.add(new MyStatistic("picked_up", MyStatType.ITEM));
        statistics.add(new MyStatistic("dropped", MyStatType.ITEM));
        statistics.add(new MyStatistic("killed", MyStatType.ENTITY));
        statistics.add(new MyStatistic("killed_by", MyStatType.ENTITY));
        return statistics;
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

    public @NotNull MyStatistic getBlockTypeStatFromSpigot() {
        return new MyStatistic(Statistic.MINE_BLOCK.toString().toLowerCase(Locale.ENGLISH), MyStatType.BLOCK);
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