
package com.wickedgaminguk.TranxCraft;

import java.util.logging.Logger;
import net.minecraft.server.v1_7_R1.BanEntry;
import net.minecraft.server.v1_7_R1.BanList;
import net.minecraft.server.v1_7_R1.MinecraftServer;
import org.bukkit.ChatColor;
import org.bukkit.Server;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.PluginDescriptionFile;

public class TCP_Util {
    
    public static TranxCraft plugin;
    public String pluginName;
    protected Server server;
    PluginDescriptionFile pdf = plugin.getDescription();
    public static final String Invalid_Usage = ChatColor.RED + "Invalid Usage.";
    public static final String noPerms = ChatColor.RED + "You don't have permission for this command.";
    public static final Logger logger = Logger.getLogger("Minecraft-Server");
   
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
    
}
