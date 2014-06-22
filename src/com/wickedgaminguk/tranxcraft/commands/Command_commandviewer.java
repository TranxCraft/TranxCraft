package com.wickedgaminguk.tranxcraft.commands;

import com.wickedgaminguk.tranxcraft.TCP_ModeratorList.AdminType;
import com.wickedgaminguk.tranxcraft.TranxCraft;
import net.pravian.bukkitlib.command.BukkitCommand;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

public class Command_commandviewer extends BukkitCommand<TranxCraft> {

    @Override
    public boolean run(CommandSender sender, Command command, String commandLabel, String[] args) {
        if (!(plugin.util.hasPermission("tranxcraft.moderator", sender) || plugin.util.hasPermission(AdminType.MODERATOR, sender))) {
            return noPerms();
        }

        plugin.moderatorList.toggleCommandViewer(playerSender);

        if (plugin.moderatorList.hasCommandViewerEnabled(playerSender)) {
            sender.sendMessage(ChatColor.GREEN + "Command Viewer is now: enabled");
        }
        else {
            sender.sendMessage(ChatColor.GREEN + "Command Viewer is now: disabled");
        }

        return true;
    }
}
