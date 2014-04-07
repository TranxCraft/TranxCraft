package com.wickedgaminguk.TranxCraft;

import com.wickedgaminguk.MySQL.MySQL;
import com.wickedgaminguk.TranxCraft.Commands.Command_tranxcraft;
import com.wickedgaminguk.TranxCraft.UCP.TCP_UCP;
import java.io.*;
import java.sql.*;
import java.util.HashMap;
import java.util.Map;
import net.milkbowl.vault.permission.Permission;
import net.pravian.bukkitlib.BukkitLib;
import net.pravian.bukkitlib.command.BukkitCommandHandler;
import net.pravian.bukkitlib.config.YamlConfig;
import net.pravian.bukkitlib.implementation.BukkitPlugin;
import net.pravian.bukkitlib.util.LoggerUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.*;
import org.bukkit.entity.Player;
import org.bukkit.plugin.*;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.Scoreboard;
import org.mcstats.Metrics;

public class TranxCraft extends BukkitPlugin {

    public Map<String, Long> playerLogins;
    public TranxCraft plugin;
    public YamlConfig config;
    public YamlConfig playerConfig;
    public YamlConfig adminConfig;
    public BukkitCommandHandler handler;
    public TranxListener listener;
    public Permission permission;
    public PluginManager pm;
    public MySQL mySQL;
    public TCP_Mail mail;
    public TCP_Twitter twitter;
    public Scoreboard board;
    public Objective o;
    public HashMap<String, Score> kills = new HashMap<>();
    public HashMap<String, Score> deaths = new HashMap<>();
    public HashMap<String, Score> kd = new HashMap<>();

    @Override
    public void onLoad() {
        plugin = this;
        config = new YamlConfig(plugin, "config.yml");
        playerConfig = new YamlConfig(plugin, "players.yml");
        adminConfig = new YamlConfig(plugin, "admins.yml");
        playerLogins = new HashMap<>();
        handler = new BukkitCommandHandler(plugin);
        mail = new TCP_Mail(plugin);
        twitter = new TCP_Twitter(plugin);
    }

    @Override
    public void onEnable() {
        BukkitLib.init(plugin);

        this.pm = getServer().getPluginManager();

        config.load();
        playerConfig.load();
        adminConfig.load();

        mySQL = new MySQL(plugin, config.getString("HOSTNAME"), config.getString("PORT"), config.getString("DATABASE"), config.getString("USER"), config.getString("PASSWORD"));

        twitter.init();

        LoggerUtils.info(plugin.getName() + " v." + plugin.getVersion() + " by " + plugin.getAuthor() + " is enabled");

        new TCP_Scheduler(plugin).runTaskTimer(plugin, config.getInt("interval") * 20L, config.getInt("interval") * 20L);
        new TCP_UCP(plugin).runTaskTimer(plugin, 6000L, 6000L);

        listener = new TranxListener(plugin);
        register(listener);

        //mail.send(RecipientType.SYS, "TranxCraft Reports - Server Started", "Hey there, TranxCraft has been successfully started on " + TCP_Time.getDate());
        /*try {
         TCP_Twitter.tweet("TranxCraft has been successfully started on " + TCP_Time.getDate());
         }
         catch (TwitterException | IOException ex) {
         LoggerUtils.warning("[TranxCraft] Twitter functionality is broken!\n" + ex);
         }*/
        setupPermissions();

        handler.setCommandLocation(Command_tranxcraft.class.getPackage());
        handler.setPermissionMessage(ChatColor.RED + "You don't have permission for this command.");

        board = Bukkit.getServer().getScoreboardManager().getNewScoreboard();

        o = board.registerNewObjective("test", "dummy");
        o.setDisplayName("Stats");
        o.setDisplaySlot(DisplaySlot.SIDEBAR);

        try {
            Metrics metrics = new Metrics(this);
            metrics.start();
            LoggerUtils.info(plugin, "Metrics have started.");
        }
        catch (IOException ex) {
            LoggerUtils.severe(plugin, "Plugin Metrics failed to submit to McStats.\n " + ex);
        }
    }

    @Override
    public void onDisable() {
        LoggerUtils.info(plugin.getName() + " v." + plugin.getVersion() + " configuration file saved.");
        LoggerUtils.info(plugin.getName() + " v" + plugin.getVersion() + " by" + plugin.getAuthor() + " is disabled.");
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String commandLabel, String[] args) {
        return handler.handleCommand(sender, command, commandLabel, args);
    }

    public void updateDatabase(String SQLquery) throws SQLException {
        Connection c = mySQL.openConnection();
        Statement statement = c.createStatement();
        statement.executeUpdate(SQLquery);
    }

    public ResultSet getValueFromDB(String SQLquery) throws SQLException {
        Connection c = mySQL.openConnection();
        Statement statement = c.createStatement();
        ResultSet res = statement.executeQuery(SQLquery);
        res.next();
        return res;
    }

    private boolean setupPermissions() {
        RegisteredServiceProvider<Permission> permissionProvider = Bukkit.getServer().getServicesManager().getRegistration(Permission.class);
        if (permissionProvider != null) {
            permission = permissionProvider.getProvider();
        }
        return (permission != null);
    }

    public String getPlayerGroup(Player player) {
        String perm = permission.getPrimaryGroup(player);
        return perm;
    }
}
