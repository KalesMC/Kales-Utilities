package io.github.evercraftmc.core.api.server;

import io.github.evercraftmc.core.ECPlugin;
import io.github.evercraftmc.core.api.server.player.ECConsole;
import io.github.evercraftmc.core.api.server.player.ECPlayer;
import io.github.evercraftmc.core.impl.ECEnvironment;
import java.util.Collection;
import java.util.UUID;

public interface ECServer {
    ECPlugin getPlugin();

    String getMinecraftVersion();

    String getSoftwareVersion();

    ECEnvironment getEnvironment();

    Collection<? extends ECPlayer> getPlayers();

    ECPlayer getPlayer(UUID uuid);

    ECPlayer getPlayer(String name);

    Collection<? extends ECPlayer> getOnlinePlayers();

    ECPlayer getOnlinePlayer(UUID uuid);

    ECPlayer getOnlinePlayer(String name);

    ECConsole getConsole();

    default void broadcastMessage(String message) {
        for (ECPlayer player : this.getOnlinePlayers()) {
            player.sendMessage(message);
        }
    }

    ECCommandManager getCommandManager();

    ECEventManager getEventManager();

    ECScheduler getScheduler();
}