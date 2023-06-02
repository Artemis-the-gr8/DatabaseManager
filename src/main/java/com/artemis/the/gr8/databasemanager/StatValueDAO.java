package com.artemis.the.gr8.databasemanager;

import com.artemis.the.gr8.databasemanager.models.MyStatistic;
import com.artemis.the.gr8.databasemanager.sql.StatValueTableQueries;
import org.jetbrains.annotations.NotNull;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.UUID;

public class StatValueDAO {

    private final StatValueTableQueries sqlQueries;

    public StatValueDAO(StatValueTableQueries statValueTableQueries) {
        sqlQueries = statValueTableQueries;
    }

    protected void create(@NotNull Connection connection) {
        try (Statement statement = connection.createStatement()) {
            statement.execute(sqlQueries.createTable());
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateCustomStatTypeForPlayer(UUID playerUUID, HashMap<MyStatistic, Integer> values, Connection connection) {

    }
}
