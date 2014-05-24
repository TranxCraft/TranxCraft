package com.wickedgaminguk.tranxcraft;

import java.util.ArrayList;
import java.util.List;
import net.pravian.bukkitlib.util.LoggerUtils;
import org.apache.commons.mail.DefaultAuthenticator;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.HtmlEmail;

public class TCP_Mail {

    private final TranxCraft plugin;

    public TCP_Mail(TranxCraft plugin) {
        this.plugin = plugin;
    }

    public enum RecipientType {

        COMMANDER, EXECUTIVE, LEADADMIN, ADMIN, MODERATOR, ALL;

        private final List<String> addresses;

        RecipientType() {
            addresses = new ArrayList<>();
        }

        public void addAll(List<String> addresses) {
            this.addresses.addAll(addresses);
        }

        public List<String> getAddresses() {
            return addresses;
        }
    }

    private void addRecipientTypes() {
        RecipientType.COMMANDER.addAll(plugin.config.getStringList("commander_mail"));
        RecipientType.EXECUTIVE.addAll(plugin.config.getStringList("executive_mail"));
        RecipientType.LEADADMIN.addAll(plugin.config.getStringList("leadadmin_mail"));
        RecipientType.ADMIN.addAll(plugin.config.getStringList("admin_mail"));
        RecipientType.MODERATOR.addAll(plugin.config.getStringList("moderator_mail"));

        RecipientType.ALL.addAll(plugin.config.getStringList("commander_mail"));
        RecipientType.ALL.addAll(plugin.config.getStringList("executive_mail"));
        RecipientType.ALL.addAll(plugin.config.getStringList("leadadmin_mail"));
        RecipientType.ALL.addAll(plugin.config.getStringList("admin_mail"));
        RecipientType.ALL.addAll(plugin.config.getStringList("moderator_mail"));
    }

    public void send(RecipientType rt, String subject, String msg) {
        addRecipientTypes();
        
        rt.getAddresses().stream().forEach((String recipients) -> {
            send(recipients, subject, msg);
        });
    }

    public void send(String[] addresses, String subject, String msg) {
        for (String recipients : addresses) {
            send(recipients, subject, msg);
        }
    }

    public void send(String address, String subject, String msg) {
        final String host = plugin.config.getString("smtp_host");
        final int port = plugin.config.getInt("smtp_port");
        final String user = plugin.config.getString("login_username");
        final String pass = plugin.config.getString("login_password");
        final String from = plugin.config.getString("email_from");
        
        Thread.currentThread().setContextClassLoader(getClass().getClassLoader());
        
        try {
            HtmlEmail email = new HtmlEmail();
            email.setHostName(host);
            email.setSmtpPort(port);
            email.setAuthenticator(new DefaultAuthenticator(user, pass));
            email.setTLS(true);
            email.setFrom(from);
            email.setSubject(subject);
            email.setHtmlMsg(msg);
            email.addTo(address);
            email.send();
        }
        catch (EmailException ex) {
            LoggerUtils.severe(ex);
        }
    }
}
