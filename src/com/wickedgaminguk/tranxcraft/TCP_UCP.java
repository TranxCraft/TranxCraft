package com.wickedgaminguk.tranxcraft;

import java.sql.SQLException;
import net.pravian.bukkitlib.util.LoggerUtils;
import net.pravian.bukkitlib.util.TimeUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class TCP_UCP extends BukkitRunnable {

    private final TranxCraft plugin;
    private final TCP_Util TCP_Util;
    private final TCP_Time TCP_Time;
    private final TCP_Logger logger;

    public TCP_UCP(TranxCraft instance) {
        this.plugin = instance;
        this.TCP_Util = new TCP_Util(plugin);
        this.TCP_Time = new TCP_Time();
        this.logger = plugin.tranxcraftLogger;
    }

    @Override
    public void run() {
        logger.info(plugin, "Starting UCP Sync Now.");

        if (Bukkit.getOnlinePlayers().length == 0) {
            logger.info(plugin, "No players online, recreating table...");

            try {
                plugin.updateDatabase("DROP TABLE players");
                plugin.updateDatabase("CREATE TABLE players ( ID INT NOT NULL AUTO_INCREMENT, InGameName CHAR(30), OnlineTime CHAR(30), Rank CHAR(30), LastUpdated CHAR(30), PRIMARY KEY(ID) )");
                plugin.updateDatabase("INSERT INTO players (LastUpdated) VALUES ('" + TCP_Time.getUnixTimestamp() + "');");
                logger.info(plugin, "Table Recreated");
            }
            catch (SQLException ex) {
                LoggerUtils.severe(plugin, "SQL Connection Failed.");
                plugin.util.debug(ex);
            }

            logger.info(plugin, "UCP Sync Finished.");
        }

        if (Bukkit.getOnlinePlayers().length != 0) {
            try {
                plugin.updateDatabase("DROP TABLE players");
                plugin.updateDatabase("CREATE TABLE players ( ID INT NOT NULL AUTO_INCREMENT, InGameName CHAR(30), OnlineTime CHAR(30), Rank CHAR(30), LastUpdated CHAR(30), PRIMARY KEY(ID) )");
            }
            catch (SQLException ex) {
                logger.severe(plugin, "SQL Connection Failed.");
                plugin.util.debug(ex);
            }

            for (Player player : plugin.getServer().getOnlinePlayers()) {
                String playerPermission = null;

                try {
                    playerPermission = TCP_Util.getPlayerGroup(player);
                }
                catch (Exception ex) {

                }

                if (playerPermission == null) {
                    playerPermission = "Argh - it didn't work. Contact a developer.";
                }

                String playerName = player.getName();

                long Unix = TimeUtils.getUnix();
                long playerTime = plugin.playerLogins.get(player.getUniqueId().toString());
                final long currentOnlineTime = (Unix - playerTime);

                try {
                    plugin.updateDatabase("INSERT INTO players (InGameName, OnlineTime, Rank, LastUpdated) VALUES ('" + playerName + "', " + currentOnlineTime + ", '" + playerPermission + "', '" + TCP_Time.getUnixTimestamp() + "');");
                }
                catch (SQLException ex) {
                    logger.severe(plugin, "SQL Connection Failed.");
                    plugin.util.debug(ex);
                }

            }

            logger.info(plugin, "UCP Sync Finished.");
        }
    }
}
