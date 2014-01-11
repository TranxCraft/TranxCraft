
package com.wickedgaminguk.TranxCraft;

import java.text.SimpleDateFormat;
import java.util.Date;
import net.minecraft.server.v1_7_R1.BanEntry;
import net.minecraft.server.v1_7_R1.BanList;
import net.minecraft.server.v1_7_R1.MinecraftServer;
import org.bukkit.ChatColor;
import org.bukkit.Server;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

public class TCP_Util extends TranxCraft {
    
    protected Server server;
    public static final String Invalid_Usage = ChatColor.RED + "Invalid Usage.";
    public static final String noPerms = ChatColor.RED + "You don't have permission for this command.";
    public static final int second = 1;
    public static final int minute = second * 60;
    public static final int hour = minute * 60;
    public static final int day = hour * 24;
    public static final int week = day * 7;
    public static final int month = week * 4;
    public static final int year = month * 12;
   
   //Credits to Steven Lawson/Madgeek & Jerom Van Der Sar/DarthSalamon for various methods.
    public static void banUsername(String name, String reason, String source) {
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
    
    public static void banIP(String ip, String reason, String source) {
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
    
    public static boolean isNameBanned(String name) {
        name = name.toLowerCase().trim();
        BanList nameBans = MinecraftServer.getServer().getPlayerList().getNameBans();
        nameBans.removeExpired();
        return nameBans.getEntries().containsKey(name);
    }

    public static boolean isIPBanned(String ip) {
        ip = ip.toLowerCase().trim();
        BanList ipBans = MinecraftServer.getServer().getPlayerList().getIPBans();
        ipBans.removeExpired();
        return ipBans.getEntries().containsKey(ip);
    }
    
    public static FileConfiguration getConfigFile() {
        return TranxCraft.plugin.getConfig();
    }
    
    public static String getDate() {    
        SimpleDateFormat sdf = new SimpleDateFormat("hh:mm:ss");
        String date = sdf.format(new Date());
        return date;
    }
    /*
    public static void rollbackPlayer(Player player, int time) {
        
        String p = player.getName();
        CoreProtectAPI CoreProtect = getCoreProtect();
        
        if (CoreProtect!=null) { //Ensure we have access to the API
            CoreProtect.performRollback(p, time, 0, null, null, null);
        }
    }*/
    
    public static String getPrimaryGroup(Player player) {
        String permission = plugin.permission.getPrimaryGroup(player);  
        return permission;
    }
}
