package com.wickedgaminguk.tranxcraft.commands;

import com.wickedgaminguk.tranxcraft.TCP_Ban;
import com.wickedgaminguk.tranxcraft.TCP_ModeratorList.AdminType;
import com.wickedgaminguk.tranxcraft.TCP_Util;
import com.wickedgaminguk.tranxcraft.TranxCraft;
import java.util.UUID;
import net.pravian.bukkitlib.command.BukkitCommand;
import net.pravian.bukkitlib.util.IpUtils;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

public class Command_unban extends BukkitCommand<TranxCraft> {

    @Override
    public boolean run(CommandSender sender, Command command, String commandLabel, String[] args) {
        TCP_Ban TCP_Ban = new TCP_Ban(plugin);
        TCP_Util TCP_Util = new TCP_Util(plugin);

        if (args.length == 0) {
            return false;
        }

        if (!(TCP_Util.hasPermission("tranxcraft.moderator", sender) || TCP_Util.hasPermission(AdminType.MODERATOR, sender))) {
            return noPerms();
        }

        if (IpUtils.isValidIp(args[0])) {
            if (!(TCP_Ban.isIPBanned(args[0]))) {
                sender.sendMessage(ChatColor.RED + "This IP isn't banned.");
                return true;
            }

            TCP_Ban.unbanIP(args[0]);
            sender.sendMessage(ChatColor.GREEN + "You have successfully unbanned " + args[0]);
            return true;
        }

        String player = args[0];

        if (player == null) {
            sender.sendMessage(ChatColor.RED + "This player either isn't online, or doesn't exist.");
            return true;
        }

        if (player.equals(sender.getName())) {
            sender.sendMessage(ChatColor.RED + "You can't unban yourself.");
        }

        UUID playerID = TCP_Util.playerToUUID(player);

        if (playerID != null) {
            if (!(TCP_Ban.isUUIDBanned(playerID.toString()))) {
                sender.sendMessage(ChatColor.RED + "This Player isn't banned.");
                return true;
            }

            TCP_Ban.unbanUser(playerID);

            String playerName = TCP_Util.UUIDToPlayer(playerID);

            sender.sendMessage(ChatColor.GREEN + "You have successfully unbanned: " + playerName + " with the UUID of " + playerID.toString());
        }

        return true;
    }
}
