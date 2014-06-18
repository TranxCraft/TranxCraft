package com.wickedgaminguk.tranxcraft.commands;

import com.wickedgaminguk.tranxcraft.TCP_ModeratorList.AdminType;
import com.wickedgaminguk.tranxcraft.TranxCraft;
import net.pravian.bukkitlib.command.BukkitCommand;
import net.pravian.bukkitlib.command.CommandPermissions;
import net.pravian.bukkitlib.command.SourceType;
import net.pravian.bukkitlib.util.LoggerUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;

@CommandPermissions(source = SourceType.ANY)
public class Command_plm extends BukkitCommand<TranxCraft> {

    @Override
    public boolean run(CommandSender sender, Command command, String commandLabel, String[] args) {
        PluginManager pm = Bukkit.getPluginManager();

        if (!(plugin.util.hasPermission("tranxcraft.exec", sender) || plugin.util.hasPermission(AdminType.EXECUTIVE, sender))) {
            return noPerms();
        }

        if (!(args.length == 1 || args.length == 2)) {
            return false;
        }

        if (args[0].equalsIgnoreCase("reload")) {
            if (args.length == 1) {
                Bukkit.broadcastMessage("[TranxCraft]" + ChatColor.RED + " Server Reloading.");
                plugin.pluginHandler.reloadServer();
                LoggerUtils.info(plugin, "Server Reloaded.");
                Bukkit.broadcastMessage("[TranxCraft]" + ChatColor.GREEN + " Server Reloaded.");
                return true;
            }

            if (args.length != 2) {
                sender.sendMessage(plugin.util.invalidUsage);
                return true;
            }

            if (args.length == 2) {
                final String searchPluginName = args[1].toLowerCase().trim();

                Plugin targetPlugin = null;

                for (Plugin serverPlugin : pm.getPlugins()) {
                    if (searchPluginName.equalsIgnoreCase(serverPlugin.getName().toLowerCase().trim())) {
                        targetPlugin = serverPlugin;
                        break;
                    }
                }

                if (targetPlugin == null) {
                    sender.sendMessage(ChatColor.RED + "Can't find plugin: " + searchPluginName);
                    return true;
                }
                else if (targetPlugin == plugin) {
                    sender.sendMessage(ChatColor.RED + "You can't use plugin manager for TranxCraft");
                    return true;
                }

                if (!pm.isPluginEnabled(targetPlugin)) {
                    sender.sendMessage(ChatColor.RED + "[TranxCraft] Plugin is either invalid or already disabled.");
                    return true;
                }

                plugin.pluginHandler.reloadPlugin(targetPlugin);
                sender.sendMessage(ChatColor.GREEN + "[TranxCraft] Plugin %a reloaded.".replaceAll("%a", targetPlugin.getName()));
                LoggerUtils.info(plugin, sender.getName() + " reloaded " + targetPlugin.getName() + " at " + plugin.time.getLongDate());
                return true;
            }
        }

        if (args[0].equalsIgnoreCase("enable")) {
            if (args.length != 2) {
                sender.sendMessage(plugin.util.invalidUsage);
                return false;
            }

            final String searchPluginName = args[1].toLowerCase().trim();

            Plugin targetPlugin = null;

            for (Plugin serverPlugin : pm.getPlugins()) {
                if (searchPluginName.equalsIgnoreCase(serverPlugin.getName().toLowerCase().trim())) {
                    targetPlugin = serverPlugin;
                    break;
                }
            }

            if (targetPlugin == null) {
                sender.sendMessage(ChatColor.RED + "Can't find plugin: " + searchPluginName);
                return true;
            }
            else if (targetPlugin == plugin) {
                sender.sendMessage(ChatColor.RED + "You can't use plugin manager for TranxCraft");
                return true;
            }

            if (pm.isPluginEnabled(targetPlugin)) {
                sender.sendMessage(ChatColor.RED + "[TranxCraft] Plugin Already Enabled.");
                return true;
            }
            else {
                plugin.pluginHandler.enablePlugin(targetPlugin);
                sender.sendMessage(ChatColor.GREEN + "[TranxCraft] Plugin " + targetPlugin + " is enabled.");
                return true;
            }
        }

        if (args[0].equalsIgnoreCase("disable")) {
            if (args.length != 2) {
                sender.sendMessage(plugin.util.invalidUsage);
                return true;
            }

            final String searchPluginName = args[1].toLowerCase().trim();

            Plugin targetPlugin = null;

            for (Plugin serverPlugin : pm.getPlugins()) {
                if (searchPluginName.equalsIgnoreCase(serverPlugin.getName().toLowerCase().trim())) {
                    targetPlugin = serverPlugin;
                    break;
                }
            }

            if (targetPlugin == null) {
                sender.sendMessage(ChatColor.RED + "Can't find plugin: " + searchPluginName);
                return true;
            }
            else if (targetPlugin == plugin) {
                sender.sendMessage(ChatColor.RED + "You can't use plugin manager for TranxCraft");
                return true;
            }

            if (!pm.isPluginEnabled(targetPlugin)) {
                sender.sendMessage(ChatColor.RED + "[TranxCraft] Plugin Already Disabled.");
                return true;
            }
            else {
                plugin.pluginHandler.disablePlugin(targetPlugin);
                sender.sendMessage(ChatColor.RED + "[TranxCraft] Plugin " + targetPlugin + " is disabled.");
                return true;
            }
        }

        return true;
    }
}
