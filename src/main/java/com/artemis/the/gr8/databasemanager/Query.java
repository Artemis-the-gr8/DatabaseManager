package com.artemis.the.gr8.databasemanager;

public class Query {

    private static final String playerTable = "players";
    private static final String statTable = "statistics";
    private static final String subStatTable = "substatistics";

    /**
     * INSERT INTO <code>playerTable</code> (playerName, UUID, isExcluded)
     * <p>
     *     VALUES(?, ?, ?);
     */
    public static final String INSERT_NEW_PLAYER;

    /**
     * INSERT INTO <code>statTable</code> (statName, statType)
     * <p>
     *     VALUES (?, ?);
     */
    public static final String INSERT_STATISTIC;

    /**
     * INSERT INTO <code>subStatTable</code> (subStatName, subStatType)
     * <p>
     *     VALUES (?, ?);
     */
    public static final String INSERT_SUB_STATISTIC;

    /**
     * SELECT * from <code>playerTable</code>
     * <p>
     *     WHERE UUID = ?;
     */
    public static final String SELECT_PLAYER_BY_UUID;

    /**
     * UPDATE <code>playerTable</code>
     * <p>
     *     SET playerName = ?
     *     <p>
     *         WHERE UUID = ?;
     */
    public static final String UPDATE_PLAYER_NAME;

    /**
     * SELECT * from <code>statTable</code>;
     */
    public static final String SELECT_ALL_FROM_STAT_TABLE;

    static {
        INSERT_NEW_PLAYER = "INSERT INTO " + playerTable + " (playerName, UUID, isExcluded) VALUES(?, ?, ?);";
        INSERT_STATISTIC = "INSERT INTO " + statTable + " (statName, statType) VALUES (?, ?);";
        INSERT_SUB_STATISTIC = "INSERT INTO " + subStatTable + " (subStatName, subStatType) VALUES (?, ?);";
        SELECT_PLAYER_BY_UUID = "SELECT * from " + playerTable + " WHERE UUID = ?;";
        UPDATE_PLAYER_NAME = "UPDATE " + playerTable + " SET playerName = ? WHERE UUID = ?;";
        SELECT_ALL_FROM_STAT_TABLE = "SELECT * from " + statTable + ";";
    }
}