package com.wickedgaminguk.TranxCraft.UCP;

import com.wickedgaminguk.TranxCraft.*;
import java.sql.SQLException;
import net.pravian.bukkitlib.util.LoggerUtils;
import net.pravian.bukkitlib.util.TimeUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class TCP_UCP extends BukkitRunnable {

    private final TranxCraft plugin;

    public TCP_UCP(TranxCraft instance) {
        this.plugin = instance;
    }

    @Override
    public void run() {
        LoggerUtils.info(plugin, "Starting UCP Sync Now.");

        if (Bukkit.getOnlinePlayers().length == 0) {
            LoggerUtils.info(plugin, "No players online, recreating table...");

            try {
                plugin.updateDatabase("DROP TABLE players");
                plugin.updateDatabase("CREATE TABLE players ( ID INT NOT NULL AUTO_INCREMENT, InGameName CHAR(30), OnlineTime CHAR(30), Rank CHAR(30), LastUpdated CHAR(30), PRIMARY KEY(ID) )");
                plugin.updateDatabase("INSERT INTO players (LastUpdated) VALUES ('" + TCP_Time.getUnixTimestamp() + "');");
                LoggerUtils.info(plugin, "Table Recreated");
            }
            catch (SQLException ex) {
                LoggerUtils.severe(plugin, "SQL Connection Failed.");
                LoggerUtils.severe(ex);
            }

            LoggerUtils.info(plugin, "UCP Sync Finished.");
        }

        if (Bukkit.getOnlinePlayers().length != 0) {
            try {
                plugin.updateDatabase("DROP TABLE players");
                plugin.updateDatabase("CREATE TABLE players ( ID INT NOT NULL AUTO_INCREMENT, InGameName CHAR(30), OnlineTime CHAR(30), Rank CHAR(30), LastUpdated CHAR(30), PRIMARY KEY(ID) )");
            }
            catch (SQLException ex) {
                LoggerUtils.severe(plugin, "SQL Connection Failed.");
                LoggerUtils.severe(ex);
            }

            for (Player player : plugin.getServer().getOnlinePlayers()) {
                String playerPermission = null;

                try {
                    playerPermission = plugin.getPlayerGroup(player);
                }
                catch (Exception ex) {

                }

                if (playerPermission == null) {
                    playerPermission = "Argh - it didn't work. Contact a developer.";
                }

                String playerName = player.getName();

                long Unix = TimeUtils.getUnix();
                long PlayerTime = plugin.playerLogins.get(playerName);
                final long currentOnlineTime = (Unix - PlayerTime);

                try {
                    plugin.updateDatabase("INSERT INTO players (InGameName, OnlineTime, Rank, LastUpdated) VALUES ('" + playerName + "', " + currentOnlineTime + ", '" + playerPermission + "', '" + TCP_Time.getUnixTimestamp() + "');");
                }
                catch (SQLException ex) {
                    LoggerUtils.severe(plugin, "SQL Connection Failed.");
                    LoggerUtils.severe(ex);
                }

            }

            LoggerUtils.info(plugin, "UCP Sync Finished.");
        }
    }
}
