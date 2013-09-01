package com.wickedgaminguk.TranxCraft;

import com.wickedgaminguk.mcstats.Metrics;
import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class TranxCraft extends JavaPlugin {
    
    public TranxCraft plugin;
    public TranxListener listener;
    public String pluginName;
    public String pluginVersion;
    public String pluginAuthor;
    File configFile;
    FileConfiguration config;
    PluginManager pm;
    public static final String Invalid_Usage = ChatColor.RED + "Invalid Usage.";
    public static final String noPerms = ChatColor.RED + "You don't have permission for this command.";
    public static final Logger logger = Logger.getLogger("Minecraft-Server");

  @Override
  public void onEnable() { 
        plugin = this;
        configFile = new File(plugin.getDataFolder(), "config.yml");
        this.pm = getServer().getPluginManager();
         
        PluginDescriptionFile pdf = plugin.getDescription();
        pluginName = pdf.getName();
        pluginVersion = pdf.getVersion();
        pluginAuthor = pdf.getAuthors().get(0);
        if(!new File(plugin.getDataFolder(), "config.yml").isFile()) {
            this.saveDefaultConfig();
            logger.log(Level.INFO, "{0} version {1} configuration file saved.", new Object[]{pluginName, pluginVersion});
        }
        YamlConfiguration.loadConfiguration(configFile);
        
        logger.log(Level.INFO, "{0} version {1} by {2} is enabled", new Object[]{pluginName, pluginVersion, pluginAuthor});
        
        try {
            Metrics metrics = new Metrics(this);
            metrics.start();
        }
        catch (IOException e) {
            logger.log(Level.SEVERE, "{0} Plugin Metrics have failed to submit the statistics to McStats! >:(", pluginName);
        }
        listener = new TranxListener(plugin);
        pm.registerEvents(listener, plugin);
        
        init();
  }
  
  @Override
  public void onDisable() {
        logger.log(Level.INFO, "{0} version {1} configuration file saved.", new Object[]{pluginName, pluginVersion});
        logger.log(Level.INFO, "{0} version {1} by {2} is disabled", new Object[]{pluginName, pluginVersion, pluginAuthor});
  }
  
  public void onReload() {
  }
  
  @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
      List<String> Executives = plugin.getConfig().getStringList("Executives");
      List<String> leadAdmins = plugin.getConfig().getStringList("Lead_Admins");
      List<String> Admins = plugin.getConfig().getStringList("Admins");
      List<String> Moderators = plugin.getConfig().getStringList("Moderators");
      List<String> Donators = plugin.getConfig().getStringList("Donators");
      DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy-HH:mm:ss");
      Calendar cal = Calendar.getInstance(); 
      
      
        if(cmd.getName().equalsIgnoreCase("tranxcraft")) {
                if(args.length == 0 || args[0].equalsIgnoreCase("info")) {
                    sender.sendMessage(ChatColor.GREEN + "-- Basic TranxCraft Information --");
                    sender.sendMessage(ChatColor.AQUA + "Owner: HeXeRei452/WickedGamingUK");
                    sender.sendMessage(ChatColor.AQUA + "Lead Developer: HeXeRei452/WickedGamingUK");
                    sender.sendMessage(ChatColor.AQUA + "Lead Builder: miwojedk");
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
                if(args[0].equalsIgnoreCase("dev")) {
                    if(args.length > 1){
                        sender.sendMessage(Invalid_Usage);
                        return false;
                    }
                    sender.sendMessage(ChatColor.GREEN + "-- TranxCraft Plugin Information --");
                    sender.sendMessage(ChatColor.AQUA + "Plugin Version: " + pluginVersion);
                    sender.sendMessage(ChatColor.AQUA + "Plugin Author: " + pluginAuthor);
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
                            Bukkit.dispatchCommand(sender, "manuadd " + playerName + " moderator");
                        }
                        
                        if(args[2].equalsIgnoreCase("Admin")) {
                            Admins.add(playerName);
                            plugin.getConfig().set("Admins",Admins);
                            plugin.saveConfig();
                            Bukkit.broadcastMessage(ChatColor.GREEN + playerName + " has been promoted to Admin, congratulations!");
                            Bukkit.dispatchCommand(sender, "manuadd " + playerName + " admin");
                        }
                        
                        if(args[2].equalsIgnoreCase("LeadAdmin")) {
                            leadAdmins.add(playerName);
                            plugin.getConfig().set("Lead_Admins",Admins);
                            plugin.saveConfig();
                            Bukkit.broadcastMessage(ChatColor.GREEN + playerName + " has been promoted to Admin, congratulations!");
                            Bukkit.dispatchCommand(sender, "manuadd " + playerName + " leadadmin");
                        }
                        
                        if(args[2].equalsIgnoreCase("Executive")) {
                            Executives.add(playerName);
                            plugin.getConfig().set("Executives",Executives);
                            plugin.saveConfig();
                            Bukkit.broadcastMessage(ChatColor.GREEN + playerName + " has been promoted to an Executive, congratulations!");
                            Bukkit.dispatchCommand(sender, "manuadd " + playerName + " executive");
                        }
                     }
                    
                     if(args[1].equalsIgnoreCase("remove")) {
                        if(args[2].equalsIgnoreCase("Moderator")) {
                            Moderators.remove(playerName);
                            plugin.getConfig().set("Moderators",Moderators);
                            plugin.saveConfig();
                            Bukkit.broadcastMessage(ChatColor.GREEN + playerName + " has been removed from Moderator!");
                            Bukkit.dispatchCommand(sender, "manuadd " + playerName + " member");
                        }
                        
                        if(args[2].equalsIgnoreCase("Admin")) {
                            Admins.remove(playerName);
                            plugin.getConfig().set("Admins",Admins);
                            plugin.saveConfig();
                            Bukkit.broadcastMessage(ChatColor.GREEN + playerName + " has been removed from Admin!");
                            Bukkit.dispatchCommand(sender, "manuadd " + playerName + " member");
                        }
                        
                        if(args[2].equalsIgnoreCase("LeadAdmin")) {
                            leadAdmins.remove(playerName);
                            plugin.getConfig().set("Lead_Admins",Admins);
                            plugin.saveConfig();
                            Bukkit.broadcastMessage(ChatColor.GREEN + playerName + " has been removed from being a lead Admin!");
                            Bukkit.dispatchCommand(sender, "manuadd " + playerName + " member");
                        }
                        
                        if(args[2].equalsIgnoreCase("Executive")) {
                            Executives.remove(playerName);
                            plugin.getConfig().set("Executives",Executives);
                            plugin.saveConfig();
                            Bukkit.broadcastMessage(ChatColor.GREEN + playerName + " has been removed from being an Executive!");
                            Bukkit.dispatchCommand(sender, "manuadd " + playerName + " member");
                        }
                     }
                    
                    
                    
                }
            }
        
        if(cmd.getName().equalsIgnoreCase("mong")) {
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
        }
        
        if(cmd.getName().equalsIgnoreCase("donator")) {
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
        }
        return false;
  }
      
   public Player getPlayer(String name) {
        for(Player player : plugin.getServer().getOnlinePlayers()) {
            if(name.equalsIgnoreCase(player.getName())) {
                return player;
            }
        }
       return null;
   }
   
   public void init(){
       Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "mv unload Spawn_nether");
       Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "mv unload Spawn_the_end");
       logger.log(Level.INFO, "[{0}] Hopefully the Nether and End have unloaded!", pluginName);
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