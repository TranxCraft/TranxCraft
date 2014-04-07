package com.wickedgaminguk.TranxCraft;

import org.apache.commons.lang.WordUtils;
import org.bukkit.entity.Player;

public class TCP_ModeratorList {

    private final TranxCraft plugin;

    public enum AdminType {

        SYS, EXECUTIVE, LEADADMIN, ADMIN, MODERATOR, DONATOR, REGULAR;
    }

    public TCP_ModeratorList(TranxCraft plugin) {
        this.plugin = plugin;
    }

    public AdminType getRank(Player player) {
        if (plugin.adminConfig.contains("Admins." + player.getUniqueId().toString())) {
            switch (plugin.adminConfig.getString("Admins." + player.getUniqueId().toString() + ".Rank")) {
                case "Moderator": {
                    return AdminType.MODERATOR;
                }
                case "Admin": {
                    return AdminType.ADMIN;
                }
                case "Leadadmin": {
                    return AdminType.LEADADMIN;
                }
                case "Executive": {
                    return AdminType.EXECUTIVE;
                }
                case "Sys": {
                    return AdminType.SYS;
                }
                case "Donator": {
                    return AdminType.DONATOR;
                }
            }
        }
        return AdminType.REGULAR;
    }

    public boolean isPlayerMod(Player player) {
        if (plugin.adminConfig.contains("Admins." + player.getUniqueId().toString())) {
            switch (plugin.adminConfig.getString("Admins." + player.getUniqueId().toString() + ".Rank")) {
                case "Moderator": {
                    return true;
                }
                case "Admin": {
                    return true;
                }
                case "Leadadmin": {
                    return true;
                }
                case "Executive": {
                    return true;
                }
                case "Sys": {
                    return true;
                }
                case "Donator": {
                    return false;
                }
            }
        }

        return false;
    }

    public void add(AdminType at, Player player) {
        plugin.adminConfig.set("Admins." + player.getUniqueId().toString() + ".IGN", player.getName());
        plugin.adminConfig.set("Admins." + player.getUniqueId().toString() + ".Rank", WordUtils.capitalizeFully(at.toString().toLowerCase()));
        plugin.adminConfig.set("Admins." + player.getUniqueId().toString() + ".Login_Message", "");
        plugin.adminConfig.save();
    }

    public String getLoginMessage(Player player) {
        if (plugin.adminConfig.contains("Admins." + player.getUniqueId().toString())) {
            return plugin.adminConfig.getString("Admins." + player.getUniqueId().toString() + ".Login_Message");
        }
        else {
            return "";
        }
    }

    public void remove(Player player) {
        plugin.adminConfig.set("Admins." + player.getUniqueId().toString(), null);
        plugin.adminConfig.save();
    }
}
