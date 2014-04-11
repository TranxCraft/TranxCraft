package com.wickedgaminguk.TranxCraft.Commands;

import com.wickedgaminguk.TranxCraft.TCP_Util;
import com.wickedgaminguk.TranxCraft.TranxCraft;
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
        TCP_Util TCP_Util = new TCP_Util(plugin);

        if (sender instanceof Player && !(sender.hasPermission("tranxcraft.donator") || sender.isOp())) {
            return noPerms();
        }

        if (!(sender instanceof Player)) {
            sender.sendMessage("This command can't be used from the console.");
            return true;
        }

        if (args.length > 1) {
            sender.sendMessage(TCP_Util.Invalid_Usage);
            return true;
        }

        if (args.length == 0) {
            String status;

            if (TCP_Util.hasGrapple(playerSender)) {
                status = ChatColor.GREEN + "enabled.";
            }
            else {
                status = ChatColor.RED + "disabled.";
            }

            sender.sendMessage("You currently have Grapple: " + status);
            return true;
        }

        if (args[0].equalsIgnoreCase("enable") || args[0].equalsIgnoreCase("on")) {
            if (TCP_Util.hasGrapple(playerSender)) {
                sender.sendMessage(ChatColor.RED + "You already have grapple enabled.");
                return true;
            }

            TCP_Util.setGrapple(playerSender, true);
            sender.sendMessage(ChatColor.GREEN + "You have enabled grapple.");

            if (!(TCP_Util.hasItem(playerSender, new ItemStack(Material.FISHING_ROD)))) {
                TCP_Util.sendItem(playerSender, Material.FISHING_ROD, 1, null);
            }

            return true;
        }

        if (args[0].equalsIgnoreCase("disable") || args[0].equalsIgnoreCase("off")) {
            if (TCP_Util.hasGrapple(playerSender) == false) {
                sender.sendMessage(ChatColor.RED + "You already have grapple disabled.");
                return true;
            }

            TCP_Util.setGrapple(playerSender, false);
            sender.sendMessage(ChatColor.GREEN + "You have disabled grapple.");
            return true;
        }

        return true;
    }
}
