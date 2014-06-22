package com.wickedgaminguk.tranxcraft.commands;

import com.wickedgaminguk.tranxcraft.TranxCraft;
import net.pravian.bukkitlib.command.BukkitCommand;
import net.pravian.bukkitlib.util.LoggerUtils;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Command_backpack extends BukkitCommand<TranxCraft> {

    @Override
    public boolean run(CommandSender sender, Command command, String commandLabel, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("The console cannot have a backpack.");
            return true;
        }

        if (!(plugin.util.hasBoughtBackpack(playerSender.getUniqueId()))) {
            sender.sendMessage(ChatColor.RED + "You haven't bought a backpack, buy one with /buybackpack ($100)");
        }
        else {
            if (plugin.util.getBackpackData().containsKey(playerSender.getUniqueId())) {
                LoggerUtils.info(plugin.util.getBackpackData().get(playerSender.getUniqueId()));
                playerSender.openInventory(plugin.util.getBackpack(playerSender));
            }
        }

        return true;
    }
}
