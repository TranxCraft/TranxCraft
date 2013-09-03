
package com.wickedgaminguk.TranxCraft;

import java.util.List;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

class Command_a extends TranxCraft implements CommandExecutor {

    public Command_a(TranxCraft plugin) {
    this.plugin = plugin;
    }
    
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
    
        Player sender_p = (Player) sender;
        List<String> Admins = plugin.getConfig().getStringList("Admins");
        
        if (!(sender instanceof Player)) {
            if (args.length == 0) {
                sender.sendMessage("When used from the console, you must define a target user to change gamemode on.");
                return true;
            }
        }
        
        if(args.length == 0) {
            sender_p.setGameMode(GameMode.ADVENTURE);
        }
        
        if(args.length == 1) {
            Player player = getPlayer(args[0]);
            if(player == sender_p) {
                sender.sendMessage(ChatColor.AQUA + "Please just use the command without parameters, it's just easier.");
                sender_p.setGameMode(GameMode.ADVENTURE);
            }
            if(!(player == sender_p) && !Admins.contains(player.getName())) {
                sender.sendMessage("Only Admins can change other user's gamemode.");
            }
            else {
                player.setGameMode(GameMode.ADVENTURE);
            }
        }
        
        if(args.length >1) {
            return false;
        }
        
        return false;
    }
}