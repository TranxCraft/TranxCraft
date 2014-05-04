package com.wickedgaminguk.tranxcraft;

import java.net.InetSocketAddress;
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

        plugin.bans.set("bans." + UUID + ".ign", name);
        plugin.bans.set("bans." + UUID + ".ip", playerIP);
        plugin.bans.set("bans." + UUID + ".source", source);
        plugin.bans.set("bans." + UUID + ".reason", reason);
        plugin.bans.set("bans." + UUID + ".time", TCP_Time.getLongDate());
        plugin.bans.save();
    }

    public void banUser(String player, String source, String reason) {
        String UUID = TCP_Util.playerToUUID(player).toString();
        String IP = IpUtils.toEscapedString(plugin.playerConfig.getString(UUID + ".ip"));

        plugin.bans.set("bans." + UUID + ".ign", player);
        plugin.bans.set("bans." + UUID + ".ip", IP);
        plugin.bans.set("bans." + UUID + ".source", source);
        plugin.bans.set("bans." + UUID + ".reason", reason);
        plugin.bans.set("bans." + UUID + ".time", TCP_Time.getLongDate());
    }

    public void banUser(UUID uuid, String source, String reason) {
        String player = TCP_Util.UUIDToPlayer(uuid);
        String IP = IpUtils.toEscapedString(plugin.playerConfig.getString(uuid + ".ip"));

        plugin.bans.set("bans." + uuid + ".ign", player);
        plugin.bans.set("bans." + uuid + ".ip", IP);
        plugin.bans.set("bans." + uuid + ".source", source);
        plugin.bans.set("bans." + uuid + ".reason", reason);
        plugin.bans.set("bans." + uuid + ".time", TCP_Time.getLongDate());
    }

    public void unbanUser(Player player) {
        String UUID = player.getUniqueId().toString();
        plugin.bans.set("bans." + UUID, null);
        plugin.bans.save();
    }

    public void unbanUser(String uuid) {
        plugin.bans.set("bans." + uuid, null);
    }

    public void unbanUser(UUID uuid) {
        plugin.bans.set("bans." + uuid.toString(), null);
        plugin.bans.save();
    }

    public void banIP(String IP, String source, String reason) {
        String Ip = IpUtils.toEscapedString(IP);
        plugin.bans.set("bans.banned_ips." + Ip + ".source", source);
        plugin.bans.set("bans.banned_ips." + Ip + ".reason", reason);
        plugin.bans.set("bans.banned_ips." + Ip + ".time", TCP_Time.getLongDate());
        plugin.bans.save();
    }

    public void banIP(Player player, String source, String reason) {
        String IP = IpUtils.toEscapedString(player.getAddress().getHostString());
        plugin.bans.set("bans.banned_ips." + IP + ".source", source);
        plugin.bans.set("bans.banned_ips." + IP + ".reason", reason);
        plugin.bans.set("bans.banned_ips." + IP + ".time", TCP_Time.getLongDate());
        plugin.bans.save();
    }

    public void banIP(InetSocketAddress address, String source, String reason) {
        String IP = IpUtils.toEscapedString(address.getHostString());
        plugin.bans.set("bans.banned_ips." + IP + ".source", source);
        plugin.bans.set("bans.banned_ips." + IP + ".reason", reason);
        plugin.bans.set("bans.banned_ips." + IP + ".time", TCP_Time.getLongDate());
        plugin.bans.save();
    }

    public void unbanIP(String IP) {
        plugin.bans.set("bans.banned_ips." + IpUtils.toEscapedString(IP), null);
        plugin.bans.save();
    }

    public boolean isUUIDBanned(Player player) {
        return plugin.bans.contains("bans." + player.getUniqueId().toString());
    }

    public boolean isUUIDBanned(String UUID) {
        return plugin.bans.contains("bans." + UUID);
    }

    public boolean isUUIDBanned(UUID uuid) {
        return plugin.bans.contains("bans." + uuid.toString());
    }

    public boolean isIPBanned(String IP) {
        return plugin.bans.contains("bans.banned_ips." + IpUtils.toEscapedString(IP));
    }

    public boolean isIPBanned(Player player) {
        return plugin.bans.contains("bans.banned_ips." + IpUtils.toEscapedString(player.getAddress().getHostString()));
    }

    public String getBanReason(Player player) {
        if (plugin.bans.contains("bans." + player.getUniqueId().toString())) {
            return plugin.bans.getString("bans." + player.getUniqueId().toString() + ".reason");
        }
        else {
            return "";
        }
    }

    public String getBanReason(String player) {
        String playerID = TCP_Util.playerToUUID(player).toString();

        if (plugin.bans.contains("bans." + playerID)) {
            return plugin.bans.getString("bans." + playerID + ".reason");
        }
        else {
            return "";
        }
    }

    public String getIPBanReason(String IP) {
        String ip = IpUtils.toEscapedString(IP);

        if (plugin.bans.contains("bans.banned_ips." + ip)) {
            return plugin.bans.getString("bans.banned_ips." + ip + ".reason");
        }
        else {
            return "";
        }
    }
}
