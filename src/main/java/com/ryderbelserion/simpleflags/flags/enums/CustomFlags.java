package com.ryderbelserion.simpleflags.flags.enums;

public enum CustomFlags {

    NATURAL_FLAG("prevent_natural_spawning"),
    CRAFTER_INTERACTION_FLAG("prevent_crafter_interaction"),
    CRAFTER_BLOCK_FLAG("prevent_crafter_block"),
    DROWN_FLAG("prevent_drowning"),
    CAPACITY_FLAG("max_players");

    private final String name;

    CustomFlags(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }
}