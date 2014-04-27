package com.wickedgaminguk.TranxCraft.Commands;

import com.wickedgaminguk.TranxCraft.TCP_Ban;
import com.wickedgaminguk.TranxCraft.TCP_ModeratorList;
import com.wickedgaminguk.TranxCraft.TranxCraft;
import java.sql.SQLException;
import net.pravian.bukkitlib.command.BukkitCommand;
import net.pravian.bukkitlib.command.CommandPermissions;
import net.pravian.bukkitlib.command.SourceType;
import net.pravian.bukkitlib.util.LoggerUtils;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@CommandPermissions(source = SourceType.ANY)
public class Command_banip extends BukkitCommand<TranxCraft> {

    @Override
    public boolean run(CommandSender sender, Command command, String commandLabel, String[] args) {
        TCP_ModeratorList TCP_ModeratorList = new TCP_ModeratorList(plugin);
        TCP_Ban TCP_Ban = new TCP_Ban(plugin);

        if (sender instanceof Player && !(sender.hasPermission("tranxcraft.moderator") || sender.isOp())) {
            return noPerms();
        }

        if (args.length == 0 || args.length == 1) {
            return false;
        }

        Player player = getPlayer(args[0]);
        Player sender_p = null;

        if (sender instanceof Player) {
            sender_p = (Player) sender;
        }

        if (player == null) {
            sender.sendMessage(ChatColor.RED + "This player either isn't online, or doesn't exist.");
            return true;
        }

        if (sender instanceof Player) {
            if (player == sender_p) {
                sender.sendMessage(ChatColor.RED + "Don't try to ban yourself, idiot.");
                return true;
            }
        }

        if (!sender.hasPermission("tranxcraft.override")) {
            if (!((TCP_ModeratorList.isPlayerMod(player)))) {
                sender.sendMessage(ChatColor.RED + "You may not ban " + player.getName());
                return true;
            }
        }

        String ban_reason = null;

        if (args.length >= 2) {
            ban_reason = StringUtils.join(ArrayUtils.subarray(args, 1, args.length), " ");
        }

        Bukkit.broadcastMessage(ChatColor.RED + "" + sender.getName() + " - banning " + player.getName() + " for " + ban_reason);

        //set gamemode to survival
        player.setGameMode(GameMode.SURVIVAL);

        // kick Player:
        player.kickPlayer(ChatColor.RED + "You have been IP banned by " + sender.getName() + "." + (ban_reason != null ? ("\nReason: " + ChatColor.YELLOW + ban_reason) : ""));

        //Ban IP
        TCP_Ban.banIP(player, sender.getName(), ban_reason);

        try {
            plugin.updateDatabase("INSERT INTO bans (username, admin, reason, ip) VALUES ('" + player.getName() + "', '" + sender.getName() + "', '" + ban_reason + "', '" + player.getAddress().getHostString() + "');");
        }
        catch (SQLException ex) {
            LoggerUtils.severe(plugin, ex);
        }

        return true;
    }
}
