package com.artemis.the.gr8.databasemanager;

import com.artemis.the.gr8.databasemanager.models.MyPlayer;
import com.artemis.the.gr8.databasemanager.utils.Timer;
import org.junit.jupiter.api.*;

import java.util.ArrayList;
import java.util.List;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class PlayerDAOTest extends TestDatabase {

    @Test
    @Order(1)
    void insertFakePlayers() {
        List<MyPlayer> players = testDataProvider.getSomeFakePlayers();

        int before = database.playerDAO.getFullPlayerCount(connection);
        Timer timer = Timer.start();

        database.playerDAO.update(players, connection);
        int expectedCount = players.size();
        int actualCount = database.playerDAO.getFullPlayerCount(connection);

        Assertions.assertEquals(expectedCount, actualCount,
                "db contents should equal the fake player-list!");

        System.out.println("1. Inserted " + (actualCount - before) + " players into db in " + timer.reset() + "ms" +
                "\n" + "   Table now contains " + actualCount + " entries");
    }

    @Test
    @Order(2)
    void updateNameForExistingTzEntry() {
        List<MyPlayer> players = testDataProvider.getSameFakePlayersWithOneNewName();
        MyPlayer currentTz = database.playerDAO.getPlayer(testDataProvider.getUUIDForTz(), connection);
        Assertions.assertNotNull(currentTz);
        String currentTzName = currentTz.playerName();

        Timer timer = Timer.start();
        database.playerDAO.update(players, connection);
        MyPlayer newTz = database.playerDAO.getPlayer(testDataProvider.getUUIDForTz(), connection);
        Assertions.assertNotNull(newTz);
        String newTzName = newTz.playerName();

        Assertions.assertNotEquals(currentTzName, newTzName, "Tz's name should have updated!");
        System.out.println("2. Changed playerName from " + currentTzName + " to " + newTzName + " in " + timer.reset() + "ms");
    }

    @Test
    @Order(3)
    void getPlayerID() {
        int currentTableContents = database.playerDAO.getFullPlayerCount(connection);
        List<MyPlayer> newPlayerList = new ArrayList<>();

        MyPlayer newPlayer = testDataProvider.getNewRandomlyGeneratedPlayer();
        newPlayerList.add(newPlayer);
        database.playerDAO.update(newPlayerList, connection);

        int newlyInsertedPlayerId = database.playerDAO.getPlayerID(newPlayer.playerUUID(), connection);
        System.out.println("3. Inserted new player and confirmed id auto-incremented");
        Assertions.assertEquals(currentTableContents + 1, newlyInsertedPlayerId, "id should have auto-incremented!");
    }
}