package com.wickedgaminguk.TranxCraft;

import java.util.ArrayList;
import java.util.List;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class TCP_ModeratorList {

    private final TranxCraft plugin;

    public TCP_ModeratorList(TranxCraft plugin) {
        this.plugin = plugin;
        this.Executives = plugin.config.getStringList("Executives");
        this.Donators = plugin.config.getStringList("Donators");
        this.Moderators = plugin.config.getStringList("Moderators");
        this.Admins = plugin.config.getStringList("Admins");
        this.leadAdmins = plugin.config.getStringList("Lead_Admins");
    }

    private final List<String> Executives;
    private final List<String> leadAdmins;
    private final List<String> Admins;
    private final List<String> Moderators;
    private final List<String> Donators;

    public List<String> getModerators() {
        return Moderators;
    }

    public List<String> getAdmins() {
        return Admins;
    }

    public List<String> getleadAdmins() {
        return leadAdmins;
    }

    public List<String> getExecutives() {
        return Executives;
    }

    public List<String> getAllAdmins() {
        List<String> all = Moderators;
        all.addAll(Executives);
        all.addAll(leadAdmins);
        all.addAll(Admins);

        List<String> ops = new ArrayList();
        for (Player p : Bukkit.getOnlinePlayers()) {
            if (p.isOp()) {
                String pn = p.getName();
                ops.add(pn);
            }
        }

        all.addAll(ops);
        return all;
    }

    public List<String> getDonators() {
        return Donators;
    }

    public boolean isPlayerMod(Player player) {
        if (getAllAdmins().contains(player.getName())) {
            return true;
        }
        else {
            return false;
        }
    }

    public boolean isPlayerMod(String player) {
        return getAllAdmins().contains(player);
    }

    public boolean isPlayerDonator(Player player) {
        return getDonators().contains(player.getName());
    }

    public boolean isPlayerDonator(String player) {
        return getDonators().contains(player);
    }
}
