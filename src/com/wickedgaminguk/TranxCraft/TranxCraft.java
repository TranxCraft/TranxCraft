
package com.wickedgaminguk.TranxCraft;

import husky.mysql.MySQL;
import com.wickedgaminguk.TranxCraft.Commands.Command_tranxcraft;
import com.wickedgaminguk.mcstats.Metrics;
import java.io.IOException;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.pravian.bukkitlib.command.BukkitCommandHandler;
import net.pravian.bukkitlib.config.YamlConfig;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import twitter4j.TwitterException;

public class TranxCraft extends JavaPlugin {
    
    public static TranxCraft plugin = null;
    public YamlConfig config;
    public BukkitCommandHandler handler;
    public TranxListener listener;
    public static String pluginName;
    public String pluginVersion;
    public String pluginAuthor;
    PluginManager pm;
    MySQL mySQL;
    
  @Override
  public void onLoad() {
      plugin = this;
      config = new YamlConfig(plugin, "config.yml", true);
      handler = new BukkitCommandHandler(plugin);
  }

  @Override
  public void onEnable() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd-M-yyyy hh:mm:ss");
	String date = sdf.format(new Date());
        
        this.pm = getServer().getPluginManager();
         
        PluginDescriptionFile pdf = plugin.getDescription();
        pluginName = pdf.getName();
        pluginVersion = pdf.getVersion();
        pluginAuthor = pdf.getAuthors().get(0);
        config.load();    
        mySQL = new MySQL(plugin, config.getString("HOSTNAME"), config.getString("PORT"), config.getString("DATABASE"), config.getString("USER"), config.getString("PASSWORD"));    
        handler.setCommandLocation(Command_tranxcraft.class.getPackage());
        
        TCP_Log.info(pluginName + " version " + pluginVersion + " by " + pluginAuthor + " is enabled");
        
        new TCP_Scheduler(plugin).runTaskTimer(plugin, config.getInt("interval") * 20L, config.getInt("interval") * 20L);
        
        listener = new TranxListener(plugin);
        pm.registerEvents(listener, plugin);        
        
        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "mv unload Spawn_nether");
        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "mv unload Spawn_the_end");
        TCP_Log.info("[TranxCraft] Hopefully the Nether and End have unloaded!");
        
        TCP_Mail.send("TranxCraft Reports - Server Started", "Hey there, TranxCraft has been successfully started on " + date);
        try {
            TCP_Twitter.tweet("TranxCraft has been successfully started on " + date);
        }
        catch (TwitterException | IOException ex) {
            
        }
        
        try {
            Metrics metrics = new Metrics(this);
            metrics.start();
        }
        catch (IOException e) {
            TCP_Util.logger.log(Level.SEVERE, "{0} Plugin Metrics have failed to submit the statistics to McStats! >:(", pluginName);
            
        }    
  }
  
  @Override
  public void onDisable() {
      TCP_Util.logger.log(Level.INFO, "{0} version {1} configuration file saved.", new Object[]{pluginName, pluginVersion});
      TCP_Util.logger.log(Level.INFO, "{0} version {1} by {2} is disabled", new Object[]{pluginName, pluginVersion, pluginAuthor});
  }
  
  @Override
   public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
       return handler.handleCommand(sender, command, label, args);
   }
  
  public static String getPluginName() {
     return pluginName; 
  }
  
  public void updateDatabase(String SQLquery) throws SQLException {
      Connection c = mySQL.openConnection();
      Statement statement = c.createStatement();      
      statement.executeUpdate(SQLquery);
  }
  
  public void getValueFromDB(String SQLquery) throws SQLException {      
      Connection c = mySQL.openConnection();
      Statement statement = c.createStatement();
      ResultSet res = statement.executeQuery(SQLquery);
      res.next();
  }
  
}