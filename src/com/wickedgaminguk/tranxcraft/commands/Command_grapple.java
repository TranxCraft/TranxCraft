package com.wickedgaminguk.tranxcraft.commands;

import com.wickedgaminguk.tranxcraft.TCP_PremiumList.PremiumType;
import com.wickedgaminguk.tranxcraft.TranxCraft;
import net.pravian.bukkitlib.command.BukkitCommand;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class Command_grapple extends BukkitCommand<TranxCraft> {

    @Override
    public boolean run(CommandSender sender, Command command, String commandLabel, String[] args) {
        if (!(plugin.util.hasPermission("tranxcraft.premium", sender) || plugin.util.hasPermission(PremiumType.ONE, sender))) {
            return noPerms();
        }

        if (!(sender instanceof Player)) {
            sender.sendMessage("This command can't be used from the console.");
            return true;
        }

        if (args.length > 1) {
            sender.sendMessage(plugin.util.invalidUsage);
            return true;
        }

        if (args.length == 0) {
            String status;

            if (plugin.util.hasGrapple(playerSender)) {
                status = ChatColor.GREEN + "enabled.";
            }
            else {
                status = ChatColor.RED + "disabled.";
            }

            sender.sendMessage("You currently have Grapple: " + status);
            return true;
        }

        if (args[0].equalsIgnoreCase("enable") || args[0].equalsIgnoreCase("on")) {
            if (plugin.util.hasGrapple(playerSender)) {
                sender.sendMessage(ChatColor.RED + "You already have grapple enabled.");
                return true;
            }

            plugin.util.setGrapple(playerSender, true);
            sender.sendMessage(ChatColor.GREEN + "You have enabled grapple.");

            if (!(plugin.util.hasItem(playerSender, new ItemStack(Material.FISHING_ROD)))) {
                plugin.util.sendItem(playerSender, Material.FISHING_ROD, 1, null);
            }

            return true;
        }

        if (args[0].equalsIgnoreCase("disable") || args[0].equalsIgnoreCase("off")) {
            if (plugin.util.hasGrapple(playerSender) == false) {
                sender.sendMessage(ChatColor.RED + "You already have grapple disabled.");
                return true;
            }

            plugin.util.setGrapple(playerSender, false);
            sender.sendMessage(ChatColor.GREEN + "You have disabled grapple.");
            return true;
        }

        return true;
    }
}
