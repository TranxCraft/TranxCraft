package com.wickedgaminguk.tranxcraft;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.logging.Level;
import org.bukkit.plugin.Plugin;

public class TCP_Logger {

    private final TranxCraft plugin;

    TCP_Logger(TranxCraft plugin) {
        this.plugin = plugin;
    }

    private void write(String message) {
        try {
            File file = new File(plugin.getDataFolder() + "/log.log");

            if (!file.exists()) {
                file.createNewFile();
            }

            FileWriter fw = new FileWriter(file.getAbsoluteFile(), true);
            try (BufferedWriter bw = new BufferedWriter(fw)) {
                if (file.length() != 0) {
                    bw.newLine();
                }
                bw.write(message);
            }
        }
        catch (IOException ex) {

        }
    }

    public void log(Level level, String message) {
        write("[" + plugin.time.getLongDate() + "] " + "[" + level.getName() + "] " + message);
    }

    public void log(Level level, Plugin plugin, String message) {
        write("[" + this.plugin.time.getLongDate() + "] " + "[" + plugin.getName() + "] [" + level.getName() + "] " + message);
    }

    public void info(Plugin plugin, String message) {
        log(Level.INFO, plugin, message);
    }

    public void info(String message) {
        log(Level.INFO, message);
    }

    public void warning(Plugin plugin, String message) {
        log(Level.WARNING, plugin, message);
    }

    public void warning(String message) {
        log(Level.WARNING, message);
    }

    public void severe(Plugin plugin, String message) {
        log(Level.SEVERE, plugin, message);
    }

    public void severe(String message) {
        log(Level.SEVERE, message);
    }
}
