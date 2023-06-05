package com.artemis.the.gr8.databasemanager;

import com.artemis.the.gr8.databasemanager.models.MyPlayer;
import com.artemis.the.gr8.databasemanager.sql.PlayerTableQueries;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class PlayerDAO {

    private final PlayerTableQueries sqlQueries;

    public PlayerDAO(PlayerTableQueries playerTableQueries) {
        sqlQueries = playerTableQueries;
    }

    protected void create(@NotNull Connection connection) {
        try (Statement statement = connection.createStatement()) {
            statement.execute(sqlQueries.createTable());
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void update(List<MyPlayer> players, Connection connection) {
        if (players != null) {
            List<UUID> currentlyStored = getAllPlayers(connection)
                    .stream()
                    .map(MyPlayer::playerUUID)
                    .toList();
            List<MyPlayer> overlappingValues = new ArrayList<>();
            List<MyPlayer> newValues = new ArrayList<>();

            players.forEach(myPlayer -> {
                if (currentlyStored.contains(myPlayer.playerUUID())) {
                    overlappingValues.add(myPlayer);
                }
                else {
                    newValues.add(myPlayer);
                }
            });

            updateExisting(overlappingValues, connection);
            insert(newValues, connection);
        }
    }

    public int getPlayerID(UUID uuid, @NotNull Connection connection) {
        int id = 0;
        try (Statement statement = connection.createStatement()) {
            ResultSet resultSet = statement.executeQuery(sqlQueries.selectIdFromUUID(uuid));
            resultSet.next();
            id = resultSet.getInt(1);
            resultSet.close();
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        return id;
    }

    public @Nullable MyPlayer getPlayer(UUID uuid, @NotNull Connection connection) {
        MyPlayer player = null;
        try (Statement statement = connection.createStatement()) {
            ResultSet resultSet = statement.executeQuery(
                    sqlQueries.selectPlayerFromUUID(uuid));

            resultSet.next();
            player = new MyPlayer(
                    resultSet.getString(sqlQueries.NAME_COLUMN),
                    uuid,
                    resultSet.getBoolean(sqlQueries.IS_EXCLUDED_COLUMN));

            resultSet.close();
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        return player;
    }

    protected int getFullPlayerCount(@NotNull Connection connection) {
        int count = 0;
        try (Statement statement = connection.createStatement()) {
            ResultSet resultSet = statement.executeQuery(sqlQueries.selectCount());
            resultSet.next();
            count = resultSet.getInt(1);
            resultSet.close();
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        return count;
    }

    private @NotNull List<MyPlayer> getAllPlayers(@NotNull Connection connection) {
        ArrayList<MyPlayer> players = new ArrayList<>();
        try (Statement statement = connection.createStatement()) {
            ResultSet resultSet = statement.executeQuery(sqlQueries.selectAll());

            while (resultSet.next()) {
                players.add(
                        new MyPlayer(
                                resultSet.getString(sqlQueries.NAME_COLUMN),
                                UUID.fromString(resultSet.getString(sqlQueries.UUID_COLUMN)),
                                resultSet.getBoolean(sqlQueries.IS_EXCLUDED_COLUMN)));
            }
            resultSet.close();
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        return players;
    }

    private void updateExisting(@NotNull List<MyPlayer> players, @NotNull Connection connection) {
        try (PreparedStatement statement = connection.prepareStatement(sqlQueries.updateWhereMatchingUUID())) {
            for (MyPlayer player : players) {
                statement.setString(1, player.playerName());
                statement.setBoolean(2, player.isExcluded());
                statement.setString(3, player.playerUUID().toString());
                statement.addBatch();
            }
            statement.executeBatch();
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void insert(@NotNull List<MyPlayer> players, @NotNull Connection connection) {
        try (PreparedStatement statement = connection.prepareStatement(sqlQueries.insert())) {
            for (MyPlayer player : players) {
                statement.setString(1, player.playerName());
                statement.setString(2, player.playerUUID().toString());
                statement.setBoolean(3, player.isExcluded());
                statement.addBatch();
            }
            statement.executeBatch();
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
    }
}