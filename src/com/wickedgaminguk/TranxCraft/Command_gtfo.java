
package com.wickedgaminguk.TranxCraft;

import java.util.List;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

class Command_gtfo extends TranxCraft implements CommandExecutor {

    public Command_gtfo(TranxCraft plugin) {
    this.plugin = plugin;
    }
    
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        
        List<String> Executives = plugin.getConfig().getStringList("Executives");
        List<String> leadAdmins = plugin.getConfig().getStringList("Lead_Admins");
        List<String> Admins = plugin.getConfig().getStringList("Admins");
        List<String> Moderators = plugin.getConfig().getStringList("Moderators");
        
        if(sender instanceof Player && !(sender.hasPermission("tranxcraft.gtfo") || sender.isOp())){
            sender.sendMessage(noPerms);
            return true;
        }
        
        if(!(sender instanceof Player)) {
            sender.sendMessage("GTFO From Console currently doesn't work, sorry.");
        }
        
        if(args.length == 0 || args.length == 1) {
            return false;
        }
        
        Player player = getPlayer(args[0]);
        Player sender_p = (Player) sender;
        
        if(player == null) {
            sender.sendMessage(ChatColor.RED + "This player either isn't online, or doesn't exist.");
            return true;
        }
        
        if(player == sender_p) {
            sender.sendMessage(ChatColor.RED + "Don't try to ban yourself, idiot.");
        }
        
        if(!sender.hasPermission("tranxcraft.gtfo.override")) {
            if(Moderators.contains(player.getName()) || Admins.contains(player.getName()) || leadAdmins.contains(player.getName()) || Executives.contains(player.getName())) {
                sender.sendMessage(ChatColor.RED + "You may not ban " + player.getName());
            }
        }
        
        String ban_reason = null;
        
        if (args.length >= 2) {
            ban_reason = StringUtils.join(ArrayUtils.subarray(args, 1, args.length), " ");
        }
        
        Bukkit.broadcastMessage(ChatColor.RED + "" + sender_p.getName() + " - banning " + player.getName() + " for " + ban_reason);
        
        //rollback
        Bukkit.dispatchCommand(sender, "co rollback " + player.getName() + " t:500d r:#global");
        
        //set gamemode to survival
        player.setGameMode(GameMode.SURVIVAL);
        
        // strike with lightning effect, credits again to Steven Lawson/Madgeek & Jerom Van Der Sar/DarthSalamon for this bit of code
        final Location targetPos = player.getLocation();
        for (int x = -1; x <= 1; x++) {
            for (int z = -1; z <= 1; z++) {
                final Location strike_pos = new Location(targetPos.getWorld(), targetPos.getBlockX() + x, targetPos.getBlockY(), targetPos.getBlockZ() + z);
                targetPos.getWorld().strikeLightning(strike_pos);
            }
        }
        
        //Ban Username
        TranxCraft.banUsername(player.getName(), ban_reason, null);
        
        // kick Player:
        player.kickPlayer(ChatColor.RED + "GTFO" + (ban_reason != null ? ("\nReason: " + ChatColor.YELLOW + ban_reason) : ""));
        
        return true;
    }
}