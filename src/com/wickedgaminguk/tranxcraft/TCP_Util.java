package com.wickedgaminguk.tranxcraft;

import com.earth2me.essentials.Essentials;
import com.earth2me.essentials.User;
import com.wickedgaminguk.tranxcraft.TCP_ModeratorList.AdminType;
import com.wickedgaminguk.tranxcraft.TCP_PremiumList.PremiumType;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import me.confuser.barapi.BarAPI;
import net.pravian.bukkitlib.util.LoggerUtils;
import org.apache.commons.codec.digest.DigestUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;
import org.bukkit.util.Vector;

public class TCP_Util {

    private final TranxCraft plugin;
    private final TCP_ModeratorList TCP_ModeratorList;
    private final TCP_PremiumList TCP_PremiumList;
    private final TCP_Shop TCP_Shop;
    private Essentials essentialsPlugin = null;

    public TCP_Util(TranxCraft plugin) {
        this.plugin = plugin;
        this.TCP_ModeratorList = plugin.moderatorList;
        this.TCP_PremiumList = plugin.premiumList;
        this.TCP_Shop = new TCP_Shop(plugin);
    }

    public final String invalidUsage = ChatColor.RED + "Invalid Usage.";
    public List<String> permBan = Arrays.asList("3d4ad828721f44a4b6e1a18aeac31f88");

    public boolean isAdminMode() {
        return plugin.config.getBoolean("adminmode");
    }

    public boolean hasGrapple(Player player) {
        return plugin.playerConfig.getBoolean(player.getUniqueId().toString() + ".grapple");
    }

    public void setGrapple(Player player, boolean mode) {
        if (mode == true) {
            plugin.playerConfig.set(player.getUniqueId().toString() + ".grapple", true);
        }

        if (mode == false) {
            plugin.playerConfig.set(player.getUniqueId().toString() + ".grapple", false);
        }

        plugin.playerConfig.save();
    }

    public boolean hasDoubleJump(Player player) {
        return plugin.playerConfig.getBoolean(player.getUniqueId().toString() + ".doublejump");
    }

    public void setDoubleJump(Player player, boolean mode) {
        if (mode == true) {
            plugin.playerConfig.set(player.getUniqueId().toString() + ".doublejump", true);
        }

        if (mode == false) {
            plugin.playerConfig.set(player.getUniqueId().toString() + ".doublejump", false);
        }

        plugin.playerConfig.save();
    }

    public void broadcastItem(Material material, int quantity, String message) {
        for (Player player : plugin.getServer().getOnlinePlayers()) {
            ItemStack item = new ItemStack(material, quantity);
            player.getInventory().setItem(player.getInventory().firstEmpty(), item);
        }

        if (!(message.isEmpty())) {
            Bukkit.broadcastMessage(message);
        }
    }

    public void sendItem(Player player, Material material, int quantity, String message) {
        ItemStack item = new ItemStack(material, quantity);
        player.getInventory().setItem(player.getInventory().firstEmpty(), item);

        if (!(message == null)) {
            player.sendMessage(message);
        }
    }

    public void sendItem(Player player, ItemStack material, String message) {
        player.getInventory().setItem(player.getInventory().firstEmpty(), material);

        if (!(message == null)) {
            player.sendMessage(message);
        }
    }

    public boolean hasItem(Player player, ItemStack i) {
        ItemStack[] inv = player.getInventory().getContents();

        for (ItemStack item : inv) {
            if (item == null) {
                return false;
            }
            if (item.getType().equals(i.getType())) {
                return true;
            }
        }
        return false;
    }

    public UUID playerToUUID(String player) {
        UUID playerID = null;
        try {
            playerID = UUIDFetcher.getUUIDOf(player);
        }
        catch (Exception ex) {
        }
        return playerID;
    }

    public UUID playerToUUID(Player player) {
        UUID playerID = null;
        try {
            playerID = UUIDFetcher.getUUIDOf(player.getName());
        }
        catch (Exception ex) {
        }
        return playerID;
    }

    public String UUIDToPlayer(UUID uuid) {
        NameFetcher fetcher = new NameFetcher(Arrays.asList(uuid));
        Map<UUID, String> response = null;

        try {
            response = fetcher.call();
        }
        catch (Exception e) {
        }

        String playerName = response.get(uuid);

        return playerName;
    }

    public void kickPlayer(Player player, PlayerLoginEvent event) {
        Player[] players = plugin.getServer().getOnlinePlayers();

        for (Player p : players) {
            if (!((TCP_ModeratorList.isPlayerMod(player))) || TCP_PremiumList.isPlayerPremium(player)) {
                p.kickPlayer("I'm sorry, but you've been kicked to make room for a reserved player, to stop this happening, buy a premium rank!");
                event.allow();
                LoggerUtils.info(plugin, "Allowed player " + player.getName() + " to join full server by kicking player " + p.getName() + "!");
            }
        }

        event.disallow(PlayerLoginEvent.Result.KICK_FULL, "Unable to find any kickable players to open slots!");
    }

    public Vector getVectorForPoints(Location l1, Location l2) {
        double g = -0.08;
        double d = l2.distance(l1);
        double t = d;
        double vX = (1.0 + 0.07 * t) * (l2.getX() - l1.getX()) / t;
        double vY = (1.0 + 0.03 * t) * (l2.getY() - l1.getY()) / t - 0.5 * g * t;
        double vZ = (1.0 + 0.07 * t) * (l2.getZ() - l1.getZ()) / t;
        return new Vector(vX, vY, vZ);
    }

    public Map<String, String> getPlayerData() throws FileNotFoundException, IOException, ClassNotFoundException {
        Map<String, String> players;

        try (FileInputStream fis = new FileInputStream(plugin.getDataFolder() + "/playerData.dat"); ObjectInputStream ois = new ObjectInputStream(fis)) {
            players = (HashMap) ois.readObject();
        }

        return players;
    }

    public boolean hasPermission(String permission, Player player) {
        if (player instanceof Player && !(player.hasPermission(permission) || player.isOp())) {
            return false;
        }
        else {
            return true;
        }
    }

    public boolean hasPermission(String permission, CommandSender player) {
        if (player instanceof Player && !(player.hasPermission(permission) || player.isOp())) {
            return false;
        }
        else {
            return true;
        }
    }

    public boolean hasPermission(AdminType adminType, CommandSender sender) {
        return TCP_ModeratorList.getRank(sender).equals(adminType);
    }

    public boolean hasPermission(AdminType adminType, Player player) {
        return TCP_ModeratorList.getRank(player).equals(adminType);
    }

    public boolean hasPermission(PremiumType premiumType, CommandSender player) {
        return TCP_PremiumList.getRank(player).equals(premiumType);
    }

    public boolean hasPermission(PremiumType premiumType, Player player) {
        return TCP_PremiumList.getRank(player).equals(premiumType);
    }

    public void register(Player player, String email) {
        plugin.playerConfig.set(player.getUniqueId().toString() + ".registered", true);
        plugin.playerConfig.getStringList("registered_emails").add(email);
        plugin.playerConfig.save();
    }

    public boolean hasRegistered(Player player) {
        return plugin.playerConfig.getBoolean(player.getUniqueId().toString() + ".registered");
    }

    public boolean hasRegistered(String email) {
        return plugin.playerConfig.getStringList("registered_emails").contains(email);
    }

    public int getTotalUniquePlayers() {
        int total = plugin.playerConfig.getValues(false).size();
        return total;
    }

    public void setBarMessage(String message, int seconds) {
        BarAPI.setMessage(message, seconds);
    }

    public void teleport(World world, Player player, int x, int y, int z) {
        player.teleport(new Location(world, x, y, z));
    }

    public void openGUI(Player player) {
        Inventory inv = Bukkit.createInventory(null, 9, ChatColor.DARK_GREEN + "Server Utilities");

        ItemStack spawn = new ItemStack(Material.COMPASS);
        ItemStack lobby = new ItemStack(Material.BOW);
        ItemStack shop = new ItemStack(Material.GOLD_BLOCK);
        ItemStack home = new ItemStack(Material.BED);
        ItemStack adeam = new ItemStack(Material.MAP);

        ItemMeta spawnMeta = spawn.getItemMeta();
        ItemMeta lobbyMeta = lobby.getItemMeta();
        ItemMeta shopMeta = shop.getItemMeta();
        ItemMeta homeMeta = home.getItemMeta();
        ItemMeta adeamMeta = adeam.getItemMeta();

        spawnMeta.setDisplayName(ChatColor.GREEN + "Spawn");
        lobbyMeta.setDisplayName(ChatColor.RED + "Survival Games Lobby");
        shopMeta.setDisplayName(ChatColor.GOLD + "Shop");
        homeMeta.setDisplayName(ChatColor.DARK_GREEN + "Home");
        adeamMeta.setDisplayName(ChatColor.LIGHT_PURPLE + "Adeam Kingdom");

        spawnMeta.setLore(Arrays.asList(ChatColor.GREEN + "Teleports you to Spawn."));
        lobbyMeta.setLore(Arrays.asList(ChatColor.RED + "Teleports you to the Survival Games Lobby."));
        shopMeta.setLore(Arrays.asList(ChatColor.GOLD + "Teleports you to the Shop."));
        homeMeta.setLore(Arrays.asList(ChatColor.DARK_GREEN + "Teleports you to your home, if you have one set."));
        adeamMeta.setLore(Arrays.asList(ChatColor.LIGHT_PURPLE + "Teleports you to the Adeam Kingdom."));

        spawn.setItemMeta(spawnMeta);
        lobby.setItemMeta(lobbyMeta);
        shop.setItemMeta(shopMeta);
        home.setItemMeta(homeMeta);
        adeam.setItemMeta(adeamMeta);

        inv.setItem(0, spawn);
        inv.setItem(1, lobby);
        inv.setItem(2, shop);
        inv.setItem(3, home);
        inv.setItem(4, adeam);

        player.openInventory(inv);
    }

    public String sha512Hash(String toHash) {
        return DigestUtils.sha512Hex(toHash);
    }

    public String sha1Hash(String toHash) {
        return DigestUtils.sha1Hex(toHash);
    }

    public int getVotes(Player player) {
        return plugin.playerConfig.getInt(player.getUniqueId().toString() + ".votes");
    }

    public int getVotes(UUID uuid) {
        return plugin.playerConfig.getInt(uuid.toString() + ".votes");
    }

    public void setVotes(Player player) {
        plugin.playerConfig.set(player.getUniqueId().toString() + ".votes", getVotes(player) + 1);
    }

    public void setVotes(UUID uuid) {
        plugin.playerConfig.set(uuid.toString() + ".votes", getVotes(uuid) + 1);
    }

    public String getPlayerGroup(Player player) {
        String perm = plugin.permission.getPrimaryGroup(player);
        return perm;
    }

    public double getPlayerBalance(String player) {
        return plugin.economy.getBalance(player);
    }

    public double getPlayerBalance(Player player) {
        return getPlayerBalance(player.getName());
    }

    public double getPlayerBalance(UUID uuid) {
        return getPlayerBalance(UUIDToPlayer(uuid));
    }

    public void depositPlayer(String player, double amount) {
        plugin.economy.depositPlayer(player, amount);
    }

    public void depositPlayer(Player player, double amount) {
        depositPlayer(player.getName(), amount);
    }

    public void depositPlayer(UUID uuid, double amount) {
        depositPlayer(UUIDToPlayer(uuid), amount);
    }

    public void withdrawPlayer(String player, double amount) {
        plugin.economy.withdrawPlayer(player, amount);
    }

    public void withdrawPlayer(Player player, double amount) {
        withdrawPlayer(player.getName(), amount);
    }

    public void withdrawPlayer(UUID uuid, double amount) {
        withdrawPlayer(UUIDToPlayer(uuid), amount);
    }

    public void buyItem(Player player, Material material, int quantity) {
        TCP_Shop.buy(player, material, quantity);
    }

    public Essentials getEssentialsPlugin() {
        if (this.essentialsPlugin == null) {
            try {
                final Plugin essentials = Bukkit.getServer().getPluginManager().getPlugin("Essentials");
                if (essentials != null) {
                    if (essentials instanceof Essentials) {
                        this.essentialsPlugin = (Essentials) essentials;
                    }
                }
            }
            catch (Exception ex) {
            }
        }
        return this.essentialsPlugin;
    }

    public User getEssentialsUser(String username) {
        try {
            final Essentials essentials = getEssentialsPlugin();
            if (essentials != null) {
                return essentials.getUserMap().getUser(username);
            }
        }
        catch (Exception ex) {
        }
        return null;
    }

    public String[] getCredits() {
        return new String[]{"Pravian - BukkitLib", "TotalFreedom - Various Methods", "JeromSar - Programming Help"};
    }
}
