package com.wickedgaminguk.tranxcraft.commands;

import com.wickedgaminguk.tranxcraft.TCP_PremiumList.PremiumType;
import com.wickedgaminguk.tranxcraft.TranxCraft;
import net.pravian.bukkitlib.command.BukkitCommand;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Command_doublejump extends BukkitCommand<TranxCraft> {

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

            if (plugin.util.hasDoubleJump(playerSender)) {
                status = ChatColor.GREEN + "enabled.";
            }
            else {
                status = ChatColor.RED + "disabled.";
            }

            sender.sendMessage("You currently have Double Jump: " + status);
            return true;
        }

        if (args[0].equalsIgnoreCase("enable") || args[0].equalsIgnoreCase("on")) {
            if (plugin.util.hasDoubleJump(playerSender)) {
                sender.sendMessage(ChatColor.RED + "You already have Double Jump enabled.");
                return true;
            }

            plugin.util.setDoubleJump(playerSender, true);
            sender.sendMessage(ChatColor.GREEN + "You have enabled Double Jump.");

            return true;
        }

        if (args[0].equalsIgnoreCase("disable") || args[0].equalsIgnoreCase("off")) {
            if (plugin.util.hasDoubleJump(playerSender) == false) {
                sender.sendMessage(ChatColor.RED + "You already have Double Jump disabled.");
                return true;
            }

            plugin.util.setDoubleJump(playerSender, false);

            playerSender.setFlying(false);
            playerSender.setAllowFlight(false);

            sender.sendMessage(ChatColor.GREEN + "You have disabled Double Jump.");
            return true;
        }

        return true;
    }
}
