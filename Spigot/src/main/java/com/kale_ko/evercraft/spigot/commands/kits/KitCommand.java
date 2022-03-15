package com.kale_ko.evercraft.spigot.commands.kits;

import java.util.List;
import com.kale_ko.evercraft.spigot.SpigotPlugin;
import com.kale_ko.evercraft.spigot.Util;
import com.kale_ko.evercraft.spigot.commands.SpigotCommand;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.v1_18_R2.inventory.CraftItemStack;
import org.bukkit.entity.Player;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public class KitCommand extends SpigotCommand {
    public KitCommand(String name, String description, List<String> aliases, String usage, String permission) {
        super(name, description, aliases, usage, permission);
    }

    @Override
    public void run(CommandSender sender, String label, String[] args) {
        if (args.length > 1) {
            if (Util.hasPermission(sender, "evercraft.commands.staff.sudo")) {
                Player player = SpigotPlugin.Instance.getServer().getPlayer(args[0]);

                if (player != null) {
                    List<String> items = SpigotPlugin.Instance.kits.getStringList(args[0]);

                    for (String item : items) {
                        NBTTagCompound nbt = Util.parseNBT(item);
                        ItemStack itemStack = CraftItemStack.asBukkitCopy(net.minecraft.world.item.ItemStack.a(nbt));

                        if (itemStack.getType().getEquipmentSlot() != EquipmentSlot.HAND && itemStack.getType().getEquipmentSlot() != EquipmentSlot.OFF_HAND && (player.getEquipment().getItem(itemStack.getType().getEquipmentSlot()) == null || player.getEquipment().getItem(itemStack.getType().getEquipmentSlot()).getType().isAir())) {
                            player.getEquipment().setItem(itemStack.getType().getEquipmentSlot(), itemStack);
                        } else {
                            player.getInventory().addItem(itemStack);
                        }
                    }

                    Util.sendMessage(player, SpigotPlugin.Instance.config.getString("messages.kit").replace("{kit}", args[1]));
                } else {
                    Util.sendMessage(sender, SpigotPlugin.Instance.config.getString("messages.playernotfound").replace("{player}", args[0]));
                }
            } else {
                Util.sendMessage(sender, SpigotPlugin.Instance.config.getString("messages.noperms").replace("{permission}", "evercraft.commands.staff.sudo"));
            }
        } else if (args.length > 0) {
            if (sender instanceof Player player) {
                List<String> items = SpigotPlugin.Instance.kits.getStringList(args[0]);

                for (String item : items) {
                    NBTTagCompound nbt = Util.parseNBT(item);
                    ItemStack itemStack = CraftItemStack.asBukkitCopy(net.minecraft.world.item.ItemStack.a(nbt));

                    if (itemStack.getType().getEquipmentSlot() != EquipmentSlot.HAND && itemStack.getType().getEquipmentSlot() != EquipmentSlot.OFF_HAND && (player.getEquipment().getItem(itemStack.getType().getEquipmentSlot()) == null || player.getEquipment().getItem(itemStack.getType().getEquipmentSlot()).getType().isAir())) {
                        player.getEquipment().setItem(itemStack.getType().getEquipmentSlot(), itemStack);
                    } else {
                        player.getInventory().addItem(itemStack);
                    }
                }

                Util.sendMessage(player, SpigotPlugin.Instance.config.getString("messages.kit").replace("{kit}", args[0]));
            } else {
                Util.sendMessage(sender, SpigotPlugin.Instance.config.getString("messages.noconsole"));
            }
        } else {
            Util.sendMessage(sender, SpigotPlugin.Instance.config.getString("messages.usage").replace("{usage}", SpigotPlugin.Instance.getCommand("kit").getUsage()));
        }
    }
}