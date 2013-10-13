
package com.wickedgaminguk.TranxCraft;

import com.wickedgaminguk.TranxCraft.Commands.*;
import java.util.logging.Logger;
import net.minecraft.server.v1_6_R3.BanEntry;
import net.minecraft.server.v1_6_R3.BanList;
import net.minecraft.server.v1_6_R3.MinecraftServer;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Server;
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
    
    
   
   public static void init() {
       Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "mv unload Spawn_nether");
       Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "mv unload Spawn_the_end");
       TCP_Log.info("[TranxCraft] Hopefully the Nether and End have unloaded!");
       
       //Register Command Executors.
       try { 
           plugin.getCommand("mong").setExecutor(new Command_mong(plugin));
           plugin.getCommand("tranxcraft").setExecutor(new Command_tranxcraft(plugin));
           plugin.getCommand("donator").setExecutor(new Command_donator(plugin));
           plugin.getCommand("admininfo").setExecutor(new Command_admininfo(plugin));
           plugin.getCommand("gtfo").setExecutor(new Command_gtfo(plugin));
           plugin.getCommand("fuckoff").setExecutor(new Command_fuckoff(plugin)); 
           plugin.getCommand("cake").setExecutor(new Command_cake(plugin));
           plugin.getCommand("grandslam").setExecutor(new Command_grandslam(plugin));
           TCP_Log.info("[" + TranxCraft.getPluginName() + "]" + "Commands Loaded.");
       }
       catch(Exception ex) {
           TCP_Log.warning("[" + TranxCraft.getPluginName() + "]" + " Commands Failed To Load!");
           TCP_Log.info("Error: " + ex);
       }
   }
}
