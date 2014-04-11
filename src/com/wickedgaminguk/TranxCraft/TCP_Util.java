package com.wickedgaminguk.TranxCraft;

import java.util.Arrays;
import java.util.List;
import net.minecraft.server.v1_7_R1.BanEntry;
import net.minecraft.server.v1_7_R1.BanList;
import net.minecraft.server.v1_7_R1.MinecraftServer;
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

    public static final String Invalid_Usage = ChatColor.RED + "Invalid Usage.";
    public static List<String> swear = Arrays.asList("fuck", "fuckwit", "dildo", "slut", "cunt", "arse", "arselicker", "ass", "asshole", "bastard", "bitch", "bullocks", "fucker", "asswipe", "shit");

    //Credits to Steven Lawson/Madgeek & Jerom Van Der Sar/DarthSalamon for various methods.
    public void banUsername(String name, String reason, String source) {
        name = name.toLowerCase().trim();

        BanEntry entry = new BanEntry(name);

        if (reason != null) {
            entry.setReason(reason);
        }

        if (source != null) {
            entry.setSource(source);
        }

        BanList nameBans = MinecraftServer.getServer().getPlayerList().getNameBans();
        nameBans.add(entry);
    }

    public void banIP(String ip, String reason, String source) {
        ip = ip.toLowerCase().trim();
        BanEntry entry = new BanEntry(ip);

        if (reason != null) {
            entry.setReason(reason);
        }

        if (source != null) {
            entry.setSource(source);
        }

        BanList ipBans = MinecraftServer.getServer().getPlayerList().getIPBans();
        ipBans.add(entry);
    }

    public boolean isNameBanned(String name) {
        name = name.toLowerCase().trim();
        BanList nameBans = MinecraftServer.getServer().getPlayerList().getNameBans();
        nameBans.removeExpired();
        return nameBans.getEntries().containsKey(name);
    }

    public boolean isIPBanned(String ip) {
        ip = ip.toLowerCase().trim();
        BanList ipBans = MinecraftServer.getServer().getPlayerList().getIPBans();
        ipBans.removeExpired();
        return ipBans.getEntries().containsKey(ip);
    }

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
    /*
     public static String hashString(String s) {
     String f = DigestUtils.sha512Hex(s);
     return f;
     }
     */
}
