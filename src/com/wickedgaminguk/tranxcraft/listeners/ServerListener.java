package com.wickedgaminguk.tranxcraft.listeners;

import com.vexsoftware.votifier.model.Vote;
import com.vexsoftware.votifier.model.VotifierEvent;
import com.wickedgaminguk.tranxcraft.TranxCraft;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.UUID;
import net.minecraft.server.v1_7_R3.MinecraftServer;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.server.ServerListPingEvent;

public class ServerListener implements Listener {

    private final TranxCraft plugin;

    public ServerListener(TranxCraft plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onServerPing(ServerListPingEvent event) {
        String playerIP = event.getAddress().toString();
        playerIP = playerIP.replaceAll("/", "");
        playerIP = playerIP.replaceAll("\\.", "-");

        try {
            if (plugin.util.getPlayerData().containsKey(playerIP)) {
                final String player = plugin.util.getPlayerData().get(playerIP);
                final String ip = event.getAddress().getHostAddress();

                if (plugin.ban.isUUIDBanned(player) || plugin.ban.isIPBanned(ip)) {
                    event.setMotd(ChatColor.RED + "Hey " + player + ", you are" + ChatColor.BOLD + " banned.");
                }
                else if (plugin.util.isAdminMode() == true && !(plugin.adminConfig.getStringList("Admin_IPs").contains(ip))) {
                    event.setMotd(ChatColor.RED + "Hey " + player + ", sadly, adminmode is on - come back soon!" + ChatColor.LIGHT_PURPLE + " <3");
                }
                else if (Bukkit.hasWhitelist() && Bukkit.getWhitelistedPlayers().contains(Bukkit.getOfflinePlayer(player)) == false) {
                    event.setMotd(ChatColor.RED + "Hey " + player + ", sadly, the whitelist is on - come back soon!" + ChatColor.LIGHT_PURPLE + " <3");
                }
                else if (Bukkit.getOnlinePlayers().length >= Bukkit.getMaxPlayers() && !(plugin.moderatorList.isPlayerMod(player) || plugin.premiumList.isPlayerPremium(player))) {
                    event.setMotd(ChatColor.RED + "Hey " + player + ", sadly, the server is full - come back soon!" + ChatColor.LIGHT_PURPLE + " <3");
                }
                else {
                    event.setMotd(ChatColor.GREEN + "Welcome " + ChatColor.GOLD + (String) player + ChatColor.WHITE + " to " + ChatColor.GREEN + "TranxCraft " + ChatColor.WHITE + "- " + ChatColor.DARK_PURPLE + "Craftbukkit " + MinecraftServer.getServer().getVersion());
                }
            }
            else if (plugin.ban.isIPBanned(event.getAddress().getHostAddress())) {
                event.setMotd(ChatColor.RED + "You are banned.");
            }
            else if (plugin.util.isAdminMode() == true) {
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
    
    @EventHandler(priority=EventPriority.NORMAL)
    public void onVotifierEvent(VotifierEvent event) {
        Vote vote = event.getVote();
        
        String player = vote.getUsername();
        UUID playerId = plugin.util.playerToUUID(player);
        
        plugin.util.setVotes(playerId);
        
        Bukkit.broadcastMessage(ChatColor.GOLD + player + ChatColor.GREEN + " has voted for TranxCraft on " + vote.getServiceName() + "! They have been rewarded with $500!");
        
        plugin.util.depositPlayer(player, 500.0);
    }
}
