package kr.openbase.adcsmart.service.impl;

import java.util.Properties;

//import javax.mail.Address;
//import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
//import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class OBEmailImpl {
//	public static void main(String[] args)
//	{
//		final String username = "username@gmail.com";
//		final String password = "password";
// 
//		Properties props = new Properties();
//		props.put("mail.smtp.auth", "true");
//		props.put("mail.smtp.starttls.enable", "true");
//		props.put("mail.smtp.host", "smtp.gmail.com");
//		props.put("mail.smtp.port", "587");
// 
//		Session session = Session.getInstance(props,
//		  new javax.mail.Authenticator() {
//			protected PasswordAuthentication getPasswordAuthentication() {
//				return new PasswordAuthentication(username, password);
//			}
//		  });
// 
//		try {
// 
//			Message message = new MimeMessage(session);
//			message.setFrom(new InternetAddress("from-email@gmail.com"));
//			message.setRecipients(Message.RecipientType.TO,
//				InternetAddress.parse("to-email@gmail.com"));
//			message.setSubject("Testing Subject");
//			message.setText("Dear Mail Crawler,"
//				+ "\n\n No spam to my email, please!");
// 
//			Transport.send(message);
// 
//			System.out.println("Done");
// 
//		} catch (MessagingException e) {
//			throw new RuntimeException(e);
//		}
//	}

//	public static void main(String[] args)
//	{
//		try
//		{
//			String recipients[ ] = {"bwpark@openbase.co.kr"};
////			String recipients[ ] = {"bwpark2000@gmail.com"};
//			new OBEmailImpl().postMail(recipients, "test", "hohoho", "bwpark@openbase.co.kr");
//		}
//		catch(Exception e)
//		{
//			e.printStackTrace();
//		}
//	}

//	 public static void main(String[] args)
//	 {
//	  Properties p = new Properties();
//	  p.put("mail.smtp.user", "bwpark000@gmail.com"); // Google계정@gmail.com으로 설정
//	  p.put("mail.smtp.host", "smtp.gmail.com");
//	  p.put("mail.smtp.port", "465");
//	  p.put("mail.smtp.starttls.enable","true");
//	  p.put( "mail.smtp.auth", "true");
//
//	  p.put("mail.smtp.debug", "true");
//	  p.put("mail.smtp.socketFactory.port", "465");
//	  p.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
//	  p.put("mail.smtp.socketFactory.fallback", "false");
//
//	  //Security.addProvider(new com.sun.net.ssl.internal.ssl.Provider());
//
//	  try {
//	   Authenticator auth = new SMTPAuthenticator();
//	   Session session = Session.getInstance(p, auth);
//	   session.setDebug(true); // 메일을 전송할 때 상세한 상황을 콘솔에 출력한다.
//
//	   //session = Session.getDefaultInstance(p);
//	   MimeMessage msg = new MimeMessage(session);
//	   String message = "Gmail SMTP 서버를 이용한 JavaMail 테스트";
//	   msg.setSubject("Gmail SMTP 서버를 이용한 JavaMail 테스트");
//	   Address fromAddr = new InternetAddress("bwpark2000@lycos.co.kr"); // 보내는 사람의 메일주소
//	   msg.setFrom(fromAddr);
//	   Address toAddr = new InternetAddress("bwpark@openbase.co.kr");  // 받는 사람의 메일주소
//	   msg.addRecipient(Message.RecipientType.TO, toAddr);
//	   msg.setContent(message, "text/plain;charset=KSC5601");
//	   System.out.println("Message: " + msg.getContent());
//	   Transport.send(msg);
//	   System.out.println("Gmail SMTP서버를 이용한 메일보내기 성공");
//	  }
//	  catch (Exception mex) { // Prints all nested (chained) exceptions as well
//	   System.out.println("I am here??? ");
//	   mex.printStackTrace();
//	  }
//	 }

//	 public static void main(String[] args)
//	 {
//		 String recipients[ ]={"bwpark2000@lycos.co.kr", "bwpark@openbase.co.kr"};
//		 String bodyMessage = "This is actual message\n aaaaa\n bbbb\n   * ccc\n";
//		 // summary
//
//		 // Time: 
//		 // Type: Virtual Server or Real server, ADC System, ADCSMART System
//		 // Contents: 
//		 // Solution: - 
//		 // 
//		 
//		 String from = "system@adcsmart.com";
//		 String subject = "[ADCSMART] alert message";
//		 
//		 try
//		 {
//			 new OBEmailImpl().postMail(recipients, subject, bodyMessage , from);
//		 }
//		 catch(Exception e)
//		 {
//			 e.printStackTrace();
//		 }
//		 
////		 // Recipient's email ID needs to be mentioned.
//////	      String to = "neutrin0@daum.net";
////	      String to = "bwpark2000@lycos.co.kr";
//////	      String to = "bwpark@openbase.co.kr";
////
////	      // Sender's email ID needs to be mentioned
////	      String from = "admin@adcsmart.com";
////
////	      // Assuming you are sending email from localhost
////	      String host = "localhost";
////
////	      // Get system properties
////	      Properties properties = System.getProperties();
////
////	      // Setup mail server
////	      properties.setProperty("mail.smtp.host", host);
////
////	      // Get the default Session object.
////	      Session session = Session.getDefaultInstance(properties);
////
////	      try{
////	         // Create a default MimeMessage object.
////	         MimeMessage message = new MimeMessage(session);
////
////	         // Set From: header field of the header.
////	         message.setFrom(new InternetAddress(from));
////
////	         // Set To: header field of the header.
////	         message.addRecipient(Message.RecipientType.TO,
////	                                  new InternetAddress(to));
////
////	         // Set Subject: header field
////	         message.setSubject("This is the Subject Line!333");
////
////	         // Now set the actual message
////	         message.setText("This is actual message\n aaaaa\n bbbb\n   ccc\n");
////
////	         // Send message
////	         Transport.send(message);
////	         System.out.println("Sent message successfully....");
////	      }catch (MessagingException mex) {
////	         mex.printStackTrace();
////	      }
//    }
//	private static class SMTPAuthenticator extends javax.mail.Authenticator 
//	{
//		  public PasswordAuthentication getPasswordAuthentication() 
//		  {
//			  return new PasswordAuthentication("bwpark2000", "no1rnrmf"); // Google id, pwd, 주의) @gmail.com 은 제외하세요
//		  }
//	 } 

	public void postMail(String recipients[], String subject, String bodyMessage, String from)
			throws MessagingException {
		String host = "localhost";

		// Get system properties
		Properties properties = System.getProperties();

		// Setup mail server
		properties.setProperty("mail.smtp.host", host);

		// Get the default Session object.
		Session session = Session.getDefaultInstance(properties);

		try {
			// Create a default MimeMessage object.
			MimeMessage message = new MimeMessage(session);

			// Set From: header field of the header.
			message.setFrom(new InternetAddress(from));

			// Set To: header field of the header.
			for (String rcv : recipients)
				message.addRecipient(Message.RecipientType.TO, new InternetAddress(rcv));

			// Set Subject: header field
			message.setSubject(subject);

			// Now set the actual message
			message.setText(bodyMessage);

			// Send message
			Transport.send(message);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
