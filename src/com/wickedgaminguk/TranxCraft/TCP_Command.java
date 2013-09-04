
package com.wickedgaminguk.TranxCraft;

import java.util.List;
import org.bukkit.Server;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;

public abstract class TCP_Command {
    protected TranxCraft plugin;
    protected Server server;
    PluginManager pm;
    
    public TCP_Command() {
    }
    
    //Credits to Steven Lawson/Madgeek & Jerom Van Der Sar/DarthSalamon for various methods.
    public Player getPlayer(final String partialname) throws PlayerNotFoundException {
        List<Player> matches = server.matchPlayer(partialname);
        if (matches.isEmpty()) {
            for (Player player : server.getOnlinePlayers()) {
                if (player.getDisplayName().toLowerCase().contains(partialname.toLowerCase())) {
                    return player;
                }
            }
            throw new PlayerNotFoundException(partialname);
        }
        else {
            return matches.get(0);
        }
    }
}
