package io.github.evercraftmc.core.impl.paper.server;

import io.github.evercraftmc.core.ECPlayerData;
import io.github.evercraftmc.core.ECPlugin;
import io.github.evercraftmc.core.api.server.ECServer;
import io.github.evercraftmc.core.impl.ECEnvironment;
import io.github.evercraftmc.core.impl.ECEnvironmentType;
import io.github.evercraftmc.core.impl.paper.server.player.ECPaperConsole;
import io.github.evercraftmc.core.impl.paper.server.player.ECPaperPlayer;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.UUID;
import org.bukkit.Server;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Unmodifiable;

public class ECPaperServer implements ECServer {
    protected final @NotNull ECPlugin plugin;

    protected final @NotNull Server handle;

    protected final @NotNull ECPaperCommandManager commandManager;
    protected final @NotNull ECPaperEventManager eventManager;

    protected final @NotNull ECPaperScheduler scheduler;

    public ECPaperServer(@NotNull ECPlugin plugin, @NotNull Server handle) {
        this.plugin = plugin;

        this.handle = handle;

        this.eventManager = new ECPaperEventManager(this);
        this.commandManager = new ECPaperCommandManager(this);

        this.scheduler = new ECPaperScheduler(this);
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
        return ECEnvironment.PAPER;
    }

    @Override
    public @NotNull ECEnvironmentType getEnvironmentType() {
        return ECEnvironmentType.BACKEND;
    }

    @Override
    public @NotNull @Unmodifiable Collection<ECPaperPlayer> getPlayers() {
        ArrayList<ECPaperPlayer> players = new ArrayList<>();

        for (ECPlayerData.Player player : this.plugin.getPlayerData().players.values()) {
            players.add(new ECPaperPlayer(player));
        }

        return Collections.unmodifiableCollection(players);
    }

    @Override
    public @Nullable ECPaperPlayer getPlayer(@NotNull UUID uuid) {
        if (this.plugin.getPlayerData().players.containsKey(uuid.toString())) {
            return new ECPaperPlayer(this.plugin.getPlayerData().players.get(uuid.toString()));
        }

        return null;
    }

    @Override
    public @Nullable ECPaperPlayer getPlayer(@NotNull String name) {
        for (ECPlayerData.Player player : this.plugin.getPlayerData().players.values()) { // TODO Name -> UUID map
            if (player.name.equalsIgnoreCase(name)) {
                return new ECPaperPlayer(player);
            }
        }

        return null;
    }

    @Override
    public @NotNull @Unmodifiable Collection<ECPaperPlayer> getOnlinePlayers() {
        ArrayList<ECPaperPlayer> players = new ArrayList<>();

        for (Player paperPlayer : this.handle.getOnlinePlayers()) {
            if (this.plugin.getPlayerData().players.containsKey(paperPlayer.getUniqueId().toString())) {
                players.add(new ECPaperPlayer(this.plugin.getPlayerData().players.get(paperPlayer.getUniqueId().toString()), this, paperPlayer));
            }
        }

        return Collections.unmodifiableCollection(players);
    }

    @Override
    public @Nullable ECPaperPlayer getOnlinePlayer(@NotNull UUID uuid) {
        Player paperPlayer = this.handle.getPlayer(uuid);
        if (paperPlayer != null && this.plugin.getPlayerData().players.containsKey(uuid.toString())) {
            return new ECPaperPlayer(this.plugin.getPlayerData().players.get(uuid.toString()), this, paperPlayer);
        }

        return null;
    }

    @Override
    public @Nullable ECPaperPlayer getOnlinePlayer(@NotNull String name) {
        Player paperPlayer = this.handle.getPlayer(name);
        if (paperPlayer != null) {
            for (ECPlayerData.Player player : this.plugin.getPlayerData().players.values()) { // TODO Name -> UUID map
                if (player.name.equalsIgnoreCase(name)) {
                    return new ECPaperPlayer(player, this, paperPlayer);
                }
            }
        }

        return null;
    }

    @Override
    public @NotNull ECPaperConsole getConsole() {
        return new ECPaperConsole(this.handle.getConsoleSender());
    }

    @Override
    public @NotNull ECPaperCommandManager getCommandManager() {
        return this.commandManager;
    }

    @Override
    public @NotNull ECPaperEventManager getEventManager() {
        return this.eventManager;
    }

    @Override
    public @NotNull ECPaperScheduler getScheduler() {
        return this.scheduler;
    }
}