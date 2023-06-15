package io.github.evercraftmc.global.commands;

import java.util.Arrays;
import java.util.List;
import io.github.evercraftmc.core.api.commands.ECCommand;
import io.github.evercraftmc.core.api.server.player.ECConsole;
import io.github.evercraftmc.core.api.server.player.ECPlayer;
import io.github.evercraftmc.core.impl.util.ECTextFormatter;
import io.github.evercraftmc.global.GlobalModule;

public class PrefixCommand implements ECCommand {
    protected final GlobalModule parent;

    public PrefixCommand(GlobalModule parent) {
        this.parent = parent;
    }

    @Override
    public String getName() {
        return "prefix";
    }

    @Override
    public String getDescription() {
        return "Change your prefix";
    }

    @Override
    public List<String> getAlias() {
        return Arrays.asList("setPrefix");
    }

    @Override
    public String getPermission() {
        return "evercraft.global.commands.prefix";
    }

    @Override
    public void run(ECPlayer player, String[] args, boolean sendFeedback) {
        if (!(player instanceof ECConsole)) {
            if (args.length > 0) {
                if (args.length == 1) {
                    if (ECTextFormatter.stripColors(args[0]).length() <= 16 && args[0].length() <= 32) {
                        if (args[0].equalsIgnoreCase("reset")) {
                            this.parent.getPlugin().getPlayerData().players.get(player.getUuid().toString()).prefix = null;
                            this.parent.getPlugin().saveData();

                            if (sendFeedback) {
                                player.sendMessage(ECTextFormatter.translateColors("&aYour prefix has been reset."));
                            }
                        } else {
                            this.parent.getPlugin().getPlayerData().players.get(player.getUuid().toString()).prefix = args[0];
                            this.parent.getPlugin().saveData();

                            if (sendFeedback) {
                                player.sendMessage(ECTextFormatter.translateColors("&aSuccessfully set your prefix to &r" + args[0] + "&r&a."));
                            }
                        }
                    } else if (sendFeedback) {
                        player.sendMessage(ECTextFormatter.translateColors("&cThat prefix is too long."));
                    }
                } else if (sendFeedback) {
                    player.sendMessage(ECTextFormatter.translateColors("&cYour prefix cant contain spaces."));
                }
            } else {
                this.parent.getPlugin().getPlayerData().players.get(player.getUuid().toString()).prefix = null;
                this.parent.getPlugin().saveData();

                if (sendFeedback) {
                    player.sendMessage(ECTextFormatter.translateColors("&aYour prefix has been reset."));
                }
            }

            player.setDisplayName(ECTextFormatter.translateColors((parent.getPlugin().getPlayerData().players.get(player.getUuid().toString()).prefix != null ? parent.getPlugin().getPlayerData().players.get(player.getUuid().toString()).prefix + "&r " : "&r") + parent.getPlugin().getPlayerData().players.get(player.getUuid().toString()).displayName + "&r"));
        } else if (sendFeedback) {
            player.sendMessage(ECTextFormatter.translateColors("&cYou cant do that from the console."));
        }
    }

    @Override
    public List<String> tabComplete(ECPlayer player, String[] args) {
        if (args.length == 1) {
            return Arrays.asList("reset");
        } else {
            return Arrays.asList();
        }
    }
}