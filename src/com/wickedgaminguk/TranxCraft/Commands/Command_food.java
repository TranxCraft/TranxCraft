package com.wickedgaminguk.TranxCraft.Commands;

import com.wickedgaminguk.TranxCraft.TCP_Util;
import net.pravian.bukkitlib.command.BukkitCommand;
import net.pravian.bukkitlib.command.CommandPermissions;
import net.pravian.bukkitlib.command.SourceType;
import org.apache.commons.lang.WordUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

@CommandPermissions(source = SourceType.ANY)
public class Command_food extends BukkitCommand {

    @Override
    public boolean run(CommandSender sender, Command command, String commandLabel, String[] args) {
        if (!(sender.hasPermission("tranxcraft.moderator"))) {
            return noPerms();
        }

        if (args.length != 1) {
            sender.sendMessage(TCP_Util.Invalid_Usage);
            return false;
        }

        switch (args[0].toLowerCase()) {
            case "cake": {
                broadcastItem(Material.CAKE, 1, false);
                break;
            }

            case "cookie": {
                broadcastItem(Material.COOKIE, 64, true);
                break;
            }

            case "melon": {
                broadcastItem(Material.MELON, 64, true);
                break;
            }

            case "goldenapple": {
                broadcastItem(Material.GOLDEN_APPLE, 1, false);
                break;
            }

            case "apple": {
                broadcastItem(Material.APPLE, 64, true);
                break;
            }

            case "porkchop": {
                broadcastItem(Material.PORK, 64, true);
                break;
            }

            case "steak":
            case "beef": {
                broadcastItem(Material.COOKED_BEEF, 64, false);
                break;
            }

            case "chicken": {
                broadcastItem(Material.COOKED_CHICKEN, 64, false);
                break;
            }

            case "fish": {
                broadcastItem(Material.COOKED_FISH, 64, false);
                break;
            }

            case "stew":
            case "mushroomstew":
            case "mushroom": {
                broadcastItem(Material.MUSHROOM_SOUP, 1, false);
                break;
            }

            case "bread": {
                broadcastItem(Material.BREAD, 64, false);
                break;
            }

            case "potato": {
                broadcastItem(Material.POTATO, 1, false);
                break;
            }

            case "carrot":
            case "goldencarrot": {
                broadcastItem(Material.GOLDEN_CARROT, 64, true);
                break;
            }

            case "pumpkinpie": {
                broadcastItem(Material.PUMPKIN_PIE, 1, false);
                break;
            }

            default: {
                sender.sendMessage(ChatColor.RED + "Incorrect item.");
                break;
            }
        }

        return true;
    }

    public void broadcastItem(Material material, int quantity, boolean plural) {
        for (Player player : plugin.getServer().getOnlinePlayers()) {
            ItemStack item = new ItemStack(material, quantity);
            player.getInventory().setItem(player.getInventory().firstEmpty(), item);
        }

        if (plural == false) {
            Bukkit.broadcastMessage(ChatColor.GREEN + "A Free " + WordUtils.capitalizeFully(material.toString().toLowerCase().replaceAll("_", " ").replaceAll("cooked", "")) + " for everyone!");
        }

        if (plural == true) {
            Bukkit.broadcastMessage(ChatColor.GREEN + "Free " + WordUtils.capitalizeFully(material.toString().toLowerCase().replaceAll("_", " ").replaceAll("cooked", "")) + "s" + " for everyone!");
        }
    }
}
