package com.wickedgaminguk.tranxcraft.commands;

import com.wickedgaminguk.tranxcraft.TranxCraft;
import net.pravian.bukkitlib.command.BukkitCommand;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Command_buybackpack extends BukkitCommand<TranxCraft> {

    @Override
    public boolean run(CommandSender sender, Command command, String commandLabel, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("The console cannot buy a backpack.");
            return true;
        }

        if (plugin.util.hasBoughtBackpack(playerSender.getUniqueId()) == true) {
            sender.sendMessage(ChatColor.RED + "You have already bought a backpack, you can't do it again.");
            return true;
        }

        if (plugin.util.getPlayerBalance(playerSender.getUniqueId()) < 100.0) {
            sender.sendMessage(ChatColor.RED + "You have insufficient funds to buy a backpack.");
            return true;
        }

        plugin.util.withdrawPlayer(playerSender.getUniqueId(), 100.0);
        plugin.playerConfig.set(playerSender.getUniqueId().toString() + ".backpack", true);
        plugin.playerConfig.save();

        sender.sendMessage(ChatColor.GREEN + "You have successfully bought a backpack for $100, use it with /backpack.");

        return true;
    }
}
