
package com.wickedgaminguk.TranxCraft;

import static com.wickedgaminguk.TranxCraft.TranxCraft.Invalid_Usage;
import static com.wickedgaminguk.TranxCraft.TranxCraft.logger;
import static com.wickedgaminguk.TranxCraft.TranxCraft.noPerms;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.logging.Level;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

class Command_tranxcraft extends TranxCraft implements CommandExecutor {

    public Command_tranxcraft(TranxCraft plugin) {
    this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        
        List<String> Executives = plugin.getConfig().getStringList("Executives");
        List<String> leadAdmins = plugin.getConfig().getStringList("Lead_Admins");
        List<String> Admins = plugin.getConfig().getStringList("Admins");
        List<String> Moderators = plugin.getConfig().getStringList("Moderators");
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy-HH:mm:ss");
        Calendar cal = Calendar.getInstance(); 
        
        if(args.length == 0 || args[0].equalsIgnoreCase("info")) {
                    sender.sendMessage(ChatColor.GREEN + "-- Basic TranxCraft Information --");
                    sender.sendMessage(ChatColor.AQUA + "Owner: HeXeRei452/WickedGamingUK");
                    sender.sendMessage(ChatColor.AQUA + "Lead Developer: HeXeRei452/WickedGamingUK");
                    sender.sendMessage(ChatColor.AQUA + "Lead Builder: kromeblade");
                    sender.sendMessage(ChatColor.AQUA + "Website: http://www.tranxcraft.com/");
                    sender.sendMessage(ChatColor.AQUA + "Forums: http://www.tranxcraft.com/forums");
                    sender.sendMessage(ChatColor.GREEN + "------------------------");
                    return true;
                }
                if(args[0].equalsIgnoreCase("reload")) {
                    if(!(sender.hasPermission("tranxcraft.reload") || sender.isOp())){
                        sender.sendMessage(noPerms);
                        return true;
                    }
                    if(args.length > 2) {
                        sender.sendMessage(Invalid_Usage);
                        return true;
                    }
                    if(args.length == 1) {
                        Bukkit.broadcastMessage("[TranxCraft]" + ChatColor.RED + " Server Reloading.");
                        reloadServer();
                        plugin.getLogger().log(Level.INFO, "Server Reloaded.");
                        Bukkit.broadcastMessage("[TranxCraft]" + ChatColor.GREEN + " Server Reloaded.");
                        return true;
                    }
                    if(args.length == 2) {
                        if (!pm.isPluginEnabled(args[1])) {
                            sender.sendMessage(ChatColor.RED + "[TranxCraft] Plugin Invalid.");
                            return true;
                        }
                        Plugin tPlugin = pm.getPlugin(args[1]);
                        reloadPlugin(tPlugin);
                        sender.sendMessage(ChatColor.GREEN + "[TranxCraft] Plugin %a reloaded.".replaceAll("%a", tPlugin.getName()));
                        logger.log(Level.INFO, "{0} reloaded {1} at {2}", new Object[]{sender.getName(), tPlugin, dateFormat.format(cal.getTime())});
                        return true;
                   }
                }
                
                if(args[0].equalsIgnoreCase("enable")) {
                    if(!(sender.hasPermission("tranxcraft.enable"))){
                        sender.sendMessage(noPerms);
                        return true;
                    }
                    if(args.length > 2|| args.length == 0){
                        sender.sendMessage(Invalid_Usage);
                        return false;
                    }
                    if(pm.isPluginEnabled(args[1])) {
                        sender.sendMessage(ChatColor.RED + "[TranxCraft] Plugin Already Enabled.");
                        return true;
                    }
                    else {
                        Plugin tPlugin = pm.getPlugin(args[1]);
                        enablePlugin(tPlugin);
                        sender.sendMessage(ChatColor.GREEN + "[TranxCraft] Plugin " + tPlugin + " is enabled.");
                        return true;
                    }
                }
                if(args[0].equalsIgnoreCase("disable")) {
                    if(!(sender.hasPermission("tranxcraft.disable"))) {
                        sender.sendMessage(noPerms);
                        return true;
                    }
                    if(args.length > 2 || args.length == 0){
                        sender.sendMessage(Invalid_Usage);
                        return false;
                    }
                    if(!pm.isPluginEnabled(args[1])) {
                        sender.sendMessage(ChatColor.RED + "[TranxCraft] Plugin Already Disabled.");
                        return true;
                    }
                    else {
                        Plugin tPlugin = pm.getPlugin(args[1]);
                        disablePlugin(tPlugin);
                        sender.sendMessage(ChatColor.RED + "[TranxCraft] Plugin " + tPlugin + " is disabled.");
                    }
                    return true;
                }
                
                if(args[0].equalsIgnoreCase("system")) {
                    Player player = getPlayer(args[3]);
                    String playerName = player.getName();
                    
                    if(sender instanceof Player && !(sender.hasPermission("tranxcraft.system"))) {
                    sender.sendMessage(noPerms);
                    return true;
                    }
                    
                    if(args[1].equalsIgnoreCase("add")) {
                        if(args[2].equalsIgnoreCase("Moderator")) {
                            Moderators.add(playerName);
                            plugin.getConfig().set("Moderators",Moderators);
                            plugin.saveConfig();
                            Bukkit.broadcastMessage(ChatColor.GREEN + playerName + " has been promoted to Moderator, congratulations!");
                            Bukkit.dispatchCommand(sender, "manuadd " + playerName + " moderator Spawn");
                        }
                        
                        if(args[2].equalsIgnoreCase("Admin")) {
                            Admins.add(playerName);
                            plugin.getConfig().set("Admins",Admins);
                            plugin.saveConfig();
                            Bukkit.broadcastMessage(ChatColor.GREEN + playerName + " has been promoted to Admin, congratulations!");
                            Bukkit.dispatchCommand(sender, "manuadd " + playerName + " admin Spawn");
                        }
                        
                        if(args[2].equalsIgnoreCase("LeadAdmin")) {
                            leadAdmins.add(playerName);
                            plugin.getConfig().set("Lead_Admins",Admins);
                            plugin.saveConfig();
                            Bukkit.broadcastMessage(ChatColor.GREEN + playerName + " has been promoted to Admin, congratulations!");
                            Bukkit.dispatchCommand(sender, "manuadd " + playerName + " leadadmin Spawn");
                        }
                        
                        if(args[2].equalsIgnoreCase("Executive")) {
                            Executives.add(playerName);
                            plugin.getConfig().set("Executives",Executives);
                            plugin.saveConfig();
                            Bukkit.broadcastMessage(ChatColor.GREEN + playerName + " has been promoted to an Executive, congratulations!");
                            Bukkit.dispatchCommand(sender, "manuadd " + playerName + " executive Spawn");
                        }
                     }
                    
                     if(args[1].equalsIgnoreCase("remove")) {
                        if(args[2].equalsIgnoreCase("Moderator")) {
                            Moderators.remove(playerName);
                            plugin.getConfig().set("Moderators",Moderators);
                            plugin.saveConfig();
                            Bukkit.broadcastMessage(ChatColor.RED + playerName + " has been removed from Moderator!");
                            Bukkit.dispatchCommand(sender, "manuadd " + playerName + " member Spawn");
                        }
                        
                        if(args[2].equalsIgnoreCase("Admin")) {
                            Admins.remove(playerName);
                            plugin.getConfig().set("Admins",Admins);
                            plugin.saveConfig();
                            Bukkit.broadcastMessage(ChatColor.RED + playerName + " has been removed from Admin!");
                            Bukkit.dispatchCommand(sender, "manuadd " + playerName + " member Spawn");
                        }
                        
                        if(args[2].equalsIgnoreCase("LeadAdmin")) {
                            leadAdmins.remove(playerName);
                            plugin.getConfig().set("Lead_Admins",Admins);
                            plugin.saveConfig();
                            Bukkit.broadcastMessage(ChatColor.RED + playerName + " has been removed from being a lead Admin!");
                            Bukkit.dispatchCommand(sender, "manuadd " + playerName + " member Spawn");
                        }
                        
                        if(args[2].equalsIgnoreCase("Executive")) {
                            Executives.remove(playerName);
                            plugin.getConfig().set("Executives",Executives);
                            plugin.saveConfig();
                            Bukkit.broadcastMessage(ChatColor.RED + playerName + " has been removed from being an Executive!");
                            Bukkit.dispatchCommand(sender, "manuadd " + playerName + " member Spawn");
                        }
                     }
                    
                    
                    
                }
        return false;
    }
    
   
   public void reloadPlugin(Plugin plugin) {
       pm.disablePlugin(plugin);
       pm.enablePlugin(plugin);
   }
   public void enablePlugin(Plugin plugin) {
       pm.enablePlugin(plugin);
   }
   public void disablePlugin(Plugin plugin) {
       pm.disablePlugin(plugin);
   }
   public void reloadServer() {
       Plugin[] plugins = pm.getPlugins();

           for (int x = 0; x < plugins.length; x++) {
               reloadPlugin(plugins[x]);
           }
   }
}
