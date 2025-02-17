package com.ryderbelserion.simpleflags.enums;

import org.eclipse.aether.artifact.DefaultArtifact;
import org.eclipse.aether.graph.Dependency;

public enum Plugins {

    fusion("com.ryderbelserion.fusion", "fusion-paper", "0.18.1"),
    configme("ch.jalu", "configme", "1.4.3");

    private final String group;
    private final String id;
    private final String version;

    Plugins(final String group, final String id, final String version) {
        this.group = group;
        this.id = id;
        this.version = version;
    }

    public Dependency asDependency() {
        return new Dependency(new DefaultArtifact(String.format("%s:%s:%s", this.group, this.id, this.version)), null);
    }
}