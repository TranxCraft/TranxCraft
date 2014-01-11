
package com.wickedgaminguk.TranxCraft;

import java.util.*;
import javax.mail.*;
import javax.mail.internet.*;

public class TCP_Mail {
    
    public enum RecipientType {
        SYS, EXECUTIVE, LEADADMIN, ADMIN, MODERATOR, ALL;
        
        private final List<String> addresses;
        
        RecipientType() {
            addresses = new ArrayList<String>();
        }
        
        public void addAll(List<String> addresses) {
            this.addresses.addAll(addresses);
        }
        
        public List<String> getAddresses() {
            return addresses;
        }
    }
   
    static {
        RecipientType.SYS.addAll(TCP_Util.getConfigFile().getStringList("SYS_MAIL"));
        RecipientType.EXECUTIVE.addAll(TCP_Util.getConfigFile().getStringList("EXECUTIVE_MAIL"));
        RecipientType.LEADADMIN.addAll(TCP_Util.getConfigFile().getStringList("LEADADMIN_MAIL"));
        RecipientType.ADMIN.addAll(TCP_Util.getConfigFile().getStringList("ADMIN_MAIL"));
        RecipientType.MODERATOR.addAll(TCP_Util.getConfigFile().getStringList("MODERATOR_MAIL"));
        
        RecipientType.ALL.addAll(TCP_Util.getConfigFile().getStringList("SYS_MAIL"));
        RecipientType.ALL.addAll(TCP_Util.getConfigFile().getStringList("EXECUTIVE_MAIL"));
        RecipientType.ALL.addAll(TCP_Util.getConfigFile().getStringList("LEADADMIN_MAIL"));
        RecipientType.ALL.addAll(TCP_Util.getConfigFile().getStringList("ADMIN_MAIL"));
        RecipientType.ALL.addAll(TCP_Util.getConfigFile().getStringList("MODERATOR_MAIL"));
    }
    
    public void send(RecipientType rt, String Subject, String Msg) {
        
        String host = TCP_Util.getConfigFile().getString("SMTP_HOST");
        String port = TCP_Util.getConfigFile().getString("SMTP_PORT");
        final String user = TCP_Util.getConfigFile().getString("LOGIN_USERNAME");
        final String pass = TCP_Util.getConfigFile().getString("LOGIN_PASSWORD");
        String from = TCP_Util.getConfigFile().getString("EMAIL_FROM");
        
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
        
        for(String recipients: rt.getAddresses()) {
            try { 
                Message message = new MimeMessage(session);
                message.setFrom(new InternetAddress(from));
                message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipients));
                message.setSubject(Subject);
                message.setText(Msg);

                Transport.send(message);
                TCP_Log.info("E-mail Successfully Sent");
            }
            catch (MessagingException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
