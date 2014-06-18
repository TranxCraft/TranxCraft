package com.wickedgaminguk.tranxcraft.commands;

import com.wickedgaminguk.tranxcraft.TCP_Mail.RecipientType;
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
        if (sender instanceof Player && !(sender.hasPermission("tranxcraft.owner"))) {
            return noPerms();
        }

        if (args.length != 3) {
            return false;
        }

        Player player = getPlayer(args[2]);        
        
        if (player == null) {
            sender.sendMessage(ChatColor.RED + "This player either isn't online, or doesn't exist.");
            return true;
        }
        
        String playerName = player.getName();

        if (args[0].equalsIgnoreCase("add")) {
            if (args[1].equalsIgnoreCase("moderator")) {
                plugin.moderatorList.add(AdminType.MODERATOR, player);                
                plugin.groupManager.setGroup(player, "moderator");

                Bukkit.broadcastMessage(ChatColor.GREEN + playerName + " has been promoted to Moderator, congratulations!");

                plugin.mail.send(RecipientType.ALL, "TranxCraft Reports - " + playerName + " has been promoted to Moderator!", "Hey there, just to let you know, " + playerName + " has been promoted to Moderator by " + sender.getName());
                plugin.twitter.tweet("Say congratulations to " + playerName + ", they have been promoted to Moderator status by " + sender.getName() + "!");
            }

            if (args[1].equalsIgnoreCase("admin")) {
                plugin.moderatorList.add(AdminType.ADMIN, player);
                plugin.groupManager.setGroup(player, "admin");

                Bukkit.broadcastMessage(ChatColor.GREEN + playerName + " has been promoted to Admin, congratulations!");
                
                plugin.mail.send(RecipientType.ALL, "TranxCraft Reports - " + playerName + " has been promoted to Admin!", "Hey there, just to let you know, " + playerName + " has been promoted to Admin by " + sender.getName());
                plugin.twitter.tweet("Say congratulations to " + playerName + ", they have been promoted to Admin status by " + sender.getName() + "!");
            }

            if (args[1].equalsIgnoreCase("leadadmin")) {
                plugin.moderatorList.add(AdminType.LEADADMIN, player);
                plugin.groupManager.setGroup(player, "leadadmin");
                
                Bukkit.broadcastMessage(ChatColor.GREEN + playerName + " has been promoted to Admin, congratulations!");

                plugin.mail.send(RecipientType.ALL, "TranxCraft Reports - " + playerName + " has been promoted to Lead Admin!", "Hey there, just to let you know, " + playerName + " has been promoted to Lead Admin by " + sender.getName());
                plugin.twitter.tweet("Say congratulations to " + playerName + ", they have been promoted to Lead Admin status by " + sender.getName() + "!");
            }

            if (args[1].equalsIgnoreCase("executive")) {
                plugin.moderatorList.add(AdminType.EXECUTIVE, player);
                plugin.groupManager.setGroup(player, "executive");

                Bukkit.broadcastMessage(ChatColor.GREEN + playerName + " has been promoted to an Executive rank, congratulations!");

                plugin.mail.send(RecipientType.ALL, "TranxCraft Reports - " + playerName + " has been promoted to Executive!", "Hey there, just to let you know, " + playerName + " has been promoted to Executive by " + sender.getName());
                plugin.twitter.tweet("Say congratulations to " + playerName + ", they have been promoted to Executive status by " + sender.getName() + "!");
            }
        }

        if (args[0].equalsIgnoreCase("remove")) {
            if (args[1].equalsIgnoreCase("Moderator")) {
                plugin.moderatorList.remove(player);
                plugin.groupManager.setGroup(player, "member");
                
                Bukkit.broadcastMessage(ChatColor.RED + playerName + " has been removed from Moderator!");

                plugin.mail.send(RecipientType.ALL, "TranxCraft Reports - " + playerName + " has been removed from Moderator", "Hey there, just to let you know, " + playerName + " has been removed from Moderator by " + sender.getName());
                plugin.twitter.tweet(playerName + " has been removed from Moderator by " + sender.getName() + "!");
            }

            if (args[1].equalsIgnoreCase("Admin")) {
                plugin.moderatorList.remove(player);
                plugin.groupManager.setGroup(player, "member");

                Bukkit.broadcastMessage(ChatColor.RED + playerName + " has been removed from Admin!");

                plugin.mail.send(RecipientType.ALL, "TranxCraft Reports - " + playerName + " has been removed from Admin", "Hey there, just to let you know, " + playerName + " has been removed from Admin by " + sender.getName());
                plugin.twitter.tweet(playerName + " has been removed from Admin by " + sender.getName() + "!");
            }

            if (args[1].equalsIgnoreCase("LeadAdmin")) {
                plugin.moderatorList.remove(player);
                plugin.groupManager.setGroup(player, "member");

                Bukkit.broadcastMessage(ChatColor.RED + playerName + " has been removed from being a lead Admin!");

                plugin.mail.send(RecipientType.ALL, "TranxCraft Reports - " + playerName + " has been removed from Lead Admin", "Hey there, just to let you know, " + playerName + " has been removed from Lead Admin by " + sender.getName());
                plugin.twitter.tweet(playerName + " has been removed from Lead Admin by " + sender.getName() + "!");
            }

            if (args[1].equalsIgnoreCase("Executive")) {
                plugin.moderatorList.remove(player);
                plugin.groupManager.setGroup(player, "member");

                Bukkit.broadcastMessage(ChatColor.RED + playerName + " has been removed from being an Executive!");

                plugin.mail.send(RecipientType.ALL, "TranxCraft Reports - " + playerName + " has been removed from Executive", "Hey there, just to let you know, " + playerName + " has been removed from Executive by " + sender.getName());
                plugin.twitter.tweet(playerName + " has been removed from Executive by " + sender.getName() + "!");
            }
        }

        return true;
    }
}
