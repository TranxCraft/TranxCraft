
package com.wickedgaminguk.TranxCraft;

import java.util.*;
import javax.mail.*;
import javax.mail.internet.*;

public class TCP_Mail {
    public static void send(String Subject, String Msg) {
        
        String Recipients = TCP_Util.getConfigFile().getString("EMAIL_TO");
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
        
        try { 
            Message message = new MimeMessage(session);
	    message.setFrom(new InternetAddress(from));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(Recipients));
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
