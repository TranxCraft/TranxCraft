package com.wickedgaminguk.TranxCraft;

import java.util.List;
import java.util.logging.Level;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.inventory.ItemStack;


public class TranxListener extends TranxCraft implements Listener {
    
    String kickMessage = "I'm sorry, but you've been kicked to make room for a reserved player, to stop this happening, buy a donator rank!";
    
    
    TranxListener(TranxCraft plugin) {
        this.plugin = plugin;
    }
        
        
    @EventHandler(priority=EventPriority.HIGH)
    public void onPlayerEvent(PlayerJoinEvent event) {
        int TotalPlayers = plugin.getConfig().getInt("TotalPlayers");
        List<String> Executives = plugin.getConfig().getStringList("Executives");
        List<String> leadAdmins = plugin.getConfig().getStringList("Lead_Admins");
        List<String> Admins = plugin.getConfig().getStringList("Admins");
        List<String> Moderators = plugin.getConfig().getStringList("Moderators");
        List<String> Donators = plugin.getConfig().getStringList("Donators");
        
        TotalPlayers++;
        plugin.getConfig().set("TotalPlayers", Integer.valueOf(TotalPlayers));
        plugin.saveConfig();
        Bukkit.broadcastMessage(ChatColor.BLUE + "[Player Counter] " + TotalPlayers + " players have joined in total.");
        
        if(event.getPlayer().getName().equals("HeXeRei452")) {
            Bukkit.broadcastMessage(ChatColor.AQUA + "HeXeRei452 is the Owner of TranxCraft.");
        }
        
        if(event.getPlayer().getName().equals("miwojedk")) {
            Bukkit.broadcastMessage(ChatColor.AQUA + "Miwojedk is the lead builder of TranxCraft.");
        }
        
        if(leadAdmins.contains(event.getPlayer().getName())) {
            Bukkit.broadcastMessage(ChatColor.AQUA + event.getPlayer().getName() + " is a lead Admin.");
        }
        
        if(Executives.contains(event.getPlayer().getName())) {
            Bukkit.broadcastMessage(ChatColor.AQUA + event.getPlayer().getName() + " is an executive Admin.");
        }
        
        if(Admins.contains(event.getPlayer().getName())) {
            Bukkit.broadcastMessage(ChatColor.AQUA + event.getPlayer().getName() + " is an " + ChatColor.GOLD + "Admin.");
        }
        
        if(Moderators.contains(event.getPlayer().getName())) {
            Bukkit.broadcastMessage(ChatColor.AQUA + event.getPlayer().getName() + " is a " + ChatColor.DARK_PURPLE + "Moderator.");
        }
        
        if(Donators.contains(event.getPlayer().getName())) {
            Bukkit.broadcastMessage(ChatColor.AQUA + event.getPlayer().getName() + " is a " + ChatColor.LIGHT_PURPLE + "Donator! <3");
        }
        
    }
    
    @EventHandler(priority=EventPriority.HIGH)
    public void onPlayerLogin(PlayerLoginEvent event) {
        
        List<String> Executives = plugin.getConfig().getStringList("Executives");
        List<String> leadAdmins = plugin.getConfig().getStringList("Lead_Admins");
        List<String> Admins = plugin.getConfig().getStringList("Admins");
        List<String> Moderators = plugin.getConfig().getStringList("Moderators");
        List<String> Donators = plugin.getConfig().getStringList("Donators");
        Player player = event.getPlayer();
        
            if (event.getResult() == PlayerLoginEvent.Result.KICK_FULL) {
                if(player.hasPermission("tranxcraft.reserved") || Donators.contains(event.getPlayer().getName()) || Moderators.contains(event.getPlayer().getName()) || Admins.contains(event.getPlayer().getName())|| Executives.contains(event.getPlayer().getName())|| leadAdmins.contains(event.getPlayer().getName())) {
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
        List<String> Donators = plugin.getConfig().getStringList("Donators");
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
    //Only For TranxCraft Creative
    @EventHandler
        public void onDrop(PlayerDropItemEvent event) {
                event.getItemDrop().remove();
                event.setCancelled(true);
    }
    
    private void kickPlayer(Player player, PlayerLoginEvent event) {
    Player[] players = this.plugin.getServer().getOnlinePlayers();
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