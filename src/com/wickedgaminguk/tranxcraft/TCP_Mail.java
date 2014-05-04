package com.wickedgaminguk.tranxcraft;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import net.pravian.bukkitlib.util.LoggerUtils;

public class TCP_Mail {

    private final TranxCraft plugin;

    public TCP_Mail(TranxCraft plugin) {
        this.plugin = plugin;
    }

    public enum RecipientType {

        SYS, EXECUTIVE, LEADADMIN, ADMIN, MODERATOR, ALL;

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
        RecipientType.SYS.addAll(plugin.config.getStringList("sys_mail"));
        RecipientType.EXECUTIVE.addAll(plugin.config.getStringList("executive_mail"));
        RecipientType.LEADADMIN.addAll(plugin.config.getStringList("leadadmin_mail"));
        RecipientType.ADMIN.addAll(plugin.config.getStringList("admin_mail"));
        RecipientType.MODERATOR.addAll(plugin.config.getStringList("moderator_mail"));

        RecipientType.ALL.addAll(plugin.config.getStringList("sys_mail"));
        RecipientType.ALL.addAll(plugin.config.getStringList("executive_mail"));
        RecipientType.ALL.addAll(plugin.config.getStringList("leadadmin_mail"));
        RecipientType.ALL.addAll(plugin.config.getStringList("admin_mail"));
        RecipientType.ALL.addAll(plugin.config.getStringList("moderator_mail"));
    }

    public void send(RecipientType rt, String Subject, String Msg) {
        addRecipientTypes();
        String host = plugin.config.getString("smtp_host");
        String port = plugin.config.getString("smtp_port");
        final String user = plugin.config.getString("login_username");
        final String pass = plugin.config.getString("login_password");
        String from = plugin.config.getString("email_from");

        Properties props = new Properties();
        props.put("mail.smtp.host", host);
        props.put("mail.smtp.socketFactory.port", port);
        props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.port", port);

        Session session = Session.getInstance(props, new javax.mail.Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(user, pass);
            }
        });

        for (String recipients : rt.getAddresses()) {
            try {
                Message message = new MimeMessage(session);
                message.setFrom(new InternetAddress(from));
                message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipients));
                message.setSubject(Subject);
                message.setText(Msg);

                Transport.send(message);
                LoggerUtils.info(plugin, "E-mail Successfully Sent");
            }
            catch (MessagingException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void send(String[] Addresses, String Subject, String Msg) {
        String host = plugin.config.getString("smtp_host");
        String port = plugin.config.getString("smtp_port");
        final String user = plugin.config.getString("login_username");
        final String pass = plugin.config.getString("login_password");
        String from = plugin.config.getString("email_from");

        Properties props = new Properties();
        props.put("mail.smtp.host", host);
        props.put("mail.smtp.socketFactory.port", port);
        props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.port", port);

        Session session = Session.getInstance(props, new javax.mail.Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(user, pass);
            }
        });

        for (String recipients : Addresses) {
            try {
                Message message = new MimeMessage(session);
                message.setFrom(new InternetAddress(from));
                message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipients));
                message.setSubject(Subject);
                message.setText(Msg);

                Transport.send(message);
                LoggerUtils.info(plugin, "E-mail Successfully Sent");
            }
            catch (MessagingException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void send(String Address, String Subject, String Msg) {
        String host = plugin.config.getString("smtp_host");
        String port = plugin.config.getString("smtp_port");
        final String user = plugin.config.getString("login_username");
        final String pass = plugin.config.getString("login_password");
        String from = plugin.config.getString("email_from");

        Properties props = new Properties();
        props.put("mail.smtp.host", host);
        props.put("mail.smtp.socketFactory.port", port);
        props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.port", port);

        Session session = Session.getInstance(props, new javax.mail.Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(user, pass);
            }
        });

        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(from));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(Address));
            message.setSubject(Subject);
            message.setText(Msg);

            Transport.send(message);
            LoggerUtils.info(plugin, "E-mail Successfully Sent");
        }
        catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }
}
