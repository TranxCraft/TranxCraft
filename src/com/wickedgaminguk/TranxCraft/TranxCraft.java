package com.wickedgaminguk.TranxCraft;

import com.wickedgaminguk.mcstats.Metrics;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class TranxCraft extends JavaPlugin {
    
    public static TranxCraft plugin = null;
    public TranxListener listener;
    public String pluginName;
    public String pluginVersion;
    public String pluginAuthor;
    File configFile;
    FileConfiguration config;
    PluginManager pm;

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
            TCP_Util.logger.log(Level.INFO, "{0} version {1} configuration file saved.", new Object[]{pluginName, pluginVersion});
        }
        
        YamlConfiguration.loadConfiguration(configFile);
        
        TCP_Log.info(pluginName + " version " + pluginVersion + " by " + pluginAuthor + " is enabled");
        
        try {
            Metrics metrics = new Metrics(this);
            metrics.start();
        }
        catch (IOException e) {
            TCP_Util.logger.log(Level.SEVERE, "{0} Plugin Metrics have failed to submit the statistics to McStats! >:(", pluginName);
            
        }
        
        listener = new TranxListener(plugin);
        pm.registerEvents(listener, plugin);
        
        init();
  }
  
  @Override
  public void onDisable() {
      TCP_Util.logger.log(Level.INFO, "{0} version {1} configuration file saved.", new Object[]{pluginName, pluginVersion});
      TCP_Util.logger.log(Level.INFO, "{0} version {1} by {2} is disabled", new Object[]{pluginName, pluginVersion, pluginAuthor});
  }
  
  public void onReload() {
      plugin.saveConfig();
  }
   
   public void init() {
       Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "mv unload Spawn_nether");
       Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "mv unload Spawn_the_end");
       TCP_Log.info("[TranxCraft] Hopefully the Nether and End have unloaded!");       
       
       //Register Command Executors.
       try {
           plugin.getCommand("mong").setExecutor(new com.wickedgaminguk.TranxCraft.Commands.Command_mong(plugin));
           plugin.getCommand("tranxcraft").setExecutor(new com.wickedgaminguk.TranxCraft.Commands.Command_tranxcraft(plugin));
           plugin.getCommand("donator").setExecutor(new com.wickedgaminguk.TranxCraft.Commands.Command_donator(plugin));
           plugin.getCommand("admininfo").setExecutor(new com.wickedgaminguk.TranxCraft.Commands.Command_admininfo(plugin));
           plugin.getCommand("gtfo").setExecutor(new com.wickedgaminguk.TranxCraft.Commands.Command_gtfo(plugin));
           plugin.getCommand("fuckoff").setExecutor(new com.wickedgaminguk.TranxCraft.Commands.Command_fuckoff(plugin)); 
           plugin.getCommand("cake").setExecutor(new com.wickedgaminguk.TranxCraft.Commands.Command_cake(plugin));
           TCP_Log.info("[" + pluginName + "] Commands Loaded.");
       }
       catch(Exception ex) {
           TCP_Log.warning("[" + pluginName + "] Commands Failed To Load!");
           TCP_Log.info("Error: " + ex);
       }
   }
}