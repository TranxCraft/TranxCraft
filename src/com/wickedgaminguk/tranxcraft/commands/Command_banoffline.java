package com.wickedgaminguk.tranxcraft.commands;

import com.wickedgaminguk.tranxcraft.TCP_ModeratorList.AdminType;
import com.wickedgaminguk.tranxcraft.TranxCraft;
import java.sql.SQLException;
import java.util.UUID;
import net.pravian.bukkitlib.command.BukkitCommand;
import net.pravian.bukkitlib.command.CommandPermissions;
import net.pravian.bukkitlib.command.SourceType;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@CommandPermissions(source = SourceType.ANY)
public class Command_banoffline extends BukkitCommand<TranxCraft> {

    @Override
    public boolean run(CommandSender sender, Command command, String commandLabel, String[] args) {
        if (!(plugin.util.hasPermission("tranxcraft.moderator", sender) || plugin.util.hasPermission(AdminType.MODERATOR, sender))) {
            return noPerms();
        }

        if (args.length == 0 || args.length == 1) {
            return false;
        }

        OfflinePlayer player = getOfflinePlayer(args[0]);

        if (player == null) {
            sender.sendMessage(ChatColor.RED + "This player either isn't online, or doesn't exist.");
            return true;
        }

        if (sender instanceof Player) {
            if (player == playerSender) {
                sender.sendMessage(ChatColor.RED + "Don't try to ban yourself, idiot.");
                return true;
            }
        }

        if (player.isOnline()) {
            sender.sendMessage(ChatColor.RED + "Please use /ban or /banip for online players.");
            return true;
        }

        UUID playerID = plugin.util.playerToUuid(player.getName());

        if (plugin.ban.isUUIDBanned(playerID)) {
            sender.sendMessage(ChatColor.RED + player.getName() + " is already banned.");
        }

        if (!sender.hasPermission("tranxcraft.override")) {
            if (plugin.moderatorList.isPlayerMod((Player) player)) {
                sender.sendMessage(ChatColor.RED + "You may not ban " + player.getName());
                return true;
            }
        }

        String banReason = null;

        if (args.length >= 2) {
            banReason = StringUtils.join(ArrayUtils.subarray(args, 1, args.length), " ");
        }

        Bukkit.broadcastMessage(ChatColor.RED + "" + sender.getName() + " - banning " + player.getName() + " for " + banReason);

        //Ban Username
        plugin.ban.banUser(playerID, sender.getName(), banReason);

        try {
            plugin.updateDatabase("INSERT INTO bans (username, admin, reason, ip) VALUES ('" + player.getName() + "', '" + sender.getName() + "', '" + banReason + "', '" + "127.0.0.2" + "');");
        }
        catch (SQLException ex) {
            plugin.util.debug(ex);
        }

        return true;
    }
}
