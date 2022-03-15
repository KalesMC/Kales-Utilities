package com.kale_ko.evercraft.spigot.commands.warps;

import java.util.List;
import com.kale_ko.evercraft.spigot.SpigotPlugin;
import com.kale_ko.evercraft.spigot.Util;
import com.kale_ko.evercraft.spigot.commands.SpigotCommand;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;

public class SpawnCommand extends SpigotCommand {
    public SpawnCommand(String name, String description, List<String> aliases, String usage, String permission) {
        super(name, description, aliases, usage, permission);
    }

    @Override
    public void run(CommandSender sender, String label, String[] args) {
        if (args.length == 0 && sender instanceof Player player) {
            player.teleport(SpigotPlugin.Instance.spawn.getSerializable(player.getWorld().getName(), Location.class));
            if (SpigotPlugin.Instance.config.getBoolean("config.clearOnWarp")) {
                player.getInventory().clear();
                for (PotionEffect effect : player.getActivePotionEffects()) {
                    player.removePotionEffect(effect.getType());
                }
            }

            Util.sendMessage(sender, SpigotPlugin.Instance.config.getString("messages.spawned"));
        } else if (args.length == 0) {
            Util.sendMessage(sender, SpigotPlugin.Instance.config.getString("messages.noconsole"));
        } else {
            if (Util.hasPermission(sender, "evercraft.commands.staff.sudo")) {
                Player player = SpigotPlugin.Instance.getServer().getPlayer(args[0]);

                if (player != null) {
                    player.teleport(SpigotPlugin.Instance.spawn.getSerializable(player.getWorld().getName(), Location.class));
                    if (SpigotPlugin.Instance.config.getBoolean("config.clearOnWarp")) {
                        player.getInventory().clear();
                        for (PotionEffect effect : player.getActivePotionEffects()) {
                            player.removePotionEffect(effect.getType());
                        }
                    }

                    Util.sendMessage(sender, SpigotPlugin.Instance.config.getString("messages.spawnedplayer").replace("{player}", args[0]));
                    Util.sendMessage(player, SpigotPlugin.Instance.config.getString("messages.spawned"));
                } else {
                    Util.sendMessage(sender, SpigotPlugin.Instance.config.getString("messages.playernotfound").replace("{player}", args[0]));
                }
            } else {
                Util.sendMessage(sender, SpigotPlugin.Instance.config.getString("messages.noperms").replace("{permission}", "evercraft.commands.staff.sudo"));
            }
        }
    }
}