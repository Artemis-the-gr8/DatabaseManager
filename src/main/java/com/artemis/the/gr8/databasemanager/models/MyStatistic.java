package com.artemis.the.gr8.databasemanager.models;

/**
 * Represents a Minecraft statistic as it should be stored in the database.
 * @param name the name of the statistic, corresponding to the vanilla
 *             ResourceLocation of this statistic
 * @param type the type of the statistic. This can be CUSTOM (untyped),
 *             ENTITY, BLOCK or ITEM.
 */
public record MyStatistic(String name, MyStatType type) {
}