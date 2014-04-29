package com.wickedgaminguk.TranxCraft;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class TCP_Util {

    private final TranxCraft plugin;

    public TCP_Util(TranxCraft plugin) {
        this.plugin = plugin;
    }

    public static final String invalidUsage = ChatColor.RED + "Invalid Usage.";
    public static List<String> swear = Arrays.asList("fuck", "fuckwit", "dildo", "slut", "cunt", "arse", "arselicker", "ass", "asshole", "bastard", "bitch", "bullocks", "fucker", "asswipe", "shit");
    public static List<String> permBan = Arrays.asList("3d4ad828721f44a4b6e1a18aeac31f88");

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
}
