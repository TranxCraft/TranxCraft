
package com.wickedgaminguk.TranxCraft.Commands;

import com.wickedgaminguk.TranxCraft.TCP_Log;
import com.wickedgaminguk.TranxCraft.TCP_Mail;
import com.wickedgaminguk.TranxCraft.TranxCraft;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import net.pravian.bukkitlib.command.BukkitCommand;
import net.pravian.bukkitlib.command.CommandPermissions;
import net.pravian.bukkitlib.command.SourceType;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

@CommandPermissions(source = SourceType.PLAYER, usage = "Usage: /<command> <player> <reason>")
public class Command_report extends BukkitCommand<TranxCraft> {
        
    @Override
    public boolean run(CommandSender sender, Command command, String commandLabel, String[] args) {
        
        Player player = getPlayer(args[0]);
        Player sender_p = player;
        
        try {
            sender_p = (Player) sender;
        }
        catch(Exception ex) {
            sender.sendMessage(ChatColor.RED + "Player could not be found.");
        }
        
        if(player == null) {
            sender.sendMessage(ChatColor.RED + "This player either isn't online, or doesn't exist.");
            return true;
        }
        
        if(player == sender_p) {
            sender.sendMessage(ChatColor.RED + "Don't try to report yourself, idiot.");
            sender_p.addPotionEffect(new PotionEffect(PotionEffectType.CONFUSION, 1200, 50));
            return true;
        }
        
        String Reported = player.getName();
        String Reporter = sender.getName();        
        String Report = StringUtils.join(ArrayUtils.subarray(args, 1, args.length), " ");
        
        Bukkit.broadcast(ChatColor.RED + "[REPORTS]" + ChatColor.GOLD + sender.getName() + " has reported " + player.getName() + " for " + Report, "tranxcraft.moderator");
        TCP_Mail.send("TranxCraft Reports - User " + Reported + " has been reported", "Hey, just to let you know, " + Reported + " has been reported by " + Reporter + " for " + Report);
        
        SimpleDateFormat sdf = new SimpleDateFormat("dd-M hh:mm");
	String Time = sdf.format(new Date());
        try {
            plugin.updateDatabase("INSERT INTO reports (Reported, Reporter, Report, Time, Status) VALUES ('" + Reported + "', '" + Reporter + "', '" + Report + "', '" + Time + "', 'open');");
            TCP_Log.info("New Report Added by: " + Reporter);
        }
        catch (SQLException ex) {
            sender.sendMessage("Error submitting report to Database.");
            TCP_Log.severe(ex);
        }
        return true;
   }
}