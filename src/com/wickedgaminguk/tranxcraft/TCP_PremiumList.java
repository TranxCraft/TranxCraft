package com.wickedgaminguk.tranxcraft;

import java.util.List;
import org.apache.commons.lang.WordUtils;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class TCP_PremiumList {

    private final TranxCraft plugin;

    public enum PremiumType {

        ONE, TWO, THREE;
    }

    public TCP_PremiumList(TranxCraft plugin) {
        this.plugin = plugin;
    }

    public PremiumType getRank(Player player) {
        if (plugin.premiumConfig.contains("premium." + player.getUniqueId().toString())) {
            switch (plugin.premiumConfig.getString("premium." + player.getUniqueId().toString() + ".rank")) {
                case "one": {
                    return PremiumType.ONE;
                }
                case "two": {
                    return PremiumType.TWO;
                }
                case "three": {
                    return PremiumType.THREE;
                }
            }
        }
        return null;
    }

    public PremiumType getRank(CommandSender sender) {
        Player player = (Player) sender;
        if (plugin.premiumConfig.contains("premium." + player.getUniqueId().toString())) {
            switch (plugin.premiumConfig.getString("premium." + player.getUniqueId().toString() + ".rank")) {
                case "one": {
                    return PremiumType.ONE;
                }
                case "two": {
                    return PremiumType.TWO;
                }
                case "three": {
                    return PremiumType.THREE;
                }
            }
        }
        return null;
    }

    public boolean isPlayerPremium(Player player) {
        return plugin.premiumConfig.contains("premium." + player.getUniqueId().toString());
    }

    public boolean isPlayerPremium(String player) {
        return plugin.premiumConfig.contains("premium." + plugin.util.playerToUuid(player).toString());
    }

    public void add(PremiumType at, Player player) {
        List<String> premiumIPs = plugin.premiumConfig.getStringList("premium_ips");
        premiumIPs.add(player.getAddress().getHostString());
        plugin.premiumConfig.set("premium." + player.getUniqueId().toString() + ".ign", player.getName());
        plugin.premiumConfig.set("premium." + player.getUniqueId().toString() + ".rank", WordUtils.capitalizeFully(at.toString().toLowerCase()));
        plugin.premiumConfig.set("premium." + player.getUniqueId().toString() + ".login_message", "");
        plugin.premiumConfig.set("premium_ips", premiumIPs);
        plugin.premiumConfig.save();
    }

    public String getLoginMessage(Player player) {
        if (plugin.premiumConfig.contains("premium." + player.getUniqueId().toString())) {
            return plugin.premiumConfig.getString("premium." + player.getUniqueId().toString() + ".login_message");
        }
        else {
            return "";
        }
    }

    public void setLoginMessage(Player player, String message) {
        if (plugin.premiumConfig.contains("premium." + player.getUniqueId().toString())) {
            plugin.premiumConfig.set("premium." + player.getUniqueId().toString() + ".login_message", message);
            plugin.premiumConfig.save();
        }
    }

    public void remove(Player player) {
        plugin.premiumConfig.set("premium." + player.getUniqueId().toString(), null);
        plugin.premiumConfig.set("premium_ips", plugin.premiumConfig.getStringList("premium_ips").remove(player.getAddress().getHostString()));
        plugin.premiumConfig.save();
    }
}
