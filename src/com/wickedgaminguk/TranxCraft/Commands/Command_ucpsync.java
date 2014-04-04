package com.wickedgaminguk.TranxCraft.Commands;

import com.wickedgaminguk.TranxCraft.*;
import com.wickedgaminguk.TranxCraft.UCP.TCP_UCP;
import net.pravian.bukkitlib.command.BukkitCommand;
import net.pravian.bukkitlib.command.CommandPermissions;
import net.pravian.bukkitlib.command.SourceType;
import net.pravian.bukkitlib.util.LoggerUtils;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@CommandPermissions(source = SourceType.ANY)
public class Command_ucpsync extends BukkitCommand<TranxCraft> {

    @Override
    public boolean run(CommandSender sender, Command command, String commandLabel, String[] args) {
        if (sender instanceof Player && !(sender.hasPermission("tranxcraft.admin") || sender.isOp())) {
            return noPerms();
        }

        try {
            sender.sendMessage(ChatColor.GREEN + "Starting UCP Sync Now.");
            new TCP_UCP(plugin).runTaskAsynchronously(plugin);
            sender.sendMessage(ChatColor.GREEN + "UCP Sync Finished.");
        }
        catch (IllegalArgumentException | IllegalStateException ex) {
            sender.sendMessage("Something went wrong. Check the logs.");
            LoggerUtils.severe(plugin, ex);
        }

        return true;
    }
}
