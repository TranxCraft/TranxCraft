package com.wickedgaminguk.TranxCraft;

import java.util.List;
import org.apache.commons.lang.WordUtils;
import org.bukkit.entity.Player;

public class TCP_DonatorList {

    private final TranxCraft plugin;
    private final TCP_Util TCP_Util;

    public enum DonatorType {

        ONE, TWO, THREE;
    }

    public TCP_DonatorList(TranxCraft plugin) {
        this.plugin = plugin;
        this.TCP_Util = new TCP_Util(plugin);
    }

    public DonatorType getRank(Player player) {
        if (plugin.donatorConfig.contains("Donators." + player.getUniqueId().toString())) {
            switch (plugin.donatorConfig.getString("Donators." + player.getUniqueId().toString() + ".Rank")) {
                case "One": {
                    return DonatorType.ONE;
                }
                case "Two": {
                    return DonatorType.TWO;
                }
                case "Three": {
                    return DonatorType.THREE;
                }
            }
        }
        return null;
    }

    public boolean isPlayerDonator(Player player) {
        return plugin.donatorConfig.contains("Donators." + player.getUniqueId().toString());
    }

    public boolean isPlayerDonator(String player) {
        return plugin.donatorConfig.contains("Donators." + TCP_Util.playerToUUID(player).toString());
    }

    public void add(DonatorType at, Player player) {
        List<String> donatorIPs = plugin.donatorConfig.getStringList("Donator_IPs");
        donatorIPs.add(player.getAddress().getHostString());
        plugin.donatorConfig.set("Donators." + player.getUniqueId().toString() + ".IGN", player.getName());
        plugin.donatorConfig.set("Donators." + player.getUniqueId().toString() + ".Rank", WordUtils.capitalizeFully(at.toString().toLowerCase()));
        plugin.donatorConfig.set("Donators." + player.getUniqueId().toString() + ".Login_Message", "");
        plugin.donatorConfig.set("Donator_IPs", donatorIPs);
        plugin.donatorConfig.save();
    }

    public String getLoginMessage(Player player) {
        if (plugin.donatorConfig.contains("Donators." + player.getUniqueId().toString())) {
            return plugin.donatorConfig.getString("Donators." + player.getUniqueId().toString() + ".Login_Message");
        }
        else {
            return "";
        }
    }

    public void setLoginMessage(Player player, String message) {
        if (plugin.donatorConfig.contains("Donators." + player.getUniqueId().toString())) {
            plugin.donatorConfig.set("Donators." + player.getUniqueId().toString() + ".Login_Message", message);
            plugin.donatorConfig.save();
        }
    }

    public void remove(Player player) {
        plugin.donatorConfig.set("Donators." + player.getUniqueId().toString(), null);
        plugin.donatorConfig.set("Donator_IPs", plugin.donatorConfig.getStringList("Donator_IPs").remove(player.getAddress().getHostString()));
        plugin.donatorConfig.save();
    }
}
