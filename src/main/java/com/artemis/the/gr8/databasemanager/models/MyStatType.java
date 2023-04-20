package com.artemis.the.gr8.databasemanager.models;

import org.jetbrains.annotations.NotNull;

import java.util.Locale;

public enum MyStatType {
    CUSTOM,
    ENTITY,
    BLOCK,
    ITEM;

    public static MyStatType fromString(@NotNull String name) {
        return switch (name.toUpperCase(Locale.ENGLISH)) {
            case "UNTYPED", "CUSTOM" -> CUSTOM;
            case "ENTITY" -> ENTITY;
            case "BLOCK" -> BLOCK;
            case "ITEM" -> ITEM;
            default -> throw new IllegalArgumentException("This is not a valid statType!");
        };
    }
}
