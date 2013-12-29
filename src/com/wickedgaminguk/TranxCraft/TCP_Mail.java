
package com.wickedgaminguk.TranxCraft;

import java.util.*;
import javax.mail.*;
import javax.mail.internet.*;

public class TCP_Mail {
    public static void send(String Subject, String Msg) {
        
        String Recipients = TCP_Util.getConfigFile().getString("e-mails");
        
        Properties props = new Properties();
	props.put("mail.smtp.host", "ns1.uservenet.com");
	props.put("mail.smtp.socketFactory.port", "465");
	props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
	props.put("mail.smtp.auth", "true");
	props.put("mail.smtp.port", "465");
                
        Session session = Session.getInstance(props, new javax.mail.Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication("reports@tranxcraft.net","<masked>");
            }
        });
        
        try { 
            Message message = new MimeMessage(session);
	    message.setFrom(new InternetAddress("reports@tranxcraft.net"));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(Recipients));
	    message.setSubject(Subject);
	    message.setText(Msg);
                    
            Transport.send(message); 
	}
        catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }
}
