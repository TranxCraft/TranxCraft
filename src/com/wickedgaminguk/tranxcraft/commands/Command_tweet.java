package com.wickedgaminguk.tranxcraft.commands;

import com.wickedgaminguk.tranxcraft.TCP_ModeratorList.AdminType;
import com.wickedgaminguk.tranxcraft.TranxCraft;
import net.pravian.bukkitlib.command.BukkitCommand;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

public class Command_tweet extends BukkitCommand<TranxCraft> {

    @Override
    public boolean run(CommandSender sender, Command command, String commandLabel, String[] args) {
        if (!(plugin.util.hasPermission("tranxcraft.exec", sender) || plugin.util.hasPermission(AdminType.EXECUTIVE, sender))) {
            return noPerms();
        }

        if (args.length != 1) {
            return false;
        }

        String tweet = StringUtils.join(ArrayUtils.subarray(args, 0, args.length), " ");

        if (tweet.length() > 140) {
            sender.sendMessage(ChatColor.RED + "Tweets have a limit of 140 characters.");
            return true;
        }

        plugin.twitter.tweet(tweet);

        return true;
    }
}
