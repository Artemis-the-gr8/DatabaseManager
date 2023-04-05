package com.artemis.the.gr8.databasemanager.datamodels;

import java.util.UUID;

public record MyPlayer(String playerName, UUID playerUUID, boolean isExcluded) {
}