package com.wickedgaminguk.TranxCraft.Commands;

import com.wickedgaminguk.TranxCraft.TCP_ModeratorList;
import com.wickedgaminguk.TranxCraft.TranxCraft;
import net.pravian.bukkitlib.command.BukkitCommand;
import net.pravian.bukkitlib.command.CommandPermissions;
import net.pravian.bukkitlib.command.SourceType;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@CommandPermissions(source = SourceType.ANY)
public class Command_adminmode extends BukkitCommand<TranxCraft> {

    @Override
    public boolean run(CommandSender sender, Command command, String commandLabel, String[] args) {
        TCP_ModeratorList TCP_ModeratorList = new TCP_ModeratorList(plugin);

        if (!(sender.hasPermission("tranxcraft.admin"))) {
            return noPerms();
        }

        if (args.length == 0) {
            if (plugin.config.getBoolean("adminmode") == false) {
                sender.sendMessage("Adminmode is currently disabled.");
                return true;
            }

            if (plugin.config.getBoolean("adminmode") == true) {
                sender.sendMessage("Adminmode is currently enabled.");
                return true;
            }
        }

        if (args.length > 1) {
            return false;
        }

        if (args[0].equalsIgnoreCase("off")) {
            if (plugin.config.getBoolean("adminmode") == false) {
                sender.sendMessage(ChatColor.RED + "Adminmode is already disabled.");
                return true;
            }
            Bukkit.broadcastMessage(ChatColor.AQUA + "[" + sender.getName() + "] Opening server to all players.");
            plugin.config.set("adminmode", false);
        }

        if (args[0].equalsIgnoreCase("on")) {
            if (plugin.config.getBoolean("adminmode") == true) {
                sender.sendMessage(ChatColor.RED + "Adminmode is already enabled.");
                return true;
            }

            plugin.config.set("adminmode", true);
            Bukkit.broadcastMessage(ChatColor.AQUA + "[" + sender.getName() + "] Closing the server to all non-moderators and admins.");

            for (Player player : server.getOnlinePlayers()) {
                if (!TCP_ModeratorList.isPlayerMod(player)) {
                    player.kickPlayer(ChatColor.RED + "Server is now closed to non-moderators.");
                }
            }

            return true;
        }

        return true;
    }
}
