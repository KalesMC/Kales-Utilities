package com.kale_ko.kalesutilities.commands.staff;

import com.kale_ko.kalesutilities.KalesUtilities;
import com.kale_ko.kalesutilities.Util;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SudoCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, org.bukkit.command.Command command, String label, String[] args) {
        if (Util.hasPermission(sender, "kalesutilities.sudo")) {
            if (args.length > 1) {
                if (args[0].equalsIgnoreCase("*")) {
                    StringBuilder sudoMessageBuilder = new StringBuilder();

                    for (Integer i = 1; i < args.length; i++) {
                        sudoMessageBuilder.append(args[i] + " ");
                    }

                    String sudoMessage = sudoMessageBuilder.toString();

                    if (sudoMessage.startsWith("/")) {
                        for (Player player : KalesUtilities.Instance.getServer().getOnlinePlayers()) {
                            KalesUtilities.Instance.getServer().dispatchCommand(player, sudoMessage.substring(1));
                        }

                        Util.sendMessage(sender, KalesUtilities.Instance.config.getString("messages.sudocommand").replace("{player}", args[0]).replace("{command}", sudoMessage));
                    } else {
                        for (Player player : KalesUtilities.Instance.getServer().getOnlinePlayers()) {
                            player.chat(sudoMessage);
                        }

                        Util.sendMessage(sender, KalesUtilities.Instance.config.getString("messages.sudomessage").replace("{player}", args[0]).replace("{message}", sudoMessage));
                    }
                } else {
                    Player sudoedPlayer = KalesUtilities.Instance.getServer().getPlayer(args[0]);

                    if (sudoedPlayer != null) {
                        StringBuilder sudoMessageBuilder = new StringBuilder();

                        for (Integer i = 1; i < args.length; i++) {
                            sudoMessageBuilder.append(args[i] + " ");
                        }

                        String sudoMessage = sudoMessageBuilder.toString();

                        if (sudoMessage.startsWith("/")) {
                            KalesUtilities.Instance.getServer().dispatchCommand(sudoedPlayer, sudoMessage.substring(1));

                            Util.sendMessage(sender, KalesUtilities.Instance.config.getString("messages.sudocommand").replace("{player}", args[0]).replace("{command}", sudoMessage));
                        } else {
                            sudoedPlayer.chat(sudoMessage);

                            Util.sendMessage(sender, KalesUtilities.Instance.config.getString("messages.sudomessage").replace("{player}", args[0]).replace("{message}", sudoMessage));
                        }
                    } else {
                        Util.sendMessage(sender, KalesUtilities.Instance.config.getString("messages.playernotfound").replace("{player}", args[0]));
                    }
                }
            } else {
                Util.sendMessage(sender, KalesUtilities.Instance.config.getString("messages.usage").replace("{usage}", KalesUtilities.Instance.getCommand("sudo").getUsage()));
            }
        } else {
            Util.sendMessage(sender, KalesUtilities.Instance.config.getString("messages.noperms").replace("{permission}", "kalesutilities.sudo"));
        }

        return true;
    }
}