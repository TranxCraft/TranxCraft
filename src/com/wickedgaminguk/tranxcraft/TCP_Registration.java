package com.wickedgaminguk.tranxcraft;

import java.sql.SQLException;
import java.util.UUID;
import net.pravian.bukkitlib.util.LoggerUtils;
import org.bukkit.scheduler.BukkitRunnable;

public class TCP_Registration extends BukkitRunnable {

    private final TranxCraft plugin;
    private final TCP_Util TCP_Util;
    private final UUID uuid;
    private final String email;
    private final String username;

    public TCP_Registration(TranxCraft instance, UUID uuid, String email, String username) {
        this.plugin = instance;
        this.TCP_Util = new TCP_Util(plugin);
        this.uuid = uuid;
        this.email = email;
        this.username = username;
    }

    @Override
    public void run() {        
        int n = 0;
        double salt = Math.random() * 150;
        
        String pass = uuid.toString();
        String userPassword = TCP_Util.sha1Hash(pass + salt);
        
        while (n < 1000) {
            pass = TCP_Util.sha512Hash(userPassword + salt);
            n++;
        }
        
        try {
            plugin.updateRegistrationDatabase("INSERT INTO server_users.users_login (username, uuid, email, password, salt) VALUES ('" + username + "', '" + uuid + "', '" + email + "', '" + pass + "', '" + salt + "');");
        }
        catch (SQLException ex) {
            LoggerUtils.severe(plugin, "Failure to connect to the Registration Server.\n" + ex);
        }
        
        plugin.mail.send(email, "TranxCraft User Panel - Registration", "Hello " + username + ",<br>Thank you for registering for the TranxCraft User Panel, below you will see your details for logging in:<br><b>Username</b>: " + username + "<br><b>Password</b>: " + userPassword + "<br><b>Control Panel</b>: https://cp.tranxcraft.com/");
    }    
}
