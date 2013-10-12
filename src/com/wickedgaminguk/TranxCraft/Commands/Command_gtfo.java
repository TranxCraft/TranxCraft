
package com.wickedgaminguk.TranxCraft.Commands;

import com.wickedgaminguk.TranxCraft.*;
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

public class Command_gtfo extends TCP_Command implements CommandExecutor {

    public Command_gtfo(TranxCraft plugin) {
    this.plugin = plugin;
    }
    
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        
        if(sender instanceof Player && !(sender.hasPermission("tranxcraft.gtfo") || sender.isOp())){
            sender.sendMessage(TCP_Util.noPerms);
            return true;
        }
        /*
        if(!(sender instanceof Player)) {
            sender.sendMessage("GTFO From Console currently doesn't work, sorry.");
        }*/
        
        if(args.length == 0 || args.length == 1) {
            return false;
        }
        
        Player player;
            //try {
                player = getPlayer(args[0]);
            //}
            /*catch (PlayerNotFoundException ex) {
                sender.sendMessage(ChatColor.RED + ex.getMessage());
                return true;
            }*/
                
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
            sender.sendMessage(ChatColor.RED + "Don't try to ban yourself, idiot.");
        }
        
        if(!sender.hasPermission("tranxcraft.gtfo.override")) {
            if(TCP_ModeratorList.getModerators().contains(player.getName()) || TCP_ModeratorList.getAdmins().contains(player.getName()) || TCP_ModeratorList.getleadAdmins().contains(player.getName()) || TCP_ModeratorList.getExecutives().contains(player.getName())) {
                sender.sendMessage(ChatColor.RED + "You may not ban " + player.getName());
                return true;
            }
        }
        
        String ban_reason = null;
        
        if (args.length >= 2) {
            ban_reason = StringUtils.join(ArrayUtils.subarray(args, 1, args.length), " ");
        }
        
        Bukkit.broadcastMessage(ChatColor.RED + "" + sender.getName() + " - banning " + player.getName() + " for " + ban_reason);
        
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
        TCP_Util.banUsername(player.getName(), ban_reason, null);
        
        // kick Player:
        player.kickPlayer(ChatColor.RED + "GTFO" + (ban_reason != null ? ("\nReason: " + ChatColor.YELLOW + ban_reason) : ""));
        
        return true;
    }
}