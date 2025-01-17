package io.github.evercraftmc.core.impl;

import org.jetbrains.annotations.NotNull;

public enum ECEnvironment {
    PAPER(ECEnvironmentType.BACKEND),
    VELOCITY(ECEnvironmentType.PROXY);

    private final @NotNull ECEnvironmentType type;

    private ECEnvironment(@NotNull ECEnvironmentType type) {
        this.type = type;
    }

    public @NotNull ECEnvironmentType getType() {
        return this.type;
    }
}