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
        
        //Register Command Executors.
        plugin.getCommand("mong").setExecutor(new Command_mong(plugin));
        plugin.getCommand("tranxcraft").setExecutor(new Command_tranxcraft(plugin));
        plugin.getCommand("donator").setExecutor(new Command_donator(plugin));
        
        init();
  }
  
  @Override
  public void onDisable() {
        logger.log(Level.INFO, "{0} version {1} configuration file saved.", new Object[]{pluginName, pluginVersion});
        logger.log(Level.INFO, "{0} version {1} by {2} is disabled", new Object[]{pluginName, pluginVersion, pluginAuthor});
  }
  
  public void onReload() {
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