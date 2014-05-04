package com.wickedgaminguk.tranxcraft;

import com.wickedgaminguk.MySQL.MySQL;
import com.wickedgaminguk.tranxcraft.commands.Command_tranxcraft;
import com.wickedgaminguk.tranxcraft.listeners.BlockListener;
import com.wickedgaminguk.tranxcraft.listeners.EntityListener;
import com.wickedgaminguk.tranxcraft.listeners.PlayerListener;
import com.wickedgaminguk.tranxcraft.listeners.ServerListener;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import net.milkbowl.vault.permission.Permission;
import net.pravian.bukkitlib.BukkitLib;
import net.pravian.bukkitlib.command.BukkitCommandHandler;
import net.pravian.bukkitlib.config.YamlConfig;
import net.pravian.bukkitlib.implementation.BukkitPlugin;
import net.pravian.bukkitlib.metrics.Metrics;
import net.pravian.bukkitlib.util.LoggerUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.Scoreboard;

public class TranxCraft extends BukkitPlugin {

    public TranxCraft plugin;
    public Map<String, Long> playerLogins;
    public ArrayList<Player> noFall = new ArrayList<>();
    public ArrayList<Player> cooldown = new ArrayList<>();
    public YamlConfig config;
    public YamlConfig playerConfig;
    public YamlConfig adminConfig;
    public YamlConfig donatorConfig;
    public YamlConfig bans;
    public BukkitCommandHandler handler;
    public Permission permission;
    public PluginManager pm;
    public MySQL mySQL;
    public TCP_Mail mail;
    public TCP_Twitter twitter;
    public TCP_Util util;
    public TCP_ModeratorList moderatorList;
    public TCP_DonatorList donatorList;
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
        donatorConfig = new YamlConfig(plugin, "donators.yml");
        bans = new YamlConfig(plugin, "bans.yml");
        playerLogins = new HashMap<>();
        handler = new BukkitCommandHandler(plugin);
        mail = new TCP_Mail(plugin);
        twitter = new TCP_Twitter(plugin);
        util = new TCP_Util(plugin);
        moderatorList = new TCP_ModeratorList(plugin);
        donatorList = new TCP_DonatorList(plugin);
    }

    @Override
    public void onEnable() {
        BukkitLib.init(plugin);

        this.pm = getServer().getPluginManager();

        config.load();
        playerConfig.load();
        adminConfig.load();
        donatorConfig.load();
        bans.load();

        mySQL = new MySQL(plugin, config.getString("hostname"), config.getString("port"), config.getString("database"), config.getString("user"), config.getString("password"));

        twitter.init();

        LoggerUtils.info(plugin.getName() + " v." + plugin.getVersion() + " by " + plugin.getAuthor() + " is enabled");

        new TCP_Scheduler(plugin).runTaskTimer(plugin, config.getInt("interval") * 20L, config.getInt("interval") * 20L);
        new TCP_UCP(plugin).runTaskTimer(plugin, 6000L, 6000L);

        BlockListener blockListener = new BlockListener(plugin);
        EntityListener entityListener = new EntityListener(plugin);
        PlayerListener playerListener = new PlayerListener(plugin);
        ServerListener serverListener = new ServerListener(plugin);

        register(blockListener);
        register(entityListener);
        register(playerListener);
        register(serverListener);

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
