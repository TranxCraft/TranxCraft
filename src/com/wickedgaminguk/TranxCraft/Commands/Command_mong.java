package com.wickedgaminguk.TranxCraft.Commands;

import com.wickedgaminguk.TranxCraft.TCP_Ban;
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
public class Command_mong extends BukkitCommand<TranxCraft> {

    @Override
    public boolean run(CommandSender sender, Command command, String commandLabel, String[] args) {
        TCP_Util TCP_Util = new TCP_Util(plugin);
        TCP_Ban TCP_Ban = new TCP_Ban(plugin);

        if (sender instanceof Player && !(sender.hasPermission("tranxcraft.moderator") || sender.isOp())) {
            return noPerms();
        }

        if (args.length == 0) {
            sender.sendMessage(TCP_Util.Invalid_Usage);
            return false;
        }

        Player player;
        player = getPlayer(args[0]);

        Player sender_p = player;

        try {
            sender_p = (Player) sender;
        }
        catch (Exception ex) {
            sender.sendMessage(ChatColor.RED + "Player could not be found.");
        }

        if (player == null) {
            sender.sendMessage(ChatColor.RED + "This player either isn't online, or doesn't exist.");
            return true;
        }

        if (player == sender_p) {
            sender.sendMessage(ChatColor.RED + "What are you trying to do, you stupid player.");
            sender_p.getWorld().strikeLightning(player.getLocation());
            Bukkit.broadcastMessage(ChatColor.RED + sender.getName() + " is a mong, (s)he tried to mong him/herself!");
            sender_p.kickPlayer(ChatColor.RED + "You have been kicked, don't try to mong yourself...");
            return true;
        }

        if (!sender.hasPermission("tranxcraft.supermong")) {
            if (player.hasPermission("tranxcraft.admin") || player.hasPermission("tranxcraft.exempt") || player.isOp()) {
                sender.sendMessage(ChatColor.RED + "I'm sorry, but you can't mong that player.");
                return true;
            }
        }
        else {
            Bukkit.broadcastMessage(ChatColor.RED + player.getName() + " is a mong, (s)he better read the rules now.");
            String Message = ChatColor.RED + "You have been deemed as a mong by " + sender.getName() + " so you'd better read the website (https://www.tranxcraft.com/) and learn the rules, then create a un-ban request on the forums (https://www.tranxcraft.com/forums)";
            player.kickPlayer(Message);
            TCP_Ban.banUser(player, sender.getName(), Message);
            return true;
        }

        return true;
    }
}
