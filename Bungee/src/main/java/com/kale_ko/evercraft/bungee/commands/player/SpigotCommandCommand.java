package com.kale_ko.evercraft.bungee.commands.player;

import java.util.Arrays;
import java.util.List;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import com.kale_ko.evercraft.bungee.BungeeMain;
import com.kale_ko.evercraft.bungee.commands.BungeeCommand;
import com.kale_ko.evercraft.bungee.util.formatting.ComponentFormatter;
import com.kale_ko.evercraft.shared.util.formatting.TextFormatter;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;

public class SpigotCommandCommand extends BungeeCommand {
    public SpigotCommandCommand(String name, String description, List<String> aliases, String permission) {
        super(name, description, aliases, permission);
    }

    @Override
    public void run(CommandSender sender, String[] args) {
        if (args.length > 0) {
            if (sender instanceof ProxiedPlayer player) {
                StringBuilder command = new StringBuilder();

                for (String arg : args) {
                    command.append(arg + " ");
                }

                ByteArrayDataOutput out = ByteStreams.newDataOutput();
                out.writeUTF("crossCommand");
                out.writeUTF(player.getUniqueId().toString());
                out.writeUTF(command.substring(0, command.length() - 1));

                player.getServer().sendData("BungeeCord", out.toByteArray());
            }
        } else {
            sender.sendMessage(ComponentFormatter.stringToComponent(TextFormatter.translateColors(BungeeMain.getInstance().getPluginMessages().getString("error.invalidArgs"))));
        }
    }

    @Override
    public List<String> tabComplete(CommandSender sender, String[] args) {
        return Arrays.asList();
    }
}