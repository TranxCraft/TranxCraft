package com.wickedgaminguk.tranxcraft.commands;

import com.wickedgaminguk.tranxcraft.TCP_ModeratorList;
import com.wickedgaminguk.tranxcraft.TCP_ModeratorList.AdminType;
import com.wickedgaminguk.tranxcraft.TCP_PremiumList;
import com.wickedgaminguk.tranxcraft.TCP_Util;
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
        TCP_Util TCP_Util = new TCP_Util(plugin);
        TCP_ModeratorList TCP_ModeratorList = new TCP_ModeratorList(plugin);
        TCP_PremiumList TCP_PremiumList = new TCP_PremiumList(plugin);

        if (!(TCP_Util.hasPermission("tranxcraft.moderator", sender) || TCP_Util.hasPermission(AdminType.MODERATOR, sender))) {
            return noPerms();
        }

        Player player = getPlayer(args[0]);

        String playerRank = TCP_Util.getPlayerGroup(player);
        String tcpRank = null;

        if (TCP_ModeratorList.isPlayerMod(player)) {
            switch (TCP_ModeratorList.getRank(player)) {
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

        if (TCP_PremiumList.isPlayerPremium(player)) {
            switch (TCP_PremiumList.getRank(player)) {
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

        sender.sendMessage(ChatColor.GOLD + player.getName() + ChatColor.AQUA + "'s GroupManager Group is: " + ChatColor.GOLD + playerRank + " and there TranxCraft Group is: " + ChatColor.GOLD + tcpRank);

        return true;
    }
}
