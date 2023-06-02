package com.artemis.the.gr8.databasemanager;

import com.artemis.the.gr8.databasemanager.models.MyPlayer;
import com.artemis.the.gr8.databasemanager.utils.Timer;
import org.junit.jupiter.api.*;

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
    void updateNameForExistingTzvi_Entry() {
        List<MyPlayer> players = testDataProvider.getSameFakePlayersWithOneNewName();
        String currentName = database.playerDAO.getPlayer(
                testDataProvider.getUUIDForTzvi_(), connection).playerName();

        Timer timer = Timer.start();
        database.playerDAO.update(players, connection);
        String newName = database.playerDAO.getPlayer(
                testDataProvider.getUUIDForTzvi_(), connection).playerName();

        Assertions.assertNotEquals(currentName, newName, "Tzvi_'s name should have updated!");
        System.out.println("2. Changed playerName from " + currentName + " to " + newName + " in " + timer.reset() + "ms");
    }
}