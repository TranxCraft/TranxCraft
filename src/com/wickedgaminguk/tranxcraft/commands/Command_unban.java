package com.wickedgaminguk.tranxcraft.commands;

import com.wickedgaminguk.tranxcraft.TCP_ModeratorList.AdminType;
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
        if (args.length == 0) {
            return false;
        }

        if (!(plugin.util.hasPermission("tranxcraft.moderator", sender) || plugin.util.hasPermission(AdminType.MODERATOR, sender))) {
            return noPerms();
        }

        if (IpUtils.isValidIp(args[0])) {
            if (!(plugin.ban.isIPBanned(args[0]))) {
                sender.sendMessage(ChatColor.RED + "This IP isn't banned.");
                return true;
            }

            plugin.ban.unbanIP(args[0]);
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

        UUID playerID = plugin.util.playerToUUID(player);

        if (playerID != null) {
            if (!(plugin.ban.isUUIDBanned(playerID.toString()))) {
                sender.sendMessage(ChatColor.RED + "This Player isn't banned.");
                return true;
            }

            plugin.ban.unbanUser(playerID);

            String playerName = plugin.util.UUIDToPlayer(playerID);

            sender.sendMessage(ChatColor.GREEN + "You have successfully unbanned: " + playerName + " with the UUID of " + playerID.toString());
        }

        return true;
    }
}
