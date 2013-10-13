
package com.wickedgaminguk.TranxCraft.Commands;

import com.wickedgaminguk.TranxCraft.*;
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
    public Player getPlayer(String name) {
        for(Player player : plugin.getServer().getOnlinePlayers()) {
          if(name.equalsIgnoreCase(player.getName())) {
              return player;
          }
        }
        return null;
    }
}
