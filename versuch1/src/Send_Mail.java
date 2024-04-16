import java.util.Properties;

import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage; 

public class Send_Mail {
	public static void main(String[] args) {
		sendMail();   
	}
	
	public static void sendMail() {
		try {
			// your code here
			String sender = "labrat@localhost";
			String recipient = "labrat@localhost";
			String host = "localhost";

			Properties props = System.getProperties();
			props.setProperty("mail.smtp.host", host);
			Session session = Session.getDefaultInstance(props);

			Message message = new MimeMessage(session);
			message.setFrom(new InternetAddress(sender));
			message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipient));
			message.setSubject("Versuch 1");
			message.setText("Hallo, wir testen Versuch 1!");

			Transport.send(message);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
