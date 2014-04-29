package com.wickedgaminguk.TranxCraft.Commands;

import com.wickedgaminguk.TranxCraft.TCP_Ban;
import com.wickedgaminguk.TranxCraft.TCP_ModeratorList;
import com.wickedgaminguk.TranxCraft.TranxCraft;
import net.pravian.bukkitlib.command.BukkitCommand;
import net.pravian.bukkitlib.command.CommandPermissions;
import net.pravian.bukkitlib.command.SourceType;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.bukkit.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

@CommandPermissions(source = SourceType.ANY)
public class Command_grandslam extends BukkitCommand<TranxCraft> {

    @Override
    public boolean run(CommandSender sender, Command command, String commandLabel, String[] args) {
        TCP_ModeratorList TCP_ModeratorList = new TCP_ModeratorList(plugin);
        TCP_Ban TCP_Ban = new TCP_Ban(plugin);

        if (sender instanceof Player && !(sender.hasPermission("tranxcraft.moderator") || sender.isOp())) {
            return noPerms();
        }

        if (args.length == 0 || args.length == 1) {
            return false;
        }

        Player player = getPlayer(args[0]);

        if (player == null) {
            sender.sendMessage(ChatColor.RED + "This player either isn't online, or doesn't exist.");
            return true;
        }

        if (sender instanceof Player) {
            if (player == playerSender) {
                sender.sendMessage(ChatColor.RED + "Don't try to ban yourself, idiot.");
                return true;
            }
        }

        if (!sender.hasPermission("tranxcraft.override")) {
            if (!((TCP_ModeratorList.isPlayerMod(player)))) {
                sender.sendMessage(ChatColor.RED + "You may not ban " + player.getName());
                return true;
            }
        }

        String banReason = null;

        if (args.length >= 2) {
            banReason = StringUtils.join(ArrayUtils.subarray(args, 1, args.length), " ");
        }

        World world = player.getWorld();

        player.setVelocity(new Vector(0, 10, 0));
        player.setHealth(0.0);
        player.setOp(false);
        player.getPlayer().setGameMode(GameMode.SURVIVAL);

        final Location target = player.getLocation();

        for (int x = -1; x <= 1; x++) {
            for (int z = -1; z <= 1; z++) {
                final Location strike_pos = new Location(world, target.getBlockX() + x, target.getBlockY(), target.getBlockZ() + z);
                world.strikeLightning(strike_pos);
            }
        }

        Bukkit.broadcastMessage(ChatColor.RED + "" + sender.getName() + " - grandslamming " + player.getName() + " for " + banReason);

        //rollback
        Bukkit.dispatchCommand(sender, "co rollback " + player.getName() + " t:500d r:#global");

        //Ban Username
        TCP_Ban.banUser(player, sender.getName(), banReason);

        // kick Player:
        player.kickPlayer(ChatColor.RED + "GTFO" + (banReason != null ? ("\nReason: " + ChatColor.YELLOW + banReason) : ""));

        return true;
    }
}
