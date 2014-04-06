package com.wickedgaminguk.TranxCraft;

import com.wickedgaminguk.TranxCraft.UCP.TCP_UCP;
import java.io.*;
import java.util.HashMap;
import java.util.Map;
import net.minecraft.server.v1_7_R1.MinecraftServer;
import net.pravian.bukkitlib.util.LoggerUtils;
import net.pravian.bukkitlib.util.TimeUtils;
import org.apache.commons.lang.WordUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.server.ServerListPingEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

public class TranxListener implements Listener {

    String kickMessage = "I'm sorry, but you've been kicked to make room for a reserved player, to stop this happening, buy a donator rank!";
    public Map<String, String> playerData = new HashMap();

    private final TranxCraft plugin;
    private final TCP_ModeratorList TCP_ModeratorList;
    private final TCP_Util TCP_Util;

    public TranxListener(TranxCraft plugin) {
        this.plugin = plugin;
        this.TCP_ModeratorList = new TCP_ModeratorList(plugin);
        this.TCP_Util = new TCP_Util(plugin);
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onEntityExplode(EntityExplodeEvent event) {
        if (!(event.getEntityType().equals(EntityType.PRIMED_TNT))) {
            LoggerUtils.info("A " + WordUtils.capitalizeFully(event.getEntityType().toString().toLowerCase()) + " exploded at: " + event.getLocation().getBlockX() + ", " + event.getLocation().getBlockY() + ", " + event.getLocation().getBlockZ());
            event.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onPlayerEvent(PlayerJoinEvent event) {
        int totalPlayers = plugin.config.getInt("TotalPlayers");
        totalPlayers++;

        plugin.config.set("TotalPlayers", totalPlayers);
        plugin.saveConfig();

        Bukkit.broadcastMessage(ChatColor.BLUE + "[Player Counter] " + totalPlayers + " players have joined in total.");

        final Player player = event.getPlayer();

        switch (player.getName()) {
            case "WickedGamingUK":
            case "HeXeRei452":
                Bukkit.broadcastMessage(ChatColor.AQUA + player.getName() + " is the " + ChatColor.DARK_RED + "Owner!");
                break;
            case "thecjgcjg":
                Bukkit.broadcastMessage(ChatColor.AQUA + player.getName() + " is the " + ChatColor.RED + "Executive Technical Admin!");
                break;
        }

        if (TCP_ModeratorList.getleadAdmins().contains(player.getName())) {
            Bukkit.broadcastMessage(ChatColor.AQUA + player.getName() + " is a lead Admin.");
        }

        if (TCP_ModeratorList.getExecutives().contains(player.getName())) {
            Bukkit.broadcastMessage(ChatColor.AQUA + player.getName() + " is an executive Admin.");
        }

        if (TCP_ModeratorList.getAdmins().contains(player.getName())) {
            Bukkit.broadcastMessage(ChatColor.AQUA + player.getName() + " is an " + ChatColor.GOLD + "Admin.");
        }

        if (TCP_ModeratorList.getModerators().contains(player.getName())) {
            Bukkit.broadcastMessage(ChatColor.AQUA + player.getName() + " is a " + ChatColor.DARK_PURPLE + "Moderator.");
        }

        if (TCP_ModeratorList.getDonators().contains(player.getName())) {
            Bukkit.broadcastMessage(ChatColor.AQUA + player.getName() + " is a " + ChatColor.LIGHT_PURPLE + "Donator! <3");
        }

        player.setScoreboard(plugin.board);

        if (plugin.kills.get(player.getName()) == null) {
            plugin.kills.put(player.getName(), plugin.o.getScore(Bukkit.getServer().getOfflinePlayer(ChatColor.GREEN + "Kills:")));
        }

        if (plugin.deaths.get(player.getName()) == null) {
            plugin.deaths.put(player.getName(), plugin.o.getScore(Bukkit.getServer().getOfflinePlayer(ChatColor.GREEN + "Deaths:")));
        }

        if (plugin.kd.get(player.getName()) == null) {
            plugin.kd.put(player.getName(), plugin.o.getScore(Bukkit.getServer().getOfflinePlayer(ChatColor.GREEN + "K/D:")));
        }

        plugin.kills.get(player.getName()).setScore(plugin.playerConfig.getInt(player.getName() + ".kills"));
        plugin.deaths.get(player.getName()).setScore(plugin.playerConfig.getInt(player.getName() + ".deaths"));
        plugin.kd.get(player.getName()).setScore(plugin.playerConfig.getInt(player.getName() + ".kills") / plugin.playerConfig.getInt(player.getName() + ".deaths"));

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
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onPlayerLogin(PlayerLoginEvent event) throws FileNotFoundException, IOException {
        Player player = event.getPlayer();

        if (!(TCP_ModeratorList.isPlayerMod(player))) {
            if (plugin.config.getBoolean("adminmode") == true) {
                event.disallow(PlayerLoginEvent.Result.KICK_OTHER, ChatColor.RED + "TranxCraft is currently open to moderators and admins only.");
                return;
            }
        }

        if (event.getResult() == PlayerLoginEvent.Result.KICK_FULL) {
            if (TCP_ModeratorList.getAllAdmins().contains(event.getPlayer().getName()) || TCP_ModeratorList.getDonators().contains(event.getPlayer().getName())) {
                kickPlayer(player, event);
                event.allow();
                Bukkit.broadcastMessage(ChatColor.AQUA + player.getName() + ChatColor.GREEN + " is a reserved member!");
            }
        }

        plugin.playerLogins.put(player.getName(), TimeUtils.getUnix());

        if (!plugin.playerConfig.contains(player.getName())) {
            plugin.playerConfig.set(player.getName() + ".time", 0);
            plugin.playerConfig.set(player.getName() + ".kills", 0);
            plugin.playerConfig.set(player.getName() + ".deaths", 0);
            plugin.playerConfig.save();
        }

        String playerIP = event.getAddress().toString();
        playerIP = playerIP.replaceAll("/", "");
        playerIP = playerIP.replaceAll("\\.", "-");
        try {
            if (!this.playerData.containsKey(playerIP)) {
                this.playerData.put(playerIP, player.getName());

                File players = new File("playerData.dat");
                if (!players.exists()) {
                    players.createNewFile();
                }
                try (FileOutputStream fos = new FileOutputStream("playerData.dat"); ObjectOutputStream oos = new ObjectOutputStream(fos)) {
                    oos.writeObject(playerData);
                }
            }
        }
        catch (FileNotFoundException ex) {
            LoggerUtils.warning(plugin, "Error with playerData");
        }

        new TCP_UCP(plugin).runTaskAsynchronously(plugin);
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();

        plugin.playerConfig.set(player.getName() + ".time", plugin.playerConfig.getLong(player.getName() + ".time") + (TimeUtils.getUnix() - plugin.playerLogins.get(player.getName())));
        plugin.playerConfig.save();

        plugin.playerLogins.put(player.getName(), null);

        new TCP_UCP(plugin).runTaskAsynchronously(plugin);
    }

    @EventHandler
    public void onPlayerChat(AsyncPlayerChatEvent event) {
        if (!event.getPlayer().hasPermission("tranxcraft.admin")) {
            if (TCP_Util.swear.contains(ChatColor.stripColor(event.getMessage().toLowerCase()))) {
                event.getPlayer().sendMessage(ChatColor.RED + "Profanic words aren't allowed on TranxCraft. If you try to bypass our filters you will get muted.");
                LoggerUtils.info(plugin, "Player " + event.getPlayer().getName() + " has used profanic words in his message: " + ChatColor.stripColor(event.getMessage()));
                event.setCancelled(true);
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onBlockPlace(BlockPlaceEvent event) {
        Player player = event.getPlayer();

        switch (event.getBlockPlaced().getType()) {
            case FIREWORK: {
                if (!((TCP_ModeratorList.getAllAdmins().contains(event.getPlayer().getName())))) {
                    if (!(event.getPlayer().getName().equalsIgnoreCase("WickedGamingUK"))) {
                        player.sendMessage(ChatColor.RED + "The Use of Fireworks is not permitted on TranxCraft.");
                        player.getInventory().setItem(player.getInventory().getHeldItemSlot(), new ItemStack(Material.COOKIE, 1));
                        event.setCancelled(true);
                    }
                }
                break;
            }

            case TNT: {
                if (!((TCP_ModeratorList.getAllAdmins().contains(event.getPlayer().getName())))) {
                    if (!(event.getPlayer().getName().equalsIgnoreCase("WickedGamingUK"))) {
                        player.sendMessage(ChatColor.RED + "The Use of TNT is not permitted on TranxCraft.");
                        player.getInventory().setItem(player.getInventory().getHeldItemSlot(), new ItemStack(Material.COOKIE, 1));
                        event.setCancelled(true);
                    }
                }
                break;
            }

            case LAVA:
            case STATIONARY_LAVA:
            case LAVA_BUCKET: {
                if (!((TCP_ModeratorList.getAllAdmins().contains(event.getPlayer().getName())))) {
                    if (!(event.getPlayer().getName().equalsIgnoreCase("WickedGamingUK"))) {
                        player.sendMessage(ChatColor.RED + "The Use of Lava is not permitted on TranxCraft.");
                        player.getInventory().setItem(player.getInventory().getHeldItemSlot(), new ItemStack(Material.COOKIE, 1));
                        event.setCancelled(true);
                    }
                }
                break;
            }

            case WATER:
            case STATIONARY_WATER:
            case WATER_BUCKET: {
                if (!((TCP_ModeratorList.getAllAdmins().contains(event.getPlayer().getName())))) {
                    if (!(event.getPlayer().getName().equalsIgnoreCase("WickedGamingUK"))) {
                        player.sendMessage(ChatColor.RED + "The Use of Water is not permitted on TranxCraft.");
                        player.getInventory().setItem(player.getInventory().getHeldItemSlot(), new ItemStack(Material.COOKIE, 1));
                        event.setCancelled(true);
                    }
                }
                break;
            }

            case FIRE: {
                if (!((TCP_ModeratorList.getAllAdmins().contains(event.getPlayer().getName())))) {
                    if (!(event.getPlayer().getName().equalsIgnoreCase("WickedGamingUK"))) {
                        player.sendMessage(ChatColor.RED + "The Use of Fire is not permitted on TranxCraft.");
                        player.getInventory().setItem(player.getInventory().getHeldItemSlot(), new ItemStack(Material.COOKIE, 1));
                        event.setCancelled(true);
                    }
                }
                break;
            }
        }
    }

    //Credits to Madgeek1450 & DarthSalamon for this event handler, and various other people.
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onServerPing(ServerListPingEvent event) {
        String playerIP = event.getAddress().toString();
        playerIP = playerIP.replaceAll("/", "");
        playerIP = playerIP.replaceAll("\\.", "-");

        try {
            if (getPlayerData().containsKey(playerIP)) {
                if (TCP_Util.isNameBanned((String) getPlayerData().get(playerIP)) || TCP_Util.isIPBanned(event.getAddress().getHostAddress())) {
                    event.setMotd(ChatColor.RED + "Hey " + (String) getPlayerData().get(playerIP) + ", you are" + ChatColor.BOLD + "banned.");
                }
                else if (TCP_ModeratorList.isPlayerMod((String) getPlayerData().get(playerIP))) {
                    if (TCP_Util.isAdminMode() == true) {
                        event.setMotd(ChatColor.RED + "Hey " + (String) getPlayerData().get(playerIP) + ", sadly, adminmode is on, come back soon!" + ChatColor.LIGHT_PURPLE + " <3");
                    }
                }
                else if (Bukkit.hasWhitelist()) {
                    event.setMotd(ChatColor.RED + "Hey " + (String) getPlayerData().get(playerIP) + ", sadly, the whitelist is on, come back soon!" + ChatColor.LIGHT_PURPLE + " <3");
                }
                else if (Bukkit.getOnlinePlayers().length >= Bukkit.getMaxPlayers()) {
                    event.setMotd(ChatColor.RED + "Hey " + (String) getPlayerData().get(playerIP) + ", sadly, the server is full, come back soon!" + ChatColor.LIGHT_PURPLE + " <3");
                }
                else {
                    event.setMotd(ChatColor.GREEN + "Welcome " + ChatColor.GOLD + (String) getPlayerData().get(playerIP) + ChatColor.WHITE + " to " + ChatColor.GREEN + "TranxCraft " + ChatColor.WHITE + "- " + ChatColor.DARK_PURPLE + "Craftbukkit " + MinecraftServer.getServer().getVersion());
                }
            }
            else {
                if (TCP_Util.isIPBanned(event.getAddress().getHostAddress())) {
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
        }
        catch (FileNotFoundException ex) {
        }
        catch (IOException | ClassNotFoundException ex) {
        }
    }

    /* ONLY USE FOR DEBUGGING/TESTING
     @EventHandler
     public void onPlayerMove(PlayerMoveEvent event) {
     Player player = event.getPlayer();
     plugin.kills.get(event.getPlayer().getName()).setScore(plugin.kills.get(event.getPlayer().getName()).getScore() + 5);
     plugin.deaths.get(event.getPlayer().getName()).setScore(plugin.deaths.get(event.getPlayer().getName()).getScore() + 1);
     plugin.playerConfig.set(player.getName() + ".deaths", plugin.deaths.get(event.getPlayer().getName()).getScore() + 1);
     plugin.playerConfig.set(player.getName() + ".kills", plugin.kills.get(event.getPlayer().getName()).getScore() + 5);
     plugin.playerConfig.save();
     plugin.kd.get(player.getName()).setScore(plugin.playerConfig.getInt(player.getName() + ".kills") / plugin.playerConfig.getInt(player.getName() + ".deaths"));
     }
     */
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

    private void kickPlayer(Player player, PlayerLoginEvent event) {
        Player[] players = plugin.getServer().getOnlinePlayers();

        for (Player p : players) {
            if (TCP_ModeratorList.getAllAdmins().contains(p.getName()) || TCP_ModeratorList.getDonators().contains(p.getName())) {
                p.kickPlayer(this.kickMessage);
                event.allow();
                LoggerUtils.info(plugin, "Allowed player " + player.getName() + " to join full server by kicking player " + p.getName() + "!");
            }
        }

        event.disallow(PlayerLoginEvent.Result.KICK_FULL, "Unable to find any kickable players to open slots!");
    }

    private Map<String, String> getPlayerData() throws FileNotFoundException, IOException, ClassNotFoundException {
        Map<String, String> players;

        try (FileInputStream fis = new FileInputStream("playerData.dat"); ObjectInputStream ois = new ObjectInputStream(fis)) {
            players = (HashMap) ois.readObject();
        }

        return players;
    }
}
