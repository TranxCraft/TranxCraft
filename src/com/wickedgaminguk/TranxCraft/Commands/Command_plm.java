package com.wickedgaminguk.TranxCraft.Commands;

import com.wickedgaminguk.TranxCraft.TCP_PluginHandler;
import com.wickedgaminguk.TranxCraft.TCP_Time;
import com.wickedgaminguk.TranxCraft.TCP_Util;
import net.pravian.bukkitlib.command.BukkitCommand;
import net.pravian.bukkitlib.command.CommandPermissions;
import net.pravian.bukkitlib.command.SourceType;
import net.pravian.bukkitlib.util.LoggerUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

@CommandPermissions(source = SourceType.ANY)
public class Command_plm extends BukkitCommand {

    @Override
    public boolean run(CommandSender sender, Command command, String commandLabel, String[] args) {
        TCP_PluginHandler TCP_PluginHandler = new TCP_PluginHandler();

        if (sender instanceof Player && !(sender.hasPermission("tranxcraft.exec") || sender.isOp())) {
            return noPerms();
        }

        if (!(args.length == 1 || args.length == 2)) {
            return false;
        }

        if (args[0].equalsIgnoreCase("reload")) {
            if (args.length == 1) {
                Bukkit.broadcastMessage("[TranxCraft]" + ChatColor.RED + " Server Reloading.");
                TCP_PluginHandler.reloadServer();
                LoggerUtils.info(plugin, "Server Reloaded.");
                Bukkit.broadcastMessage("[TranxCraft]" + ChatColor.GREEN + " Server Reloaded.");
                return true;
            }

            if (args.length != 2) {
                sender.sendMessage(TCP_Util.invalidUsage);
                return true;
            }

            if (args.length == 2) {
                if (!Bukkit.getPluginManager().isPluginEnabled(args[1])) {
                    sender.sendMessage(ChatColor.RED + "[TranxCraft] Plugin is either invalid or already disabled.");
                }

                Plugin tPlugin = Bukkit.getPluginManager().getPlugin(args[1]);
                TCP_PluginHandler.reloadPlugin(tPlugin);
                sender.sendMessage(ChatColor.GREEN + "[TranxCraft] Plugin %a reloaded.".replaceAll("%a", tPlugin.getName()));
                LoggerUtils.info(plugin, sender.getName() + " reloaded " + tPlugin.getName() + " at " + TCP_Time.getLongDate());
                return true;
            }
        }

        if (args[0].equalsIgnoreCase("enable")) {
            if (args.length != 2) {
                sender.sendMessage(TCP_Util.invalidUsage);
                return false;
            }

            if (Bukkit.getPluginManager().isPluginEnabled(args[1])) {
                sender.sendMessage(ChatColor.RED + "[TranxCraft] Plugin Already Enabled.");
                return true;
            }
            else {
                Plugin tPlugin = Bukkit.getPluginManager().getPlugin(args[1]);
                TCP_PluginHandler.enablePlugin(tPlugin);
                sender.sendMessage(ChatColor.GREEN + "[TranxCraft] Plugin " + tPlugin + " is enabled.");
                return true;
            }
        }

        if (args[0].equalsIgnoreCase("disable")) {
            if (args.length != 2) {
                sender.sendMessage(TCP_Util.invalidUsage);
                return true;
            }

            if (!Bukkit.getPluginManager().isPluginEnabled(args[1])) {
                sender.sendMessage(ChatColor.RED + "[TranxCraft] Plugin Already Disabled.");
                return true;
            }
            else {
                Plugin tPlugin = Bukkit.getPluginManager().getPlugin(args[1]);
                TCP_PluginHandler.disablePlugin(tPlugin);
                sender.sendMessage(ChatColor.RED + "[TranxCraft] Plugin " + tPlugin + " is disabled.");
                return true;
            }
        }

        return true;
    }
}
