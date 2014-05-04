package com.wickedgaminguk.tranxcraft;

import java.util.List;
import org.apache.commons.lang.WordUtils;
import org.bukkit.command.CommandSender;
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
        if (plugin.donatorConfig.contains("donators." + player.getUniqueId().toString())) {
            switch (plugin.donatorConfig.getString("donators." + player.getUniqueId().toString() + ".rank")) {
                case "one": {
                    return DonatorType.ONE;
                }
                case "two": {
                    return DonatorType.TWO;
                }
                case "three": {
                    return DonatorType.THREE;
                }
            }
        }
        return null;
    }

    public DonatorType getRank(CommandSender sender) {
        Player player = (Player) sender;
        if (plugin.donatorConfig.contains("donators." + player.getUniqueId().toString())) {
            switch (plugin.donatorConfig.getString("donators." + player.getUniqueId().toString() + ".rank")) {
                case "one": {
                    return DonatorType.ONE;
                }
                case "two": {
                    return DonatorType.TWO;
                }
                case "three": {
                    return DonatorType.THREE;
                }
            }
        }
        return null;
    }

    public boolean isPlayerDonator(Player player) {
        return plugin.donatorConfig.contains("donators." + player.getUniqueId().toString());
    }

    public boolean isPlayerDonator(String player) {
        return plugin.donatorConfig.contains("donators." + TCP_Util.playerToUUID(player).toString());
    }

    public void add(DonatorType at, Player player) {
        List<String> donatorIPs = plugin.donatorConfig.getStringList("donator_ips");
        donatorIPs.add(player.getAddress().getHostString());
        plugin.donatorConfig.set("donators." + player.getUniqueId().toString() + ".ign", player.getName());
        plugin.donatorConfig.set("donators." + player.getUniqueId().toString() + ".rank", WordUtils.capitalizeFully(at.toString().toLowerCase()));
        plugin.donatorConfig.set("donators." + player.getUniqueId().toString() + ".login_message", "");
        plugin.donatorConfig.set("donator_ips", donatorIPs);
        plugin.donatorConfig.save();
    }

    public String getLoginMessage(Player player) {
        if (plugin.donatorConfig.contains("donators." + player.getUniqueId().toString())) {
            return plugin.donatorConfig.getString("donators." + player.getUniqueId().toString() + ".login_message");
        }
        else {
            return "";
        }
    }

    public void setLoginMessage(Player player, String message) {
        if (plugin.donatorConfig.contains("donators." + player.getUniqueId().toString())) {
            plugin.donatorConfig.set("donators." + player.getUniqueId().toString() + ".login_message", message);
            plugin.donatorConfig.save();
        }
    }

    public void remove(Player player) {
        plugin.donatorConfig.set("donators." + player.getUniqueId().toString(), null);
        plugin.donatorConfig.set("donator_ips", plugin.donatorConfig.getStringList("donator_ips").remove(player.getAddress().getHostString()));
        plugin.donatorConfig.save();
    }
}
