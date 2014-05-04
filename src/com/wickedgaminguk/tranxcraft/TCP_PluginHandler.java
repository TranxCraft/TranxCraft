package com.wickedgaminguk.tranxcraft;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

public class TCP_PluginHandler {

    public void reloadPlugin(Plugin plugin) {
        Bukkit.getPluginManager().disablePlugin(plugin);
        Bukkit.getPluginManager().enablePlugin(plugin);
    }

    public void enablePlugin(Plugin plugin) {
        Bukkit.getPluginManager().enablePlugin(plugin);
    }

    public void disablePlugin(Plugin plugin) {
        Bukkit.getPluginManager().disablePlugin(plugin);
    }

    public void reloadServer() {
        Plugin[] plugins = Bukkit.getPluginManager().getPlugins();
        for (Plugin plugin : plugins) {
            reloadPlugin(plugin);
        }
    }
}
