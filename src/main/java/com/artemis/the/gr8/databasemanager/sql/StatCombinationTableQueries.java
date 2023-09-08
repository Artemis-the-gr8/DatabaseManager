package com.artemis.the.gr8.databasemanager.sql;

import com.artemis.the.gr8.databasemanager.models.MyStatType;
import org.jetbrains.annotations.NotNull;

public abstract class StatCombinationTableQueries {

    public static final String TABLE_NAME = "stat_combinations";
    public final String ID_COLUMN = "id";
    public final String STAT_ID_COLUMN = "stat_id";
    public final String SUB_STAT_ID_COLUMN = "sub_stat_id";

    public final String ID_COLUMN_WITH_TABLE_NAME = TABLE_NAME + "." + ID_COLUMN;
    public final String STAT_ID_COLUMN_WITH_TABLE_NAME = TABLE_NAME + "." + STAT_ID_COLUMN;
    public final String SUB_STAT_ID_COLUMN_WITH_TABLE_NAME = TABLE_NAME + "." + SUB_STAT_ID_COLUMN;

    public abstract @NotNull String createTable();

    public @NotNull String insert() {
        return "INSERT INTO " + TABLE_NAME + " (" +
                STAT_ID_COLUMN + ", " +
                SUB_STAT_ID_COLUMN + ") VALUES (?, ?);";
    }

    public String selectID(@NotNull String statName, String subStatName, @NotNull MyStatType type) {
        // SELECT stat_combinations.id FROM stat_combinations
        // LEFT JOIN statistics ON stat_combinations.stat_id = statistics.id
        String query = "SELECT " + ID_COLUMN_WITH_TABLE_NAME + " FROM " + TABLE_NAME +
                " LEFT JOIN " + StatTableQueries.TABLE_NAME +
                    " ON " + STAT_ID_COLUMN_WITH_TABLE_NAME + " = " + StatTableQueries.ID_COLUMN_WITH_TABLE_NAME +
                " LEFT JOIN " + SubStatTableQueries.TABLE_NAME +
                    " ON " + SUB_STAT_ID_COLUMN_WITH_TABLE_NAME + " = " + SubStatTableQueries.ID_COLUMN_WITH_TABLE_NAME +
                " WHERE " + StatTableQueries.NAME_COLUMN_WITH_TABLE_NAME + " = " + "'" + statName + "'";

        if (type == MyStatType.CUSTOM || subStatName == null) {
            return query;
        }
        return query +
                " AND " + SubStatTableQueries.NAME_COLUMN_WITH_TABLE_NAME + " = " + "'" + subStatName + "'" +
                " AND " + SubStatTableQueries.TYPE_COLUMN_WITH_TABLE_NAME + " = " + "'" + type.getName() + "'";
    }

    public @NotNull String selectIDFromStatAndSubStatID(int statId, int subStatId) {
        String subStatCondition = subStatId == 0 ? " IS NULL" : " = " + subStatId;

        return "SELECT " + ID_COLUMN + " FROM " + TABLE_NAME +
                " WHERE " + STAT_ID_COLUMN + " = " + statId +
                " AND " + SUB_STAT_ID_COLUMN + subStatCondition + ";";
    }

    public @NotNull String selectCount() {
        return "SELECT COUNT(*) FROM " + TABLE_NAME + ";";
    }

    public @NotNull String selectAll() {
        return "SELECT * FROM " + TABLE_NAME + ";";
    }
}