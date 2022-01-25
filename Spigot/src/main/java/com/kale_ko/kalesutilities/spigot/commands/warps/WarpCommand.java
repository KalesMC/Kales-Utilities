package com.kale_ko.kalesutilities.spigot.commands.warps;

import java.util.List;
import com.kale_ko.kalesutilities.spigot.Main;
import com.kale_ko.kalesutilities.spigot.Util;
import com.kale_ko.kalesutilities.spigot.commands.SpigotCommand;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class WarpCommand extends SpigotCommand {
    public WarpCommand(String name, String description, List<String> aliases, String usage, String permission) {
        super(name, description, aliases, usage, permission);
    }

    @Override
    public boolean execute(CommandSender sender, String label, String[] args) {
        if (args.length == 1 && sender instanceof Player player) {
            player.teleport(Main.Instance.warps.getSerializable(args[0], Location.class));

            Util.sendMessage(sender, Main.Instance.config.getString("messages.warped").replace("{warp}", args[0]));
        } else if (args.length == 1) {
            Util.sendMessage(sender, Main.Instance.config.getString("messages.noconsole"));
        } else {
            if (Util.hasPermission(sender, "kalesutilities.commands.staff.sudo")) {
                Player player = Main.Instance.getServer().getPlayer(args[0]);

                if (player != null) {
                    player.teleport(Main.Instance.warps.getSerializable(args[1], Location.class));

                    Util.sendMessage(sender, Main.Instance.config.getString("messages.warpedplayer").replace("{warp}", args[1]).replace("{player}", args[0]));
                    Util.sendMessage(player, Main.Instance.config.getString("messages.warped").replace("{warp}", args[1]));
                } else {
                    Util.sendMessage(sender, Main.Instance.config.getString("messages.playernotfound").replace("{player}", args[0]));
                }
            } else {
                Util.sendMessage(sender, Main.Instance.config.getString("messages.noperms").replace("{permission}", "kalesutilities.commands.staff.sudo"));
            }
        }

        return true;
    }
}