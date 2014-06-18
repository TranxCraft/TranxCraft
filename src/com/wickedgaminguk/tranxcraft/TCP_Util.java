package com.wickedgaminguk.tranxcraft;

import com.earth2me.essentials.Essentials;
import com.earth2me.essentials.User;
import com.wickedgaminguk.tranxcraft.TCP_ModeratorList.AdminType;
import com.wickedgaminguk.tranxcraft.TCP_PremiumList.PremiumType;
import com.wickedgaminguk.tranxcraft.Updater.UpdateType;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.UUID;
import me.confuser.barapi.BarAPI;
import net.pravian.bukkitlib.util.FileUtils;
import net.pravian.bukkitlib.util.LoggerUtils;
import org.apache.commons.codec.digest.DigestUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;

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
        this.TCP_Shop = plugin.shop;
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
        plugin.playerConfig.set(player.getUniqueId().toString() + ".grapple", mode);
        plugin.playerConfig.save();
    }

    public boolean hasDoubleJump(Player player) {
        return plugin.playerConfig.getBoolean(player.getUniqueId().toString() + ".doublejump");
    }

    public void setDoubleJump(Player player, boolean mode) {
        plugin.playerConfig.set(player.getUniqueId().toString() + ".doublejump", mode);
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

        if (message != null) {
            player.sendMessage(message);
        }
    }

    public void sendItem(Player player, ItemStack material, String message) {
        player.getInventory().setItem(player.getInventory().firstEmpty(), material);

        if (message != null) {
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
        catch (Exception ex) {
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

    public boolean hasPermission(AdminType adminType, PremiumType premiumType, Player player) {
        return hasPermission(adminType, player) || hasPermission(premiumType, player);
    }

    public boolean hasPermission(String permission, AdminType adminType, Player player) {
        return hasPermission(permission, player) || hasPermission(adminType, player);
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

    public void setBarMessage(Player player, String message, int seconds) {
        BarAPI.setMessage(player, message, seconds);
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

    public String getIp(String player) {
        return plugin.playerConfig.getString(playerToUUID(player) + ".ip");
    }

    public String getIp(UUID uuid) {
        return plugin.playerConfig.getString(uuid.toString() + ".ip");
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

    public boolean isNegative(double d) {
        return Double.doubleToRawLongBits(d) < 0;
    }

    public int getPluginId(Plugin p) throws MalformedURLException, IOException, ParseException, Exception {
        /*final JSONParser jsonParser = new JSONParser();
         final BufferedReader in = new BufferedReader(new InputStreamReader(new URL("https://api.curseforge.com/servermods/projects?search=" + plugin.getName().toLowerCase()).openStream()));
         JSONArray response = (JSONArray) jsonParser.parse(in.readLine());
         LoggerUtils.info(response.toJSONString());
         return (Integer) response.size();
         HttpURLConnection connection = createConnection(plugin);
         JSONArray array = (JSONArray) jsonParser.parse(new InputStreamReader(connection.getInputStream()));
         String id = "0";
         for (Object profile : array) {
         JSONObject jsonProfile = (JSONObject) profile;
         id = (String) jsonProfile.get("id");
         }
        
         return Integer.valueOf(id);*/

        return plugin.pluginConfig.getInt("plugins." + p.getName());
    }
    /*
     private static HttpURLConnection createConnection(Plugin plugin) throws Exception {
     URL url = new URL(PROFILE_URL + plugin.getName().toLowerCase());
     HttpURLConnection connection = (HttpURLConnection) url.openConnection();        
     connection.setRequestMethod("GET");
     connection.setRequestProperty("Content-Type", "application/json");
     connection.setUseCaches(false);
     connection.setDoInput(true);
     connection.setDoOutput(true);
     return connection;
     }*/

    public void update() throws MalformedURLException, IOException {
        if (plugin.config.getBoolean("auto-update") == true) {
            BukkitRunnable runnable = new BukkitRunnable() {
                URL url;
                InputStream in;

                @Override
                public void run() {
                    try {
                        url = new URL("https://www.tranxcraft.com/development/latest.txt");
                        in = url.openStream();
                        Scanner s = new Scanner(in);
                        String version = s.nextLine();
                        LoggerUtils.info(plugin, "Latest plugin version: " + version + ". Using " + plugin.getVersion());

                        if (isNegative(Double.valueOf(plugin.getVersion()) - Double.valueOf(version))) {
                            LoggerUtils.info(plugin, "Downloading update...");

                            File updateFile = Bukkit.getServer().getUpdateFolderFile();

                            if (!(updateFile.exists())) {
                                updateFile.mkdirs();
                            }

                            download("https://www.tranxcraft.com/development/latest.jar", new File(updateFile + "/TranxCraft.jar"), false);
                            LoggerUtils.info(plugin, "Finished downloading the latest version of TranxCraft, restart the server for this to take effect (DO NOT RELOAD)");
                        }
                        else {
                            LoggerUtils.info(plugin, "Not downloading update, running latest version");
                        }
                    }
                    catch (MalformedURLException ex) {
                        LoggerUtils.info(ex.getMessage());
                    }
                    catch (IOException ex) {
                        LoggerUtils.info(ex.getMessage());
                    }

                    if (in != null) {
                        try {
                            in.close();
                        }
                        catch (IOException ex) {

                        }
                    }
                }
            };

            updatePlugins();
            runnable.runTaskAsynchronously(plugin);
        }
        else {
            LoggerUtils.info(plugin, "Not updating plugins, it's disabled in the config.");
        }
    }

    public void updatePlugins() {
        if (plugin.config.getBoolean("auto-update") == true) {
            Plugin[] plugins = Bukkit.getPluginManager().getPlugins();

            BukkitRunnable runnable = new BukkitRunnable() {
                @Override
                public void run() {
                    for (Plugin p : plugins) {
                        try {
                            Updater updater = new Updater(p, getPluginId(p), new File(FileUtils.getPluginsFolder() + "/update"), UpdateType.DEFAULT, true);
                        }
                        catch (Exception ex) {
                            LoggerUtils.warning(plugin, "Automatic updating of " + p.getName() + " has failed.");
                        }
                    }
                }
            };

            runnable.runTaskAsynchronously(plugin);
        }
        else {
            LoggerUtils.info(plugin, "Not updating plugins, it's disabled in the config.");
        }
    }

    public void download(String url, File output, boolean verbose) {
        try {
            final URL website = new URL(url);
            ReadableByteChannel rbc = Channels.newChannel(website.openStream());
            try (FileOutputStream fos = new FileOutputStream(output)) {
                fos.getChannel().transferFrom(rbc, 0, 1 << 24);
            }

            if (verbose) {
                LoggerUtils.info("Downloaded " + url + " to " + output.toString() + ".");
            }
        }
        catch (MalformedURLException ex) {
            plugin.util.debug(ex);
        }
        catch (IOException ex) {
            plugin.util.debug(ex);
        }
    }

    public void debug(Object message) {
        plugin.logger.debug("[TranxCraft] " + message);
    }

    private String minecraftJSON(String username, String password) {
        JSONObject agent = new JSONObject();
        agent.put("name", "Minecraft");
        agent.put("version", 1);

        JSONObject json = new JSONObject();
        json.put("agent", agent);
        json.put("username", username);
        json.put("password", password);

        return json.toJSONString();
    }

    public boolean authenticate(String username, String password) {
        try {
            final byte[] contentBytes = minecraftJSON(username, password).getBytes("UTF-8");
            final URLConnection connection = new URL("https://authserver.mojang.com/authenticate").openConnection();

            connection.setDoInput(true);
            connection.setDoOutput(true);
            connection.setRequestProperty("Accept-Charset", "UTF-8");
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setRequestProperty("Content-Length", Integer.toString(contentBytes.length));

            try (OutputStream requestStream = connection.getOutputStream()) {
                requestStream.write(contentBytes, 0, contentBytes.length);
            }

            return ((HttpURLConnection) connection).getResponseCode() == 200;
        }
        catch (IOException ex) {
            debug(ex);
            return false;
        }
    }

    public String getUuidFromEmail(String email) {
        String uuid = null;
        ConfigurationSection section = plugin.adminConfig.getConfigurationSection("admins");

        for (String key : section.getKeys(false)) {
            if (section.getString(key + ".email").equalsIgnoreCase(email)) {
                uuid = key;
            }
        }

        return uuid;
    }

    public String[] getCredits() {
        return new String[]{"Pravian - BukkitLib", "TotalFreedom - Various Methods", "JeromSar - Programming Help"};
    }
}
