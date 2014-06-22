package com.wickedgaminguk.tranxcraft.commands;

import com.wickedgaminguk.tranxcraft.TCP_Mail.RecipientType;
import com.wickedgaminguk.tranxcraft.TranxCraft;
import java.sql.SQLException;
import net.pravian.bukkitlib.command.BukkitCommand;
import net.pravian.bukkitlib.command.CommandPermissions;
import net.pravian.bukkitlib.command.SourceType;
import net.pravian.bukkitlib.util.LoggerUtils;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

@CommandPermissions(source = SourceType.PLAYER)
public class Command_report extends BukkitCommand<TranxCraft> {

    @Override
    public boolean run(CommandSender sender, Command command, String commandLabel, String[] args) {
        if (args.length < 2) {
            sender.sendMessage(ChatColor.RED + "Incorrect Usage");
            return false;
        }

        Player player = getPlayer(args[0]);

        if (player == null) {
            sender.sendMessage(ChatColor.RED + "This player either isn't online, or doesn't exist.");
            return true;
        }

        if (player == playerSender) {
            sender.sendMessage(ChatColor.RED + "Don't try to report yourself, idiot.");
            playerSender.addPotionEffect(new PotionEffect(PotionEffectType.CONFUSION, 1200, 50));
            return true;
        }

        if (plugin.moderatorList.isPlayerMod(player)) {
            sender.sendMessage(ChatColor.RED + "You may not report " + player.getName() + ", they are a moderator. For issues with our moderators, report them on our forums.");
            return true;
        }

        String reported = player.getName();
        String reporter = sender.getName();
        String report = StringUtils.join(ArrayUtils.subarray(args, 1, args.length), " ");

        sender.sendMessage(ChatColor.GREEN + "Thank you, your report has been successfully logged.");

        Bukkit.broadcast(ChatColor.RED + "[REPORTS] " + ChatColor.GOLD + sender.getName() + " has reported " + player.getName() + " for " + report, "tranxcraft.moderator");

        plugin.mail.send(RecipientType.ALL, "TranxCraft Reports - User " + reported + " has been reported", "Hey, just to let you know, " + reported + " has been reported by " + reporter + " for " + report);

        try {
            plugin.updateDatabase("INSERT INTO reports (Reported, Reporter, Report, Time, Status) VALUES ('" + reported + "', '" + reporter + "', '" + report + "', '" + plugin.time.getLongDate() + "', 'open');");
            LoggerUtils.info(plugin, "New Report Added by: " + reporter);
        }
        catch (SQLException ex) {
            sender.sendMessage("Error submitting report to Database.");
            plugin.util.debug(ex);
        }

        return true;
    }
}
