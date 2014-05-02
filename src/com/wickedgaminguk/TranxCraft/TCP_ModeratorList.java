package com.wickedgaminguk.TranxCraft;

import java.util.List;
import org.apache.commons.lang.WordUtils;
import org.bukkit.entity.Player;

public class TCP_ModeratorList {

    private final TranxCraft plugin;
    private final TCP_Util TCP_Util;

    public enum AdminType {

        SYS, EXECUTIVE, LEADADMIN, ADMIN, MODERATOR;
    }

    public TCP_ModeratorList(TranxCraft plugin) {
        this.plugin = plugin;
        this.TCP_Util = new TCP_Util(plugin);
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
            }
        }
        return null;
    }

    public boolean isPlayerMod(Player player) {
        return plugin.adminConfig.contains("Admins." + player.getUniqueId().toString());
    }

    public boolean isPlayerMod(String player) {
        return plugin.adminConfig.contains("Admins." + TCP_Util.playerToUUID(player).toString());
    }

    public void add(AdminType at, Player player) {
        List<String> adminIPs = plugin.adminConfig.getStringList("Admin_IPs");
        adminIPs.add(player.getAddress().getHostString());
        plugin.adminConfig.set("Admins." + player.getUniqueId().toString() + ".IGN", player.getName());
        plugin.adminConfig.set("Admins." + player.getUniqueId().toString() + ".Rank", WordUtils.capitalizeFully(at.toString().toLowerCase()));
        plugin.adminConfig.set("Admins." + player.getUniqueId().toString() + ".Login_Message", "");
        plugin.adminConfig.set("Admins." + player.getUniqueId().toString() + ".AdminChat_Toggle", false);
        plugin.adminConfig.set("Admin_IPs", adminIPs);
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

    public void setLoginMessage(Player player, String message) {
        if (plugin.adminConfig.contains("Admins." + player.getUniqueId().toString())) {
            plugin.adminConfig.set("Admins." + player.getUniqueId().toString() + ".Login_Message", message);
            plugin.adminConfig.save();
        }
    }

    public void remove(Player player) {
        plugin.adminConfig.set("Admins." + player.getUniqueId().toString(), null);
        plugin.adminConfig.set("Admin_IPs", plugin.adminConfig.getStringList("Admin_IPs").remove(player.getAddress().getHostString()));
        plugin.adminConfig.save();
    }

    public void toggleAdminChat(Player player) {
        if (plugin.adminConfig.getBoolean("Admins." + player.getUniqueId().toString() + ".AdminChat_Toggle") == false) {
            plugin.adminConfig.set("Admins." + player.getUniqueId().toString() + ".AdminChat_Toggle", true);
        }
        else if (plugin.adminConfig.getBoolean("Admins." + player.getUniqueId().toString() + ".AdminChat_Toggle") == true) {
            plugin.adminConfig.set("Admins." + player.getUniqueId().toString() + ".AdminChat_Toggle", false);
        }
    }

    public boolean hasAdminChatEnabled(Player player) {
        return plugin.adminConfig.getBoolean("Admins." + player.getUniqueId().toString() + ".AdminChat_Toggle");
    }
}
