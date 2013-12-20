
package com.wickedgaminguk.TranxCraft.Commands;

import com.wickedgaminguk.TranxCraft.*;
import net.pravian.bukkitlib.command.BukkitCommand;
import net.pravian.bukkitlib.command.CommandPermissions;
import net.pravian.bukkitlib.command.SourceType;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@CommandPermissions(source = SourceType.ANY, usage = "Usage: /<command> <player> <reason>")
public class Command_gtfo extends BukkitCommand {

    @Override
    public boolean run(CommandSender sender, Command command, String commandLabel, String[] args) {
        
        if(sender instanceof Player && !(sender.hasPermission("tranxcraft.gtfo") || sender.isOp())){
            sender.sendMessage(TCP_Util.noPerms);
            return true;
        }
        
        if(args.length == 0 || args.length == 1) {
            return false;
        }
        
        Player player;
        player = getPlayer(args[0]);
                
        Player sender_p = player;
        try {
            sender_p = (Player) sender;
        }
        catch(Exception ex) {
            sender.sendMessage(ChatColor.RED + "Player could not be found.");
        }
        
        if(player == null) {
            sender.sendMessage(ChatColor.RED + "This player either isn't online, or doesn't exist.");
            return true;
        }
        
        if(player == sender_p) {
            sender.sendMessage(ChatColor.RED + "Don't try to ban yourself, idiot.");
            return true;
        }
        
        if(!sender.hasPermission("tranxcraft.override")) {
            if(TCP_ModeratorList.getModerators().contains(player.getName()) || TCP_ModeratorList.getAdmins().contains(player.getName()) || TCP_ModeratorList.getleadAdmins().contains(player.getName()) || TCP_ModeratorList.getExecutives().contains(player.getName())) {
                sender.sendMessage(ChatColor.RED + "You may not ban " + player.getName());
                return true;
            }
        }
        
        String ban_reason = null;
        
        if (args.length >= 2) {
            ban_reason = StringUtils.join(ArrayUtils.subarray(args, 1, args.length), " ");
        }
        
        Bukkit.broadcastMessage(ChatColor.RED + "" + sender.getName() + " - banning " + player.getName() + " for " + ban_reason);
        
        //rollback
        Bukkit.dispatchCommand(sender, "co rollback " + player.getName() + " t:500d r:#global");
        
        //set gamemode to survival
        player.setGameMode(GameMode.SURVIVAL);
        
        // strike with lightning effect, credits again to Steven Lawson/Madgeek & Jerom Van Der Sar/DarthSalamon for this bit of code
        final Location targetPos = player.getLocation();
        for (int x = -1; x <= 1; x++) {
            for (int z = -1; z <= 1; z++) {
                final Location strike_pos = new Location(targetPos.getWorld(), targetPos.getBlockX() + x, targetPos.getBlockY(), targetPos.getBlockZ() + z);
                targetPos.getWorld().strikeLightning(strike_pos);
            }
        }
        
        //Ban Username
        TCP_Util.banUsername(player.getName(), ban_reason, null);
        
        // kick Player:
        player.kickPlayer(ChatColor.RED + "GTFO" + (ban_reason != null ? ("\nReason: " + ChatColor.YELLOW + ban_reason) : ""));
        
        return true;
    }
}