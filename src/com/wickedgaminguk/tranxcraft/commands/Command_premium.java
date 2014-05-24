package com.wickedgaminguk.tranxcraft.commands;

import com.wickedgaminguk.tranxcraft.TCP_PremiumList;
import com.wickedgaminguk.tranxcraft.TCP_PremiumList.PremiumType;
import com.wickedgaminguk.tranxcraft.TranxCraft;
import net.pravian.bukkitlib.command.BukkitCommand;
import net.pravian.bukkitlib.command.CommandPermissions;
import net.pravian.bukkitlib.command.SourceType;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@CommandPermissions(source = SourceType.ANY)
public class Command_premium extends BukkitCommand<TranxCraft> {

    @Override
    public boolean run(CommandSender sender, Command command, String commandLabel, String[] args) {
        TCP_PremiumList TCP_PremiumList = new TCP_PremiumList(plugin);

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
                TCP_PremiumList.add(PremiumType.ONE, player);
                Bukkit.broadcastMessage(ChatColor.GREEN + player.getName() + " has bought a level one premium rank, congratulations!");

                plugin.twitter.tweet("Say congratulations to " + playerName + ", they have just bought a Level One Premium Rank!");
            }

            if (args[1].equalsIgnoreCase("two")) {
                TCP_PremiumList.add(PremiumType.TWO, player);
                Bukkit.broadcastMessage(ChatColor.GREEN + player.getName() + " has bought a level two premium rank, congratulations!");

                plugin.twitter.tweet("Say congratulations to " + playerName + ", they have just bought a Level two Premium Rank!");
            }

            if (args[1].equalsIgnoreCase("three")) {
                TCP_PremiumList.add(PremiumType.THREE, player);
                Bukkit.broadcastMessage(ChatColor.GREEN + player.getName() + " has bought a level three premium rank, congratulations!");

                plugin.twitter.tweet("Say congratulations to " + playerName + ", they have just bought a Level three Premium Rank!");
            }
        }

        if (args[0].equalsIgnoreCase("remove")) {
            player = getPlayer(args[1]);
            String playerName = player.getName();
            TCP_PremiumList.remove(player);

            Bukkit.broadcastMessage(ChatColor.RED + playerName + " has been striped of their premium privileges!");
        }

        return true;
    }
}
