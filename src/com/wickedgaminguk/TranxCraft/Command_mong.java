
package com.wickedgaminguk.TranxCraft;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;


public class Command_mong extends TranxCraft implements CommandExecutor {
  
    
    Command_mong(TranxCraft plugin) {
    this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
            
            Player sender_p = (Player) sender;
            Player player = getPlayer(args[0]);
            if(sender instanceof Player && !(sender.hasPermission("tranxcraft.mong") || sender.isOp())){
                sender.sendMessage(noPerms);
                return true;
            }
            if(args.length == 0) {
                sender.sendMessage(Invalid_Usage);
                return false; 
            }
            
            if(player == null) {
                sender.sendMessage(ChatColor.RED + "This player either isn't online, or doesn't exist.");
                return true;
            }
            
            if(player == sender_p) {
                sender.sendMessage(ChatColor.RED + "What are you trying to do, you stupid player.");
                sender_p.getWorld().strikeLightning(player.getLocation());
                Bukkit.broadcastMessage(ChatColor.RED + sender.getName() + " is a mong, (s)he tried to mong him/herself!");
                sender_p.kickPlayer(ChatColor.RED + "You have been kicked, don't try to mong yourself...");
                return true;
            }
            
            if(!sender.hasPermission("tranxcraft.supermong")) {
                if(player.hasPermission("tranxcraft.admin") || player.hasPermission("tranxcraft.exempt") || player.isOp()) {
                    sender.sendMessage(ChatColor.RED + "I'm sorry, but you can't mong that player.");
                    return true;
                }
            }
            else {
                Bukkit.broadcastMessage(ChatColor.RED + player.getName() + " is a mong, (s)he better read the rules now.");
                player.kickPlayer(ChatColor.RED + "You have been deemed as a mong by " + sender.getName() + " so you'd better read the website (http://www.tranxcraft.com/) and learn the rules, then create a un-ban request on the forums (http://www.tranxcraft.com/forums)");
                player.setBanned(true);
                return true;
            }
        return false;
    }
}

