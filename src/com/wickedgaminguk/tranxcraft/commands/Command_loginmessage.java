package com.wickedgaminguk.tranxcraft.commands;

import com.wickedgaminguk.tranxcraft.TCP_ModeratorList;
import com.wickedgaminguk.tranxcraft.TCP_PremiumList;
import com.wickedgaminguk.tranxcraft.TCP_Util;
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
        TCP_ModeratorList TCP_ModeratorList = new TCP_ModeratorList(plugin);
        TCP_PremiumList TCP_PremiumList = new TCP_PremiumList(plugin);
        TCP_Util TCP_Util = new TCP_Util(plugin);

        if (!(TCP_ModeratorList.isPlayerMod(playerSender) || TCP_PremiumList.isPlayerPremium(playerSender) || sender instanceof Player)) {
            return noPerms();
        }

        if (args.length == 0) {
            sender.sendMessage(TCP_Util.invalidUsage);
            return false;
        }

        String loginMessage;

        if (TCP_ModeratorList.isPlayerMod(playerSender)) {
            loginMessage = StringUtils.join(ArrayUtils.subarray(args, 0, args.length), " ");
            TCP_ModeratorList.setLoginMessage(playerSender, loginMessage);
            sender.sendMessage(ChatColor.GREEN + "Set login message to: " + ChatUtils.colorize(TCP_ModeratorList.getLoginMessage(playerSender)));
            return true;
        }

        if (TCP_PremiumList.isPlayerPremium(playerSender)) {
            loginMessage = StringUtils.join(ArrayUtils.subarray(args, 0, args.length), " ");
            TCP_PremiumList.setLoginMessage(playerSender, loginMessage);
            sender.sendMessage(ChatColor.GREEN + "Set login message to: " + ChatUtils.colorize(TCP_PremiumList.getLoginMessage(playerSender)));
            return true;
        }

        return true;
    }
}
