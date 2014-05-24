package com.wickedgaminguk.tranxcraft.commands;

import com.wickedgaminguk.tranxcraft.TCP_Registration;
import com.wickedgaminguk.tranxcraft.TCP_Util;
import com.wickedgaminguk.tranxcraft.TranxCraft;
import java.util.UUID;
import net.pravian.bukkitlib.command.BukkitCommand;
import net.pravian.bukkitlib.command.CommandPermissions;
import net.pravian.bukkitlib.command.SourceType;
import org.apache.commons.validator.routines.EmailValidator;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

@CommandPermissions(source = SourceType.ANY)
public class Command_register extends BukkitCommand<TranxCraft> {

    @Override
    public boolean run(CommandSender sender, Command command, String commandLabel, String[] args) {
        TCP_Util TCP_Util = new TCP_Util(plugin);

        if (args.length != 1) {
            return false;
        }

        if (TCP_Util.hasRegistered(playerSender) || TCP_Util.hasRegistered(args[0])) {
            sender.sendMessage(ChatColor.RED + "You have already registered!");
            return true;
        }

        if (!(EmailValidator.getInstance().isValid(args[0]))) {
            sender.sendMessage(ChatColor.RED + "The e-mail that you have provided is invalid.");
            return true;
        }
        
        UUID uuid = playerSender.getUniqueId();
        String email = args[0];
        String username = sender.getName();
        
        new TCP_Registration(plugin, uuid, email, username).runTaskAsynchronously(plugin);
        
        TCP_Util.register(playerSender, email);
        
        sender.sendMessage(ChatColor.GREEN + "You have successfully registered, check your e-mails for more details. (It may be in your Spam folder)");

        return true;
    }
}
