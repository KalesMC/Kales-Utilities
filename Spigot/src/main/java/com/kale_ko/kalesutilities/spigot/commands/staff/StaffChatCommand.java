package com.kale_ko.kalesutilities.spigot.commands.staff;

import com.kale_ko.kalesutilities.spigot.Main;
import com.kale_ko.kalesutilities.spigot.Util;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class StaffChatCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, org.bukkit.command.Command command, String label, String[] args) {
        StringBuilder messageBuilder = new StringBuilder();

        for (Integer i = 0; i < args.length; i++) {
            messageBuilder.append(args[i] + " ");
        }

        String message = messageBuilder.toString();

        String senderName = "CONSOLE";
        if (sender instanceof Player player) {
            senderName = Util.getPlayerName(player);
        }

        for (Player player : Main.Instance.getServer().getOnlinePlayers()) {
            if (Util.hasPermission(player, "kalesutilities.commands.staff.staffchat")) {
                Util.sendMessage(player, Main.Instance.config.getString("messages.staffchat").replace("{player}", senderName).replace("{message}", message), true);
            }
        }

        return true;
    }
}