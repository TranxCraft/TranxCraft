package com.wickedgaminguk.TranxCraft;

import com.wickedgaminguk.mcstats.Metrics;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import net.minecraft.server.v1_6_R2.BanEntry;
import net.minecraft.server.v1_6_R2.BanList;
import net.minecraft.server.v1_6_R2.MinecraftServer;
import net.minecraft.server.v1_6_R2.PlayerList;
import net.minecraft.server.v1_6_R2.PropertyManager;

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
        
        //Register Command Executors.
        plugin.getCommand("mong").setExecutor(new Command_mong(plugin));
        plugin.getCommand("tranxcraft").setExecutor(new Command_tranxcraft(plugin));
        plugin.getCommand("donator").setExecutor(new Command_donator(plugin));
        plugin.getCommand("admininfo").setExecutor(new Command_admininfo(plugin));
        plugin.getCommand("c").setExecutor(new Command_c(plugin));
        plugin.getCommand("a").setExecutor(new Command_a(plugin));
        plugin.getCommand("s").setExecutor(new Command_s(plugin));
        plugin.getCommand("gtfo").setExecutor(new Command_gtfo(plugin));
        plugin.getCommand("fuckoff").setExecutor(new Command_fuckoff(plugin));  
        
        init();
  }
  
  @Override
  public void onDisable() {
    logger.log(Level.INFO, "{0} version {1} configuration file saved.", new Object[]{pluginName, pluginVersion});
    logger.log(Level.INFO, "{0} version {1} by {2} is disabled", new Object[]{pluginName, pluginVersion, pluginAuthor});
  }
  
  public void onReload() {
    plugin.saveConfig();
  }
      
   public Player getPlayer(String name) {
        for(Player player : plugin.getServer().getOnlinePlayers()) {
            if(name.equalsIgnoreCase(player.getName())) {
                return player;
            }
        }
       return null;
   }
   
   public void init() {
       Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "mv unload Spawn_nether");
       Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "mv unload Spawn_the_end");
       logger.log(Level.INFO, "[{0}] Hopefully the Nether and End have unloaded!", pluginName);
   }
   
   //Credits to Steven Lawson/Madgeek & Jerom Van Der Sar/DarthSalamon for the banUsername and banIP methods.
    public static void banUsername(String name, String reason, String source) {
        name = name.toLowerCase().trim();

        BanEntry entry = new BanEntry(name);
        
        if (reason != null) {
            entry.setReason(reason);
        }
        
        if (source != null) {
            entry.setSource(source);
        }
        
        BanList nameBans = MinecraftServer.getServer().getPlayerList().getNameBans();
        nameBans.add(entry);
    }
    
    public static void banIP(String ip, String reason, String source) {
        ip = ip.toLowerCase().trim();
        BanEntry entry = new BanEntry(ip);
        
        if (reason != null) {
            entry.setReason(reason);
        }
        
        if (source != null) {
            entry.setSource(source);
        }
        
        BanList ipBans = MinecraftServer.getServer().getPlayerList().getIPBans();
        ipBans.add(entry);
    }

}