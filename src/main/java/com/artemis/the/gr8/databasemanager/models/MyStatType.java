package com.artemis.the.gr8.databasemanager.models;

public enum MyStatType {
    CUSTOM,
    ENTITY,
    BLOCK,
    ITEM;

    public static MyStatType fromString(String name) {
        return switch (name) {
            case "CUSTOM" -> CUSTOM;
            case "ENTITY" -> ENTITY;
            case "BLOCK" -> BLOCK;
            case "ITEM" -> ITEM;
            default -> throw new IllegalArgumentException("This is not a valid statType!");
        };
    }
}
