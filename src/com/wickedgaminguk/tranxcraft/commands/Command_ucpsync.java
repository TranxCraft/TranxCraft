package com.wickedgaminguk.tranxcraft.commands;

import com.wickedgaminguk.tranxcraft.TCP_ModeratorList.AdminType;
import com.wickedgaminguk.tranxcraft.TCP_UCP;
import com.wickedgaminguk.tranxcraft.TranxCraft;
import net.pravian.bukkitlib.command.BukkitCommand;
import net.pravian.bukkitlib.command.CommandPermissions;
import net.pravian.bukkitlib.command.SourceType;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

@CommandPermissions(source = SourceType.ANY)
public class Command_ucpsync extends BukkitCommand<TranxCraft> {

    @Override
    public boolean run(CommandSender sender, Command command, String commandLabel, String[] args) {
        if (!(plugin.util.hasPermission("tranxcraft.admin", sender) || plugin.util.hasPermission(AdminType.ADMIN, sender))) {
            return noPerms();
        }

        try {
            sender.sendMessage(ChatColor.GREEN + "Starting UCP Sync Now.");
            new TCP_UCP(plugin).runTaskAsynchronously(plugin);
            sender.sendMessage(ChatColor.GREEN + "UCP Sync Finished.");
        }
        catch (IllegalArgumentException | IllegalStateException ex) {
            sender.sendMessage("Something went wrong. Check the logs.");
            plugin.util.debug(ex);
        }

        return true;
    }
}
