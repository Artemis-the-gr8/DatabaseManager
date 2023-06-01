package com.artemis.the.gr8.databasemanager;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.io.File;

public class DatabaseManager {


    @Contract("_, _, _ -> new")
    public static @NotNull Database getMySQLManager(String URL, String username, String password) {
        Database database = Database.getMySQLDatabase(URL, username, password);
        database.setUp();
        return database;
    }

    public static @NotNull Database getSQLiteManager(@NotNull File pluginDataFolder) {
        String URL = "jdbc:sqlite:" +
                pluginDataFolder.getPath() +
                "/stats.db";
        Database database = Database.getSQLiteDatabase(URL);
        database.setUp();
        return database;
    }
}