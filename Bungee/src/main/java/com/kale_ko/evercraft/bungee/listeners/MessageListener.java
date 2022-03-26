package com.kale_ko.evercraft.bungee.listeners;

import java.util.UUID;
import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import com.kale_ko.evercraft.bungee.BungeeMain;
import com.kale_ko.evercraft.shared.util.formatting.TextFormatter;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.event.PluginMessageEvent;
import net.md_5.bungee.event.EventHandler;

public class MessageListener extends BungeeListener {
    @EventHandler
    public void onMessage(PluginMessageEvent event) {
        if (event.getTag().equals("BungeeCord")) {
            ByteArrayDataInput in = ByteStreams.newDataInput(event.getData());

            String subChannel = in.readUTF();

            if (subChannel.equals("globalChat")) {
                String sender = in.readUTF();

                for (ServerInfo server : BungeeMain.getInstance().getProxy().getServers().values()) {
                    if (server.getName().equals(sender)) {
                        ByteArrayDataOutput out = ByteStreams.newDataOutput();

                        out.writeUTF("globalMessage");
                        out.writeUTF(TextFormatter.translateColors(BungeeMain.getInstance().getPluginMessages().getString("chat.default").replace("{player}", BungeeMain.getInstance().getProxy().getPlayer(UUID.fromString(in.readUTF())).getDisplayName()).replace("{message}", in.readUTF())));

                        server.sendData("BungeeCord", out.toByteArray());
                    } else {
                        ByteArrayDataOutput out = ByteStreams.newDataOutput();

                        out.writeUTF("globalMessage");
                        out.writeUTF(TextFormatter.translateColors(BungeeMain.getInstance().getPluginMessages().getString("globalMessage").replace("{server}", sender).replace("{message}", BungeeMain.getInstance().getPluginMessages().getString("chat.default").replace("{player}", BungeeMain.getInstance().getProxy().getPlayer(UUID.fromString(in.readUTF())).getDisplayName()).replace("{message}", in.readUTF()))));

                        server.sendData("BungeeCord", out.toByteArray());
                    }
                }

                BungeeMain.getInstance().getDiscordBot().getGuild().getTextChannelById(BungeeMain.getInstance().getPluginConfig().getString("discord.channelId")).sendMessage(TextFormatter.discordFormat(BungeeMain.getInstance().getPluginMessages().getString("globalMessage").replace("{server}", sender).replace("{message}", BungeeMain.getInstance().getPluginMessages().getString("chat.default").replace("{player}", BungeeMain.getInstance().getProxy().getPlayer(UUID.fromString(in.readUTF())).getDisplayName()).replace("{message}", in.readUTF())))).queue();
            } else if (subChannel.equals("globalMessage")) {
                String sender = in.readUTF();

                for (ServerInfo server : BungeeMain.getInstance().getProxy().getServers().values()) {
                    if (server.getName().equals(sender)) {
                        ByteArrayDataOutput out = ByteStreams.newDataOutput();

                        out.writeUTF("globalMessage");
                        out.writeUTF(TextFormatter.translateColors(in.readUTF()));

                        server.sendData("BungeeCord", out.toByteArray());
                    } else {
                        ByteArrayDataOutput out = ByteStreams.newDataOutput();

                        out.writeUTF("globalMessage");
                        out.writeUTF(TextFormatter.translateColors(BungeeMain.getInstance().getPluginMessages().getString("globalMessage").replace("{server}", sender).replace("{message}", in.readUTF())));

                        server.sendData("BungeeCord", out.toByteArray());
                    }
                }

                BungeeMain.getInstance().getDiscordBot().getGuild().getTextChannelById(BungeeMain.getInstance().getPluginConfig().getString("discord.channelId")).sendMessage(TextFormatter.discordFormat(BungeeMain.getInstance().getPluginMessages().getString("globalMessage").replace("{server}", sender).replace("{message}", in.readUTF()))).queue();
            } else if (subChannel.equals("globalCommandSpy")) {
                String sender = in.readUTF();

                for (ServerInfo server : BungeeMain.getInstance().getProxy().getServers().values()) {
                    if (server.getName().equals(sender)) {
                        ByteArrayDataOutput out = ByteStreams.newDataOutput();

                        out.writeUTF("globalPermMessage");
                        out.writeUTF(TextFormatter.translateColors(in.readUTF()));
                        out.writeUTF("evercraft.commands.staff.commandspy");

                        server.sendData("BungeeCord", out.toByteArray());
                    } else {
                        ByteArrayDataOutput out = ByteStreams.newDataOutput();

                        out.writeUTF("globalPermMessage");
                        out.writeUTF(TextFormatter.translateColors(BungeeMain.getInstance().getPluginMessages().getString("globalMessage").replace("{server}", sender).replace("{message}", in.readUTF())));
                        out.writeUTF("evercraft.commands.staff.commandspy");

                        server.sendData("BungeeCord", out.toByteArray());
                    }
                }
            }
        }
    }
}