
package com.wickedgaminguk.TranxCraft.Commands;

import com.wickedgaminguk.TranxCraft.*;
import net.pravian.bukkitlib.command.BukkitCommand;
import net.pravian.bukkitlib.command.CommandPermissions;
import net.pravian.bukkitlib.command.SourceType;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

@CommandPermissions(source = SourceType.ANY, usage = "Usage: /<command> <name>")
public class Command_admininfo extends BukkitCommand {

    @Override
    public boolean run(CommandSender sender, Command command, String commandLabel, String[] args) {
        
        if(args.length != 0) {
            sender.sendMessage(TCP_Util.Invalid_Usage);
        }
        else {
            sender.sendMessage(ChatColor.BLUE + "----TranxCraft Moderator Application Information----");
            sender.sendMessage(ChatColor.AQUA + "If you wish to become a moderator on TranxCraft, you need to apply on the forums: http://www.tranxcraft.com/forums/");
            sender.sendMessage(ChatColor.AQUA + "Only apply if you feel like you have what it takes to be a Moderator, you'll probably need recommendations.");
            sender.sendMessage(ChatColor.RED + "But, don't beg for recommendations, this will get your application denied.");
            sender.sendMessage(ChatColor.AQUA + "Good luck to all Members that apply.");
            sender.sendMessage(ChatColor.BLUE + "----End of TranxCraft Moderator Application Information----");
            return true;
        }
        
        return false;
    }
    
}
