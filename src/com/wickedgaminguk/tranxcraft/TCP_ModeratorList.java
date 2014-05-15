package com.wickedgaminguk.tranxcraft;

import java.util.List;
import org.apache.commons.lang.WordUtils;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class TCP_ModeratorList {

    private final TranxCraft plugin;
    private final TCP_Util TCP_Util;

    public enum AdminType {

        COMMANDER, EXECUTIVE, LEADADMIN, ADMIN, MODERATOR;
    }

    public TCP_ModeratorList(TranxCraft plugin) {
        this.plugin = plugin;
        this.TCP_Util = plugin.util;
    }

    public AdminType getRank(Player player) {
        if (plugin.adminConfig.contains("admins." + player.getUniqueId().toString())) {
            switch (plugin.adminConfig.getString("admins." + player.getUniqueId().toString() + ".rank")) {
                case "moderator": {
                    return AdminType.MODERATOR;
                }
                case "admin": {
                    return AdminType.ADMIN;
                }
                case "leadadmin": {
                    return AdminType.LEADADMIN;
                }
                case "executive": {
                    return AdminType.EXECUTIVE;
                }
                case "commander": {
                    return AdminType.COMMANDER;
                }
            }
        }
        return null;
    }

    public AdminType getRank(CommandSender sender) {
        Player player = (Player) sender;
        if (plugin.adminConfig.contains("admins." + player.getUniqueId().toString())) {
            switch (plugin.adminConfig.getString("admins." + player.getUniqueId().toString() + ".rank")) {
                case "moderator": {
                    return AdminType.MODERATOR;
                }
                case "admin": {
                    return AdminType.ADMIN;
                }
                case "leadadmin": {
                    return AdminType.LEADADMIN;
                }
                case "executive": {
                    return AdminType.EXECUTIVE;
                }
                case "commander": {
                    return AdminType.COMMANDER;
                }
            }
        }
        return null;
    }

    public boolean isPlayerMod(Player player) {
        return plugin.adminConfig.contains("admins." + player.getUniqueId().toString());
    }

    public boolean isPlayerMod(CommandSender sender) {
        Player player = (Player) sender;
        return plugin.adminConfig.contains("admins." + player.getUniqueId().toString());
    }

    public boolean isPlayerMod(String player) {
        return plugin.adminConfig.contains("admins." + TCP_Util.playerToUUID(player).toString());
    }

    public void add(AdminType at, Player player) {
        List<String> adminIPs = plugin.adminConfig.getStringList("admin_ips");
        adminIPs.add(player.getAddress().getHostString());
        plugin.adminConfig.set("admins." + player.getUniqueId().toString() + ".ign", player.getName());
        plugin.adminConfig.set("admins." + player.getUniqueId().toString() + ".rank", WordUtils.capitalizeFully(at.toString().toLowerCase()));
        plugin.adminConfig.set("admins." + player.getUniqueId().toString() + ".login_message", "");
        plugin.adminConfig.set("admins." + player.getUniqueId().toString() + ".adminchat_toggle", false);
        plugin.adminConfig.set("admin_ips", adminIPs);
        plugin.adminConfig.save();
    }

    public String getLoginMessage(Player player) {
        if (plugin.adminConfig.contains("admins." + player.getUniqueId().toString())) {
            return plugin.adminConfig.getString("admins." + player.getUniqueId().toString() + ".login_message");
        }
        else {
            return "";
        }
    }

    public void setLoginMessage(Player player, String message) {
        if (plugin.adminConfig.contains("admins." + player.getUniqueId().toString())) {
            plugin.adminConfig.set("admins." + player.getUniqueId().toString() + ".login_message", message);
            plugin.adminConfig.save();
        }
    }

    public void remove(Player player) {
        plugin.adminConfig.set("admins." + player.getUniqueId().toString(), null);
        plugin.adminConfig.set("admin_ips", plugin.adminConfig.getStringList("admin_ips").remove(player.getAddress().getHostString()));
        plugin.adminConfig.save();
    }

    public void toggleAdminChat(Player player) {
        if (plugin.adminConfig.getBoolean("admins." + player.getUniqueId().toString() + ".adminchat_toggle") == false) {
            plugin.adminConfig.set("admins." + player.getUniqueId().toString() + ".adminchat_toggle", true);
        }
        else if (plugin.adminConfig.getBoolean("admins." + player.getUniqueId().toString() + ".adminchat_toggle") == true) {
            plugin.adminConfig.set("admins." + player.getUniqueId().toString() + ".adminchat_toggle", false);
        }
    }

    public boolean hasAdminChatEnabled(Player player) {
        return plugin.adminConfig.getBoolean("admins." + player.getUniqueId().toString() + ".adminchat_toggle");
    }
}
