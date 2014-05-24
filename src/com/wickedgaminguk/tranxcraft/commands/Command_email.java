package com.wickedgaminguk.tranxcraft.commands;

import com.wickedgaminguk.tranxcraft.TCP_Mail;
import com.wickedgaminguk.tranxcraft.TCP_ModeratorList.AdminType;
import com.wickedgaminguk.tranxcraft.TCP_Util;
import com.wickedgaminguk.tranxcraft.TranxCraft;
import net.pravian.bukkitlib.command.BukkitCommand;
import net.pravian.bukkitlib.util.LoggerUtils;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.validator.routines.EmailValidator;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

public class Command_email extends BukkitCommand<TranxCraft> {

    @Override
    public boolean run(CommandSender sender, Command command, String commandLabel, String[] args) {
        TCP_Util TCP_Util = new TCP_Util(plugin);
        TCP_Mail TCP_Mail = new TCP_Mail(plugin);
        
        if (!(TCP_Util.hasPermission("tranxcraft.exec", sender) || TCP_Util.hasPermission(AdminType.EXECUTIVE, sender))) {
            return noPerms();
        }
        
        if (args.length < 3) {
            return false;
        }
        
        String emailTo = args[0];
        
        if (!(EmailValidator.getInstance().isValid(emailTo))) {
            sender.sendMessage(ChatColor.RED + "The e-mail that you have entered is invalid.");
        }
        
        String emailSubject = args[1];
        String emailMessage = StringUtils.join(ArrayUtils.subarray(args, 2, args.length), " ");
        
        try {
            TCP_Mail.send(emailTo, emailSubject, emailMessage);
            sender.sendMessage(ChatColor.GREEN + "E-mail successfully sent to " + ChatColor.GOLD + emailTo);
        }
        catch (Exception ex) {
            sender.sendMessage(ChatColor.RED + "E-mail did not send sucessfully.");
            LoggerUtils.severe(ex);
        }
        
        return true;
    }    
}
