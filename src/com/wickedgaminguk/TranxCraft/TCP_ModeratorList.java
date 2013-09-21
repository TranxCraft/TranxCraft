
package com.wickedgaminguk.TranxCraft;

import java.util.List;

public class TCP_ModeratorList {
    
    //public static List<String> Executives = plugin.getConfig().getStringList("Executives");
    private static List<String> Executives = TranxCraft.plugin.getConfig().getStringList("Executives");
    private static List<String> leadAdmins = TranxCraft.plugin.getConfig().getStringList("Lead_Admins");
    private static List<String> Admins = TranxCraft.plugin.getConfig().getStringList("Admins");
    private static List<String> Moderators = TranxCraft.plugin.getConfig().getStringList("Moderators");
    private static List<String> Donators = TranxCraft.plugin.getConfig().getStringList("Donators");
    
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