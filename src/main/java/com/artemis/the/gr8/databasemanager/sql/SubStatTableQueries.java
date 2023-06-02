package com.artemis.the.gr8.databasemanager.sql;

import com.artemis.the.gr8.databasemanager.models.MyStatType;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public abstract class SubStatTableQueries {

    public static final String TABLE_NAME = "sub_statistics";
    public final String ID_COLUMN = "id";
    public final String NAME_COLUMN = "name";
    public final String TYPE_COLUMN = "type";

    public abstract @NotNull String createTable();

    @Contract(pure = true)
    public @NotNull String insert() {
        return "INSERT INTO " + TABLE_NAME + " (" +
                NAME_COLUMN + ", " +
                TYPE_COLUMN + ") VALUES (?, ?);";
    }

    public @NotNull String selectAll() {
        return "SELECT * FROM " + TABLE_NAME + ";";
    }

    public @NotNull String selectCount() {
        return "SELECT COUNT(*) FROM " + TABLE_NAME + ";";
    }

    public @NotNull String selectEntityType() {
        return selectType(MyStatType.ENTITY.getName());
    }

    public @NotNull String selectItemType() {
        return selectType(MyStatType.ITEM.getName());
    }

    public @NotNull String selectBlockType() {
        return selectType(MyStatType.BLOCK.getName());
    }


    @Contract(pure = true)
    private @NotNull String selectType(String type) {
        return "SELECT * FROM " + TABLE_NAME +
                " WHERE " + TYPE_COLUMN + " = '" + type + "';";
    }
}