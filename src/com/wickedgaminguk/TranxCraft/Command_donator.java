
package com.wickedgaminguk.TranxCraft;

import java.util.List;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

class Command_donator extends TCP_Command implements CommandExecutor {

    public Command_donator(TranxCraft plugin) {
    }
    
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
            
            if(args.length == 0) {
                sender.sendMessage(TCP_Util.Invalid_Usage);
                return false;
            }
        
            Player player;
            //try {
                player = getPlayer(args[0]);
           // }
           /* catch (PlayerNotFoundException ex) {
                sender.sendMessage(ChatColor.RED + ex.getMessage());
                return true;
            }*/
            String Player = player.getName();
            String Sender = sender.getName();
            
            if(args[0].equalsIgnoreCase("activate")) {
                if(sender instanceof Player && sender.hasPermission("tranxcraft.donator")) {
                    if(args.length >0) {
                        sender.sendMessage(TCP_Util.Invalid_Usage);
                        return false;
                    }
                    else {
                        TCP_ModeratorList.getDonators().add(Sender);
                        plugin.getConfig().set("Donators",TCP_ModeratorList.getDonators());
                        plugin.saveConfig();
                        Bukkit.broadcastMessage(ChatColor.GREEN + Sender + " has bought a donator rank and activated it, congratulations!");
                    }
                }
                else {
                    sender.sendMessage(TCP_Util.noPerms);
                    return true;
                }
            }
            
            if(args[0].equalsIgnoreCase("add")) {
            
                if(!(sender instanceof Player) || !(sender.getName().equalsIgnoreCase("HeXeRei452"))) {
                    sender.sendMessage(TCP_Util.noPerms);
                    return true;
                }
                
                
                TCP_ModeratorList.getDonators().add(Player);
                plugin.getConfig().set("Donators",TCP_ModeratorList.getDonators());
                plugin.saveConfig();
                Bukkit.broadcastMessage(ChatColor.GREEN + player.getName() + " has bought a donator rank, congratulations!");
                return true;
            }
            
            if(args[0].equalsIgnoreCase("remove")) {
            
                if(!(sender instanceof Player) || !(sender.getName().equalsIgnoreCase("HeXeRei452"))) {
                    sender.sendMessage(TCP_Util.noPerms);
                    return true;
                }
                
                TCP_ModeratorList.getDonators().remove(Player);
                plugin.getConfig().set("Donators",TCP_ModeratorList.getDonators());
                plugin.saveConfig();
                Bukkit.broadcastMessage(ChatColor.RED + player.getName() + "'s donator rank has expired, or (s)he's been abusing, how unfortunate!");
                return true;
            }
        return false;
        
    }
    
}
