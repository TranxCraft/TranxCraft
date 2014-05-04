package com.wickedgaminguk.tranxcraft.commands;

import net.pravian.bukkitlib.command.BukkitCommand;
import net.pravian.bukkitlib.command.CommandPermissions;
import net.pravian.bukkitlib.command.SourceType;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

@CommandPermissions(source = SourceType.ANY)
public class Command_acceptrules extends BukkitCommand {

    @Override
    public boolean run(CommandSender sender, Command command, String commandLabel, String[] args) {
        if (sender.hasPermission("tranxcraft.member")) {
            sender.sendMessage(ChatColor.RED + "You've already accepted the rules!");
            return true;
        }

        sender.sendMessage(ChatColor.GREEN + "Congratulations, you've accepted the rules and you will now be promoted to member!");
        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "manuadd " + sender.getName() + " member Spawn");

        return true;
    }
}
