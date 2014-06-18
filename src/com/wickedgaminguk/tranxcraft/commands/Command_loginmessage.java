package com.wickedgaminguk.tranxcraft.commands;

import com.wickedgaminguk.tranxcraft.TranxCraft;
import net.pravian.bukkitlib.command.BukkitCommand;
import net.pravian.bukkitlib.util.ChatUtils;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Command_loginmessage extends BukkitCommand<TranxCraft> {

    @Override
    public boolean run(CommandSender sender, Command command, String commandLabel, String[] args) {
        if (!(plugin.moderatorList.isPlayerMod(playerSender) || plugin.premiumList.isPlayerPremium(playerSender) || sender instanceof Player)) {
            return noPerms();
        }

        if (args.length == 0) {
            sender.sendMessage(plugin.util.invalidUsage);
            return false;
        }

        String loginMessage;

        if (plugin.moderatorList.isPlayerMod(playerSender)) {
            loginMessage = StringUtils.join(ArrayUtils.subarray(args, 0, args.length), " ");
            plugin.moderatorList.setLoginMessage(playerSender, loginMessage);
            sender.sendMessage(ChatColor.GREEN + "Set login message to: " + ChatUtils.colorize(plugin.moderatorList.getLoginMessage(playerSender)));
            return true;
        }

        if (plugin.premiumList.isPlayerPremium(playerSender)) {
            loginMessage = StringUtils.join(ArrayUtils.subarray(args, 0, args.length), " ");
            plugin.premiumList.setLoginMessage(playerSender, loginMessage);
            sender.sendMessage(ChatColor.GREEN + "Set login message to: " + ChatUtils.colorize(plugin.premiumList.getLoginMessage(playerSender)));
            return true;
        }

        return true;
    }
}
