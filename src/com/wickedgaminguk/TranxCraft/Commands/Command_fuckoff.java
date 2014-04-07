package com.wickedgaminguk.TranxCraft.Commands;

import com.wickedgaminguk.TranxCraft.TCP_Mail.RecipientType;
import com.wickedgaminguk.TranxCraft.TCP_ModeratorList;
import com.wickedgaminguk.TranxCraft.TCP_Util;
import com.wickedgaminguk.TranxCraft.TranxCraft;
import net.pravian.bukkitlib.command.BukkitCommand;
import net.pravian.bukkitlib.command.CommandPermissions;
import net.pravian.bukkitlib.command.SourceType;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

@CommandPermissions(source = SourceType.CONSOLE)
public class Command_fuckoff extends BukkitCommand<TranxCraft> {

    TCP_ModeratorList TCP_ModeratorList = new TCP_ModeratorList(plugin);
    TCP_Util TCP_Util = new TCP_Util(plugin);

    @Override
    public boolean run(CommandSender sender, Command command, String commandLabel, String[] args) {
        if (args.length != 1) {
            return false;
        }

        Player player;
        player = getPlayer(args[0]);

        Bukkit.broadcastMessage(ChatColor.RED + sender.getName() + " Casting oblivion over " + player.getName());
        Bukkit.broadcastMessage(ChatColor.RED + player.getName() + " will be completely obliviated!");

        final String IP = player.getAddress().getAddress().getHostAddress().trim();

        // remove from TranxCraft Moderator Ranks
        TCP_ModeratorList.remove(player);

        // remove from whitelist
        player.setWhitelisted(false);

        // deop
        player.setOp(false);

        // ban IP
        TCP_Util.banIP(IP, null, null);

        // ban name
        TCP_Util.banUsername(player.getName(), null, null);

        // set gamemode to survival
        player.setGameMode(GameMode.SURVIVAL);

        // clear inventory
        player.closeInventory();
        player.getInventory().clear();

        // ignite player
        player.setFireTicks(10000);

        // Throw the player into the air
        player.setVelocity(new Vector(0, 10, 0));

        // generate explosion
        player.getWorld().createExplosion(player.getLocation(), 4F);

        // strike lightning
        player.getWorld().strikeLightning(player.getLocation());

        // kill (if not done already)
        player.setHealth(0.0);

        // message
        Bukkit.broadcastMessage(ChatColor.RED + sender.getName() + " Banning " + player.getName() + ", IP: " + IP);

        // generate explosion
        player.getWorld().createExplosion(player.getLocation(), 4F);

        // kick player
        player.kickPlayer(ChatColor.RED + "FUCKOFF, and get your shit together, you little cunt!");

        plugin.mail.send(RecipientType.ALL, "TranxCraft Reports - " + player.getName() + " has been fucked off.", "Just to let you know, " + player.getName() + "has been 'fucked off' by " + sender.getName());
        plugin.twitter.tweet(player.getName() + " has been fucked off the server!");

        return true;
    }
}
