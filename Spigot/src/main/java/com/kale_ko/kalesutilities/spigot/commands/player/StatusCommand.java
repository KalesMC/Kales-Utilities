package com.kale_ko.kalesutilities.spigot.commands.player;

import com.kale_ko.kalesutilities.spigot.Main;
import com.kale_ko.kalesutilities.spigot.Util;
import java.util.UUID;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

public class StatusCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, org.bukkit.command.Command command, String label, String[] args) {
        if (args.length > 0) {
            if (sender instanceof Player player) {
                StringBuilder statusMessageBuilder = new StringBuilder();

                for (Integer i = 0; i < args.length; i++) {
                    statusMessageBuilder.append(args[i] + " ");
                }

                String statusMessage = statusMessageBuilder.toString().substring(0, statusMessageBuilder.length() - 1);

                if (!Util.hasMetadata(player, "statusEntityUUID")) {
                    ArmorStand armorstand = (ArmorStand) player.getWorld().spawnEntity(player.getLocation().add(0, 0.1, 0), EntityType.ARMOR_STAND);
                    armorstand.setCustomName(statusMessage);
                    armorstand.setCustomNameVisible(true);
                    armorstand.setCollidable(false);
                    armorstand.setGravity(false);
                    armorstand.setVisible(false);
                    armorstand.setInvulnerable(true);
                    armorstand.setPersistent(false);
                    Util.setMetadata(player, "statusEntityUUID", armorstand.getUniqueId().toString());
                } else {
                    Entity armorstand = Main.Instance.getServer().getEntity(UUID.fromString(Util.getMetadata(player, "statusEntityUUID").asString()));
                    armorstand.setCustomName(statusMessage);
                }
            } else {
                Util.sendMessage(sender, Main.Instance.config.getString("messages.noconsole"));
            }
        } else {
            Util.sendMessage(sender, Main.Instance.config.getString("messages.usage").replace("{usage}", Main.Instance.getCommand("status").getUsage()));
        }

        return true;
    }
}