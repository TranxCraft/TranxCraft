
package com.wickedgaminguk.TranxCraft;

import java.util.List;
import java.util.logging.Logger;
import org.bukkit.ChatColor;

public class TCP_ModeratorList {
    
    public static TranxCraft plugin;
    public static final String Invalid_Usage = ChatColor.RED + "Invalid Usage.";
    public static final String noPerms = ChatColor.RED + "You don't have permission for this command.";
    public static final Logger logger = Logger.getLogger("Minecraft-Server");
    public static List<String> Executives = plugin.getConfig().getStringList("Executives");
    public static List<String> leadAdmins = plugin.getConfig().getStringList("Lead_Admins");
    public static List<String> Admins = plugin.getConfig().getStringList("Admins");
    public static List<String> Moderators = plugin.getConfig().getStringList("Moderators");
    public static List<String> Donators = plugin.getConfig().getStringList("Donators");
    
    public static List<String> getModerators() {
        return Moderators;
    }
    
    public static List<String> getAdmins() {
        return Admins;
    }
    public static List<String> getleadAdmins() {
        return leadAdmins;
    }
    
    public static List<String> getExecutives() {
        return Executives;
    }
    
    public static List<String> getDonators() {
        return Donators;
    }
}