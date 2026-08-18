package com.ryderbelserion.simpleflags.paper.api.registry.flags;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class FlagManager {

    private final Map<String, FlagBuilder<?>> flags = new HashMap<>();

    public <F extends FlagBuilder<?>> void addFlag(F builder) {
        // Register the flag.
        builder.register();

        // Add to hashmap.
        this.flags.putIfAbsent(builder.getName(), builder);
    }

    @SuppressWarnings("BooleanMethodIsAlwaysInverted")
    public boolean hasFlag(String name) {
        return this.flags.containsKey(name);
    }

    public Optional<FlagBuilder<?>> getByFlag(String name) {
        return Optional.ofNullable(this.flags.get(name));
    }

    public FlagBuilder<?> getFlag(String name) {
        return this.flags.get(name);
    }

    public void removeFlag(String name) {
        this.flags.remove(name);
    }

    public Map<String, FlagBuilder<?>> getFlags() {
        return Collections.unmodifiableMap(this.flags);
    }
}