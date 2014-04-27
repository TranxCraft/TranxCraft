package com.wickedgaminguk.TranxCraft;

import com.wickedgaminguk.TranxCraft.TCP_ModeratorList.AdminType;
import com.wickedgaminguk.TranxCraft.UCP.TCP_UCP;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import net.minecraft.server.v1_7_R3.MinecraftServer;
import net.pravian.bukkitlib.util.ChatUtils;
import net.pravian.bukkitlib.util.LoggerUtils;
import net.pravian.bukkitlib.util.TimeUtils;
import org.apache.commons.lang.WordUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.server.ServerListPingEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

public class TranxListener implements Listener {

    String kickMessage = "I'm sorry, but you've been kicked to make room for a reserved player, to stop this happening, buy a donator rank!";
    public Map<String, String> playerData = new HashMap();

    private final ArrayList<Player> cooldown = new ArrayList<>(), nofall = new ArrayList<>();
    private final TranxCraft plugin;
    private final TCP_ModeratorList TCP_ModeratorList;
    private final TCP_DonatorList TCP_DonatorList;
    private final TCP_Util TCP_Util;
    private final TCP_Ban TCP_Ban;

    public TranxListener(TranxCraft plugin) {
        this.plugin = plugin;
        this.TCP_ModeratorList = new TCP_ModeratorList(plugin);
        this.TCP_DonatorList = new TCP_DonatorList(plugin);
        this.TCP_Util = new TCP_Util(plugin);
        this.TCP_Ban = new TCP_Ban(plugin);
    }

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void onEntityExplode(EntityExplodeEvent event) {
        if (!(event.getEntityType().equals(EntityType.PRIMED_TNT))) {
            LoggerUtils.info("A " + WordUtils.capitalizeFully(event.getEntityType().toString().toLowerCase()) + " exploded at: " + event.getLocation().getBlockX() + ", " + event.getLocation().getBlockY() + ", " + event.getLocation().getBlockZ());
            event.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void onPlayerJoin(PlayerJoinEvent event) {
        int totalPlayers = plugin.config.getInt("TotalPlayers");
        totalPlayers++;

        plugin.config.set("TotalPlayers", totalPlayers);
        plugin.saveConfig();

        Bukkit.broadcastMessage(ChatColor.BLUE + "[Player Counter] " + totalPlayers + " players have joined in total.");

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
    }

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void onPlayerLogin(PlayerLoginEvent event) throws FileNotFoundException, IOException {
        Player player = event.getPlayer();
        String IP = event.getAddress().getHostAddress().trim();
        LoggerUtils.info(IP);

        if (TCP_Ban.isUUIDBanned(player)) {
            event.disallow(PlayerLoginEvent.Result.KICK_BANNED, ChatColor.RED + "Your UUID is banned.\nReason: " + ChatColor.YELLOW + TCP_Ban.getBanReason(player) + ChatColor.RED + "\nIf you think this was made in error, appeal here: " + ChatColor.YELLOW + "https://www.tranxcraft.com/forums/");
        }
        else if (TCP_Ban.isIPBanned(IP)) {
            event.disallow(PlayerLoginEvent.Result.KICK_BANNED, ChatColor.RED + "Your IP is banned.\nReason: " + ChatColor.YELLOW + TCP_Ban.getIPBanReason(IP) + ChatColor.RED + "\nIf you think this was made in error, appeal here: " + ChatColor.YELLOW + "https://www.tranxcraft.com/forums/");
        }

        if (!(TCP_ModeratorList.isPlayerMod(player))) {
            if (plugin.config.getBoolean("adminmode") == true) {
                event.disallow(PlayerLoginEvent.Result.KICK_OTHER, ChatColor.RED + "TranxCraft is currently open to moderators and admins only.");
                return;
            }
        }

        if (event.getResult() == PlayerLoginEvent.Result.KICK_FULL) {
            if (TCP_ModeratorList.isPlayerMod(player) || TCP_DonatorList.isPlayerDonator(player)) {
                kickPlayer(player, event);
                event.allow();
                Bukkit.broadcastMessage(ChatColor.AQUA + player.getName() + ChatColor.GREEN + " is a reserved member!");
            }
        }

        plugin.playerLogins.put(player.getUniqueId().toString(), TimeUtils.getUnix());

        if (!plugin.playerConfig.contains(player.getUniqueId().toString())) {
            plugin.playerConfig.set(player.getUniqueId().toString() + ".IGN", player.getName());
            plugin.playerConfig.set(player.getUniqueId().toString() + ".IP", player.getAddress().getHostString());
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

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void onPlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();

        plugin.playerConfig.set(player.getUniqueId().toString() + ".time", plugin.playerConfig.getLong(player.getUniqueId().toString() + ".time") + (TimeUtils.getUnix() - plugin.playerLogins.get(player.getUniqueId().toString())));
        plugin.playerConfig.set(player.getUniqueId().toString() + ".IGN", player.getName());
        plugin.playerConfig.set(player.getUniqueId().toString() + ".IP", player.getAddress().getHostString());
        plugin.playerConfig.save();

        plugin.playerLogins.put(player.getUniqueId().toString(), null);

        new TCP_UCP(plugin).runTaskAsynchronously(plugin);
    }

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void onPlayerChat(AsyncPlayerChatEvent event) {
        if (!event.getPlayer().hasPermission("tranxcraft.admin")) {
            if (TCP_Util.swear.contains(ChatColor.stripColor(event.getMessage().toLowerCase()))) {
                event.getPlayer().sendMessage(ChatColor.RED + "Profanic words aren't allowed on TranxCraft. If you try to bypass our filters you will get muted.");
                LoggerUtils.info(plugin, "Player " + event.getPlayer().getName() + " has used profanic words in his message: " + ChatColor.stripColor(event.getMessage()));
                event.setCancelled(true);
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void onBlockPlace(BlockPlaceEvent event) {
        Player player = event.getPlayer();

        switch (event.getBlockPlaced().getType()) {
            case FIREWORK: {
                if (!((TCP_ModeratorList.isPlayerMod(player)))) {
                    if (!(event.getPlayer().getName().equalsIgnoreCase("WickedGamingUK"))) {
                        player.sendMessage(ChatColor.RED + "The Use of Fireworks is not permitted on TranxCraft.");
                        player.getInventory().setItem(player.getInventory().getHeldItemSlot(), new ItemStack(Material.COOKIE, 1));
                        event.setCancelled(true);
                    }
                }
                break;
            }

            case TNT: {
                if (!((TCP_ModeratorList.isPlayerMod(player)))) {
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
                if (!((TCP_ModeratorList.isPlayerMod(player)))) {
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
                if (!((TCP_ModeratorList.isPlayerMod(player)))) {
                    if (!(event.getPlayer().getName().equalsIgnoreCase("WickedGamingUK"))) {
                        player.sendMessage(ChatColor.RED + "The Use of Water is not permitted on TranxCraft.");
                        player.getInventory().setItem(player.getInventory().getHeldItemSlot(), new ItemStack(Material.COOKIE, 1));
                        event.setCancelled(true);
                    }
                }
                break;
            }

            case FIRE: {
                if (!((TCP_ModeratorList.isPlayerMod(player)))) {
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
                if (TCP_Ban.isUUIDBanned((String) getPlayerData().get(playerIP)) || TCP_Ban.isIPBanned(event.getAddress().getHostAddress())) {
                    event.setMotd(ChatColor.RED + "Hey " + (String) getPlayerData().get(playerIP) + ", you are" + ChatColor.BOLD + " banned.");
                }
                else if (TCP_Util.isAdminMode() == true) {
                    if (!(plugin.adminConfig.getStringList("Admin_IPs").contains(event.getAddress().getHostAddress()))) {
                        event.setMotd(ChatColor.RED + "Hey " + (String) getPlayerData().get(playerIP) + ", sadly, adminmode is on, come back soon!" + ChatColor.LIGHT_PURPLE + " <3");
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
            }
            else {
                if (TCP_Ban.isIPBanned(event.getAddress().getHostAddress())) {
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

    @EventHandler
    public void onGrappleThrow(ProjectileLaunchEvent event) {
        if (!event.getEntityType().equals(EntityType.FISHING_HOOK)) {
            return;
        }
        if (!(event.getEntity().getShooter() instanceof Player)) {
            return;
        }

        final Player player = (Player) event.getEntity().getShooter();

        if (!(player.hasPermission("tranxcraft.grapple"))) {
        }
        else {
            if (TCP_Util.hasGrapple(player)) {
                if (cooldown.contains(player)) {
                    event.setCancelled(true);
                    return;
                }

                Location target = null;

                for (Block block : player.getLineOfSight(null, 100)) {
                    if (!block.getType().equals(Material.AIR)) {
                        target = block.getLocation();
                        break;
                    }
                }

                if (target == null) {
                    event.setCancelled(true);
                    return;
                }

                player.teleport(player.getLocation().add(0, 0.5, 0));

                final Vector v = getVectorForPoints(player.getLocation(), target);

                event.getEntity().setVelocity(v);

                if (!nofall.contains(player)) {
                    nofall.add(player);
                }

                Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> {
                    player.setVelocity(v);
                }, 5);

                cooldown.add(player);

                Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> {
                    cooldown.remove(player);
                }, 15);
            }
        }
    }

    @EventHandler
    public void onEntityDamage(EntityDamageEvent event) {
        if (!(event.getEntity() instanceof Player)) {
            return;
        }
        if (!event.getCause().equals(DamageCause.FALL)) {
            return;
        }

        Player p = (Player) event.getEntity();

        if (nofall.contains(p)) {
            event.setCancelled(true);
            nofall.remove(p);
        }
    }

    private void kickPlayer(Player player, PlayerLoginEvent event) {
        Player[] players = plugin.getServer().getOnlinePlayers();

        for (Player p : players) {
            if (!((TCP_ModeratorList.isPlayerMod(player))) || TCP_DonatorList.isPlayerDonator(player)) {
                p.kickPlayer(this.kickMessage);
                event.allow();
                LoggerUtils.info(plugin, "Allowed player " + player.getName() + " to join full server by kicking player " + p.getName() + "!");
            }
        }

        event.disallow(PlayerLoginEvent.Result.KICK_FULL, "Unable to find any kickable players to open slots!");
    }

    private Vector getVectorForPoints(Location l1, Location l2) {
        double g = -0.08;
        double d = l2.distance(l1);
        double t = d;
        double vX = (1.0 + 0.07 * t) * (l2.getX() - l1.getX()) / t;
        double vY = (1.0 + 0.03 * t) * (l2.getY() - l1.getY()) / t - 0.5 * g * t;
        double vZ = (1.0 + 0.07 * t) * (l2.getZ() - l1.getZ()) / t;
        return new Vector(vX, vY, vZ);
    }

    private Map<String, String> getPlayerData() throws FileNotFoundException, IOException, ClassNotFoundException {
        Map<String, String> players;

        try (FileInputStream fis = new FileInputStream("playerData.dat"); ObjectInputStream ois = new ObjectInputStream(fis)) {
            players = (HashMap) ois.readObject();
        }

        return players;
    }
}
