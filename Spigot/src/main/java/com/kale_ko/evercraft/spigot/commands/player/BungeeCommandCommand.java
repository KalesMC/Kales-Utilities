package com.kale_ko.evercraft.spigot.commands.player;

import java.util.Arrays;
import java.util.List;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import com.kale_ko.evercraft.spigot.SpigotMain;
import com.kale_ko.evercraft.spigot.commands.SpigotCommand;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class BungeeCommandCommand extends SpigotCommand {
    public BungeeCommandCommand(String name, String description, List<String> aliases, String permission) {
        super(name, description, aliases, permission);
    }

    @Override
    public void run(CommandSender sender, String[] args) {
        if (sender instanceof Player player) {
            StringBuilder command = new StringBuilder();

            for (String arg : args) {
                command.append(arg + " ");
            }

            ByteArrayDataOutput out = ByteStreams.newDataOutput();
            out.writeUTF("crossCommand");
            out.writeUTF(player.getUniqueId().toString());
            out.writeUTF(command.substring(0, command.length() - 1));

            player.sendPluginMessage(SpigotMain.getInstance(), "BungeeCord", out.toByteArray());
        }
    }

    @Override
    public List<String> tabComplete(CommandSender sender, String[] args) {
        return Arrays.asList();
    }
}