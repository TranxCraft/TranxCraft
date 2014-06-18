package com.wickedgaminguk.tranxcraft.commands;

import com.wickedgaminguk.tranxcraft.TCP_ModeratorList.AdminType;
import com.wickedgaminguk.tranxcraft.TranxCraft;
import net.pravian.bukkitlib.command.BukkitCommand;
import net.pravian.bukkitlib.command.CommandPermissions;
import net.pravian.bukkitlib.command.SourceType;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

@CommandPermissions(source = SourceType.ANY)
public class Command_food extends BukkitCommand<TranxCraft> {

    @Override
    public boolean run(CommandSender sender, Command command, String commandLabel, String[] args) {
        if (!(plugin.util.hasPermission("tranxcraft.moderator", sender) || plugin.util.hasPermission(AdminType.MODERATOR, sender))) {
            return noPerms();
        }

        if (args.length != 1) {
            sender.sendMessage(plugin.util.invalidUsage);
            return false;
        }

        switch (args[0].toLowerCase()) {
            case "cake": {
                plugin.util.broadcastItem(Material.CAKE, 1, ChatColor.GREEN + "Free Cake for everyone!");
                break;
            }

            case "cookie": {
                plugin.util.broadcastItem(Material.COOKIE, 64, ChatColor.GREEN + "Free Cookies for everyone!");
                break;
            }

            case "melon": {
                plugin.util.broadcastItem(Material.MELON, 64, ChatColor.GREEN + "Free Melons for everyone!");
                break;
            }

            case "goldenapple": {
                plugin.util.broadcastItem(Material.GOLDEN_APPLE, 1, ChatColor.GREEN + "A free Golden Apple for everyone!");
                break;
            }

            case "apple": {
                plugin.util.broadcastItem(Material.APPLE, 64, ChatColor.GREEN + "A free Apple for everyone!");
                break;
            }

            case "porkchop": {
                plugin.util.broadcastItem(Material.PORK, 64, ChatColor.GREEN + "Free Pork for everyone!");
                break;
            }

            case "steak":
            case "beef": {
                plugin.util.broadcastItem(Material.COOKED_BEEF, 64, ChatColor.GREEN + "Free Beef for everyone!");
                break;
            }

            case "chicken": {
                plugin.util.broadcastItem(Material.COOKED_CHICKEN, 64, ChatColor.GREEN + "Free Chicken for everyone!");
                break;
            }

            case "fish": {
                plugin.util.broadcastItem(Material.COOKED_FISH, 64, ChatColor.GREEN + "Free Fish for everyone!");
                break;
            }

            case "stew":
            case "mushroomstew":
            case "mushroom": {
                plugin.util.broadcastItem(Material.MUSHROOM_SOUP, 1, ChatColor.GREEN + "Free Mushroom Stew for everyone!");
                break;
            }

            case "bread": {
                plugin.util.broadcastItem(Material.BREAD, 64, ChatColor.GREEN + "Free bread for everyone!");
                break;
            }

            case "potato": {
                plugin.util.broadcastItem(Material.POTATO, 1, ChatColor.GREEN + "A free Potato for everyone!");
                break;
            }

            case "carrot":
            case "goldencarrot": {
                plugin.util.broadcastItem(Material.GOLDEN_CARROT, 64, ChatColor.GREEN + "Free Golden Carrots for everyone!");
                break;
            }

            case "pumpkinpie": {
                plugin.util.broadcastItem(Material.PUMPKIN_PIE, 1, ChatColor.GREEN + "Free pumpkin pie for everyone!");
                break;
            }

            default: {
                sender.sendMessage(ChatColor.RED + "Incorrect item.");
                break;
            }
        }

        return true;
    }
}
