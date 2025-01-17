package io.github.evercraftmc.core.impl.paper.server;

import io.github.evercraftmc.core.api.ECModule;
import io.github.evercraftmc.core.api.commands.ECCommand;
import io.github.evercraftmc.core.api.events.ECHandler;
import io.github.evercraftmc.core.api.events.ECListener;
import io.github.evercraftmc.core.api.events.messaging.MessageEvent;
import io.github.evercraftmc.core.api.server.ECCommandManager;
import io.github.evercraftmc.core.api.server.player.ECConsole;
import io.github.evercraftmc.core.api.server.player.ECPlayer;
import io.github.evercraftmc.core.impl.ECEnvironmentType;
import io.github.evercraftmc.core.impl.util.ECComponentFormatter;
import io.github.evercraftmc.core.impl.util.ECTextFormatter;
import io.github.evercraftmc.core.messaging.ECEnvironmentTypeMessageId;
import io.github.evercraftmc.core.messaging.ECMessageType;
import io.github.evercraftmc.messaging.common.ECMessage;
import java.io.*;
import java.util.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.permissions.Permission;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Unmodifiable;

public class ECPaperCommandManager implements ECCommandManager {
    protected class CommandInter extends Command {
        protected final @NotNull ECPaperCommandManager parent = ECPaperCommandManager.this;

        protected final @NotNull ECCommand command;
        protected final boolean forwardToOther;

        public CommandInter(@NotNull ECCommand command, boolean distinguishServer, boolean forwardToOther) {
            super((distinguishServer ? "b" : "") + command.getName().toLowerCase());

            this.setName((distinguishServer ? "b" : "") + command.getName().toLowerCase());
            this.setDescription(command.getDescription());
            this.setAliases(CommandInter.alias(command.getName(), command.getAlias(), distinguishServer));
            this.setPermission(command.getPermission() != null ? command.getPermission().toLowerCase() : null);

            this.command = command;
            this.forwardToOther = forwardToOther;
        }

        @Override
        public boolean execute(@NotNull CommandSender sender, @NotNull String label, @NotNull String @NotNull [] args) {
            if (sender instanceof Player paperPlayer) {
                if (this.testPermissionSilent(paperPlayer)) {
                    try {
                        this.command.run(parent.server.getOnlinePlayer(paperPlayer.getUniqueId()), Arrays.asList(args), true);
                    } catch (Exception e) {
                        parent.getServer().getPlugin().getLogger().error("Error while running command {}.", label, e);

                        return false;
                    }

                    if (this.forwardToOther) {
                        try {
                            ByteArrayOutputStream commandMessageData = new ByteArrayOutputStream();
                            DataOutputStream commandMessage = new DataOutputStream(commandMessageData);
                            commandMessage.writeInt(ECMessageType.GLOBAL_COMMAND);
                            commandMessage.writeBoolean(false);
                            commandMessage.writeUTF(paperPlayer.getUniqueId().toString());
                            commandMessage.writeUTF(this.getName());
                            commandMessage.writeInt(args.length);
                            for (String arg : args) {
                                commandMessage.writeUTF(arg);
                            }
                            commandMessage.close();

                            parent.server.getPlugin().getMessenger().send(new ECEnvironmentTypeMessageId(ECEnvironmentType.PROXY), commandMessageData.toByteArray());
                        } catch (IOException e) {
                            parent.server.getPlugin().getLogger().error("[Messenger] Failed to send message", e);
                        }
                    }
                } else {
                    sender.sendMessage(ECComponentFormatter.stringToComponent(ECTextFormatter.translateColors("&cYou do not have permission to run that command")));
                }
            } else if (sender instanceof ConsoleCommandSender) {
                try {
                    this.command.run(parent.server.getConsole(), Arrays.asList(args), true);
                } catch (Exception e) {
                    parent.getServer().getPlugin().getLogger().error("Error while running command {}.", label, e);

                    return false;
                }

                if (this.forwardToOther) {
                    try {
                        ByteArrayOutputStream commandMessageData = new ByteArrayOutputStream();
                        DataOutputStream commandMessage = new DataOutputStream(commandMessageData);
                        commandMessage.writeInt(ECMessageType.GLOBAL_COMMAND);
                        commandMessage.writeBoolean(true);
                        commandMessage.writeUTF(this.getName());
                        commandMessage.writeInt(args.length);
                        for (String arg : args) {
                            commandMessage.writeUTF(arg);
                        }
                        commandMessage.close();

                        parent.server.getPlugin().getMessenger().send(new ECEnvironmentTypeMessageId(ECEnvironmentType.PROXY), commandMessageData.toByteArray());
                    } catch (IOException e) {
                        parent.server.getPlugin().getLogger().error("[Messenger] Failed to send message", e);
                    }
                }
            }

            return true;
        }

        @Override
        public @NotNull List<String> tabComplete(@NotNull CommandSender sender, @NotNull String label, @NotNull String[] args) {
            if (sender instanceof Player paperPlayer) {
                if (this.testPermissionSilent(paperPlayer)) {
                    try {
                        List<String> completions = this.command.tabComplete(parent.server.getOnlinePlayer(paperPlayer.getUniqueId()), Arrays.asList(args));

                        List<String> matches = new ArrayList<>();
                        for (String string : completions) {
                            if (string.toLowerCase().startsWith(args[args.length - 1].toLowerCase())) {
                                matches.add(string);
                            }
                        }
                        return matches;
                    } catch (Exception e) {
                        parent.getServer().getPlugin().getLogger().error("Error while tab-completing command {}.", label, e);

                        return List.of();
                    }
                } else {
                    return List.of();
                }
            } else if (sender instanceof ConsoleCommandSender) {
                try {
                    List<String> completions = this.command.tabComplete(parent.getServer().getConsole(), Arrays.asList(args));

                    List<String> matches = new ArrayList<>();
                    for (String string : completions) {
                        if (string.toLowerCase().startsWith(args[args.length - 1].toLowerCase())) {
                            matches.add(string);
                        }
                    }
                    return matches;
                } catch (Exception e) {
                    parent.getServer().getPlugin().getLogger().error("Error while tab-completing command {}.", label, e);

                    return List.of();
                }
            }

            return List.of();
        }

        @Override
        public boolean testPermissionSilent(@NotNull CommandSender sender) {
            return this.getPermission() == null || sender.hasPermission(this.getPermission());
        }

        private static @NotNull List<String> alias(@NotNull String uName, @NotNull List<String> uAliases, boolean distinguishServer) {
            ArrayList<String> aliases = new ArrayList<>();

            for (String alias : uAliases) {
                aliases.add((distinguishServer ? "b" : "") + alias.toLowerCase());
            }

            return aliases;
        }
    }

    protected final @NotNull ECPaperServer server;

    protected final @NotNull Map<String, ECCommand> commands = new HashMap<>();
    protected final @NotNull Map<String, CommandInter> interCommands = new HashMap<>();

    protected final @NotNull Map<String, ECCommand> commandsAndAliases = new HashMap<>();

    public ECPaperCommandManager(@NotNull ECPaperServer server) {
        this.server = server;

        this.server.getEventManager().register(new ECListener() {
            private final ECPaperCommandManager parent = ECPaperCommandManager.this;

            @SuppressWarnings("DataFlowIssue")
            @Override
            public @NotNull ECModule getModule() {
                return null;
            }

            @ECHandler
            public void onMessage(@NotNull MessageEvent event) {
                ECMessage message = event.getMessage();

                if (!message.getSender().matches(parent.server) && message.getRecipient().matches(parent.server)) {
                    try {
                        ByteArrayInputStream commandMessageData = new ByteArrayInputStream(message.getData());
                        DataInputStream commandMessage = new DataInputStream(commandMessageData);

                        int type = commandMessage.readInt();
                        if (type == ECMessageType.GLOBAL_COMMAND) {
                            if (!commandMessage.readBoolean()) {
                                UUID uuid = UUID.fromString(commandMessage.readUTF());
                                String command = commandMessage.readUTF();
                                List<String> args = new ArrayList<>();
                                int argC = commandMessage.readInt();
                                for (int i = 0; i < argC; i++) {
                                    args.add(commandMessage.readUTF());
                                }

                                ECPlayer player = parent.server.getOnlinePlayer(uuid);
                                if (player != null) {
                                    ECCommand ecCommand = parent.server.getCommandManager().getByName(command);
                                    if (ecCommand != null) {
                                        try {
                                            ecCommand.run(player, args, false);
                                        } catch (Exception e) {
                                            parent.getServer().getPlugin().getLogger().error("Error while running command {}.", command, e);
                                        }
                                    }
                                }
                            } else {
                                String command = commandMessage.readUTF();
                                List<String> args = new ArrayList<>();
                                int argC = commandMessage.readInt();
                                for (int i = 0; i < argC; i++) {
                                    args.add(commandMessage.readUTF());
                                }

                                ECConsole player = parent.server.getConsole();

                                ECCommand ecCommand = parent.server.getCommandManager().getByName(command);
                                if (ecCommand != null) {
                                    try {
                                        ecCommand.run(player, args, false);
                                    } catch (Exception e) {
                                        parent.getServer().getPlugin().getLogger().error("Error while running command {}.", command, e);
                                    }
                                }
                            }
                        }

                        commandMessage.close();
                    } catch (IOException e) {
                        parent.server.getPlugin().getLogger().error("[Messenger] Failed to read message", e);
                    }
                }
            }
        });
    }

    public @NotNull ECPaperServer getServer() {
        return this.server;
    }

    @Override
    public @NotNull @Unmodifiable List<ECCommand> getAll() {
        return List.copyOf(this.commands.values());
    }

    @Override
    public @Nullable ECCommand getByName(@NotNull String name) {
        return this.commands.get(name.toLowerCase());
    }

    @Override
    public @Nullable ECCommand getByAlias(@NotNull String alias) {
        return this.commandsAndAliases.get(alias.toLowerCase());
    }

    @Override
    public @NotNull ECCommand register(@NotNull ECCommand command) {
        return this.register(command, false);
    }

    @Override
    public @NotNull ECCommand register(@NotNull ECCommand command, boolean distinguishServer) {
        return this.register(command, distinguishServer, !distinguishServer);
    }

    @Override
    public @NotNull ECCommand register(@NotNull ECCommand command, boolean distinguishServer, boolean forwardToOther) {
        if (!this.commands.containsKey(command.getName().toLowerCase())) {
            CommandInter interCommand = new CommandInter(command, distinguishServer, forwardToOther);

            this.commands.put(command.getName().toLowerCase(), command);
            this.interCommands.put(command.getName().toLowerCase(), interCommand);

            this.commandsAndAliases.put(command.getName().toLowerCase(), command);
            for (String alias : command.getAlias()) {
                this.commandsAndAliases.put(alias.toLowerCase(), command);
            }

            this.server.getHandle().getCommandMap().register("evercraft", interCommand);
            this.interCommands.get(command.getName().toLowerCase()).register(this.server.getHandle().getCommandMap());

            for (String permission : command.getExtraPermissions()) {
                if (this.server.getHandle().getPluginManager().getPermission(permission) == null) {
                    this.server.getHandle().getPluginManager().addPermission(new Permission(permission));
                }
            }

            return command;
        } else {
            throw new RuntimeException("Command /" + command.getName() + " is already registered");
        }
    }

    @Override
    public @NotNull ECCommand unregister(@NotNull ECCommand command) {
        if (this.commands.containsKey(command.getName().toLowerCase())) {
            this.interCommands.get(command.getName().toLowerCase()).unregister(this.server.getHandle().getCommandMap());

            this.interCommands.remove(command.getName().toLowerCase());
            this.commands.remove(command.getName().toLowerCase());

            this.commandsAndAliases.remove(command.getName().toLowerCase());
            for (String alias : command.getAlias()) {
                this.commandsAndAliases.remove(alias.toLowerCase());
            }

            return command;
        } else {
            throw new RuntimeException("Command /" + command.getName() + " is not registered");
        }
    }

    @Override
    public void unregisterAll() {
        for (ECCommand command : List.copyOf(this.commands.values())) {
            this.unregister(command);
        }
    }
}