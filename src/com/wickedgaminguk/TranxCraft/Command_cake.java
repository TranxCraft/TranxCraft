
package com.wickedgaminguk.TranxCraft;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

class Command_cake extends TCP_Command implements CommandExecutor {
    //Pointless Shit!
    public Command_cake(TranxCraft plugin) {
        this.plugin = plugin;
    }
    
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
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