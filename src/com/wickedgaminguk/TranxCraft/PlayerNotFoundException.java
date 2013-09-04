package com.wickedgaminguk.TranxCraft;

import org.bukkit.ChatColor;
 //Credits to Steven Lawson/Madgeek & Jerom Van Der Sar/DarthSalamon for the contents of this file.
public class PlayerNotFoundException extends Exception {
    
    private static final long serialVersionUID = 1L;

    public PlayerNotFoundException() {
        super(ChatColor.GRAY + "Can't find player.");
    }

    public PlayerNotFoundException(String msg) {
        super(ChatColor.GRAY + "Can't find player: " + msg);
    }
}