package io.github.evercraftmc.core.impl.spigot.server;

import io.github.evercraftmc.core.ECPlayerData;
import io.github.evercraftmc.core.ECPlugin;
import io.github.evercraftmc.core.api.server.ECServer;
import io.github.evercraftmc.core.impl.ECEnvironment;
import io.github.evercraftmc.core.impl.ECEnvironmentType;
import io.github.evercraftmc.core.impl.spigot.server.player.ECSpigotConsole;
import io.github.evercraftmc.core.impl.spigot.server.player.ECSpigotPlayer;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.UUID;
import org.bukkit.Server;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ECSpigotServer implements ECServer {
    protected @NotNull ECPlugin plugin;

    protected @NotNull Server handle;

    protected @NotNull ECSpigotCommandManager commandManager;
    protected @NotNull ECSpigotEventManager eventManager;

    protected @NotNull ECSpigotScheduler scheduler;

    public ECSpigotServer(@NotNull ECPlugin plugin, @NotNull Server handle) {
        this.plugin = plugin;

        this.handle = handle;

        this.eventManager = new ECSpigotEventManager(this);
        this.commandManager = new ECSpigotCommandManager(this);

        this.scheduler = new ECSpigotScheduler(this);
    }

    @Override
    public @NotNull ECPlugin getPlugin() {
        return this.plugin;
    }

    public @NotNull Server getHandle() {
        return this.handle;
    }

    @Override
    public @NotNull String getSoftwareVersion() {
        return this.handle.getName() + " " + this.handle.getVersion();
    }

    @Override
    public @NotNull String getMinecraftVersion() {
        return this.handle.getMinecraftVersion();
    }

    @Override
    public @NotNull ECEnvironment getEnvironment() {
        return ECEnvironment.SPIGOT;
    }

    @Override
    public @NotNull ECEnvironmentType getEnvironmentType() {
        return ECEnvironmentType.BACKEND;
    }

    @Override
    public @NotNull Collection<ECSpigotPlayer> getPlayers() {
        ArrayList<ECSpigotPlayer> players = new ArrayList<>();

        for (ECPlayerData.Player player : this.plugin.getPlayerData().players.values()) {
            players.add(new ECSpigotPlayer(player));
        }

        return Collections.unmodifiableCollection(players);
    }

    @Override
    public @Nullable ECSpigotPlayer getPlayer(@NotNull UUID uuid) {
        if (this.plugin.getPlayerData().players.containsKey(uuid.toString())) {
            return new ECSpigotPlayer(this.plugin.getPlayerData().players.get(uuid.toString()));
        } else {
            return null;
        }
    }

    @Override
    public @Nullable ECSpigotPlayer getPlayer(@NotNull String name) {
        for (ECPlayerData.Player player : this.plugin.getPlayerData().players.values()) {
            if (player.name.equalsIgnoreCase(name)) {
                return new ECSpigotPlayer(player);
            }
        }

        return null;
    }

    @Override
    public @NotNull Collection<ECSpigotPlayer> getOnlinePlayers() {
        ArrayList<ECSpigotPlayer> players = new ArrayList<>();

        for (Player spigotPlayer : this.handle.getOnlinePlayers()) {
            if (this.plugin.getPlayerData().players.containsKey(spigotPlayer.getUniqueId().toString())) {
                players.add(new ECSpigotPlayer(this.plugin.getPlayerData().players.get(spigotPlayer.getUniqueId().toString()), spigotPlayer));
            }
        }

        return Collections.unmodifiableCollection(players);
    }

    @Override
    public @Nullable ECSpigotPlayer getOnlinePlayer(@NotNull UUID uuid) {
        if (this.handle.getPlayer(uuid) != null) {
            if (this.plugin.getPlayerData().players.containsKey(uuid.toString())) {
                return new ECSpigotPlayer(this.plugin.getPlayerData().players.get(uuid.toString()), this.handle.getPlayer(uuid));
            } else {
                return null;
            }
        } else {
            return null;
        }
    }

    @Override
    public @Nullable ECSpigotPlayer getOnlinePlayer(@NotNull String name) {
        if (this.handle.getPlayer(name) != null) {
            for (ECPlayerData.Player player : this.plugin.getPlayerData().players.values()) {
                if (player.name.equalsIgnoreCase(name)) {
                    return new ECSpigotPlayer(player, this.handle.getPlayer(name));
                }
            }

            return null;
        } else {
            return null;
        }
    }

    @Override
    public @NotNull ECSpigotConsole getConsole() {
        return new ECSpigotConsole(this.handle.getConsoleSender());
    }

    @Override
    public @NotNull ECSpigotCommandManager getCommandManager() {
        return this.commandManager;
    }

    @Override
    public @NotNull ECSpigotEventManager getEventManager() {
        return this.eventManager;
    }

    @Override
    public @NotNull ECSpigotScheduler getScheduler() {
        return this.scheduler;
    }
}