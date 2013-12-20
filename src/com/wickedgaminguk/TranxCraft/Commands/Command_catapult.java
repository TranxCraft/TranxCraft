
package com.wickedgaminguk.TranxCraft.Commands;

import com.wickedgaminguk.TranxCraft.TCP_Util;
import net.pravian.bukkitlib.command.BukkitCommand;
import net.pravian.bukkitlib.command.CommandPermissions;
import net.pravian.bukkitlib.command.SourceType;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

@CommandPermissions(source = SourceType.ANY, usage = "Usage: /<command> <player>")
public class Command_catapult extends BukkitCommand {

    @Override
    public boolean run(CommandSender sender, Command command, String commandLabel, String[] args) {
        
        boolean power = false;
        if (sender instanceof Player && !(sender.hasPermission("explosivesmite.catapult") || sender.isOp())) {
            sender.sendMessage(TCP_Util.noPerms);
            return true;
        }
        
        if (args[1].equalsIgnoreCase("-p")) {
            power = true;
        }
        
        if (args.length == 0 || args.length > 2) {
            sender.sendMessage(ChatColor.RED + "Incorrect Usage \n Usage:");
            return false;
        }
            
        Player player = getPlayer(args[0]);
            
        if (player == null) {
            sender.sendMessage(ChatColor.RED + "This player either isn't online, or doesn't exist.");
            return true;
        }
            
        if (player.hasPermission("explosivesmite.exempt") || player.isOp()) {
            sender.sendMessage(ChatColor.RED + "You may not catapult this player.");
            return true;
        }
        
        Bukkit.broadcastMessage("" + ChatColor.RED + player.getName() + " has been catapulted!");
        player.setVelocity(new Vector(0,10,0));
        if(power == true) {
            player.setLevel(0);
            player.getInventory().clear();
        }
        return true;
    }
}