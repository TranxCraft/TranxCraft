
package com.wickedgaminguk.TranxCraft;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

public class TCP_PluginHandler {
    
    public static void reloadPlugin(Plugin plugin) {
        Bukkit.getPluginManager().disablePlugin(plugin);
        Bukkit.getPluginManager().enablePlugin(plugin);
    }
    public static void enablePlugin(Plugin plugin) {
        Bukkit.getPluginManager().enablePlugin(plugin);
    }
    public static void disablePlugin(Plugin plugin) {
        Bukkit.getPluginManager().disablePlugin(plugin);
    }
    public static void reloadServer() {
        Plugin[] plugins = Bukkit.getPluginManager().getPlugins();
            for (int x = 0; x < plugins.length; x++) {
                reloadPlugin(plugins[x]);
            }
    }
}
