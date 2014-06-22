package com.wickedgaminguk.tranxcraft;

import net.pravian.bukkitlib.util.ChatUtils;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;

public class TCP_Scheduler extends BukkitRunnable {

    private final TranxCraft plugin;

    public TCP_Scheduler(TranxCraft instance) {
        this.plugin = instance;
    }

    @Override
    public void run() {
        Bukkit.broadcastMessage(ChatUtils.colorize(plugin.config.getString("prefix") + plugin.config.getString("broadcast")));
    }
}
