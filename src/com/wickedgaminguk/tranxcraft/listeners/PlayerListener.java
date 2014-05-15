package com.wickedgaminguk.tranxcraft.listeners;

import com.wickedgaminguk.tranxcraft.TCP_Ban;
import com.wickedgaminguk.tranxcraft.TCP_DonatorList;
import com.wickedgaminguk.tranxcraft.TCP_DonatorList.DonatorType;
import com.wickedgaminguk.tranxcraft.TCP_ModeratorList;
import com.wickedgaminguk.tranxcraft.TCP_ModeratorList.AdminType;
import com.wickedgaminguk.tranxcraft.TCP_UCP;
import com.wickedgaminguk.tranxcraft.TCP_Util;
import com.wickedgaminguk.tranxcraft.TranxCraft;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.Map;
import net.pravian.bukkitlib.util.ChatUtils;
import net.pravian.bukkitlib.util.LoggerUtils;
import net.pravian.bukkitlib.util.TimeUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerToggleFlightEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

public class PlayerListener implements Listener {

    private final Map<String, String> playerData = new HashMap();

    private final TranxCraft plugin;
    private final TCP_ModeratorList TCP_ModeratorList;
    private final TCP_DonatorList TCP_DonatorList;
    private final TCP_Util TCP_Util;
    private final TCP_Ban TCP_Ban;

    public PlayerListener(TranxCraft plugin) {
        this.plugin = plugin;
        this.TCP_ModeratorList = new TCP_ModeratorList(plugin);
        this.TCP_DonatorList = new TCP_DonatorList(plugin);
        this.TCP_Util = new TCP_Util(plugin);
        this.TCP_Ban = new TCP_Ban(plugin);
    }

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void onPlayerLogin(PlayerLoginEvent event) throws FileNotFoundException, IOException {
        Player player = event.getPlayer();
        String IP = event.getAddress().getHostAddress().trim();
        LoggerUtils.info(IP);

        if (TCP_Util.permBan.contains(player.getUniqueId().toString())) {
            if (player.getUniqueId().toString().equals("3d4ad828721f44a4b6e1a18aeac31f88")) {
                event.disallow(PlayerLoginEvent.Result.KICK_OTHER, ChatColor.RED + "xXWilee999Xx, you are a pot stirring fuck, you're not allowed on TranxCraft, ever.");
            }
            else {
                event.disallow(PlayerLoginEvent.Result.KICK_OTHER, ChatColor.RED + TCP_Util.UUIDToPlayer(player.getUniqueId()) + ", you are permbanned, you are " + ChatColor.UNDERLINE + "never" + ChatColor.RESET + ChatColor.RED + " allowed on TranxCraft again.");
            }
        }

        if (TCP_Ban.isUUIDBanned(player)) {
            event.disallow(PlayerLoginEvent.Result.KICK_BANNED, ChatColor.RED + TCP_Util.UUIDToPlayer(player.getUniqueId()) + ", your UUID is banned.\nReason: " + ChatColor.YELLOW + TCP_Ban.getBanReason(player) + ChatColor.RED + "\nIf you think this was made in error, appeal here: " + ChatColor.YELLOW + "https://www.tranxcraft.com/forums/");
        }
        else if (TCP_Ban.isIPBanned(IP)) {
            event.disallow(PlayerLoginEvent.Result.KICK_BANNED, ChatColor.RED + TCP_Util.UUIDToPlayer(player.getUniqueId()) + ", your IP is banned.\nReason: " + ChatColor.YELLOW + TCP_Ban.getIPBanReason(IP) + ChatColor.RED + "\nIf you think this was made in error, appeal here: " + ChatColor.YELLOW + "https://www.tranxcraft.com/forums/");
        }

        if (!(TCP_ModeratorList.isPlayerMod(player))) {
            if (plugin.config.getBoolean("adminmode") == true) {
                event.disallow(PlayerLoginEvent.Result.KICK_OTHER, ChatColor.RED + "TranxCraft is currently open to moderators and admins only - sorry about that.");
                return;
            }
        }

        if (event.getResult() == PlayerLoginEvent.Result.KICK_FULL) {
            if (TCP_ModeratorList.isPlayerMod(player) || TCP_DonatorList.isPlayerDonator(player)) {
                TCP_Util.kickPlayer(player, event);
                event.allow();
                Bukkit.broadcastMessage(ChatColor.AQUA + player.getName() + ChatColor.GREEN + " is a reserved member!");
            }
        }

        plugin.playerLogins.put(player.getUniqueId().toString(), TimeUtils.getUnix());

        if (!plugin.playerConfig.contains(player.getUniqueId().toString())) {
            plugin.playerConfig.set(player.getUniqueId().toString() + ".ign", player.getName());
            plugin.playerConfig.set(player.getUniqueId().toString() + ".ip", player.getAddress().getHostString());
            plugin.playerConfig.set(player.getUniqueId().toString() + ".time", 0);
            plugin.playerConfig.set(player.getUniqueId().toString() + ".kills", 0);
            plugin.playerConfig.set(player.getUniqueId().toString() + ".deaths", 0);
            plugin.playerConfig.set(player.getUniqueId().toString() + ".grapple", false);
            plugin.playerConfig.save();
        }

        String playerIP = event.getAddress().toString();
        playerIP = playerIP.replaceAll("/", "");
        playerIP = playerIP.replaceAll("\\.", "-");
        try {
            if (!this.playerData.containsKey(playerIP)) {
                this.playerData.put(playerIP, player.getName());

                File players = new File(plugin.getDataFolder() + "/playerData.dat");
                if (!players.exists()) {
                    players.createNewFile();
                }
                try (FileOutputStream fos = new FileOutputStream(plugin.getDataFolder() + "/playerData.dat"); ObjectOutputStream oos = new ObjectOutputStream(fos)) {
                    oos.writeObject(playerData);
                }
            }
        }
        catch (FileNotFoundException ex) {
            LoggerUtils.warning(plugin, "Error with playerData");
        }

        new TCP_UCP(plugin).runTaskAsynchronously(plugin);
    }

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void onPlayerJoin(PlayerJoinEvent event) {
        int totalPlayers = plugin.config.getInt("total_players");
        totalPlayers++;

        plugin.config.set("total_players", totalPlayers);
        plugin.saveConfig();

        Bukkit.broadcastMessage(ChatColor.BLUE + "[Player Counter] " + totalPlayers + " players & " + TCP_Util.getTotalUniquePlayers() + " unique players have joined in total.");

        final Player player = event.getPlayer();

        if (!(TCP_ModeratorList.getLoginMessage(player).equals(""))) {
            Bukkit.broadcastMessage(ChatUtils.colorize(TCP_ModeratorList.getLoginMessage(player)));
        }
        else if (TCP_ModeratorList.getRank(player) == AdminType.LEADADMIN) {
            Bukkit.broadcastMessage(ChatColor.AQUA + player.getName() + " is a lead Admin.");
        }
        else if (TCP_ModeratorList.getRank(player) == AdminType.EXECUTIVE) {
            Bukkit.broadcastMessage(ChatColor.AQUA + player.getName() + " is an executive Admin.");
        }
        else if (TCP_ModeratorList.getRank(player) == AdminType.ADMIN) {
            Bukkit.broadcastMessage(ChatColor.AQUA + player.getName() + " is an " + ChatColor.GOLD + "Admin.");
        }
        else if (TCP_ModeratorList.getRank(player) == AdminType.MODERATOR) {
            Bukkit.broadcastMessage(ChatColor.AQUA + player.getName() + " is a " + ChatColor.DARK_PURPLE + "Moderator.");
        }
        else if (TCP_DonatorList.isPlayerDonator(player)) {
            Bukkit.broadcastMessage(ChatColor.AQUA + player.getName() + " is a " + ChatColor.LIGHT_PURPLE + "Donator! <3");
        }

        player.setScoreboard(plugin.board);

        if (plugin.kills.get(player.getName()) == null) {
            plugin.kills.put(player.getName(), plugin.o.getScore(Bukkit.getServer().getOfflinePlayer(ChatColor.GREEN + "Kills:")));
        }

        if (plugin.deaths.get(player.getName()) == null) {
            plugin.deaths.put(player.getName(), plugin.o.getScore(Bukkit.getServer().getOfflinePlayer(ChatColor.RED + "Deaths:")));
        }

        if (plugin.kd.get(player.getName()) == null) {
            plugin.kd.put(player.getName(), plugin.o.getScore(Bukkit.getServer().getOfflinePlayer(ChatColor.GREEN + "K/D:")));
        }

        plugin.kills.get(player.getName()).setScore(plugin.playerConfig.getInt(player.getName() + ".kills"));
        plugin.deaths.get(player.getName()).setScore(plugin.playerConfig.getInt(player.getName() + ".deaths"));

        if (!(plugin.playerConfig.getInt(player.getName() + ".deaths") == 0)) {
            plugin.kd.get(player.getName()).setScore(plugin.playerConfig.getInt(player.getName() + ".kills") / plugin.playerConfig.getInt(player.getName() + ".deaths"));
        }

        if (!(player.hasPermission("tranxcraft.member"))) {
            new BukkitRunnable() {
                @Override
                public void run() {
                    player.sendMessage(ChatColor.GREEN + "Welcome to TranxCraft!\nBefore you can continue, please read the following rules and then accept them with /acceptrules to become Member.");
                    Bukkit.dispatchCommand(player, "rules");
                    player.sendMessage(ChatColor.GREEN + "Remember to read these rules and accept them with /acceptrules !");
                }
            }.runTaskLater(plugin, 40L);
        }
        
        if(!(TCP_Util.hasItem(player, new ItemStack(Material.COMPASS)))) {
            TCP_Util.sendItem(player, Material.COMPASS, 1, null);
        }
    }
    
    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        Action a = event.getAction();        
        ItemStack is = event.getItem();
        
        if (!(a == Action.PHYSICAL || is == null || is.getType() == Material.AIR)) {
            if (is.getType() == Material.COMPASS) {
                TCP_Util.openGUI(event.getPlayer());
            }
        }
    }
    
    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (ChatColor.stripColor(event.getInventory().getName()).equalsIgnoreCase("Server Utilities")) {
            Player player = (Player) event.getWhoClicked();            
            event.setCancelled(true);
            
            if (event.getCurrentItem() == null || event.getCurrentItem().getType() == Material.AIR || !event.getCurrentItem().hasItemMeta()) {
                player.closeInventory();
                return;
            }
            
            switch (event.getCurrentItem().getType()) {
                case COMPASS: {
                    player.teleport(player.getWorld().getSpawnLocation());
                    player.closeInventory();
                    player.sendMessage(ChatColor.GREEN + "You have successfully teleported to Spawn!");
                    break;
                }
                
                case DIAMOND_SWORD: {
                    TCP_Util.teleport(player.getWorld(), player, 116, 66, 315);
                    player.closeInventory();
                    player.sendMessage(ChatColor.GREEN + "You have successfully teleported to the Survival Games Lobby!");
                    break;
                }
                
                default: {
                    player.closeInventory();
                    break;
                }
            }
        }
    }
    
    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        
        if (TCP_Util.hasDoubleJump(player) && TCP_Util.hasPermission("tranxcraft.donator", player) && (player.getGameMode() != GameMode.CREATIVE) && (player.getLocation().subtract(0, 1, 0).getBlock().getType() != Material.AIR) && (!player.isFlying())) {
            player.setAllowFlight(true);
        }
    }
    
    @EventHandler
    public void onPlayerToggleFlight(PlayerToggleFlightEvent event) {
        Player player = event.getPlayer();  
        
        if (TCP_Util.hasDoubleJump(player) && TCP_Util.hasPermission("tranxcraft.donator", player) && !(player.getGameMode() == GameMode.CREATIVE)) {
            event.setCancelled(true);
            player.setAllowFlight(false);
            player.setFlying(false);
            player.setVelocity(player.getLocation().getDirection().multiply(1.5).setY(1));
            
            if (!plugin.noFall.contains(player)) {
                plugin.noFall.add(player);
            }
        }
    }
    
    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void onPlayerChat(AsyncPlayerChatEvent event) {
        if (TCP_ModeratorList.hasAdminChatEnabled(event.getPlayer())) {
            event.getPlayer().performCommand("o " + event.getMessage());
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        if (event.getEntity() instanceof Player) {
            Player victim = event.getEntity();
            Player killer = event.getEntity().getKiller();

            if (killer instanceof Player) {
                int deaths = Integer.valueOf(plugin.playerConfig.get(victim + ".deaths").toString());
                int kills = Integer.valueOf(plugin.playerConfig.get(killer + ".kills").toString());
                plugin.playerConfig.set(victim.getName() + ".deaths", deaths + 1);
                plugin.playerConfig.set(killer.getName() + ".kills", kills + 1);
                plugin.playerConfig.save();
                plugin.kills.get(killer.getName()).setScore(plugin.kills.get(killer.getName()).getScore() + 1);
                plugin.deaths.get(victim.getName()).setScore(plugin.deaths.get(victim.getName()).getScore() + 1);
                plugin.kd.get(killer.getName()).setScore(plugin.playerConfig.getInt(killer.getName() + ".kills") / plugin.playerConfig.getInt(killer.getName() + ".deaths"));
                plugin.kd.get(victim.getName()).setScore(plugin.playerConfig.getInt(victim.getName() + ".kills") / plugin.playerConfig.getInt(victim.getName() + ".deaths"));
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void onPlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();

        plugin.playerConfig.set(player.getUniqueId().toString() + ".time", plugin.playerConfig.getLong(player.getUniqueId().toString() + ".time") + (TimeUtils.getUnix() - plugin.playerLogins.get(player.getUniqueId().toString())));
        plugin.playerConfig.set(player.getUniqueId().toString() + ".ign", player.getName());
        plugin.playerConfig.set(player.getUniqueId().toString() + ".ip", player.getAddress().getHostString());
        plugin.playerConfig.save();

        plugin.playerLogins.put(player.getUniqueId().toString(), null);

        new TCP_UCP(plugin).runTaskAsynchronously(plugin);
    }
}
