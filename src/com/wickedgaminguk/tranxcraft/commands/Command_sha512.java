package com.wickedgaminguk.tranxcraft.commands;

import com.wickedgaminguk.tranxcraft.TCP_ModeratorList;
import com.wickedgaminguk.tranxcraft.TCP_Util;
import com.wickedgaminguk.tranxcraft.TranxCraft;
import net.pravian.bukkitlib.command.BukkitCommand;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

public class Command_sha512 extends BukkitCommand<TranxCraft> {

    @Override
    public boolean run(CommandSender sender, Command command, String commandLabel, String[] args) {
        TCP_Util TCP_Util = new TCP_Util(plugin);
        
        if (!(TCP_Util.hasPermission("tranxcraft.exec", sender) || TCP_Util.hasPermission(TCP_ModeratorList.AdminType.EXECUTIVE, sender))) {
            return noPerms();
        }
        
        if (args.length == 0) {
            return false;
        }
        
        sender.sendMessage(TCP_Util.sha512Hash(StringUtils.join(ArrayUtils.subarray(args, 0, args.length), " ")));
        return true;
    }    
}
