
package com.wickedgaminguk.TranxCraft;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.logging.Level;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

class Command_tranxcraft extends TCP_Command implements CommandExecutor {

    public Command_tranxcraft(TranxCraft plugin) {
    this.plugin = plugin;
    }
    
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy-HH:mm:ss");
        Calendar cal = Calendar.getInstance(); 
        
        if(args.length == 0 || args[0].equalsIgnoreCase("info")) {
                    sender.sendMessage(ChatColor.GREEN + "-- Basic TranxCraft Information --");
                    sender.sendMessage(ChatColor.AQUA + "Owner: HeXeRei452/WickedGamingUK");
                    sender.sendMessage(ChatColor.AQUA + "Lead Developer: HeXeRei452/WickedGamingUK");
                    //sender.sendMessage(ChatColor.AQUA + "Lead Builder: kromeblade");
                    sender.sendMessage(ChatColor.AQUA + "Website: http://www.tranxcraft.com/");
                    sender.sendMessage(ChatColor.AQUA + "Forums: http://www.tranxcraft.com/forums");
                    sender.sendMessage(ChatColor.GREEN + "------------------------");
                    return true;
                }
                if(args[0].equalsIgnoreCase("reload")) {
                    if(!(sender.hasPermission("tranxcraft.reload") || sender.isOp())){
                        sender.sendMessage(TCP_Util.noPerms);
                        return true;
                    }
                    if(args.length > 2) {
                        sender.sendMessage(TCP_Util.Invalid_Usage);
                        return true;
                    }
                    if(args.length == 1) {
                        Bukkit.broadcastMessage("[TranxCraft]" + ChatColor.RED + " Server Reloading.");
                        TCP_PluginHandler.reloadServer();
                        TCP_Log.info("Server Reloaded.");
                        Bukkit.broadcastMessage("[TranxCraft]" + ChatColor.GREEN + " Server Reloaded.");
                        return true;
                    }
                    if(args.length == 2) {
                        if (!Bukkit.getPluginManager().isPluginEnabled(args[1])) {
                            sender.sendMessage(ChatColor.RED + "[TranxCraft] Plugin Invalid.");
                            return true;
                        }
                        Plugin tPlugin = Bukkit.getPluginManager().getPlugin(args[1]);
                        TCP_PluginHandler.reloadPlugin(tPlugin);
                        sender.sendMessage(ChatColor.GREEN + "[TranxCraft] Plugin %a reloaded.".replaceAll("%a", tPlugin.getName()));
                        TCP_Util.logger.log(Level.INFO, "{0} reloaded {1} at {2}", new Object[]{sender.getName(), tPlugin, dateFormat.format(cal.getTime())});
                        return true;
                   }
                }
                
                if(args[0].equalsIgnoreCase("enable")) {
                    if(!(sender.hasPermission("tranxcraft.enable"))){
                        sender.sendMessage(TCP_Util.noPerms);
                        return true;
                    }
                    if(args.length > 2|| args.length == 0){
                        sender.sendMessage(TCP_Util.Invalid_Usage);
                        return false;
                    }
                    if(Bukkit.getPluginManager().isPluginEnabled(args[1])) {
                        sender.sendMessage(ChatColor.RED + "[TranxCraft] Plugin Already Enabled.");
                        return true;
                    }
                    else {
                        Plugin tPlugin = Bukkit.getPluginManager().getPlugin(args[1]);
                        TCP_PluginHandler.enablePlugin(tPlugin);
                        sender.sendMessage(ChatColor.GREEN + "[TranxCraft] Plugin " + tPlugin + " is enabled.");
                        return true;
                    }
                }
                if(args[0].equalsIgnoreCase("disable")) {
                    if(!(sender.hasPermission("tranxcraft.disable"))) {
                        sender.sendMessage(TCP_Util.noPerms);
                        return true;
                    }
                    if(args.length > 2 || args.length == 0){
                        sender.sendMessage(TCP_Util.Invalid_Usage);
                        return false;
                    }
                    if(!Bukkit.getPluginManager().isPluginEnabled(args[1])) {
                        sender.sendMessage(ChatColor.RED + "[TranxCraft] Plugin Already Disabled.");
                        return true;
                    }
                    else {
                        Plugin tPlugin = Bukkit.getPluginManager().getPlugin(args[1]);
                        TCP_PluginHandler.disablePlugin(tPlugin);
                        sender.sendMessage(ChatColor.RED + "[TranxCraft] Plugin " + tPlugin + " is disabled.");
                    }
                    return true;
                }
                
                if(args[0].equalsIgnoreCase("system")) {
                    Player player;
                    player = getPlayer(args[3]);
                    
                    String playerName = player.getName();
                    
                    if(sender instanceof Player && !(sender.hasPermission("tranxcraft.system"))) {
                    sender.sendMessage(TCP_Util.noPerms);
                    return true;
                    }
                    
                    if(args[1].equalsIgnoreCase("add")) {
                        if(args[2].equalsIgnoreCase("Moderator")) {
                            TCP_ModeratorList.getModerators().add(playerName);
                            plugin.getConfig().set("Moderators",TCP_ModeratorList.getModerators());
                            plugin.saveConfig();
                            Bukkit.broadcastMessage(ChatColor.GREEN + playerName + " has been promoted to Moderator, congratulations!");
                            Bukkit.dispatchCommand(sender, "manuadd " + playerName + " moderator Spawn");
                        }
                        
                        if(args[2].equalsIgnoreCase("Admin")) {
                            TCP_ModeratorList.getAdmins().add(playerName);
                            plugin.getConfig().set("Admins",TCP_ModeratorList.getAdmins());
                            plugin.saveConfig();
                            Bukkit.broadcastMessage(ChatColor.GREEN + playerName + " has been promoted to Admin, congratulations!");
                            Bukkit.dispatchCommand(sender, "manuadd " + playerName + " admin Spawn");
                        }
                        
                        if(args[2].equalsIgnoreCase("LeadAdmin")) {
                            TCP_ModeratorList.getleadAdmins().add(playerName);
                            plugin.getConfig().set("Lead_Admins",TCP_ModeratorList.getleadAdmins());
                            plugin.saveConfig();
                            Bukkit.broadcastMessage(ChatColor.GREEN + playerName + " has been promoted to Admin, congratulations!");
                            Bukkit.dispatchCommand(sender, "manuadd " + playerName + " leadadmin Spawn");
                        }
                        
                        if(args[2].equalsIgnoreCase("Executive")) {
                            TCP_ModeratorList.getExecutives().remove(playerName);
                            plugin.getConfig().set("Executives",TCP_ModeratorList.getExecutives());
                            plugin.saveConfig();
                            Bukkit.broadcastMessage(ChatColor.GREEN + playerName + " has been promoted to an Executive, congratulations!");
                            Bukkit.dispatchCommand(sender, "manuadd " + playerName + " executive Spawn");
                        }
                     }
                    
                     if(args[1].equalsIgnoreCase("remove")) {
                        if(args[2].equalsIgnoreCase("Moderator")) {
                            TCP_ModeratorList.getModerators().remove(playerName);
                            plugin.getConfig().set("Moderators",TCP_ModeratorList.getModerators());
                            plugin.saveConfig();
                            Bukkit.broadcastMessage(ChatColor.RED + playerName + " has been removed from Moderator!");
                            Bukkit.dispatchCommand(sender, "manuadd " + playerName + " member Spawn");
                        }
                        
                        if(args[2].equalsIgnoreCase("Admin")) {
                            TCP_ModeratorList.getAdmins().remove(playerName);
                            plugin.getConfig().set("Admins",TCP_ModeratorList.getAdmins());
                            plugin.saveConfig();
                            Bukkit.broadcastMessage(ChatColor.RED + playerName + " has been removed from Admin!");
                            Bukkit.dispatchCommand(sender, "manuadd " + playerName + " member Spawn");
                        }
                        
                        if(args[2].equalsIgnoreCase("LeadAdmin")) {
                            TCP_ModeratorList.getleadAdmins().remove(playerName);
                            plugin.getConfig().set("Lead_Admins",TCP_ModeratorList.getAdmins());
                            plugin.saveConfig();
                            Bukkit.broadcastMessage(ChatColor.RED + playerName + " has been removed from being a lead Admin!");
                            Bukkit.dispatchCommand(sender, "manuadd " + playerName + " member Spawn");
                        }
                        
                        if(args[2].equalsIgnoreCase("Executive")) {
                            TCP_ModeratorList.getExecutives().remove(playerName);
                            plugin.getConfig().set("Executives",TCP_ModeratorList.getExecutives());
                            plugin.saveConfig();
                            Bukkit.broadcastMessage(ChatColor.RED + playerName + " has been removed from being an Executive!");
                            Bukkit.dispatchCommand(sender, "manuadd " + playerName + " member Spawn");
                        }
                     }
                    
                    
                    
                }
        return false;
    }
}
