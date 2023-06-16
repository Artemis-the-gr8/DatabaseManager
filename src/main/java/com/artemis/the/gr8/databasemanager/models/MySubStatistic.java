package com.artemis.the.gr8.databasemanager.models;

/**
 * Represents something that can function as a valid sub-statistic
 * to a {@link MyStatistic} of the corresponding type.
 * @param name the name of the sub-statistic, corresponding to the vanilla
 *             ResourceLocation of this item, block or entity
 * @param type the type of the sub-statistic.
 *             This can be ENTITY, BLOCK or ITEM.
 */
public record MySubStatistic(String name, MyStatType type) {
}