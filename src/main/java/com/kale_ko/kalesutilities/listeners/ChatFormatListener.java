package com.kale_ko.kalesutilities.listeners;

import com.kale_ko.kalesutilities.Main;
import com.kale_ko.kalesutilities.Util;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChatEvent;

@SuppressWarnings("deprecation")
public class ChatFormatListener implements Listener {
    @EventHandler
    public void onChatMessage(PlayerChatEvent event) {
        String player = Util.getPlayerNickName(event.getPlayer());
        String prefix = Util.getPlayerPrefix(event.getPlayer());

        if (Util.hasPermission(event.getPlayer(), "kalesutilities.colorchat")) {
            event.setMessage(Util.formatMessage(event.getMessage()));
        }

        event.setFormat(Util.formatMessage(Main.Instance.config.getString("config.chatFormat")).replace("{prefix}", prefix).replace("{player}", player).replace("{message}", "%2$s"));
    }
}