package com.kale_ko.kalesutilities.bungee;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;

public class Util {
    public static void sendMessage(CommandSender user, String message) {
        user.sendMessage(formatMessage(BungeePlugin.Instance.config.getString("config.prefix") + " " + message));
    }

    public static void sendMessage(CommandSender user, String message, Boolean noprefix) {
        if (!noprefix) {
            user.sendMessage(formatMessage(BungeePlugin.Instance.config.getString("config.prefix") + " " + message));
        } else {
            user.sendMessage(formatMessage(message));
        }
    }

    public static void broadcastMessage(String message) {
        BungeePlugin.Instance.getProxy().broadcast(formatMessage(BungeePlugin.Instance.config.getString("config.prefix") + " " + message));
    }

    public static void broadcastMessage(String message, Boolean noprefix) {
        if (!noprefix) {
            BungeePlugin.Instance.getProxy().broadcast(formatMessage(BungeePlugin.Instance.config.getString("config.prefix") + " " + message));
        } else {
            BungeePlugin.Instance.getProxy().broadcast(formatMessage(message));
        }
    }

    public static String formatMessage(String message) {
        return ChatColor.translateAlternateColorCodes('&', message);
    }

    public static String unFormatMessage(String message) {
        char[] chars = message.toCharArray();

        for (int i = 0; i < chars.length; i++) {
            if (chars[i] == ChatColor.COLOR_CHAR && "0123456789abcdefklmnorx".indexOf(chars[i + 1]) > -1) {
                chars[i] = '&';
            }
        }

        return new String(chars);
    }

    public static String stripFormating(String message) {
        return ChatColor.stripColor(message);
    }

    public static String discordFormating(String message) {
        return stripFormating(formatMessage(message)).replace("_", "\\_").replace("*", "\\*").replace("~~", "\\~~").replace("@everyone", "\\@everyone");
    }

    public static String getNoPermissionMessage(String permission) {
        return BungeePlugin.Instance.config.getString("messages.noperms").replace("{permission}", permission);
    }

    public static Boolean hasPermission(ProxiedPlayer player, String permission) {
        return player.hasPermission(permission);
    }

    public static Boolean hasPermission(CommandSender sender, String permission) {
        if (sender == BungeePlugin.Instance.getProxy().getConsole()) {
            return true;
        } else {
            return sender.hasPermission(permission);
        }
    }

    public static String getPlayerNickName(ProxiedPlayer player) {
        String name = player.getName();

        if (BungeePlugin.Instance.players.getString(player.getName() + ".nickname") != null && !BungeePlugin.Instance.players.getString(player.getName() + ".nickname").equalsIgnoreCase("") && !BungeePlugin.Instance.players.getString(player.getName() + ".nickname").equalsIgnoreCase(" ")) {
            if (Util.hasPermission(player, "kalesutilities.nonickstar") || BungeePlugin.Instance.players.getString(player.getName() + ".nickname").equalsIgnoreCase(name) || Util.stripFormating(Util.formatMessage(BungeePlugin.Instance.players.getString(player.getName() + ".nickname") + "&r")).equalsIgnoreCase(name)) {
                name = Util.formatMessage(BungeePlugin.Instance.players.getString(player.getName() + ".nickname") + "&r");
            } else {
                name = Util.formatMessage("*" + BungeePlugin.Instance.players.getString(player.getName() + ".nickname") + "&r");
            }
        }

        return name;
    }

    public static String getPlayerPrefix(ProxiedPlayer player) {
        String prefix = "";

        if (BungeePlugin.Instance.players.getString(player.getName() + ".prefix") != null && !BungeePlugin.Instance.players.getString(player.getName() + ".prefix").equalsIgnoreCase("") && !BungeePlugin.Instance.players.getString(player.getName() + ".prefix").equalsIgnoreCase(" ")) {
            prefix = Util.formatMessage(BungeePlugin.Instance.players.getString(player.getName() + ".prefix") + "&r") + " ";
        }

        return prefix;
    }

    public static String getPlayerName(ProxiedPlayer player) {
        return getPlayerPrefix(player) + getPlayerNickName(player);
    }

    public static String getPlayerName(CommandSender sender) {
        if (sender instanceof ProxiedPlayer player) {
            return getPlayerName(player);
        } else {
            return "CONSOLE";
        }
    }

    public static void updatePlayerName(ProxiedPlayer player) {
        player.setDisplayName(getPlayerName(player));
    }

    public static void resetPlayerName(ProxiedPlayer player) {
        player.setDisplayName(player.getName());
    }

    public static <T, N> Map<T, N> mapFromLists(List<T> keys, List<N> values) {
        Map<T, N> newMap = new HashMap<T, N>();

        for (T key : keys) {
            newMap.put(key, values.get(keys.indexOf(key)));
        }

        return newMap;
    }
}