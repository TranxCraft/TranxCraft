package com.wickedgaminguk.tranxcraft.commands;

import com.wickedgaminguk.tranxcraft.TCP_ModeratorList.AdminType;
import com.wickedgaminguk.tranxcraft.TranxCraft;
import net.pravian.bukkitlib.command.BukkitCommand;
import net.pravian.bukkitlib.command.CommandPermissions;
import net.pravian.bukkitlib.command.SourceType;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@CommandPermissions(source = SourceType.ANY)
public class Command_getrank extends BukkitCommand<TranxCraft> {

    @Override
    public boolean run(CommandSender sender, Command command, String commandLabel, String[] args) {
        if (!(plugin.util.hasPermission("tranxcraft.moderator", sender) || plugin.util.hasPermission(AdminType.MODERATOR, sender))) {
            return noPerms();
        }

        Player player = getPlayer(args[0]);

        String playerRank = plugin.util.getPlayerGroup(player);
        String tcpRank = null;

        if (plugin.moderatorList.isPlayerMod(player)) {
            switch (plugin.moderatorList.getRank(player)) {
                case MODERATOR: {
                    tcpRank = "Moderator";
                    break;
                }
                case ADMIN: {
                    tcpRank = "Admin";
                    break;
                }
                case LEADADMIN: {
                    tcpRank = "Lead Admin";
                    break;
                }
                case EXECUTIVE: {
                    tcpRank = "Executive";
                    break;
                }
                case COMMANDER: {
                    tcpRank = "Commander";
                    break;
                }
            }
        }

        if (plugin.premiumList.isPlayerPremium(player)) {
            switch (plugin.premiumList.getRank(player)) {
                case ONE: {
                    tcpRank = "Level One Premium";
                    break;
                }
                case TWO: {
                    tcpRank = "Level Two Premium";
                    break;
                }
                case THREE: {
                    tcpRank = "Level Three Premium";
                    break;
                }
            }
        }

        if (tcpRank == null) {
            tcpRank = "Member";
        }

        sender.sendMessage(ChatColor.GOLD + player.getName() + ChatColor.AQUA + "'s GroupManager Group is: " + ChatColor.GOLD + playerRank + " and their TranxCraft Group is: " + ChatColor.GOLD + tcpRank);

        return true;
    }
}
