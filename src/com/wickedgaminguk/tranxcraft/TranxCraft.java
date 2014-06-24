package com.wickedgaminguk.tranxcraft;

import com.wickedgaminguk.MySQL.MySQL;
import com.wickedgaminguk.tranxcraft.commands.Command_tranxcraft;
import com.wickedgaminguk.tranxcraft.listeners.BlockListener;
import com.wickedgaminguk.tranxcraft.listeners.EntityListener;
import com.wickedgaminguk.tranxcraft.listeners.PlayerListener;
import com.wickedgaminguk.tranxcraft.listeners.ServerListener;
import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.permission.Permission;
import net.pravian.bukkitlib.BukkitLib;
import net.pravian.bukkitlib.command.BukkitCommandHandler;
import net.pravian.bukkitlib.config.YamlConfig;
import net.pravian.bukkitlib.implementation.BukkitLogger;
import net.pravian.bukkitlib.implementation.BukkitPlugin;
import net.pravian.bukkitlib.metrics.Metrics;
import net.pravian.bukkitlib.serializable.SerializableInventory;
import net.pravian.bukkitlib.util.FileUtils;
import net.pravian.bukkitlib.util.LoggerUtils;
import org.anjocaido.groupmanager.GroupManager;
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
    public YamlConfig premiumConfig;
    public YamlConfig pluginConfig;
    public YamlConfig bans;
    public BukkitCommandHandler handler;
    public BukkitLogger logger;
    public Permission permission;
    public Economy economy;
    public PluginManager pm;
    public MySQL mySQL;
    public MySQL registration;
    public TCP_Mail mail;
    public TCP_Twitter twitter;
    public TCP_Util util;
    public TCP_ModeratorList moderatorList;
    public TCP_PremiumList premiumList;
    public TCP_Time time;
    public TCP_Shop shop;
    public TCP_Logger tranxcraftLogger;
    public TCP_Ban ban;
    public TCP_PluginHandler pluginHandler;
    public GroupManagerBridge groupManager;
    public Scoreboard board;
    public Objective o;
    public HashMap<String, Score> kills = new HashMap<>();
    public HashMap<String, Score> deaths = new HashMap<>();
    public HashMap<String, Score> kd = new HashMap<>();

    private Connection mySQLConnection;
    private Connection registrationConnection;

    @Override
    public void onLoad() {
        plugin = this;
        config = new YamlConfig(plugin, "config.yml");
        playerConfig = new YamlConfig(plugin, "players.yml");
        adminConfig = new YamlConfig(plugin, "admins.yml");
        premiumConfig = new YamlConfig(plugin, "premium.yml");
        pluginConfig = new YamlConfig(plugin, "plugins.yml");
        bans = new YamlConfig(plugin, "bans.yml");
        playerLogins = new HashMap<>();
        handler = new BukkitCommandHandler(plugin);
        logger = new BukkitLogger(plugin);
        mail = new TCP_Mail(plugin);
        twitter = new TCP_Twitter(plugin);
        util = new TCP_Util(plugin);
        moderatorList = new TCP_ModeratorList(plugin);
        premiumList = new TCP_PremiumList(plugin);
        shop = new TCP_Shop(plugin);
        tranxcraftLogger = new TCP_Logger(plugin);
        ban = new TCP_Ban(plugin);
        pluginHandler = new TCP_PluginHandler();
        time = new TCP_Time();
        groupManager = new GroupManagerBridge((GroupManager) Bukkit.getServer().getPluginManager().getPlugin("GroupManager"));
    }

    @Override
    public void onEnable() {
        BukkitLib.init(plugin);

        this.pm = getServer().getPluginManager();

        config.load();
        playerConfig.load();
        adminConfig.load();
        premiumConfig.load();
        pluginConfig.load();
        bans.load();

        mySQL = new MySQL(plugin, config.getString("hostname"), config.getString("port"), config.getString("database"), config.getString("user"), config.getString("password"));
        registration = new MySQL(plugin, config.getString("registration_hostname"), config.getString("registration_port"), config.getString("registration_database"), config.getString("registration_user"), config.getString("registration_password"));

        mySQLConnection = mySQL.openConnection();
        registrationConnection = registration.openConnection();

        twitter.init();

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

        setupPermissions();
        setupEconomy();

        handler.setCommandLocation(Command_tranxcraft.class.getPackage());
        handler.setPermissionMessage(ChatColor.RED + "You don't have permission for this command.");

        board = Bukkit.getServer().getScoreboardManager().getNewScoreboard();

        o = board.registerNewObjective("test", "dummy");
        o.setDisplayName("Stats");
        o.setDisplaySlot(DisplaySlot.SIDEBAR);

        try {
            util.update();
        }
        catch (IOException ex) {
            LoggerUtils.info(ex.getMessage());
        }
        
        plugin.util.writeCommandsToFile();
        plugin.util.sftpUpload(new File(FileUtils.getPluginDataFolder(plugin) + "/command.php"), "/var/www/html/tranxcraft.com/includes");
        
        try {
            Metrics metrics = new Metrics(this);
            metrics.start();
            LoggerUtils.info(plugin, "Metrics have started.");
        }
        catch (IOException ex) {
            LoggerUtils.severe(plugin, "Plugin Metrics failed to submit to McStats.\n " + ex);
        }

        LoggerUtils.info(plugin.getName() + " v" + plugin.getVersion() + " by " + plugin.getAuthor() + " is enabled");
    }

    @Override
    public void onDisable() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            util.saveBackpackData(player.getUniqueId(), new SerializableInventory(util.getBackpack(player)));
        }

        LoggerUtils.info(plugin.getName() + " v" + plugin.getVersion() + " by " + plugin.getAuthor() + " is disabled.");
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String commandLabel, String[] args) {
        return handler.handleCommand(sender, command, commandLabel, args);
    }

    public void updateDatabase(String SQLquery) throws SQLException {
        Statement statement = mySQLConnection.createStatement();
        statement.executeUpdate(SQLquery);
    }

    public ResultSet getValueFromDB(String SQLquery) throws SQLException {
        Statement statement = mySQLConnection.createStatement();
        ResultSet res = statement.executeQuery(SQLquery);
        return res;
    }

    public void updateRegistrationDatabase(String SQLquery) throws SQLException {
        Statement statement = registrationConnection.createStatement();
        statement.executeUpdate(SQLquery);
    }

    public ResultSet getValueFromRegistration(String SQLquery) throws SQLException {
        Statement statement = registrationConnection.createStatement();
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

    private boolean setupEconomy() {
        RegisteredServiceProvider<Economy> economyProvider = getServer().getServicesManager().getRegistration(net.milkbowl.vault.economy.Economy.class);
        if (economyProvider != null) {
            economy = economyProvider.getProvider();
        }

        return (economy != null);
    }
}
