package com.wickedgaminguk.tranxcraft.commands;

import com.wickedgaminguk.tranxcraft.TCP_ModeratorList.AdminType;
import com.wickedgaminguk.tranxcraft.TCP_Util;
import com.wickedgaminguk.tranxcraft.TranxCraft;
import java.util.UUID;
import net.pravian.bukkitlib.command.BukkitCommand;
import net.pravian.bukkitlib.command.CommandPermissions;
import net.pravian.bukkitlib.command.SourceType;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

@CommandPermissions(source = SourceType.ANY)
public class Command_getname extends BukkitCommand<TranxCraft> {

    @Override
    public boolean run(CommandSender sender, Command command, String commandLabel, String[] args) {        
        TCP_Util TCP_Util = new TCP_Util(plugin);

        if (!(TCP_Util.hasPermission("tranxcraft.moderator", sender) || TCP_Util.hasPermission(AdminType.MODERATOR, sender))) {
            return noPerms();
        }

        if (args.length != 1) {
            return false;
        }

        UUID uuid = UUID.fromString(args[0]);
        
        if (uuid == null) {
            sender.sendMessage(ChatColor.RED + "This player either isn't online, or doesn't exist.");
            return true;
        }
        
        sender.sendMessage(ChatColor.GREEN + "The Current Player Name of " + ChatColor.GOLD + uuid.toString() + ChatColor.GREEN + " is: " + ChatColor.GOLD + TCP_Util.UUIDToPlayer(uuid));
        
        return true;
    }
}
