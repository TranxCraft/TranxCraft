package com.wickedgaminguk.TranxCraft;

import net.pravian.bukkitlib.util.ChatUtils;
import org.bukkit.Server;
import org.bukkit.scheduler.BukkitRunnable;

public class TCP_Scheduler extends BukkitRunnable {

    private final TranxCraft plugin;
    private final Server server;

    public TCP_Scheduler(TranxCraft instance) {
        this.plugin = instance;
        this.server = plugin.getServer();
    }

    @Override
    public void run() {
        server.broadcastMessage(ChatUtils.colorize(plugin.getConfig().getString("prefix") + plugin.getConfig().getString("broadcast")));
    }
}
