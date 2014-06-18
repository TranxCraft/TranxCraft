package com.wickedgaminguk.tranxcraft.commands;

import com.wickedgaminguk.tranxcraft.TCP_ModeratorList.AdminType;
import com.wickedgaminguk.tranxcraft.TranxCraft;
import java.io.IOException;
import net.pravian.bukkitlib.command.BukkitCommand;
import net.pravian.bukkitlib.command.CommandPermissions;
import net.pravian.bukkitlib.command.SourceType;
import org.apache.commons.lang.StringUtils;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@CommandPermissions(source = SourceType.ANY)
public class Command_tranxcraft extends BukkitCommand<TranxCraft> {

    @Override
    public boolean run(CommandSender sender, Command command, String commandLabel, String[] args) {
        if (args[0].equalsIgnoreCase("update")) {
            if (!(plugin.util.hasPermission("tranxcraft.exec", AdminType.EXECUTIVE, (Player) sender))) {
                return noPerms();
            }

            try {
                plugin.util.update();
            }
            catch (IOException ex) {
                sender.sendMessage(ChatColor.RED + "Updating the plugins on TranxCraft failed - check the logs.");
                plugin.util.debug(ex);
            }
        }

        if (args[0].equalsIgnoreCase("debug")) {
            if (!(plugin.util.hasPermission("tranxcraft.exec", AdminType.EXECUTIVE, (Player) sender))) {
                return noPerms();
            }

            if (plugin.logger.getDebugMode() == false) {
                plugin.logger.setDebugMode(true);
            }
            else {
                plugin.logger.setDebugMode(false);
            }

            plugin.util.debug("Debug mode is now: " + plugin.logger.getDebugMode() + ". Change made by " + sender.getName());
            sender.sendMessage(ChatColor.GREEN + "Debug Mode: " + plugin.logger.getDebugMode());
        }
        else {
            sender.sendMessage(ChatColor.GREEN + "-- Basic TranxCraft Information --");
            sender.sendMessage(ChatColor.AQUA + "Owner: WickedGamingUK");
            sender.sendMessage(ChatColor.AQUA + "Lead Developer: WickedGamingUK");
            sender.sendMessage(ChatColor.AQUA + "Plugin Version: " + plugin.getVersion());
            sender.sendMessage(ChatColor.AQUA + "Credits: " + StringUtils.join(plugin.util.getCredits(), ", "));
            sender.sendMessage(ChatColor.AQUA + "Website: http://www.tranxcraft.com/");
            sender.sendMessage(ChatColor.AQUA + "Forums: http://www.tranxcraft.com/forums");
            sender.sendMessage(ChatColor.GREEN + "------------------------");
        }

        return true;
    }
}
