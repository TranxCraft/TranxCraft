
package com.wickedgaminguk.TranxCraft.Commands;

import com.wickedgaminguk.TranxCraft.*;
import net.pravian.bukkitlib.command.BukkitCommand;
import net.pravian.bukkitlib.command.CommandPermissions;
import net.pravian.bukkitlib.command.SourceType;
import net.pravian.bukkitlib.util.ChatUtils;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@CommandPermissions(source = SourceType.ANY, usage = "Usage: /<command> <message>")
public class Command_o extends BukkitCommand {

    @Override
    public boolean run(CommandSender sender, Command command, String commandLabel, String[] args) {
        
        if(sender instanceof Player && !(sender.hasPermission("tranxcraft.adminchat") || sender.isOp())) {
            sender.sendMessage(TCP_Util.noPerms);
            return true;
        }
        
        if(args.length == 0) {
            sender.sendMessage(ChatColor.RED + "Incorrect Usage.");
            return false;
        }
        
        String prefix = "&f[&bADMIN&f]&b ";
        String msg = StringUtils.join(ArrayUtils.subarray(args, 0, args.length), " ");
        
        if(sender instanceof Player) {
            Player player = (Player) sender;
            String playerName = player.getDisplayName();

            Bukkit.broadcast(ChatUtils.colorize(prefix + playerName + ": &b" + msg), "tranxcraft.adminchat");
            TCP_Log.info(ChatColor.stripColor(prefix + playerName + ": " + msg));
        }
        
        if(!(sender instanceof Player)) {
            String playerName = " &4[" + sender.getName() + "]&f";
            Bukkit.broadcast(ChatUtils.colorize(prefix + playerName + ": &b" + msg), "tranxcraft.adminchat");
            TCP_Log.info(ChatColor.stripColor(prefix + playerName + ": " + msg));
        }
        
        return true;
    }
}