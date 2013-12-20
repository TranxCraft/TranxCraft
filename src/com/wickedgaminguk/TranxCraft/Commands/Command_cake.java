
package com.wickedgaminguk.TranxCraft.Commands;

import com.wickedgaminguk.TranxCraft.*;
import net.pravian.bukkitlib.command.BukkitCommand;
import net.pravian.bukkitlib.command.CommandPermissions;
import net.pravian.bukkitlib.command.SourceType;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

@CommandPermissions(source = SourceType.ANY, usage = "Usage: /<command>")
public class Command_cake extends BukkitCommand {

    @Override
    public boolean run(CommandSender sender, Command command, String commandLabel, String[] args) {
        if(!(sender.hasPermission("tranxcraft.cake"))) {
            sender.sendMessage(TCP_Util.noPerms);
            return true;
        }
        
        for(Player player : plugin.getServer().getOnlinePlayers()) {
            ItemStack cake = new ItemStack(org.bukkit.Material.CAKE, 1);
            player.getInventory().setItem(player.getInventory().firstEmpty(), cake);
            sender.sendMessage(ChatColor.GREEN + "Free Cake For Everyone!");
        }
        return true;
   }
}