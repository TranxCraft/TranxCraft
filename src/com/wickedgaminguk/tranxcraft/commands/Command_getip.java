package com.wickedgaminguk.tranxcraft.commands;

import com.wickedgaminguk.tranxcraft.TCP_ModeratorList.AdminType;
import com.wickedgaminguk.tranxcraft.TranxCraft;
import java.util.UUID;
import net.pravian.bukkitlib.command.BukkitCommand;
import net.pravian.bukkitlib.command.CommandPermissions;
import net.pravian.bukkitlib.command.SourceType;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

@CommandPermissions(source = SourceType.ANY)
public class Command_getip extends BukkitCommand<TranxCraft> {

    @Override
    public boolean run(CommandSender sender, Command command, String commandLabel, String[] args) {
        if (!(plugin.util.hasPermission("tranxcraft.moderator", sender) || plugin.util.hasPermission(AdminType.MODERATOR, sender))) {
            return noPerms();
        }

        if (args.length != 1) {
            return false;
        }

        String player = args[0];
        UUID uuid = plugin.util.playerToUuid(player);

        sender.sendMessage(ChatColor.GOLD + player + ChatColor.GREEN + "'s IP is: " + ChatColor.GOLD + plugin.util.getIp(uuid));

        return true;
    }
}
