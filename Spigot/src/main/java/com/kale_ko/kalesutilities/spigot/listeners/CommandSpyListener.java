package com.kale_ko.kalesutilities.spigot.listeners;

import com.kale_ko.kalesutilities.spigot.Main;
import com.kale_ko.kalesutilities.spigot.Util;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

public class CommandSpyListener implements Listener {
    @EventHandler
    public void onCommand(PlayerCommandPreprocessEvent event) {
        if (!event.getMessage().startsWith("/sc") && !event.getMessage().startsWith("/staffchat")) {
            for (Player player : Main.Instance.getServer().getOnlinePlayers()) {
                if (player != event.getPlayer() && Util.hasPermission(player, "kalesutilities.commands.staff.commandspy") && (Util.hasMetadata(player, "receiveCommandSpy") && Util.getMetadata(player, "receiveCommandSpy").asBoolean())) {
                    Util.sendMessage(player, Main.Instance.config.getString("messages.commandspy").replace("{player}", Util.getPlayerName(event.getPlayer())).replace("{message}", event.getMessage()), true);
                }
            }
        }
    }
}