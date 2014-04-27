package com.wickedgaminguk.TranxCraft;

import java.util.UUID;
import net.pravian.bukkitlib.util.IpUtils;
import org.bukkit.entity.Player;

public class TCP_Ban {

    TranxCraft plugin;
    TCP_Util TCP_Util;

    public TCP_Ban(TranxCraft plugin) {
        this.plugin = plugin;
        this.TCP_Util = new TCP_Util(plugin);
    }

    public void banUser(Player player, String source, String reason) {
        String UUID = player.getUniqueId().toString();
        String name = player.getName();
        String playerIP = player.getAddress().getHostString();

        plugin.bans.set("Bans." + UUID + ".IGN", name);
        plugin.bans.set("Bans." + UUID + ".IP", playerIP);
        plugin.bans.set("Bans." + UUID + ".Source", source);
        plugin.bans.set("Bans." + UUID + ".Reason", reason);
        plugin.bans.set("Bans." + UUID + ".time", TCP_Time.getLongDate());
        plugin.bans.save();
    }

    public void banUser(String player, String source, String reason) {
        String UUID = TCP_Util.playerToUUID(player).toString();
        String IP = IpUtils.toEscapedString(plugin.playerConfig.getString(UUID + ".IP"));

        plugin.bans.set("Bans." + UUID + ".IGN", player);
        plugin.bans.set("Bans." + UUID + ".IP", IP);
        plugin.bans.set("Bans." + UUID + ".Source", source);
        plugin.bans.set("Bans." + UUID + ".Reason", reason);
        plugin.bans.set("Bans." + UUID + ".time", TCP_Time.getLongDate());
    }

    public void banUser(UUID uuid, String source, String reason) {
        String player = TCP_Util.UUIDToPlayer(uuid);
        String IP = IpUtils.toEscapedString(plugin.playerConfig.getString(uuid + ".IP"));

        plugin.bans.set("Bans." + uuid + ".IGN", player);
        plugin.bans.set("Bans." + uuid + ".IP", IP);
        plugin.bans.set("Bans." + uuid + ".Source", source);
        plugin.bans.set("Bans." + uuid + ".Reason", reason);
        plugin.bans.set("Bans." + uuid + ".time", TCP_Time.getLongDate());
    }

    public void unbanUser(Player player) {
        String UUID = player.getUniqueId().toString();
        plugin.bans.set("Bans." + UUID, null);
        plugin.bans.save();
    }

    public void unbanUser(UUID uuid) {
        plugin.bans.set("Bans." + uuid.toString(), null);
        plugin.bans.save();
    }

    public void banIP(String IP, String source, String reason) {
        String Ip = IpUtils.toEscapedString(IP);
        plugin.bans.set("Bans.Banned_IPs." + Ip + ".Source", source);
        plugin.bans.set("Bans.Banned_IPs." + Ip + ".Reason", reason);
        plugin.bans.set("Bans.Banned_IPs." + Ip + ".time", TCP_Time.getLongDate());
        plugin.bans.save();
    }

    public void banIP(Player player, String source, String reason) {
        String IP = IpUtils.toEscapedString(player.getAddress().getHostString());
        plugin.bans.set("Bans.Banned_IPs." + IP + ".Source", source);
        plugin.bans.set("Bans.Banned_IPs." + IP + ".Reason", reason);
        plugin.bans.set("Bans.Banned_IPs." + IP + ".time", TCP_Time.getLongDate());
        plugin.bans.save();
    }

    public void unbanIP(String IP) {
        plugin.bans.set("Bans.Banned_IPs." + IpUtils.toEscapedString(IP), null);
        plugin.bans.save();
    }

    public boolean isUUIDBanned(Player player) {
        return plugin.bans.contains("Bans." + player.getUniqueId().toString());
    }

    public boolean isUUIDBanned(String UUID) {
        return plugin.bans.contains("Bans." + UUID);
    }

    public boolean isUUIDBanned(UUID uuid) {
        return plugin.bans.contains("Bans." + uuid.toString());
    }

    public boolean isIPBanned(String IP) {
        return plugin.bans.contains("Bans.Banned_IPs." + IpUtils.toEscapedString(IP));
    }

    public boolean isIPBanned(Player player) {
        String IP = player.getAddress().getHostString();
        return plugin.bans.contains("Bans.Banned_IPs." + IP);
    }

    public String getBanReason(Player player) {
        if (plugin.bans.contains("Bans." + player.getUniqueId().toString())) {
            return plugin.bans.getString("Bans." + player.getUniqueId().toString() + ".Reason");
        }
        else {
            return "";
        }
    }

    public String getBanReason(String player) {
        String playerID = TCP_Util.playerToUUID(player).toString();

        if (plugin.bans.contains("Bans." + playerID)) {
            return plugin.bans.getString("Bans." + playerID + ".Reason");
        }
        else {
            return "";
        }
    }

    public String getIPBanReason(String IP) {
        String ip = IpUtils.toEscapedString(IP);

        if (plugin.bans.contains("Bans.Banned_IPs." + ip)) {
            return plugin.bans.getString("Bans.Banned_IPs." + ip + ".Reason");
        }
        else {
            return "";
        }
    }
}
