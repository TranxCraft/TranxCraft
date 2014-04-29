package com.wickedgaminguk.TranxCraft.Commands;

import com.wickedgaminguk.TranxCraft.TCP_DonatorList;
import com.wickedgaminguk.TranxCraft.TCP_DonatorList.DonatorType;
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
        TCP_DonatorList TCP_DonatorList = new TCP_DonatorList(plugin);

        if (args.length != 3) {
            return false;
        }

        Player player;

        if (sender instanceof Player && !(sender.hasPermission("tranxcraft.owner"))) {
            return noPerms();
        }

        if (args[0].equalsIgnoreCase("add")) {
            player = getPlayer(args[2]);
            String playerName = player.getName();

            if (args[1].equalsIgnoreCase("one")) {
                TCP_DonatorList.add(DonatorType.ONE, player);
                Bukkit.broadcastMessage(ChatColor.GREEN + player.getName() + " has bought a level one donator rank, congratulations!");

                plugin.twitter.tweet("Say congratulations to " + playerName + ", they have just bought a Level One Donator Rank!");
            }

            if (args[1].equalsIgnoreCase("two")) {
                TCP_DonatorList.add(DonatorType.TWO, player);
                Bukkit.broadcastMessage(ChatColor.GREEN + player.getName() + " has bought a level two donator rank, congratulations!");

                plugin.twitter.tweet("Say congratulations to " + playerName + ", they have just bought a Level two Donator Rank!");
            }

            if (args[1].equalsIgnoreCase("three")) {
                TCP_DonatorList.add(DonatorType.THREE, player);
                Bukkit.broadcastMessage(ChatColor.GREEN + player.getName() + " has bought a level three donator rank, congratulations!");

                plugin.twitter.tweet("Say congratulations to " + playerName + ", they have just bought a Level three Donator Rank!");
            }
        }

        if (args[0].equalsIgnoreCase("remove")) {
            player = getPlayer(args[1]);
            String playerName = player.getName();
            TCP_DonatorList.remove(player);

            Bukkit.broadcastMessage(ChatColor.RED + playerName + " has been striped of their donator privileges!");
        }

        return true;
    }
}
