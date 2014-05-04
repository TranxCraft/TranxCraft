package com.wickedgaminguk.tranxcraft;

import com.wickedgaminguk.tranxcraft.TCP_DonatorList.DonatorType;
import com.wickedgaminguk.tranxcraft.TCP_ModeratorList.AdminType;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import net.pravian.bukkitlib.util.LoggerUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

public class TCP_Util {

    private final TranxCraft plugin;
    private final TCP_ModeratorList TCP_ModeratorList;
    private final TCP_DonatorList TCP_DonatorList;

    public TCP_Util(TranxCraft plugin) {
        this.plugin = plugin;
        this.TCP_ModeratorList = plugin.moderatorList;
        this.TCP_DonatorList = plugin.donatorList;
    }

    public final String invalidUsage = ChatColor.RED + "Invalid Usage.";
    public List<String> permBan = Arrays.asList("3d4ad828721f44a4b6e1a18aeac31f88");

    public boolean isAdminMode() {
        return plugin.config.getBoolean("adminmode");
    }

    public boolean hasGrapple(Player player) {
        return plugin.playerConfig.getBoolean(player.getUniqueId().toString() + ".grapple");
    }

    public void setGrapple(Player player, boolean mode) {
        if (mode == true) {
            plugin.playerConfig.set(player.getUniqueId().toString() + ".grapple", true);
        }

        if (mode == false) {
            plugin.playerConfig.set(player.getUniqueId().toString() + ".grapple", false);
        }

        plugin.playerConfig.save();
    }

    public void broadcastItem(Material material, int quantity, String message) {
        for (Player player : plugin.getServer().getOnlinePlayers()) {
            ItemStack item = new ItemStack(material, quantity);
            player.getInventory().setItem(player.getInventory().firstEmpty(), item);
        }

        if (!(message.isEmpty())) {
            Bukkit.broadcastMessage(message);
        }
    }

    public void sendItem(Player player, Material material, int quantity, String message) {
        ItemStack item = new ItemStack(material, quantity);
        player.getInventory().setItem(player.getInventory().firstEmpty(), item);

        if (!(message == null)) {
            player.sendMessage(message);
        }
    }

    public boolean hasItem(Player player, ItemStack i) {
        ItemStack[] inv = player.getInventory().getContents();

        for (ItemStack item : inv) {
            if (item == null) {
                return false;
            }
            if (item.getType().equals(i.getType())) {
                return true;
            }
        }
        return false;
    }

    public UUID playerToUUID(String player) {
        UUID playerID = null;
        try {
            playerID = UUIDFetcher.getUUIDOf(player);
        }
        catch (Exception ex) {
        }
        return playerID;
    }

    public UUID playerToUUID(Player player) {
        UUID playerID = null;
        try {
            playerID = UUIDFetcher.getUUIDOf(player.getName());
        }
        catch (Exception ex) {
        }
        return playerID;
    }

    public String UUIDToPlayer(UUID uuid) {
        NameFetcher fetcher = new NameFetcher(Arrays.asList(uuid));
        Map<UUID, String> response = null;

        try {
            response = fetcher.call();
        }
        catch (Exception e) {
        }

        String playerName = response.get(uuid);

        return playerName;
    }

    public void kickPlayer(Player player, PlayerLoginEvent event) {
        Player[] players = plugin.getServer().getOnlinePlayers();

        for (Player p : players) {
            if (!((TCP_ModeratorList.isPlayerMod(player))) || TCP_DonatorList.isPlayerDonator(player)) {
                p.kickPlayer("I'm sorry, but you've been kicked to make room for a reserved player, to stop this happening, buy a donator rank!");
                event.allow();
                LoggerUtils.info(plugin, "Allowed player " + player.getName() + " to join full server by kicking player " + p.getName() + "!");
            }
        }

        event.disallow(PlayerLoginEvent.Result.KICK_FULL, "Unable to find any kickable players to open slots!");
    }

    public Vector getVectorForPoints(Location l1, Location l2) {
        double g = -0.08;
        double d = l2.distance(l1);
        double t = d;
        double vX = (1.0 + 0.07 * t) * (l2.getX() - l1.getX()) / t;
        double vY = (1.0 + 0.03 * t) * (l2.getY() - l1.getY()) / t - 0.5 * g * t;
        double vZ = (1.0 + 0.07 * t) * (l2.getZ() - l1.getZ()) / t;
        return new Vector(vX, vY, vZ);
    }

    public Map<String, String> getPlayerData() throws FileNotFoundException, IOException, ClassNotFoundException {
        Map<String, String> players;

        try (FileInputStream fis = new FileInputStream(plugin.getDataFolder() + "/playerData.dat"); ObjectInputStream ois = new ObjectInputStream(fis)) {
            players = (HashMap) ois.readObject();
        }

        return players;
    }

    public boolean hasPermission(String permission, Player player) {
        if (player instanceof Player && !(player.hasPermission(permission) || player.isOp())) {
            return false;
        }
        else {
            return true;
        }
    }

    public boolean hasPermission(String permission, CommandSender player) {
        if (player instanceof Player && !(player.hasPermission(permission) || player.isOp())) {
            return false;
        }
        else {
            return true;
        }
    }

    public boolean hasPermission(AdminType adminType, CommandSender sender) {
        return TCP_ModeratorList.getRank(sender).equals(adminType);
    }

    public boolean hasPermission(DonatorType donatorType, Player player) {
        return TCP_DonatorList.getRank(player).equals(donatorType);
    }

    public boolean hasPermission(AdminType adminType, Player player) {
        return TCP_ModeratorList.getRank(player).equals(adminType);
    }

    public boolean hasPermission(DonatorType donatorType, CommandSender player) {
        return TCP_DonatorList.getRank(player).equals(donatorType);
    }

    public int getTotalUniquePlayers() {
        int total = plugin.playerConfig.getValues(false).size();
        return total;
    }

    public String[] getCredits() {
        return new String[]{"Pravian - BukkitLib", "TotalFreedom - Various Methods", "JeromSar - Programming Help"};
    }
}
