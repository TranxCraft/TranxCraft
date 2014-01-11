
package com.wickedgaminguk.TranxCraft.Commands;

import com.wickedgaminguk.TranxCraft.*;
import java.sql.SQLException;
import net.pravian.bukkitlib.command.BukkitCommand;
import net.pravian.bukkitlib.command.CommandPermissions;
import net.pravian.bukkitlib.command.SourceType;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@CommandPermissions(source = SourceType.ANY, usage = "Usage: /<command>")
public class Command_ucpsync extends BukkitCommand<TranxCraft> {
    
    
    
    @Override
    public boolean run(CommandSender sender, Command command, String commandLabel, String[] args) {
        
        if(sender instanceof Player && !(sender.hasPermission("tranxcraft.ucpsync") || sender.isOp())) {
            sender.sendMessage(TCP_Util.noPerms);
            return true;
        }
        
        TCP_Log.info("[TranxCraft] Starting UCP Sync Now.");
        sender.sendMessage(ChatColor.GREEN + "Starting UCP Sync Now.");
        
        if(Bukkit.getOnlinePlayers().length == 0) {            
            sender.sendMessage("No players online, UCP Sync Exiting...");
            TCP_Log.info("No players online, UCP Sync Exiting..");
            
            try {
                plugin.updateDatabase("DROP TABLE players");
                plugin.updateDatabase("CREATE TABLE players ( ID INT NOT NULL AUTO_INCREMENT, InGameName CHAR(30), OnlineTime CHAR(30), Rank CHAR(30), LastUpdated CHAR(30), PRIMARY KEY(ID) )");        
                plugin.updateDatabase("INSERT INTO players (LastUpdated) VALUES ('" + TCP_Util.getDate() + "');");
            }
            catch(SQLException ex) {
                TCP_Log.severe("SQL Connection Failed.");
                TCP_Log.severe(ex);
            }
        }
        
        if(Bukkit.getOnlinePlayers().length != 0) {
            
            try {
                plugin.updateDatabase("DROP TABLE players");
                plugin.updateDatabase("CREATE TABLE players ( ID INT NOT NULL AUTO_INCREMENT, InGameName CHAR(30), OnlineTime CHAR(30), Rank CHAR(30), LastUpdated CHAR(30), PRIMARY KEY(ID));");     
            }
            catch (SQLException ex) {
                TCP_Log.severe("SQL Connection Failed.");
                TCP_Log.severe(ex);
                sender.sendMessage(ChatColor.RED + "SQL Connection Failed.");
            }

            for (Player player : plugin.getServer().getOnlinePlayers()) {
                
                //String playerPermission = plugin.permission.getPrimaryGroup(player);
                String playerPermission = TCP_Util.getPrimaryGroup(player);
                String playerName = player.getName().toString();
                try {
                    plugin.updateDatabase("INSERT INTO players (InGameName, OnlineTime, Rank, LastUpdated) VALUES ('" + playerName + "', 98, '" + playerPermission + "', '" + TCP_Util.getDate() + "');");
                    TCP_Log.info(playerName + " " + playerPermission + " " + TCP_Util.getDate()); 
                }
                catch (SQLException ex) {
                    TCP_Log.severe("SQL Connection Failed.");
                    TCP_Log.severe(ex);
                }
            }
        }
        
        TCP_Log.info("UCP Sync Finished.");
        sender.sendMessage(ChatColor.GREEN + "UCP Sync Finished.");
        return true;
   }
}