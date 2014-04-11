package com.wickedgaminguk.TranxCraft.Commands;

import com.wickedgaminguk.TranxCraft.TCP_Util;
import com.wickedgaminguk.TranxCraft.TranxCraft;
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
        TCP_Util TCP_Util = new TCP_Util(plugin);

        if (!(sender.hasPermission("tranxcraft.moderator"))) {
            return noPerms();
        }

        if (args.length != 1) {
            sender.sendMessage(TCP_Util.Invalid_Usage);
            return false;
        }

        switch (args[0].toLowerCase()) {
            case "cake": {
                TCP_Util.broadcastItem(Material.CAKE, 1, ChatColor.GREEN + "Free Cake for everyone!");
                break;
            }

            case "cookie": {
                TCP_Util.broadcastItem(Material.COOKIE, 64, ChatColor.GREEN + "Free Cookies for everyone!");
                break;
            }

            case "melon": {
                TCP_Util.broadcastItem(Material.MELON, 64, ChatColor.GREEN + "Free Melons for everyone!");
                break;
            }

            case "goldenapple": {
                TCP_Util.broadcastItem(Material.GOLDEN_APPLE, 1, ChatColor.GREEN + "A free Golden Apple for everyone!");
                break;
            }

            case "apple": {
                TCP_Util.broadcastItem(Material.APPLE, 64, ChatColor.GREEN + "A free Apple for everyone!");
                break;
            }

            case "porkchop": {
                TCP_Util.broadcastItem(Material.PORK, 64, ChatColor.GREEN + "Free Pork for everyone!");
                break;
            }

            case "steak":
            case "beef": {
                TCP_Util.broadcastItem(Material.COOKED_BEEF, 64, ChatColor.GREEN + "Free Beef for everyone!");
                break;
            }

            case "chicken": {
                TCP_Util.broadcastItem(Material.COOKED_CHICKEN, 64, ChatColor.GREEN + "Free Chicken for everyone!");
                break;
            }

            case "fish": {
                TCP_Util.broadcastItem(Material.COOKED_FISH, 64, ChatColor.GREEN + "Free Fish for everyone!");
                break;
            }

            case "stew":
            case "mushroomstew":
            case "mushroom": {
                TCP_Util.broadcastItem(Material.MUSHROOM_SOUP, 1, ChatColor.GREEN + "Free Mushroom Stew for everyone!");
                break;
            }

            case "bread": {
                TCP_Util.broadcastItem(Material.BREAD, 64, ChatColor.GREEN + "Free bread for everyone!");
                break;
            }

            case "potato": {
                TCP_Util.broadcastItem(Material.POTATO, 1, ChatColor.GREEN + "A free Potato for everyone!");
                break;
            }

            case "carrot":
            case "goldencarrot": {
                TCP_Util.broadcastItem(Material.GOLDEN_CARROT, 64, ChatColor.GREEN + "Free Golden Carrots for everyone!");
                break;
            }

            case "pumpkinpie": {
                TCP_Util.broadcastItem(Material.PUMPKIN_PIE, 1, ChatColor.GREEN + "Free pumpkin pie for everyone!");
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
