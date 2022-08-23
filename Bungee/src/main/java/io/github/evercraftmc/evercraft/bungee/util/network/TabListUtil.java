package io.github.evercraftmc.evercraft.bungee.util.network;

import io.github.evercraftmc.evercraft.bungee.BungeeMain;
import io.github.evercraftmc.evercraft.bungee.util.formatting.ComponentFormatter;
import io.github.evercraftmc.evercraft.shared.util.StringUtils;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.protocol.Property;
import net.md_5.bungee.protocol.packet.PlayerListItem;

public class TabListUtil {
    public static void addToList(ProxiedPlayer player) {
        for (ProxiedPlayer player2 : BungeeMain.getInstance().getProxy().getPlayers()) {
            PlayerListItem packet = new PlayerListItem();
            packet.setAction(PlayerListItem.Action.ADD_PLAYER);

            PlayerListItem.Item item = new PlayerListItem.Item();
            item.setUuid(player.getUniqueId());
            item.setUsername(player.getName());
            item.setDisplayName(ComponentFormatter.stringToJson(player.getDisplayName()));
            item.setPing(player.getPing());
            item.setProperties(new Property[] {});

            packet.setItems(new PlayerListItem.Item[] { item });

            player2.unsafe().sendPacket(packet);
        }
    }

    public static void addToList(ProxiedPlayer player, ServerInfo server) {
        for (ProxiedPlayer player2 : BungeeMain.getInstance().getProxy().getPlayers()) {
            PlayerListItem packet = new PlayerListItem();
            packet.setAction(PlayerListItem.Action.ADD_PLAYER);

            PlayerListItem.Item item = new PlayerListItem.Item();
            item.setUuid(player.getUniqueId());
            item.setUsername(player.getName());
            item.setDisplayName((player2.getServer() != player.getServer() ? ComponentFormatter.stringToJson("[" + StringUtils.toTtitleCase(server.getName()) + "] ") : "") + player.getDisplayName());
            item.setPing(player.getPing());
            item.setProperties(new Property[] {});

            packet.setItems(new PlayerListItem.Item[] { item });

            player2.unsafe().sendPacket(packet);
        }
    }

    public static void addToList(ProxiedPlayer player, ProxiedPlayer player2) {
        PlayerListItem packet = new PlayerListItem();
        packet.setAction(PlayerListItem.Action.ADD_PLAYER);

        PlayerListItem.Item item = new PlayerListItem.Item();
        item.setUuid(player.getUniqueId());
        item.setUsername(player.getName());
        item.setDisplayName(ComponentFormatter.stringToJson(player.getDisplayName()));
        item.setPing(player.getPing());
        item.setProperties(new Property[] {});

        packet.setItems(new PlayerListItem.Item[] { item });

        player2.unsafe().sendPacket(packet);
    }

    public static void addToList(ProxiedPlayer player, ProxiedPlayer player2, ServerInfo server) {
        PlayerListItem packet = new PlayerListItem();
        packet.setAction(PlayerListItem.Action.ADD_PLAYER);

        PlayerListItem.Item item = new PlayerListItem.Item();
        item.setUuid(player.getUniqueId());
        item.setUsername(player.getName());
        item.setDisplayName((player2.getServer() != player.getServer() ? ComponentFormatter.stringToJson("[" + StringUtils.toTtitleCase(server.getName()) + "] ") : "") + player.getDisplayName());
        item.setPing(player.getPing());
        item.setProperties(new Property[] {});

        packet.setItems(new PlayerListItem.Item[] { item });

        player2.unsafe().sendPacket(packet);
    }

    public static void removeFromList(ProxiedPlayer player) {
        for (ProxiedPlayer player2 : BungeeMain.getInstance().getProxy().getPlayers()) {
            PlayerListItem packet = new PlayerListItem();
            packet.setAction(PlayerListItem.Action.REMOVE_PLAYER);

            PlayerListItem.Item item = new PlayerListItem.Item();
            item.setUuid(player.getUniqueId());

            packet.setItems(new PlayerListItem.Item[] { item });

            player2.unsafe().sendPacket(packet);
        }
    }

    public static void removeFromList(ProxiedPlayer player, ProxiedPlayer player2) {
        PlayerListItem packet = new PlayerListItem();
        packet.setAction(PlayerListItem.Action.REMOVE_PLAYER);

        PlayerListItem.Item item = new PlayerListItem.Item();
        item.setUuid(player.getUniqueId());

        packet.setItems(new PlayerListItem.Item[] { item });

        player2.unsafe().sendPacket(packet);
    }

    public static void updatePlayer(ProxiedPlayer player) {
        updatePlayerName(player);
        updatePlayerPing(player);
    }

    public static void updatePlayer(ProxiedPlayer player, ServerInfo server) {
        updatePlayerName(player, server);
        updatePlayerPing(player);
    }

    public static void updatePlayer(ProxiedPlayer player, ProxiedPlayer player2) {
        updatePlayerName(player, player2);
        updatePlayerPing(player, player2);
    }

    public static void updatePlayer(ProxiedPlayer player, ProxiedPlayer player2, ServerInfo server) {
        updatePlayerName(player, player2, server);
        updatePlayerPing(player, player2);
    }

    public static void updatePlayerName(ProxiedPlayer player) {
        for (ProxiedPlayer player2 : BungeeMain.getInstance().getProxy().getPlayers()) {
            PlayerListItem packet = new PlayerListItem();
            packet.setAction(PlayerListItem.Action.UPDATE_DISPLAY_NAME);

            PlayerListItem.Item item = new PlayerListItem.Item();
            item.setUuid(player.getUniqueId());
            item.setDisplayName(ComponentFormatter.stringToJson(player.getDisplayName()));

            packet.setItems(new PlayerListItem.Item[] { item });

            player2.unsafe().sendPacket(packet);
        }
    }

    public static void updatePlayerName(ProxiedPlayer player, ServerInfo server) {
        for (ProxiedPlayer player2 : BungeeMain.getInstance().getProxy().getPlayers()) {
            PlayerListItem packet = new PlayerListItem();
            packet.setAction(PlayerListItem.Action.UPDATE_DISPLAY_NAME);

            PlayerListItem.Item item = new PlayerListItem.Item();
            item.setUuid(player.getUniqueId());
            item.setDisplayName((player2.getServer() != player.getServer() ? ComponentFormatter.stringToJson("[" + StringUtils.toTtitleCase(server.getName()) + "] ") : "") + player.getDisplayName());

            packet.setItems(new PlayerListItem.Item[] { item });

            player2.unsafe().sendPacket(packet);
        }
    }

    public static void updatePlayerName(ProxiedPlayer player, ProxiedPlayer player2) {
        PlayerListItem packet = new PlayerListItem();
        packet.setAction(PlayerListItem.Action.UPDATE_DISPLAY_NAME);

        PlayerListItem.Item item = new PlayerListItem.Item();
        item.setUuid(player.getUniqueId());
        item.setDisplayName(ComponentFormatter.stringToJson(player.getDisplayName()));

        packet.setItems(new PlayerListItem.Item[] { item });

        player2.unsafe().sendPacket(packet);
    }

    public static void updatePlayerName(ProxiedPlayer player, ProxiedPlayer player2, ServerInfo server) {
        PlayerListItem packet = new PlayerListItem();
        packet.setAction(PlayerListItem.Action.UPDATE_DISPLAY_NAME);

        PlayerListItem.Item item = new PlayerListItem.Item();
        item.setUuid(player.getUniqueId());
        item.setDisplayName((player2.getServer() != player.getServer() ? ComponentFormatter.stringToJson("[" + StringUtils.toTtitleCase(server.getName()) + "] ") : "") + player.getDisplayName());

        packet.setItems(new PlayerListItem.Item[] { item });

        player2.unsafe().sendPacket(packet);
    }

    public static void updatePlayerPing(ProxiedPlayer player) {
        for (ProxiedPlayer player2 : BungeeMain.getInstance().getProxy().getPlayers()) {
            PlayerListItem packet = new PlayerListItem();
            packet.setAction(PlayerListItem.Action.UPDATE_LATENCY);

            PlayerListItem.Item item = new PlayerListItem.Item();
            item.setUuid(player.getUniqueId());
            item.setPing(player.getPing());

            packet.setItems(new PlayerListItem.Item[] { item });

            player2.unsafe().sendPacket(packet);
        }
    }

    public static void updatePlayerPing(ProxiedPlayer player, ProxiedPlayer player2) {
        PlayerListItem packet = new PlayerListItem();
        packet.setAction(PlayerListItem.Action.UPDATE_LATENCY);

        PlayerListItem.Item item = new PlayerListItem.Item();
        item.setUuid(player.getUniqueId());
        item.setPing(player.getPing());

        packet.setItems(new PlayerListItem.Item[] { item });

        player2.unsafe().sendPacket(packet);
    }
}