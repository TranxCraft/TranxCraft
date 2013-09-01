
package com.wickedgaminguk.TranxCraft;

import static com.wickedgaminguk.TranxCraft.TranxCraft.Invalid_Usage;
import static com.wickedgaminguk.TranxCraft.TranxCraft.noPerms;
import java.util.List;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

class Command_donator extends TranxCraft implements CommandExecutor {

    public Command_donator(TranxCraft plugin) {
    }
    
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
            
            List<String> Donators = plugin.getConfig().getStringList("Donators");
            Player player = getPlayer(args[1]);
            String Player = player.getName();
            String Sender = sender.getName();
            
            
            if(args.length == 0) {
                Bukkit.broadcastMessage("HI!");
                return true;
            }
            
            if(args[0].equalsIgnoreCase("activate")) {
                if(sender instanceof Player && sender.hasPermission("tranxcraft.donator")) {
                    if(args.length >0) {
                        sender.sendMessage(Invalid_Usage);
                        return false;
                    }
                    else {
                        Donators.add(Sender);
                        plugin.getConfig().set("Donators",Donators);
                        plugin.saveConfig();
                        Bukkit.broadcastMessage(ChatColor.GREEN + Sender + " has bought a donator rank and activated it, congratulations!");
                    }
                }
                else {
                    sender.sendMessage(noPerms);
                    return true;
                }
            }
            
            if(args[0].equalsIgnoreCase("add")) {
            
                if(!(sender instanceof Player) || !(sender.getName().equalsIgnoreCase("HeXeRei452"))) {
                    sender.sendMessage(noPerms);
                    return true;
                }
                
                
                Donators.add(Player);
                plugin.getConfig().set("Donators",Donators);
                plugin.saveConfig();
                Bukkit.broadcastMessage(ChatColor.GREEN + player.getName() + " has bought a donator rank, congratulations!");
                return true;
            }
            
            if(args[0].equalsIgnoreCase("remove")) {
            
                if(!(sender instanceof Player) || !(sender.getName().equalsIgnoreCase("HeXeRei452"))) {
                    sender.sendMessage(noPerms);
                    return true;
                }
                
                this.getConfig().getStringList("Donators").remove(Player);
                plugin.saveConfig();
                Bukkit.broadcastMessage(ChatColor.RED + player.getName() + "'s donator rank has expired, or (s)he's been abusing, how unfortunate!");
                return true;
            }
        return false;
        
    }
    
}
