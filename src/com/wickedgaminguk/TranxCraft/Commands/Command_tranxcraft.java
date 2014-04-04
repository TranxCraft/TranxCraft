package com.wickedgaminguk.TranxCraft.Commands;

import com.wickedgaminguk.TranxCraft.TranxCraft;
import net.pravian.bukkitlib.command.BukkitCommand;
import net.pravian.bukkitlib.command.CommandPermissions;
import net.pravian.bukkitlib.command.SourceType;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

@CommandPermissions(source = SourceType.ANY)
public class Command_tranxcraft extends BukkitCommand<TranxCraft> {

    @Override
    public boolean run(CommandSender sender, Command command, String commandLabel, String[] args) {
        sender.sendMessage(ChatColor.GREEN + "-- Basic TranxCraft Information --");
        sender.sendMessage(ChatColor.AQUA + "Owner: HeXeRei452/WickedGamingUK");
        sender.sendMessage(ChatColor.AQUA + "Lead Developer: HeXeRei452/WickedGamingUK");
        sender.sendMessage(ChatColor.AQUA + "Plugin Version: " + plugin.getDescription().getVersion());
        sender.sendMessage(ChatColor.AQUA + "Website: http://www.tranxcraft.com/");
        sender.sendMessage(ChatColor.AQUA + "Forums: http://www.tranxcraft.com/forums");
        sender.sendMessage(ChatColor.GREEN + "------------------------");

        return true;
    }
}
