package io.github.evercraftmc.messaging.common;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import org.jetbrains.annotations.NotNull;

public class ECMessageId {
    protected final @NotNull String type;
    protected final @NotNull String value;

    public ECMessageId(@NotNull String type, @NotNull String value) {
        this.type = type;
        this.value = value;
    }

    public @NotNull String getType() {
        return this.type;
    }

    public @NotNull String getValue() {
        return this.value;
    }

    public @NotNull Object getParsedValue() {
        return this.value;
    }

    public boolean matches(@NotNull Object match) {
        return this.value.equals(match.toString());
    }

    protected static final @NotNull Map<String, Function<String, ECMessageId>> parserMap = new HashMap<>();

    public static void addParser(@NotNull String type, @NotNull Function<String, ECMessageId> function) {
        parserMap.put(type.toLowerCase(), function);
    }

    public static void removeParser(@NotNull String type) {
        parserMap.remove(type.toLowerCase());
    }

    public static ECMessageId parse(@NotNull String type, @NotNull String value) {
        if (!parserMap.containsKey(type.toLowerCase())) {
            throw new RuntimeException("Unknown message id type \"" + type + "\"");
        }
        return parserMap.get(type.toLowerCase()).apply(value);
    }
}