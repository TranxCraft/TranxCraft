package com.wickedgaminguk.tranxcraft.api;

import com.wickedgaminguk.tranxcraft.TCP_Util;
import com.wickedgaminguk.tranxcraft.TranxCraft;
import org.bukkit.Bukkit;

public class Authentication {
    
    public static String[] authenticate(String username, String password) {
        TCP_Util util = new TCP_Util((TranxCraft) Bukkit.getServer().getPluginManager().getPlugin("TranxCraft"));
        
        util.authenticate(username, password);
        
        String[] details = null;
        details[0] = username;
        details[1] = util.playerToUUID(username).toString();
        
        return details;
    }
}
