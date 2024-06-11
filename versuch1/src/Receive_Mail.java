import java.util.Properties;

import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Store;

public class Receive_Mail {
	public static void main(String[] args) throws Exception {
		fetchMail();
	}
	
	public static void fetchMail() {
		try {
			// your code here
			String username = "labrat";
			String password = "kn1lab";
			String folderPath = "/home/labrat/Maildir/new/";
			String host = "localhost";

			Properties props = System.getProperties();
			props.put("mail.pop3.host", host);
			Session session = Session.getDefaultInstance(props);

			Store store = session.getStore("pop3");
			store.connect(host, username, password);

			Folder folder = store.getFolder("INBOX");
			folder.open(Folder.READ_ONLY);

			Message[] messages = folder.getMessages();

			for (int i = 0; i < messages.length; i++) {
				System.out.println("Message " + (i + 1));
				System.out.println("From : " + messages[i].getFrom()[0]);
				System.out.println("Subject : " + messages[i].getSubject());
				System.out.println("Sent Date : " + messages[i].getSentDate());
				System.out.println("Text :" + messages[i].getContent().toString());
				System.out.println();
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
}
