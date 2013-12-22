package com.wickedgaminguk.TranxCraft;

import java.util.logging.Level;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.server.ServerListPingEvent;
import org.bukkit.inventory.ItemStack;


public class TranxListener extends TranxCraft implements Listener {
    
    String kickMessage = "I'm sorry, but you've been kicked to make room for a reserved player, to stop this happening, buy a donator rank!";    
    
    TranxListener(TranxCraft plugin) {
        TranxCraft.plugin = plugin;
    }
     
    @EventHandler(priority=EventPriority.HIGH)
    public void onPlayerEvent(PlayerJoinEvent event) {
        int TotalPlayers = plugin.getConfig().getInt("TotalPlayers");
        
        TotalPlayers++;
        plugin.getConfig().set("TotalPlayers", Integer.valueOf(TotalPlayers));
        plugin.saveConfig();
        Bukkit.broadcastMessage(ChatColor.BLUE + "[Player Counter] " + TotalPlayers + " players have joined in total.");
        
        if(event.getPlayer().getName().equals("HeXeRei452")) {
            Bukkit.broadcastMessage(ChatColor.AQUA + "HeXeRei452 is the Owner of TranxCraft.");
        }
        /*
        if(event.getPlayer().getName().equals("kromeblade")) {
            Bukkit.broadcastMessage(ChatColor.AQUA + "Kromeblade is the lead builder of TranxCraft.");
        }
        */
        if(TCP_ModeratorList.getleadAdmins().contains(event.getPlayer().getName())) {
            Bukkit.broadcastMessage(ChatColor.AQUA + event.getPlayer().getName() + " is a lead Admin.");
        }
        
        if(TCP_ModeratorList.getExecutives().contains(event.getPlayer().getName())) {
            Bukkit.broadcastMessage(ChatColor.AQUA + event.getPlayer().getName() + " is an executive Admin.");
        }
        
        if(TCP_ModeratorList.getAdmins().contains(event.getPlayer().getName())) {
            Bukkit.broadcastMessage(ChatColor.AQUA + event.getPlayer().getName() + " is an " + ChatColor.GOLD + "Admin.");
        }
        
        if(TCP_ModeratorList.getModerators().contains(event.getPlayer().getName())) {
            Bukkit.broadcastMessage(ChatColor.AQUA + event.getPlayer().getName() + " is a " + ChatColor.DARK_PURPLE + "Moderator.");
        }
        
        if(TCP_ModeratorList.getDonators().contains(event.getPlayer().getName())) {
            Bukkit.broadcastMessage(ChatColor.AQUA + event.getPlayer().getName() + " is a " + ChatColor.LIGHT_PURPLE + "Donator! <3");
        }
        
    }
    
    @EventHandler(priority=EventPriority.HIGH)
    public void onPlayerLogin(PlayerLoginEvent event) {
        
        Player player = event.getPlayer();
        
            if (event.getResult() == PlayerLoginEvent.Result.KICK_FULL) {
                if(player.hasPermission("tranxcraft.reserved") || TCP_ModeratorList.getDonators().contains(event.getPlayer().getName()) || TCP_ModeratorList.getModerators().contains(event.getPlayer().getName()) || TCP_ModeratorList.getAdmins().contains(event.getPlayer().getName())|| TCP_ModeratorList.getExecutives().contains(event.getPlayer().getName())|| TCP_ModeratorList.getleadAdmins().contains(event.getPlayer().getName())) {
                    kickPlayer(player, event);
                    event.allow();
                    Bukkit.broadcastMessage(ChatColor.AQUA + player.getName() + ChatColor.GREEN + " is a reserved member!");
                }
            }
    }
     
    private Action RIGHT_CLICK_BLOCK = Action.RIGHT_CLICK_BLOCK;

    @EventHandler
    public void onRightClick(PlayerInteractEvent event) {
        Player player = event.getPlayer();
            if ((event.getAction().equals(this.RIGHT_CLICK_BLOCK)) && (player.getItemInHand().getType() == Material.FIREWORK)) {
                player.sendMessage(ChatColor.RED + "The Use of Fireworks is not permitted on TranxCraft.");
                player.getInventory().setItem(player.getInventory().getHeldItemSlot(), new ItemStack(Material.COOKIE, 1));
                event.setCancelled(true);
                }
            if ((event.getAction().equals(this.RIGHT_CLICK_BLOCK)) && (player.getItemInHand().getType() == Material.TNT)) {
                player.sendMessage(ChatColor.RED + "The Use of Fireworks is not permitted on TranxCraft.");
                player.getInventory().setItem(player.getInventory().getHeldItemSlot(), new ItemStack(Material.COOKIE, 1));
                event.setCancelled(true);
            }
            if ((event.getAction().equals(this.RIGHT_CLICK_BLOCK)) && (player.getItemInHand().getType() == Material.LAVA || player.getItemInHand().getType() == Material.STATIONARY_LAVA)) {
                player.sendMessage(ChatColor.RED + "The Use of Lava is not permitted on TranxCraft.");
                player.getInventory().setItem(player.getInventory().getHeldItemSlot(), new ItemStack(Material.COOKIE, 1));
                event.setCancelled(true);
            }
            if ((event.getAction().equals(this.RIGHT_CLICK_BLOCK)) && (player.getItemInHand().getType() == Material.WATER || player.getItemInHand().getType() == Material.STATIONARY_WATER)) {
                player.sendMessage(ChatColor.RED + "The Use of Water is not permitted on TranxCraft.");
                player.getInventory().setItem(player.getInventory().getHeldItemSlot(), new ItemStack(Material.COOKIE, 1));
                event.setCancelled(true);
            }
            if ((event.getAction().equals(this.RIGHT_CLICK_BLOCK)) && (player.getItemInHand().getType() == Material.FIRE)) {
                player.sendMessage(ChatColor.RED + "The Use of Fire is not permitted on TranxCraft.");
                player.getInventory().setItem(player.getInventory().getHeldItemSlot(), new ItemStack(Material.COOKIE, 1));
                event.setCancelled(true);
            }
    }
    //Credits to Madgeek1450 & DarthSalamon for this event handler
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onServerPing(ServerListPingEvent event)
    {
        if (TCP_Util.isIPBanned(event.getAddress().getHostAddress())) {
            event.setMotd(ChatColor.RED + "You are banned.");
        }
        else if (Bukkit.hasWhitelist()) {
            event.setMotd(ChatColor.RED + "Whitelist enabled.");
        }
        else if (Bukkit.getOnlinePlayers().length >= Bukkit.getMaxPlayers()) {
            event.setMotd(ChatColor.RED + "Server is full.");
        }
    }
    
    private void kickPlayer(Player player, PlayerLoginEvent event) {
    Player[] players = TranxCraft.plugin.getServer().getOnlinePlayers();
        for (Player p : players) {
            if (!p.hasPermission("tranxcraft.kickprevent")) {
                p.kickPlayer(this.kickMessage);
                event.allow();
                TCP_Util.logger.log(Level.INFO, "Allowed player {0} to join full server by kicking player {1}!", new Object[]{player.getName(), p.getName()});
            }
        }

        event.disallow(PlayerLoginEvent.Result.KICK_FULL, "Unable to find any kickable players to open slots!");
  }
}