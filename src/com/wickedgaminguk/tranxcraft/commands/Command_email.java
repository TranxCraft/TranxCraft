package com.wickedgaminguk.tranxcraft.commands;

import com.wickedgaminguk.tranxcraft.TCP_ModeratorList.AdminType;
import com.wickedgaminguk.tranxcraft.TranxCraft;
import net.pravian.bukkitlib.command.BukkitCommand;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.validator.routines.EmailValidator;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

public class Command_email extends BukkitCommand<TranxCraft> {

    @Override
    public boolean run(CommandSender sender, Command command, String commandLabel, String[] args) {
        if (args[0].equalsIgnoreCase("list") || args[0].equalsIgnoreCase("set") || args[0].equalsIgnoreCase("remove")) {
            if (!(plugin.util.hasPermission("tranxcraft.moderator", sender) || plugin.util.hasPermission(AdminType.MODERATOR, sender))) {
                return noPerms();
            }
            
            if (args[0].equalsIgnoreCase("list")) {
                String email = plugin.moderatorList.getEmail(playerSender);                
                sender.sendMessage(ChatColor.GOLD + "The e-mail that you have associated with your account is: " + ChatColor.AQUA + email);
                return true;
            }
            
            if (args[0].equalsIgnoreCase("set")) {
                if (!(EmailValidator.getInstance().isValid(args[1]))) {
                    sender.sendMessage(ChatColor.RED + "The e-mail that you have entered is invalid.");
                    return true;
                }
                
                plugin.moderatorList.setEmail(playerSender, args[1]);                
                sender.sendMessage(ChatColor.GREEN + "You have successfully set your e-mail to: " + ChatColor.GOLD + args[1]);
                return true;
            }
            
            if (args[0].equalsIgnoreCase("remove")) {
                plugin.moderatorList.setEmail(playerSender, null);
                sender.sendMessage(ChatColor.RED + "You have removed your e-mail.");
                return true;
            }
        }
        else {
            if (!(plugin.util.hasPermission("tranxcraft.exec", sender) || plugin.util.hasPermission(AdminType.EXECUTIVE, sender))) {
                return noPerms();
            }

            if (args.length < 3) {
                return false;
            }

            String emailTo = args[0];

            if (!(EmailValidator.getInstance().isValid(emailTo))) {
                sender.sendMessage(ChatColor.RED + "The e-mail that you have entered is invalid.");
                return true;
            }

            String emailSubject = args[1];
            String emailMessage = StringUtils.join(ArrayUtils.subarray(args, 2, args.length), " ");

            try {
                plugin.mail.send(emailTo, emailSubject, emailMessage);
                sender.sendMessage(ChatColor.GREEN + "E-mail successfully sent to " + ChatColor.GOLD + emailTo);
            }
            catch (Exception ex) {
                sender.sendMessage(ChatColor.RED + "E-mail did not send sucessfully.");
                plugin.util.debug(ex);
            }
        }

        return true;
    }
}
