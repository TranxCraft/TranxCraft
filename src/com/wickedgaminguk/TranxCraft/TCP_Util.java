package com.wickedgaminguk.TranxCraft;

import java.util.Arrays;
import java.util.List;
import net.minecraft.server.v1_7_R1.BanEntry;
import net.minecraft.server.v1_7_R1.BanList;
import net.minecraft.server.v1_7_R1.MinecraftServer;
import org.bukkit.ChatColor;

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
    /*
     public static String hashString(String s) {
     String f = DigestUtils.sha512Hex(s);
     return f;
     }
     */
}
