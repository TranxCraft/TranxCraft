package com.wickedgaminguk.TranxCraft.Commands;

import com.wickedgaminguk.TranxCraft.TCP_ModeratorList;
import com.wickedgaminguk.TranxCraft.TCP_Util;
import com.wickedgaminguk.TranxCraft.TranxCraft;
import net.pravian.bukkitlib.command.BukkitCommand;
import net.pravian.bukkitlib.command.CommandPermissions;
import net.pravian.bukkitlib.command.SourceType;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@CommandPermissions(source = SourceType.ANY)
public class Command_donator extends BukkitCommand<TranxCraft> {

    @Override
    public boolean run(CommandSender sender, Command command, String commandLabel, String[] args) {
        TCP_ModeratorList TCP_ModeratorList = new TCP_ModeratorList(plugin);

        if (args.length > 4) {
            sender.sendMessage(TCP_Util.Invalid_Usage);
            return false;
        }

        Player player;
        player = getPlayer(args[0]);

        String Player = player.getName();

        if (args[0].equalsIgnoreCase("add")) {
            if (!(sender instanceof Player) || !(sender.getName().equalsIgnoreCase("WickedGamingUK"))) {
                return noPerms();
            }

            TCP_ModeratorList.getDonators().add(Player);
            plugin.config.set("Donators", TCP_ModeratorList.getDonators());
            plugin.saveConfig();

            Bukkit.broadcastMessage(ChatColor.GREEN + player.getName() + " has bought a donator rank, congratulations!");
            return true;
        }

        if (args[0].equalsIgnoreCase("remove")) {
            if (!(sender instanceof Player) || !(sender.getName().equalsIgnoreCase("WickedGamingUK"))) {
                return noPerms();
            }

            TCP_ModeratorList.getDonators().remove(Player);

            plugin.config.set("Donators", TCP_ModeratorList.getDonators());
            plugin.saveConfig();

            Bukkit.broadcastMessage(ChatColor.RED + player.getName() + "'s donator rank has expired, or (s)he's been abusing, how unfortunate!");
            return true;
        }

        return true;
    }
}
