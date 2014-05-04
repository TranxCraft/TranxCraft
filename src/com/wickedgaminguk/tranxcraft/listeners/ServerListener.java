package com.wickedgaminguk.tranxcraft.listeners;

import com.wickedgaminguk.tranxcraft.TCP_Ban;
import com.wickedgaminguk.tranxcraft.TCP_DonatorList;
import com.wickedgaminguk.tranxcraft.TCP_ModeratorList;
import com.wickedgaminguk.tranxcraft.TCP_Util;
import com.wickedgaminguk.tranxcraft.TranxCraft;
import java.io.FileNotFoundException;
import java.io.IOException;
import net.minecraft.server.v1_7_R3.MinecraftServer;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.server.ServerListPingEvent;

public class ServerListener implements Listener {

    private final TranxCraft plugin;
    private final TCP_ModeratorList TCP_ModeratorList;
    private final TCP_DonatorList TCP_DonatorList;
    private final TCP_Util TCP_Util;
    private final TCP_Ban TCP_Ban;

    public ServerListener(TranxCraft plugin) {
        this.plugin = plugin;
        this.TCP_ModeratorList = new TCP_ModeratorList(plugin);
        this.TCP_DonatorList = new TCP_DonatorList(plugin);
        this.TCP_Util = new TCP_Util(plugin);
        this.TCP_Ban = new TCP_Ban(plugin);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onServerPing(ServerListPingEvent event) {
        String playerIP = event.getAddress().toString();
        playerIP = playerIP.replaceAll("/", "");
        playerIP = playerIP.replaceAll("\\.", "-");

        try {
            if (TCP_Util.getPlayerData().containsKey(playerIP)) {
                final String player = TCP_Util.getPlayerData().get(playerIP);
                final String ip = event.getAddress().getHostAddress();

                if (TCP_Ban.isUUIDBanned(player) || TCP_Ban.isIPBanned(ip)) {
                    event.setMotd(ChatColor.RED + "Hey " + player + ", you are" + ChatColor.BOLD + " banned.");
                }
                else if (TCP_Util.isAdminMode() == true && !(plugin.adminConfig.getStringList("Admin_IPs").contains(ip))) {
                    event.setMotd(ChatColor.RED + "Hey " + player + ", sadly, adminmode is on - come back soon!" + ChatColor.LIGHT_PURPLE + " <3");
                }
                else if (Bukkit.hasWhitelist() && Bukkit.getWhitelistedPlayers().contains(Bukkit.getOfflinePlayer(player)) == false) {
                    event.setMotd(ChatColor.RED + "Hey " + player + ", sadly, the whitelist is on - come back soon!" + ChatColor.LIGHT_PURPLE + " <3");
                }
                else if (Bukkit.getOnlinePlayers().length >= Bukkit.getMaxPlayers() && !(TCP_ModeratorList.isPlayerMod(player) || TCP_DonatorList.isPlayerDonator(player))) {
                    event.setMotd(ChatColor.RED + "Hey " + player + ", sadly, the server is full - come back soon!" + ChatColor.LIGHT_PURPLE + " <3");
                }
                else {
                    event.setMotd(ChatColor.GREEN + "Welcome " + ChatColor.GOLD + (String) player + ChatColor.WHITE + " to " + ChatColor.GREEN + "TranxCraft " + ChatColor.WHITE + "- " + ChatColor.DARK_PURPLE + "Craftbukkit " + MinecraftServer.getServer().getVersion());
                }
            }
            else if (TCP_Ban.isIPBanned(event.getAddress().getHostAddress())) {
                event.setMotd(ChatColor.RED + "You are banned.");
            }
            else if (TCP_Util.isAdminMode() == true) {
                event.setMotd(ChatColor.RED + "Adminmode enabled.");
            }
            else if (Bukkit.hasWhitelist()) {
                event.setMotd(ChatColor.RED + "Whitelist enabled.");
            }
            else if (Bukkit.getOnlinePlayers().length >= Bukkit.getMaxPlayers()) {
                event.setMotd(ChatColor.RED + "Server is full.");
            }
            else {
                event.setMotd(ChatColor.GREEN + "TranxCraft" + ChatColor.WHITE + " - " + ChatColor.DARK_PURPLE + "Craftbukkit " + MinecraftServer.getServer().getVersion() + ChatColor.WHITE + " - " + ChatColor.RED + "Currently in Alpha!");
            }
        }
        catch (FileNotFoundException ex) {
            event.setMotd(ChatColor.GREEN + "TranxCraft" + ChatColor.WHITE + " - " + ChatColor.DARK_PURPLE + "Craftbukkit " + MinecraftServer.getServer().getVersion() + ChatColor.WHITE + " - " + ChatColor.RED + "Currently in Alpha!");
        }
        catch (IOException | ClassNotFoundException ex) {
            event.setMotd(ChatColor.GREEN + "TranxCraft" + ChatColor.WHITE + " - " + ChatColor.DARK_PURPLE + "Craftbukkit " + MinecraftServer.getServer().getVersion() + ChatColor.WHITE + " - " + ChatColor.RED + "Currently in Alpha!");
        }
    }
}
