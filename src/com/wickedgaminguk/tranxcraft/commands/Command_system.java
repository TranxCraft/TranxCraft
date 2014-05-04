package com.wickedgaminguk.tranxcraft.commands;

import com.wickedgaminguk.tranxcraft.TCP_Mail.RecipientType;
import com.wickedgaminguk.tranxcraft.TCP_ModeratorList;
import com.wickedgaminguk.tranxcraft.TCP_ModeratorList.AdminType;
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
public class Command_system extends BukkitCommand<TranxCraft> {

    @Override
    public boolean run(CommandSender sender, Command command, String commandLabel, String[] args) {
        TCP_ModeratorList TCP_ModeratorList = new TCP_ModeratorList(plugin);

        if (args.length != 3) {
            return false;
        }

        Player player;
        player = getPlayer(args[2]);

        String playerName = player.getName();

        if (sender instanceof Player && !(sender.hasPermission("tranxcraft.owner"))) {
            return noPerms();
        }

        if (args[0].equalsIgnoreCase("add")) {
            if (args[1].equalsIgnoreCase("Moderator")) {
                TCP_ModeratorList.add(AdminType.MODERATOR, player);

                Bukkit.broadcastMessage(ChatColor.GREEN + playerName + " has been promoted to Moderator, congratulations!");
                Bukkit.dispatchCommand(sender, "manuadd " + playerName + " moderator Spawn");

                plugin.mail.send(RecipientType.ALL, "TranxCraft Reports - " + playerName + " has been promoted to Moderator!", "Hey there, just to let you know, " + playerName + " has been promoted to Moderator by " + sender.getName());
                plugin.twitter.tweet("Say congratulations to " + playerName + ", they have been promoted to Moderator status by " + sender.getName() + "!");
            }

            if (args[1].equalsIgnoreCase("Admin")) {
                TCP_ModeratorList.add(AdminType.ADMIN, player);

                Bukkit.broadcastMessage(ChatColor.GREEN + playerName + " has been promoted to Admin, congratulations!");
                Bukkit.dispatchCommand(sender, "manuadd " + playerName + " admin Spawn");

                plugin.mail.send(RecipientType.ALL, "TranxCraft Reports - " + playerName + " has been promoted to Admin!", "Hey there, just to let you know, " + playerName + " has been promoted to Admin by " + sender.getName());
                plugin.twitter.tweet("Say congratulations to " + playerName + ", they have been promoted to Admin status by " + sender.getName() + "!");
            }

            if (args[1].equalsIgnoreCase("LeadAdmin")) {
                TCP_ModeratorList.add(AdminType.LEADADMIN, player);

                Bukkit.broadcastMessage(ChatColor.GREEN + playerName + " has been promoted to Admin, congratulations!");
                Bukkit.dispatchCommand(sender, "manuadd " + playerName + " leadadmin Spawn");

                plugin.mail.send(RecipientType.ALL, "TranxCraft Reports - " + playerName + " has been promoted to Lead Admin!", "Hey there, just to let you know, " + playerName + " has been promoted to Lead Admin by " + sender.getName());
                plugin.twitter.tweet("Say congratulations to " + playerName + ", they have been promoted to Lead Admin status by " + sender.getName() + "!");
            }

            if (args[1].equalsIgnoreCase("Executive")) {
                TCP_ModeratorList.add(AdminType.EXECUTIVE, player);

                Bukkit.broadcastMessage(ChatColor.GREEN + playerName + " has been promoted to an Executive rank, congratulations!");
                Bukkit.dispatchCommand(sender, "manuadd " + playerName + " executive Spawn");

                plugin.mail.send(RecipientType.ALL, "TranxCraft Reports - " + playerName + " has been promoted to Executive!", "Hey there, just to let you know, " + playerName + " has been promoted to Executive by " + sender.getName());
                plugin.twitter.tweet("Say congratulations to " + playerName + ", they have been promoted to Executive status by " + sender.getName() + "!");
            }
        }

        if (args[0].equalsIgnoreCase("remove")) {
            if (args[1].equalsIgnoreCase("Moderator")) {
                TCP_ModeratorList.remove(player);

                Bukkit.broadcastMessage(ChatColor.RED + playerName + " has been removed from Moderator!");
                Bukkit.dispatchCommand(sender, "manuadd " + playerName + " member Spawn");

                plugin.mail.send(RecipientType.ALL, "TranxCraft Reports - " + playerName + " has been removed from Moderator", "Hey there, just to let you know, " + playerName + " has been removed from Moderator by " + sender.getName());
                plugin.twitter.tweet(playerName + " has been removed from Moderator by " + sender.getName() + "!");
            }

            if (args[1].equalsIgnoreCase("Admin")) {
                TCP_ModeratorList.remove(player);

                Bukkit.broadcastMessage(ChatColor.RED + playerName + " has been removed from Admin!");
                Bukkit.dispatchCommand(sender, "manuadd " + playerName + " member Spawn");

                plugin.mail.send(RecipientType.ALL, "TranxCraft Reports - " + playerName + " has been removed from Admin", "Hey there, just to let you know, " + playerName + " has been removed from Admin by " + sender.getName());
                plugin.twitter.tweet(playerName + " has been removed from Admin by " + sender.getName() + "!");
            }

            if (args[1].equalsIgnoreCase("LeadAdmin")) {
                TCP_ModeratorList.remove(player);

                Bukkit.broadcastMessage(ChatColor.RED + playerName + " has been removed from being a lead Admin!");
                Bukkit.dispatchCommand(sender, "manuadd " + playerName + " member Spawn");

                plugin.mail.send(RecipientType.ALL, "TranxCraft Reports - " + playerName + " has been removed from Lead Admin", "Hey there, just to let you know, " + playerName + " has been removed from Lead Admin by " + sender.getName());
                plugin.twitter.tweet(playerName + " has been removed from Lead Admin by " + sender.getName() + "!");
            }

            if (args[1].equalsIgnoreCase("Executive")) {
                TCP_ModeratorList.remove(player);

                Bukkit.broadcastMessage(ChatColor.RED + playerName + " has been removed from being an Executive!");
                Bukkit.dispatchCommand(sender, "manuadd " + playerName + " member Spawn");

                plugin.mail.send(RecipientType.ALL, "TranxCraft Reports - " + playerName + " has been removed from Executive", "Hey there, just to let you know, " + playerName + " has been removed from Executive by " + sender.getName());
                plugin.twitter.tweet(playerName + " has been removed from Executive by " + sender.getName() + "!");
            }
        }

        return true;
    }
}
