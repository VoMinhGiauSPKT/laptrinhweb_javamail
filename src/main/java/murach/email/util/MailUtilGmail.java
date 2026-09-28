package murach.email.util;

import java.util.Properties;
import jakarta.mail.*;
import jakarta.mail.internet.*;

public class MailUtilGmail {

    private static final String GMAIL_USERNAME = "minhgiauad2024@gmail.com";
    private static final String GMAIL_PASSWORD = "jfvnfblhcvvugaxm";

    public static void sendMail(String to, String from,
            String subject, String body, boolean bodyIsHTML)
            throws MessagingException {

        // 1 - get a mail session (SMTP STARTTLS qua cổng 587)
        Properties props = new Properties();
        props.put("mail.transport.protocol", "smtp");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.starttls.required", "true");
        props.put("mail.smtp.ssl.protocols", "TLSv1.2 TLSv1.3");
        props.put("mail.smtp.ssl.trust", "smtp.gmail.com");
        props.put("mail.smtp.quitwait", "false");
        
        Session session = Session.getInstance(props);
        session.setDebug(true);

        // 2 - create a message
        Message message = new MimeMessage(session);
        message.setSubject(subject);
        if (bodyIsHTML) {
            message.setContent(body, "text/html; charset=UTF-8");
        } else {
            message.setText(body);
        }

        // 3 - address the message
        Address fromAddress = new InternetAddress(from != null && !from.isEmpty() ? from : GMAIL_USERNAME);
        Address toAddress = new InternetAddress(to);
        message.setFrom(fromAddress);
        message.setRecipient(Message.RecipientType.TO, toAddress);

        // 4 - send the message
        Transport transport = session.getTransport("smtp");
        transport.connect("smtp.gmail.com", 587, GMAIL_USERNAME, GMAIL_PASSWORD);
        transport.sendMessage(message, message.getAllRecipients());
        transport.close();
    }
}
