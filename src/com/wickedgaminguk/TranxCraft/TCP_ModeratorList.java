
package com.wickedgaminguk.TranxCraft;

import java.util.List;

public class TCP_ModeratorList {
    
    private static List<String> Executives = TCP_Util.getConfigFile().getStringList("Executives");
    private static List<String> leadAdmins = TCP_Util.getConfigFile().getStringList("Lead_Admins");
    private static List<String> Admins = TCP_Util.getConfigFile().getStringList("Admins");
    private static List<String> Moderators = TCP_Util.getConfigFile().getStringList("Moderators");
    private static List<String> Donatorsx = TCP_Util.getConfigFile().getStringList("Donators");
    
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