package io.github.evercraftmc.core.impl.bungee.server;

import io.github.evercraftmc.core.ECPlayerData;
import io.github.evercraftmc.core.api.events.ECEvent;
import io.github.evercraftmc.core.api.events.ECHandler;
import io.github.evercraftmc.core.api.events.ECListener;
import io.github.evercraftmc.core.api.events.player.PlayerChatEvent;
import io.github.evercraftmc.core.api.events.player.PlayerJoinEvent;
import io.github.evercraftmc.core.api.events.player.PlayerLeaveEvent;
import io.github.evercraftmc.core.api.server.ECEventManager;
import io.github.evercraftmc.core.impl.bungee.server.player.ECBungeePlayer;
import io.github.evercraftmc.core.impl.bungee.server.util.ECBungeeComponentFormatter;
import java.lang.reflect.Method;
import java.util.*;
import net.md_5.bungee.api.event.ChatEvent;
import net.md_5.bungee.api.event.LoginEvent;
import net.md_5.bungee.api.event.PlayerDisconnectEvent;
import net.md_5.bungee.api.event.PostLoginEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.event.EventHandler;

@SuppressWarnings("unchecked")
public class ECBungeeEventManager implements ECEventManager {
    protected class BungeeListeners implements Listener {
        protected final ECBungeeEventManager parent = ECBungeeEventManager.this;

        @EventHandler
        public void onPlayerConnect(LoginEvent event) {
            String uuid = event.getConnection().getUniqueId().toString();
            if (!parent.server.getPlugin().getPlayerData().players.containsKey(uuid)) {
                parent.server.getPlugin().getPlayerData().players.put(uuid, new ECPlayerData.Player(UUID.fromString(uuid), event.getConnection().getName()));
            }
        }

        @EventHandler
        public void onPlayerJoin(PostLoginEvent event) {
            PlayerJoinEvent newEvent = new PlayerJoinEvent(new ECBungeePlayer(parent.server.getPlugin().getPlayerData().players.get(event.getPlayer().getUniqueId().toString()), event.getPlayer()), "");
            parent.emit(newEvent);

            if (newEvent.isCancelled()) {
                event.getPlayer().disconnect(ECBungeeComponentFormatter.stringToComponent(newEvent.getCancelReason()));
            } else if (!newEvent.getJoinMessage().isEmpty()) {
                parent.server.broadcastMessage(newEvent.getJoinMessage());
            }
        }

        @EventHandler
        public void onPlayerLeave(PlayerDisconnectEvent event) {
            PlayerLeaveEvent newEvent = new PlayerLeaveEvent(new ECBungeePlayer(parent.server.getPlugin().getPlayerData().players.get(event.getPlayer().getUniqueId().toString()), event.getPlayer()), "");
            parent.emit(newEvent);

            if (!newEvent.getLeaveMessage().isEmpty()) {
                parent.server.broadcastMessage(newEvent.getLeaveMessage());
            }
        }

        @EventHandler
        public void onPlayerChat(ChatEvent event) {
            String message = event.getMessage();
            if (message.isEmpty()) {
                event.setCancelled(true);
                return;
            }

            if (message.charAt(0) != '/') {
                ECBungeePlayer player = parent.server.getOnlinePlayer(event.getSender());

                PlayerChatEvent newEvent = new PlayerChatEvent(new ECBungeePlayer(parent.server.getPlugin().getPlayerData().players.get(player.getUuid().toString()), player.getHandle()), PlayerChatEvent.MessageType.CHAT, message);
                parent.emit(newEvent);

                event.setCancelled(true);

                if (newEvent.isCancelled()) {
                    if (!newEvent.getCancelReason().isEmpty()) {
                        player.sendMessage(newEvent.getCancelReason());
                    }
                } else if (!newEvent.getMessage().isEmpty()) {
                    parent.server.broadcastMessage(newEvent.getMessage());
                }
            }
        }
    }

    protected ECBungeeServer server;

    protected Map<Class<? extends ECEvent>, List<Map.Entry<ECListener, Method>>> listeners = new HashMap<>();

    public ECBungeeEventManager(ECBungeeServer server) {
        this.server = server;

        this.server.getHandle().getPluginManager().registerListener((Plugin) this.server.getPlugin().getHandle(), new BungeeListeners());
    }

    public ECBungeeServer getServer() {
        return this.server;
    }

    @Override
    public void emit(ECEvent event) {
        if (this.listeners.containsKey(event.getClass())) {
            for (Map.Entry<ECListener, Method> entry : this.listeners.get(event.getClass())) {
                try {
                    entry.getValue().setAccessible(true);
                    entry.getValue().invoke(entry.getKey(), event);
                } catch (Exception e) {
                    this.server.getPlugin().getLogger().error("Failed to emit event", e);
                }
            }
        }
    }

    @Override
    public ECListener register(ECListener listener) {
        for (Method method : listener.getClass().getDeclaredMethods()) {
            if (method.getParameterCount() == 1 && ECEvent.class.isAssignableFrom(method.getParameterTypes()[0]) && method.getDeclaredAnnotationsByType(ECHandler.class) != null) {
                if (!this.listeners.containsKey((Class<? extends ECEvent>) method.getParameterTypes()[0])) {
                    this.listeners.put((Class<? extends ECEvent>) method.getParameterTypes()[0], new ArrayList<>());
                }
                this.listeners.get((Class<? extends ECEvent>) method.getParameterTypes()[0]).add(new AbstractMap.SimpleEntry<>(listener, method));
            }
        }

        return listener;
    }

    @Override
    public ECListener unregister(ECListener listener) {
        for (Method method : listener.getClass().getDeclaredMethods()) {
            if (method.getParameterCount() == 1 && ECEvent.class.isAssignableFrom(method.getParameterTypes()[0]) && method.getDeclaredAnnotationsByType(ECHandler.class) != null && this.listeners.containsKey((Class<? extends ECEvent>) method.getParameterTypes()[0])) {
                this.listeners.get((Class<? extends ECEvent>) method.getParameterTypes()[0]).remove(new AbstractMap.SimpleEntry<>(listener, method));
            }
        }

        return listener;
    }

    @Override
    public void unregisterAll() {
        this.listeners.clear();
    }
}