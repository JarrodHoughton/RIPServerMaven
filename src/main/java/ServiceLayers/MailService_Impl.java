/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ServiceLayers;

import DAOs.ReaderDao_Impl;
import DAOs.ReaderDao_Interface;
import Models.Reader;
import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.googleapis.json.GoogleJsonError;
import com.google.api.client.googleapis.json.GoogleJsonResponseException;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.gmail.Gmail;
import com.google.api.services.gmail.model.Message;
import org.apache.commons.codec.binary.Base64;

import jakarta.mail.Session;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Paths;
import java.util.Properties;
import java.util.Set;

import static com.google.api.services.gmail.GmailScopes.GMAIL_SEND;
import static jakarta.mail.Message.RecipientType.TO;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.AddressException;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.security.GeneralSecurityException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author jarro
 */
public class MailService_Impl implements MailService_Interface {

    private static final String SERVER_EMAIL = "readersareinnovators.vzap@gmail.com";
    private static final String CREDENTIALS_FILE_PATH = "/client_secret_888515690154-kbtgv7i9erqcs1okj1p0lg9tvbpg4qab.apps.googleusercontent.com.json";
    private final Gmail service;

    public MailService_Impl() throws IOException, GeneralSecurityException {
        NetHttpTransport httpTransport = GoogleNetHttpTransport.newTrustedTransport();
        GsonFactory jsonFactory = GsonFactory.getDefaultInstance();
        service = new Gmail.Builder(httpTransport, jsonFactory, getCredentials(httpTransport, jsonFactory))
                .setApplicationName("ReadersAreInnovators")
                .build();
    }

    private static Credential getCredentials(final NetHttpTransport httpTransport, GsonFactory jsonFactory)
            throws IOException {
        InputStream in = MailService_Impl.class.getResourceAsStream(CREDENTIALS_FILE_PATH);
        if (in == null) {
            throw new FileNotFoundException("Resource not found: " + CREDENTIALS_FILE_PATH);
        }
        GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(jsonFactory, new InputStreamReader(in));

        GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(
                httpTransport, jsonFactory, clientSecrets, Set.of(GMAIL_SEND))
                .setDataStoreFactory(new FileDataStoreFactory(Paths.get("tokens").toFile()))
                .setAccessType("offline")
                .build();

        LocalServerReceiver receiver = new LocalServerReceiver.Builder().setPort(8888).build();
        return new AuthorizationCodeInstalledApp(flow, receiver).authorize("user");
    }

    public void sendGmail(String userEmail, String message, String subject) throws AddressException, MessagingException, IOException {
        Properties props = new Properties();
        Session session = Session.getDefaultInstance(props, null);
        MimeMessage email = new MimeMessage(session);
        email.setFrom(new InternetAddress(SERVER_EMAIL));
        email.addRecipient(TO, new InternetAddress(userEmail));
        email.setSubject(subject);
        email.setText(message);

        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        email.writeTo(buffer);
        byte[] rawMessageBytes = buffer.toByteArray();
        String encodedEmail = Base64.encodeBase64URLSafeString(rawMessageBytes);
        Message msg = new Message();
        msg.setRaw(encodedEmail);

        try {
            msg = service.users().messages().send("me", msg).execute();
            System.out.println("Message id: " + msg.getId());
            System.out.println(msg.toPrettyString());
        } catch (GoogleJsonResponseException e) {
            GoogleJsonError error = e.getDetails();
            if (error.getCode() == 403) {
                Logger.getLogger(MailService_Impl.class.getName()).log(Level.SEVERE, null, e);
            } else {
                throw e;
            }
        }
    }

    @Override
    public String sendMail(String recipientEmail, String emailContent, String subject) {
        String message = "Email sent successfully!";
        try {
            sendGmail(recipientEmail, emailContent, subject);
        } catch (MessagingException | IOException ex) {
            Logger.getLogger(MailService_Impl.class.getName()).log(Level.SEVERE, null, ex);
            return "Email failed to send.";
        }
        return message;
    }

    @Override
    public String sendVerficationEmail(Reader reader) {
        ReaderDao_Interface readerDao = new ReaderDao_Impl();
        String verificationLink = "http://localhost:8080/RIPClientMaven/LoginController?submit=verifyReader&readerId=" + reader.getId() + "&verifyToken=" + readerDao.getVerifyToken(reader.getId());
        String emailContent
                = "Dear " + reader.getName() + ",\n\n"
                + "Thank you for signing up to our platform!\n\n"
                + "Kindly follow the link below to verify your account:\n"
                + verificationLink + "\n\n"
                + "Kindest Regards," + "\n"
                + "Readers Are Innovators Team!";
        String subject = "Readers Are Innovators: Verify Your Account!";
        return sendMail(reader.getEmail(), emailContent, subject);
    }

    @Override
    public String sendVerificationEmailWithHTML(Reader reader) {
        ReaderDao_Interface readerDao = new ReaderDao_Impl();
        String verificationToken = readerDao.getVerifyToken(reader.getId());
        String verificationLink = "http://localhost:8080/RIPClientMaven/LoginController?submit=verifyReader&readerId=" + reader.getId() + "&verifyToken=" + verificationToken;
        String testContent = "<html lang=\"en\" xmlns=\"http://www.w3.org/1999/xhtml\" xmlns:v=\"urn:schemas-microsoft-com:vml\" xmlns:o=\"urn:schemas-microsoft-com:office:office\">\n"
                + "<head>\n"
                + "    <meta charset=\"utf-8\"> <!-- utf-8 works for most cases -->\n"
                + "    <meta name=\"viewport\" content=\"width=device-width\"> <!-- Forcing initial-scale shouldn't be necessary -->\n"
                + "    <meta http-equiv=\"X-UA-Compatible\" content=\"IE=edge\"> <!-- Use the latest (edge) version of IE rendering engine -->\n"
                + "    <meta name=\"x-apple-disable-message-reformatting\">  <!-- Disable auto-scale in iOS 10 Mail entirely -->\n"
                + "    <title></title> <!-- The title tag shows in email notifications, like Android 4.4. -->\n"
                + "\n"
                + "    <link href=\"https://fonts.googleapis.com/css?family=Lato:300,400,700\" rel=\"stylesheet\">\n"
                + "\n"
                + "    <!-- CSS Reset : BEGIN -->\n"
                + "    <style>\n"
                + "\n"
                + "        /* What it does: Remove spaces around the email design added by some email clients. */\n"
                + "        /* Beware: It can remove the padding / margin and add a background color to the compose a reply window. */\n"
                + "        html,\n"
                + "body {\n"
                + "    margin: 0 auto !important;\n"
                + "    padding: 0 !important;\n"
                + "    height: 100% !important;\n"
                + "    width: 100% !important;\n"
                + "    background: #f1f1f1;\n"
                + "}\n"
                + "\n"
                + "/* What it does: Stops email clients resizing small text. */\n"
                + "* {\n"
                + "    -ms-text-size-adjust: 100%;\n"
                + "    -webkit-text-size-adjust: 100%;\n"
                + "}\n"
                + "\n"
                + "/* What it does: Centers email on Android 4.4 */\n"
                + "div[style*=\"margin: 16px 0\"] {\n"
                + "    margin: 0 !important;\n"
                + "}\n"
                + "\n"
                + "/* What it does: Stops Outlook from adding extra spacing to tables. */\n"
                + "table,\n"
                + "td {\n"
                + "    mso-table-lspace: 0pt !important;\n"
                + "    mso-table-rspace: 0pt !important;\n"
                + "}\n"
                + "\n"
                + "/* What it does: Fixes webkit padding issue. */\n"
                + "table {\n"
                + "    border-spacing: 0 !important;\n"
                + "    border-collapse: collapse !important;\n"
                + "    table-layout: fixed !important;\n"
                + "    margin: 0 auto !important;\n"
                + "}\n"
                + "\n"
                + "/* What it does: Uses a better rendering method when resizing images in IE. */\n"
                + "img {\n"
                + "    -ms-interpolation-mode:bicubic;\n"
                + "}\n"
                + "\n"
                + "/* What it does: Prevents Windows 10 Mail from underlining links despite inline CSS. Styles for underlined links should be inline. */\n"
                + "a {\n"
                + "    text-decoration: none;\n"
                + "}\n"
                + "\n"
                + "/* What it does: A work-around for email clients meddling in triggered links. */\n"
                + "*[x-apple-data-detectors],  /* iOS */\n"
                + ".unstyle-auto-detected-links *,\n"
                + ".aBn {\n"
                + "    border-bottom: 0 !important;\n"
                + "    cursor: default !important;\n"
                + "    color: inherit !important;\n"
                + "    text-decoration: none !important;\n"
                + "    font-size: inherit !important;\n"
                + "    font-family: inherit !important;\n"
                + "    font-weight: inherit !important;\n"
                + "    line-height: inherit !important;\n"
                + "}\n"
                + "\n"
                + "/* What it does: Prevents Gmail from displaying a download button on large, non-linked images. */\n"
                + ".a6S {\n"
                + "    display: none !important;\n"
                + "    opacity: 0.01 !important;\n"
                + "}\n"
                + "\n"
                + "/* What it does: Prevents Gmail from changing the text color in conversation threads. */\n"
                + ".im {\n"
                + "    color: inherit !important;\n"
                + "}\n"
                + "\n"
                + "/* If the above doesn't work, add a .g-img class to any image in question. */\n"
                + "img.g-img + div {\n"
                + "    display: none !important;\n"
                + "}\n"
                + "\n"
                + "/* What it does: Removes right gutter in Gmail iOS app: https://github.com/TedGoas/Cerberus/issues/89  */\n"
                + "/* Create one of these media queries for each additional viewport size you'd like to fix */\n"
                + "\n"
                + "/* iPhone 4, 4S, 5, 5S, 5C, and 5SE */\n"
                + "@media only screen and (min-device-width: 320px) and (max-device-width: 374px) {\n"
                + "    u ~ div .email-container {\n"
                + "        min-width: 320px !important;\n"
                + "    }\n"
                + "}\n"
                + "/* iPhone 6, 6S, 7, 8, and X */\n"
                + "@media only screen and (min-device-width: 375px) and (max-device-width: 413px) {\n"
                + "    u ~ div .email-container {\n"
                + "        min-width: 375px !important;\n"
                + "    }\n"
                + "}\n"
                + "/* iPhone 6+, 7+, and 8+ */\n"
                + "@media only screen and (min-device-width: 414px) {\n"
                + "    u ~ div .email-container {\n"
                + "        min-width: 414px !important;\n"
                + "    }\n"
                + "}\n"
                + "\n"
                + "    </style>\n"
                + "\n"
                + "    <!-- CSS Reset : END -->\n"
                + "\n"
                + "    <!-- Progressive Enhancements : BEGIN -->\n"
                + "    <style>\n"
                + "\n"
                + "	    .primary{\n"
                + "	background: #30e3ca;\n"
                + "}\n"
                + ".bg_white{\n"
                + "	background: #ffffff;\n"
                + "}\n"
                + ".bg_light{\n"
                + "	background: #fafafa;\n"
                + "}\n"
                + ".bg_black{\n"
                + "	background: #000000;\n"
                + "}\n"
                + ".bg_dark{\n"
                + "	background: rgba(0,0,0,.8);\n"
                + "}\n"
                + ".email-section{\n"
                + "	padding:2.5em;\n"
                + "}\n"
                + "\n"
                + "/*BUTTON*/\n"
                + ".btn{\n"
                + "	padding: 10px 15px;\n"
                + "	display: inline-block;\n"
                + "}\n"
                + ".btn.btn-primary{\n"
                + "	border-radius: 5px;\n"
                + "	background: #30e3ca;\n"
                + "	color: #ffffff;\n"
                + "}\n"
                + ".btn.btn-white{\n"
                + "	border-radius: 5px;\n"
                + "	background: #ffffff;\n"
                + "	color: #000000;\n"
                + "}\n"
                + ".btn.btn-white-outline{\n"
                + "	border-radius: 5px;\n"
                + "	background: transparent;\n"
                + "	border: 1px solid #fff;\n"
                + "	color: #fff;\n"
                + "}\n"
                + ".btn.btn-black-outline{\n"
                + "	border-radius: 0px;\n"
                + "	background: transparent;\n"
                + "	border: 2px solid #000;\n"
                + "	color: #000;\n"
                + "	font-weight: 700;\n"
                + "}\n"
                + "\n"
                + "h1,h2,h3,h4,h5,h6{\n"
                + "	font-family: 'Lato', sans-serif;\n"
                + "	color: #000000;\n"
                + "	margin-top: 0;\n"
                + "	font-weight: 400;\n"
                + "}\n"
                + "\n"
                + "body{\n"
                + "	font-family: 'Lato', sans-serif;\n"
                + "	font-weight: 400;\n"
                + "	font-size: 15px;\n"
                + "	line-height: 1.8;\n"
                + "	color: rgba(0,0,0,.4);\n"
                + "}\n"
                + "\n"
                + "a{\n"
                + "	color: #30e3ca;\n"
                + "}\n"
                + "\n"
                + "table{\n"
                + "}\n"
                + "/*LOGO*/\n"
                + "\n"
                + ".logo h1{\n"
                + "	margin: 0;\n"
                + "}\n"
                + ".logo h1 a{\n"
                + "	color: #30e3ca;\n"
                + "	font-size: 24px;\n"
                + "	font-weight: 700;\n"
                + "	font-family: 'Lato', sans-serif;\n"
                + "}\n"
                + "\n"
                + "/*HERO*/\n"
                + ".hero{\n"
                + "	position: relative;\n"
                + "	z-index: 0;\n"
                + "}\n"
                + "\n"
                + ".hero .text{\n"
                + "	color: rgba(0,0,0,.3);\n"
                + "}\n"
                + ".hero .text h2{\n"
                + "	color: #000;\n"
                + "	font-size: 40px;\n"
                + "	margin-bottom: 0;\n"
                + "	font-weight: 400;\n"
                + "	line-height: 1.4;\n"
                + "}\n"
                + ".hero .text h3{\n"
                + "	font-size: 24px;\n"
                + "	font-weight: 300;\n"
                + "}\n"
                + ".hero .text h2 span{\n"
                + "	font-weight: 600;\n"
                + "	color: #30e3ca;\n"
                + "}\n"
                + "\n"
                + "\n"
                + "/*HEADING SECTION*/\n"
                + ".heading-section{\n"
                + "}\n"
                + ".heading-section h2{\n"
                + "	color: #000000;\n"
                + "	font-size: 28px;\n"
                + "	margin-top: 0;\n"
                + "	line-height: 1.4;\n"
                + "	font-weight: 400;\n"
                + "}\n"
                + ".heading-section .subheading{\n"
                + "	margin-bottom: 20px !important;\n"
                + "	display: inline-block;\n"
                + "	font-size: 13px;\n"
                + "	text-transform: uppercase;\n"
                + "	letter-spacing: 2px;\n"
                + "	color: rgba(0,0,0,.4);\n"
                + "	position: relative;\n"
                + "}\n"
                + ".heading-section .subheading::after{\n"
                + "	position: absolute;\n"
                + "	left: 0;\n"
                + "	right: 0;\n"
                + "	bottom: -10px;\n"
                + "	content: '';\n"
                + "	width: 100%;\n"
                + "	height: 2px;\n"
                + "	background: #30e3ca;\n"
                + "	margin: 0 auto;\n"
                + "}\n"
                + "\n"
                + ".heading-section-white{\n"
                + "	color: rgba(255,255,255,.8);\n"
                + "}\n"
                + ".heading-section-white h2{\n"
                + "	font-family: \n"
                + "	line-height: 1;\n"
                + "	padding-bottom: 0;\n"
                + "}\n"
                + ".heading-section-white h2{\n"
                + "	color: #ffffff;\n"
                + "}\n"
                + ".heading-section-white .subheading{\n"
                + "	margin-bottom: 0;\n"
                + "	display: inline-block;\n"
                + "	font-size: 13px;\n"
                + "	text-transform: uppercase;\n"
                + "	letter-spacing: 2px;\n"
                + "	color: rgba(255,255,255,.4);\n"
                + "}\n"
                + "\n"
                + "\n"
                + "ul.social{\n"
                + "	padding: 0;\n"
                + "}\n"
                + "ul.social li{\n"
                + "	display: inline-block;\n"
                + "	margin-right: 10px;\n"
                + "}\n"
                + "\n"
                + "/*FOOTER*/\n"
                + "\n"
                + ".footer{\n"
                + "	border-top: 1px solid rgba(0,0,0,.05);\n"
                + "	color: rgba(0,0,0,.5);\n"
                + "}\n"
                + ".footer .heading{\n"
                + "	color: #000;\n"
                + "	font-size: 20px;\n"
                + "}\n"
                + ".footer ul{\n"
                + "	margin: 0;\n"
                + "	padding: 0;\n"
                + "}\n"
                + ".footer ul li{\n"
                + "	list-style: none;\n"
                + "	margin-bottom: 10px;\n"
                + "}\n"
                + ".footer ul li a{\n"
                + "	color: rgba(0,0,0,1);\n"
                + "}\n"
                + "\n"
                + "\n"
                + "@media screen and (max-width: 500px) {\n"
                + "\n"
                + "\n"
                + "}\n"
                + "\n"
                + "\n"
                + "    </style>\n"
                + "\n"
                + "\n"
                + "</head>\n"
                + "\n"
                + "<body width=\"100%\" style=\"margin: 0; padding: 0 !important; mso-line-height-rule: exactly; background-color: #f1f1f1;\">\n"
                + "	<center style=\"width: 100%; background-color: #f1f1f1;\">\n"
                + "    <div style=\"display: none; font-size: 1px;max-height: 0px; max-width: 0px; opacity: 0; overflow: hidden; mso-hide: all; font-family: sans-serif;\">\n"
                + "      &zwnj;&nbsp;&zwnj;&nbsp;&zwnj;&nbsp;&zwnj;&nbsp;&zwnj;&nbsp;&zwnj;&nbsp;&zwnj;&nbsp;&zwnj;&nbsp;&zwnj;&nbsp;&zwnj;&nbsp;&zwnj;&nbsp;&zwnj;&nbsp;&zwnj;&nbsp;&zwnj;&nbsp;&zwnj;&nbsp;&zwnj;&nbsp;&zwnj;&nbsp;&zwnj;&nbsp;\n"
                + "    </div>\n"
                + "    <div style=\"max-width: 600px; margin: 0 auto;\" class=\"email-container\">\n"
                + "    	<!-- BEGIN BODY -->\n"
                + "      <table align=\"center\" role=\"presentation\" cellspacing=\"0\" cellpadding=\"0\" border=\"0\" width=\"100%\" style=\"margin: auto;\">\n"
                + "      	<tr>\n"
                + "          <td valign=\"top\" class=\"bg_white\" style=\"padding: 1em 2.5em 0 2.5em;\">\n"
                + "          	<table role=\"presentation\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\">\n"
                + "          		<tr>\n"
                + "          			<td class=\"logo\" style=\"text-align: center;\">\n"
                + "			            <h1><a href=\"http://localhost:8080/RIPClientMaven/index.jsp\">Readers Are Innovators</a></h1>\n"
                + "			          </td>\n"
                + "          		</tr>\n"
                + "          	</table>\n"
                + "          </td>\n"
                + "	      </tr><!-- end tr -->\n"
                + "	      <tr>\n"
                + "          <td valign=\"middle\" class=\"hero bg_white\" style=\"padding: 3em 0 2em 0;\">\n"
                + "            <img src=\"images/email.png\" alt=\"\" style=\"width: 300px; max-width: 600px; height: auto; margin: auto; display: block;\">\n"
                + "          </td>\n"
                + "	      </tr><!-- end tr -->\n"
                + "				<tr>\n"
                + "          <td valign=\"middle\" class=\"hero bg_white\" style=\"padding: 2em 0 4em 0;\">\n"
                + "            <table>\n"
                + "            	<tr>\n"
                + "            		<td>\n"
                + "            			<div class=\"text\" style=\"padding: 0 2.5em; text-align: center;\">\n"
                + "            				<h2>Please verify your email</h2>\n"
                + "            				<p><a href=\"" + verificationLink + "\" class=\"btn btn-primary\"><button style=\\\"background-color: #30e3ca; color: white; padding: 10px 20px; text-align: center; text-decoration: none; display: inline-block; font-size: 16px; margin: 4px 2px; cursor: pointer;\\\">Verify Account</button></a></p>\n"
                + "            			</div>\n"
                + "            		</td>\n"
                + "            	</tr>\n"
                + "            </table>\n"
                + "          </td>\n"
                + "	      </tr><!-- end tr -->\n"
                + "      <!-- 1 Column Text + Button : END -->\n"
                + "    </div>\n"
                + "  </center>\n"
                + "</body>\n"
                + "</html>";
        String emailContent = "<html><body>"
                + "<h2>Dear " + reader.getName() + ",</h2>"
                + "<p>Thank you for signing up to our platform!</p>"
                + "<p>Kindly click the button below to verify your account:</p>"
                + "<p><a href=\"" + verificationLink + "\">"
                + "<button style=\"background-color: #4CAF50; color: white; padding: 10px 20px; text-align: center; text-decoration: none; display: inline-block; font-size: 16px; margin: 4px 2px; cursor: pointer;\">Verify Account</button>"
                + "</a></p>"
                + "<p>Kindest Regards,<br>Readers Are Innovators Team!</p>"
                + "</body></html>";
        String subject = "Readers Are Innovators: Verify Your Account!";

        if (sendMailWithHTML(reader.getEmail(), testContent, subject)) {
            return "A verification email has been sent to you. Please verify your account before logging into Readers Are Innovators again.";
        } else {
            return "Something went wrong... Please make sure you have registered with a valid email address.";
        }
    }

    public Boolean sendMailWithHTML(String recipientEmail, String emailContent, String subject) {
        Boolean emailSent = false;
        try {
            MimeMessage email = new MimeMessage(Session.getDefaultInstance(new Properties()));
            email.setFrom(new InternetAddress(SERVER_EMAIL));
            email.addRecipient(TO, new InternetAddress(recipientEmail));
            email.setSubject(subject);
            email.setContent(emailContent, "text/html; charset=utf-8");

            ByteArrayOutputStream buffer = new ByteArrayOutputStream();
            email.writeTo(buffer);
            byte[] rawMessageBytes = buffer.toByteArray();
            String encodedEmail = Base64.encodeBase64URLSafeString(rawMessageBytes);
            Message message = new Message();
            message.setRaw(encodedEmail);

            message = service.users().messages().send("me", message).execute();
            System.out.println("Message id: " + message.getId());
            System.out.println(message.toPrettyString());
            emailSent = true;
        } catch (MessagingException | IOException ex) {
            Logger.getLogger(MailService_Impl.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
        return emailSent;
    }

}
