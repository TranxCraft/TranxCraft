
package com.wickedgaminguk.TranxCraft;

import com.wickedgaminguk.MySQL.*;
import com.wickedgaminguk.TranxCraft.Commands.Command_tranxcraft;
import com.wickedgaminguk.TranxCraft.TCP_Mail.RecipientType;
import com.wickedgaminguk.TranxCraft.UCP.TCP_UCP;
import org.mcstats.Metrics;
import java.io.IOException;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import net.milkbowl.vault.permission.Permission;
import net.pravian.bukkitlib.BukkitLib;
import net.pravian.bukkitlib.command.BukkitCommandHandler;
import net.pravian.bukkitlib.config.YamlConfig;
import net.pravian.bukkitlib.implementation.BukkitPlugin;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.RegisteredServiceProvider;
import twitter4j.TwitterException;

public class TranxCraft extends BukkitPlugin {
    public static TranxCraft plugin = null;
    public YamlConfig config;
    public BukkitCommandHandler handler;
    public TranxListener listener;
    public static String pluginName;
    public String pluginVersion;
    public String pluginAuthor;
    PluginManager pm;
    MySQL mySQL;
    TCP_Mail TCP_mail;
    public Permission permission;
    
    @Override
    public void onLoad() {
        plugin = this;
        config = new YamlConfig(plugin, "config.yml", true);
        handler = new BukkitCommandHandler(plugin);
    }

    @Override
    public void onEnable() {
        
        BukkitLib.init(plugin);
        
        SimpleDateFormat sdf = new SimpleDateFormat("dd-M-yyyy hh:mm:ss");
	String date = sdf.format(new Date());
        
        this.pm = getServer().getPluginManager();
        
        pluginName = plugin.getName();
        pluginVersion = plugin.getVersion();
        pluginAuthor = plugin.getAuthor();
        config.load();   
        mySQL = new MySQL(plugin, config.getString("HOSTNAME"), config.getString("PORT"), config.getString("DATABASE"), config.getString("USER"), config.getString("PASSWORD"));
        TCP_mail = new TCP_Mail();
        handler.setCommandLocation(Command_tranxcraft.class.getPackage());
        
        TCP_Log.info(pluginName + " version " + pluginVersion + " by " + pluginAuthor + " is enabled");
        
        new TCP_Scheduler(plugin).runTaskTimer(plugin, config.getInt("interval") * 20L, config.getInt("interval") * 20L);
        new TCP_UCP(plugin).runTaskTimerAsynchronously(plugin, 6000L, 6000L);
        
        listener = new TranxListener(plugin);
        register(listener);       
        
        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "mv unload Spawn_nether");
        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "mv unload Spawn_the_end");
        TCP_Log.info("[TranxCraft] Hopefully the Nether and End have unloaded!");
        TCP_mail.send(RecipientType.SYS, "TranxCraft Reports - Server Started", "Hey there, TranxCraft has been successfully started on " + date);
        
        try {
            TCP_Twitter.tweet("TranxCraft has been successfully started on " + date);
        }
        catch (TwitterException | IOException ex) {
        
        }
        
        setupPermissions();
        
        try {
            Metrics metrics = new Metrics(this);
            metrics.start();
        }
        catch (IOException e) {
            TCP_Log.severe("[" + pluginName + "] Plugin Metrics failed to submit to McStats.");
        }    
    }
  
    @Override
    public void onDisable() {
        TCP_Log.info(pluginName + " v" + pluginVersion + " configuration file saved.");
        TCP_Log.info(pluginName + " v" + pluginVersion + " by" + pluginAuthor + " is disabled.");
    }
  
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        return handler.handleCommand(sender, command, label, args);
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
    
    private boolean setupPermissions() {
        RegisteredServiceProvider<Permission> permissionProvider = Bukkit.getServer().getServicesManager().getRegistration(Permission.class);
        if (permissionProvider != null) {
            permission = permissionProvider.getProvider();
        }
        return (permission != null);
    }
}