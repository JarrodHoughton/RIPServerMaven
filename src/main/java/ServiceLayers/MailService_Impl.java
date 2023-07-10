/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ServiceLayers;

import Models.Reader;
import Utils.GetProperties;
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
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Jarrod
 */
public class MailService_Impl implements MailService_Interface {
    private ReaderService_Interface readerService = null;
    private static final String SERVER_EMAIL = "readersareinnovators.platform@gmail.com";
    private static final String CREDENTIALS_FILE_PATH = "/client_secret_866186881474-3qm8fihh7svcdath62nhv9j127fpdar5.apps.googleusercontent.com.json";
    private final Gmail service;
    private String clientUrl; 
    private SMSService_Interface smsService;

    public MailService_Impl() throws IOException, GeneralSecurityException {
        this.clientUrl = new GetProperties("config.properties").get("clientUrl");
        this.emailTemplate = this.emailTemplate.replace("[clientUrl]", clientUrl);
        smsService = new SMSService_Impl();
        readerService = new ReaderService_Impl();
        NetHttpTransport httpTransport = GoogleNetHttpTransport.newTrustedTransport();
        GsonFactory jsonFactory = GsonFactory.getDefaultInstance();
        service = new Gmail.Builder(httpTransport, jsonFactory, getCredentials(httpTransport, jsonFactory))
                .setApplicationName("readersareinnovators-391220")
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
    public String sendVerificationEmail(Reader reader) {
        String verificationToken = readerService.getVerifyToken(reader.getId());
        String smsMessage = 
                "RIP Verify Account: \n"
                + clientUrl + "LoginController?submit=verifyReader&readerId=" + reader.getId() + "&verifyToken=" + verificationToken;
        smsService.sendSMS(reader.getPhoneNumber(), smsMessage);
        String verificationLink = "LoginController?submit=verifyReader&readerId=" + reader.getId() + "&verifyToken=" + verificationToken;
        String subject = "Readers Are Innovators: Verify Your Account!";
        String message
                = 
                "Thank you for signing up to our platform!\n\n"
                + "Kindly follow the link below to verify your account:\n"
                + "Kindest Regards," + "\n"
                + "Readers Are Innovators Team!";
        String emailContent = emailTemplate.replace(Reader_Name_Location, reader.getName() + " " + reader.getSurname());
        emailContent = emailContent.replace(Link_Location, verificationLink);
        emailContent = emailContent.replace(Message_Location, message);
        emailContent = emailContent.replace(Link_Button_Message, "Verify");
        if (sendMailWithHTML(reader.getEmail(), emailContent, subject)) {
            return "A verification link has been sent to you. Please verify your account before logging into Readers Are Innovators again.";
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

            try {
                message = service.users().messages().send("me", message).execute();
                System.out.println("Message id: " + message.getId());
                System.out.println(message.toPrettyString());
            } catch (GoogleJsonResponseException e) {
                GoogleJsonError error = e.getDetails();
                if (error.getCode() == 403) {
                    Logger.getLogger(MailService_Impl.class.getName()).log(Level.SEVERE, null, e);
                } else {
                    throw e;
                }
                return false;
            }
            emailSent = true;
        } catch (MessagingException | IOException ex) {
            Logger.getLogger(MailService_Impl.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
        return emailSent;
    }

    @Override
    public String sendReferralEmail(String recipientEmail, String recipientName, String phoneNumber) {
        String smsMessage = clientUrl + "StoryController?submit=storyOfTheDay&readerName="+recipientName;
        smsService.sendSMS(phoneNumber, smsMessage);
        String subject = "Readers Are Innovators: Welcome New Reader!";
        String message = 
                "A friend has shared us with you!\n\n"
                + "Click the link below to check out our story of the day!"
                + "Kindest Regards, \n\n"+ "Readers Are Innovators Team";
        String emailContent = emailTemplate.replace(Reader_Name_Location, recipientName);
        emailContent = emailContent.replace(Message_Location, message);
        emailContent = emailContent.replace(Link_Button_Message, "Story Of The Day");
        emailContent = emailContent.replace(Link_Location, "StoryController?submit=storyOfTheDay&readerName="+recipientName);
        if (sendMailWithHTML(recipientEmail, emailContent, subject)) {
            return "A an email has been sent to " + recipientName + ".";
        } else {
            return "Something went wrong... Please make sure you have entered a valid email address.";
        }
    }

    @Override
    public String notifyApprovedWriters(List<Integer> accountIds, Boolean approved) {
        String message
                = "Congratulations!\n"
                + "Your application to become a writer on the Readers Are Innovators platform has been approved.\n"
                + "You may now create short stories on our platform!\n\n"
                + "Happy writing!\n"
                + "Kind regards,\n"
                + "Readers Are Innovators Team";
        String subject = "Readers Are Innovators: Writer Application Status";
        if (!approved) {
            message
                    = "Thank you for showing your interest in becoming a writer on the RIP platform.\n"
                    + "We regret to inform you that your application to become a writer has been denied.\n"
                    + "\n\n"
                    + "Better luck next time!\n"
                    + "Kind regards,\n"
                    + "Readers Are Innovators Team";
            subject = "Readers Are Innovators: Writer Application Status";
        }
        Boolean emailsSent = true;
        for (Integer accountId : accountIds) {
            Reader reader = readerService.getReader(accountId);
            String currentEmail = emailTemplate.replace(Reader_Name_Location, reader.getName() + " " + reader.getSurname());
            currentEmail = currentEmail.replace(Link_Location, "");
            currentEmail = currentEmail.replace(Link_Button_Message, "Home Page");
            currentEmail = currentEmail.replace(Message_Location, message);
            smsService.sendSMS(reader.getPhoneNumber(), "Your writer application has been approved on the RIP platform!");
            if (!sendMailWithHTML(reader.getEmail(), currentEmail, subject)) {
                emailsSent = false;
            }
        }

        if (emailsSent) {
            return "The reader(s) have been notified via email.";
        } else {
            return "Something went wrong... Could not notify reader(s) via email.";
        }
    }

    @Override
    public String notifyWriterOfStorySubmission(Integer writerId, Boolean approved) {
        String smsMessage = "Readers Are Innovators: Your story was approved!";
        String message
                = "Congratulations!\n"
                + "A story you've submitted on the Readers Are Innovators platform has been approved.\n"
                + "Your story can now be seen by everyone!\n\n"
                + "\n\n"
                + "Kind regards,\n"
                + "Readers Are Innovators Team";
        String subject = "Readers Are Innovators: Story Submission";
        if (!approved) {
            message
                    = "Thank you for showing your story submission on the RIP platform.\n"
                    + "We regret to inform you that your story submission has been denied.\n"
                    + "\n\n"
                    + "Better luck next time!\n"
                    + "Kind regards,\n"
                    + "Readers Are Innovators Team";
            smsMessage = "Readers Are Innovators: Your story was rejected...";
        }
        Reader reader = readerService.getReader(writerId);
        String currentEmail = emailTemplate.replace(Reader_Name_Location, reader.getName() + " " + reader.getSurname()).replace(Message_Location, message);
        currentEmail = currentEmail.replace(Link_Location, "");
        currentEmail = currentEmail.replace(Link_Button_Message, "Home Page");
        smsService.sendSMS(reader.getPhoneNumber(), smsMessage);
        if (sendMailWithHTML(reader.getEmail(), currentEmail, subject)) {
            return "The author has been notified via email.";
        } else {
            return "Something went wrong notifying the author via email.";
        }
    }
    
    @Override
    public String notifyBlockedWriters(List<Integer> accountIds) {
        String subject = "Readers Are Innovators: Writer Application Status";
        String message
                    = "We regret to inform you that your writer priviliges on the RIP platform has been revoked.\n"
                    + "\n\n"
                    + "If you would like to become a writer again. Please create a new writer application.\n"
                    + "Kind regards,\n"
                    + "Readers Are Innovators Team";
        Boolean emailsSent = true;
        for (Integer accountId : accountIds) {
            Reader reader = readerService.getReader(accountId);
            String currentEmail = emailTemplate.replace(Reader_Name_Location, reader.getName() + " " + reader.getSurname());
            currentEmail = currentEmail.replace(Link_Location, "");
            currentEmail = currentEmail.replace(Link_Button_Message, "Home Page");
            currentEmail = currentEmail.replace(Message_Location, "Readers Are Innovators: You have been blocked as a writer :[");
            smsService.sendSMS(reader.getPhoneNumber(), "Readers Are Innovators: You have been blocked as a writer :[");
            if (!sendMailWithHTML(reader.getEmail(), currentEmail, subject)) {
                emailsSent = false;
            }
        }

        if (emailsSent) {
            return "The reader(s) have been notified via email.";
        } else {
            return "Something went wrong... Could not notify reader(s) via email.";
        }
    }
    
    @Override
    public String sendChangePasswordEmail(String email) {
        if (!readerService.userExists(email)) {
            return "The email you've entered does not exist.";
        }
        Reader reader = readerService.getReader(email);
        String userToken = readerService.getVerifyToken(reader.getId());
        String message =
                "Click the link below to change you're password.\n"
                + "If you did not request to change your password then please ingore this email.\n"
                + "\n\n"
                + "Kind regards,\n"
                + "Readers Are Innovators Team";
        String smsMessage =clientUrl+"ReaderController?submit=allowPasswordChangeOnLogin&readerId="+reader.getId()+"&verifyToken="+userToken;
        smsService.sendSMS(reader.getPhoneNumber(), smsMessage);
        String subject = "Readers Are Innovators: Change Password";
        String currentEmail = emailTemplate.replace(Reader_Name_Location, reader.getName() + " " + reader.getSurname()).replace(Message_Location, message);
        currentEmail = currentEmail.replace(Link_Location, "ReaderController?submit=allowPasswordChangeOnLogin&readerId="+reader.getId()+"&verifyToken="+userToken);
        currentEmail = currentEmail.replace(Link_Button_Message, "Change Password");
        if (sendMailWithHTML(reader.getEmail(), currentEmail, subject)) {
            return "A link sent via email to change your password.";
        } else {
            return "Something went wrong sending an email to your account.";
        }
    }

    private String Reader_Name_Location = "[Reader_Name]";
    private String Message_Location = "[Message_Location]";
    private String Link_Location = "[Link]";
    private String Link_Button_Message = "[Link_Button_Message]";
    private String emailTemplate = "<!DOCTYPE html>\n"
            + "<html>\n"
            + "<head>\n"
            + "    <title>Referral</title>\n"
            + "    <meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\" />\n"
            + "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1\">\n"
            + "    <meta http-equiv=\"X-UA-Compatible\" content=\"IE=edge\" />\n"
            + "    <style>\n"
            + "	\n"
            + "        @media screen {\n"
            + "            @font-face {\n"
            + "                font-family: 'Lato';\n"
            + "                font-style: normal;\n"
            + "                font-weight: 400;\n"
            + "                src: local('Lato Regular'), local('Lato-Regular'), url(https://fonts.gstatic.com/s/lato/v11/qIIYRU-oROkIk8vfvxw6QvesZW2xOQ-xsNqO47m55DA.woff) format('woff');\n"
            + "            }\n"
            + "\n"
            + "            @font-face {\n"
            + "                font-family: 'Lato';\n"
            + "                font-style: normal;\n"
            + "                font-weight: 700;\n"
            + "                src: local('Lato Bold'), local('Lato-Bold'), url(https://fonts.gstatic.com/s/lato/v11/qdgUG4U09HnJwhYI-uK18wLUuEpTyoUstqEm5AMlJo4.woff) format('woff');\n"
            + "            }\n"
            + "\n"
            + "            @font-face {\n"
            + "                font-family: 'Lato';\n"
            + "                font-style: italic;\n"
            + "                font-weight: 400;\n"
            + "                src: local('Lato Italic'), local('Lato-Italic'), url(https://fonts.gstatic.com/s/lato/v11/RYyZNoeFgb0l7W3Vu1aSWOvvDin1pK8aKteLpeZ5c0A.woff) format('woff');\n"
            + "            }\n"
            + "\n"
            + "            @font-face {\n"
            + "                font-family: 'Lato';\n"
            + "                font-style: italic;\n"
            + "                font-weight: 700;\n"
            + "                src: local('Lato Bold Italic'), local('Lato-BoldItalic'), url(https://fonts.gstatic.com/s/lato/v11/HkF_qI1x_noxlxhrhMQYELO3LdcAZYWl9Si6vvxL-qU.woff) format('woff');\n"
            + "            }\n"
            + "        }\n"
            + "\n"
            + "        /* CLIENT-SPECIFIC STYLES */\n"
            + "        body,\n"
            + "        table,\n"
            + "        td,\n"
            + "        a {\n"
            + "            -webkit-text-size-adjust: 100%;\n"
            + "            -ms-text-size-adjust: 100%;\n"
            + "        }\n"
            + "\n"
            + "        table,\n"
            + "        td {\n"
            + "            mso-table-lspace: 0pt;\n"
            + "            mso-table-rspace: 0pt;\n"
            + "        }\n"
            + "\n"
            + "        img {\n"
            + "            -ms-interpolation-mode: bicubic;\n"
            + "        }\n"
            + "\n"
            + "        /* RESET STYLES */\n"
            + "        img {\n"
            + "            border: 0;\n"
            + "            height: auto;\n"
            + "            line-height: 100%;\n"
            + "            outline: none;\n"
            + "            text-decoration: none;\n"
            + "        }\n"
            + "\n"
            + "        table {\n"
            + "            border-collapse: collapse !important;\n"
            + "        }\n"
            + "\n"
            + "        body {\n"
            + "            height: 100% !important;\n"
            + "            margin: 0 !important;\n"
            + "            padding: 0 !important;\n"
            + "            width: 100% !important;\n"
            + "        }\n"
            + "\n"
            + "        /* iOS BLUE LINKS */\n"
            + "        a[x-apple-data-detectors] {\n"
            + "            color: inherit !important;\n"
            + "            text-decoration: none !important;\n"
            + "            font-size: inherit !important;\n"
            + "            font-family: inherit !important;\n"
            + "            font-weight: inherit !important;\n"
            + "            line-height: inherit !important;\n"
            + "        }\n"
            + "\n"
            + "        /* MOBILE STYLES */\n"
            + "        @media screen and (max-width:600px) {\n"
            + "            h1 {\n"
            + "                font-size: 32px !important;\n"
            + "                line-height: 32px !important;\n"
            + "            }\n"
            + "        }\n"
            + "\n"
            + "        /* ANDROID CENTER FIX */\n"
            + "        div[style*=\"margin: 16px 0;\"] {\n"
            + "            margin: 0 !important;\n"
            + "        }\n"
            + "    </style>\n"
            + "</head>\n"
            + "\n"
            + "<body style=\"background: linear-gradient(180deg, #0d0d0d, #111111, #0d0d0d); margin: 0 !important; padding: 0 !important;\">\n"
            + "<table border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\">\n"
            + "    <!-- LOGO -->\n"
            + "    <tr>\n"
            + "        <td style=\"background: linear-gradient(180deg, #0d0d0d, #111111, #0d0d0d);\" align=\"center\">\n"
            + "            <table border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\" style=\"max-width: 600px;\">\n"
            + "                <tr>\n"
            + "                    <td align=\"center\" valign=\"top\" style=\"padding: 40px 10px 40px 10px;\"> </td>\n"
            + "                </tr>\n"
            + "            </table>\n"
            + "        </td>\n"
            + "    </tr>\n"
            + "    <tr>\n"
            + "        <td style=\"background: linear-gradient(180deg, #0d0d0d, #111111, #0d0d0d);\" align=\"center\" style=\"padding: 0px 10px 0px 10px;\">\n"
            + "            <table border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\" style=\"max-width: 600px;\">\n"
            + "                <tr>\n"
            + "                    <td bgcolor=\"#ffffff\" align=\"center\" valign=\"top\" style=\"padding: 40px 20px 20px 20px; border-radius: 4px 4px 0px 0px; color: #111111; font-family: 'Lato', Helvetica, Arial, sans-serif; font-size: 48px; font-weight: 400; letter-spacing: 4px; line-height: 48px;\">\n"
            + "                        <h1 style=\"font-size: 48px; font-weight: 400; margin: 2;\">Hello [Reader_Name]! </h1> \n"
            + "                        <a href=\"[clientUrl]\" target=\"_blank\" style=\"display: inline-block;\">\n"
            + "                            <img src=\"https://www.nicepng.com/png/full/10-101646_books-png.png\" alt=\"Readers Are Innovators\" width=\"125\" height=\"120\" style=\"display: block; border: 0px;\" />\n"
            + "                        </a>\n"
            + "                    </td>\n"
            + "                </tr>\n"
            + "            </table>\n"
            + "        </td>\n"
            + "    </tr>\n"
            + "    <tr>\n"
            + "        <td bgcolor=\"#f4f4f4\" align=\"center\" style=\"padding: 0px 10px 0px 10px;\">\n"
            + "            <table border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\" style=\"max-width: 600px;\">\n"
            + "                <tr>\n"
            + "                    <td bgcolor=\"#ffffff\" align=\"left\" style=\"padding: 0px 30px 20px 30px; color: #666666; font-family: 'Lato', Helvetica, Arial, sans-serif; font-size: 18px; font-weight: 400; line-height: 25px;\">\n"
            + "                        <p style=\"margin: 0;\">[Message_Location]</p>\n"
            + "                    </td>\n"
            + "                </tr>\n"
            + "                <tr>\n"
            + "                    <td bgcolor=\"#ffffff\" align=\"left\">\n"
            + "                        <table width=\"100%\" border=\"0\" cellspacing=\"0\" cellpadding=\"0\">\n"
            + "                            <tr>\n"
            + "                                <td bgcolor=\"#ffffff\" align=\"center\" style=\"padding: 20px 30px 30px 30px;\">\n"
            + "                                    <table border=\"0\" cellspacing=\"0\" cellpadding=\"0\">\n"
            + "                                        <tr>\n"
            + "                                            <td align=\"center\" style=\"border-radius: 3px;\" bgcolor=\"#0d6efd\">\n"
            + "                                                <a href=\"[clientUrl][Link]\" target=\"_blank\" style=\"font-size: 20px; font-family: Helvetica, Arial, sans-serif; color: #ffffff; text-decoration: none; color: #ffffff; text-decoration: none; padding: 15px 25px; border-radius: 5px; display: inline-block;\">\n"
            + "                                                    [Link_Button_Message]\n"
            + "                                                </a>\n"
            + "                                            </td>\n"
            + "                                        </tr>\n"
            + "                                    </table>\n"
            + "                                </td>\n"
            + "                            </tr>\n"
            + "                        </table>\n"
            + "                    </td>\n"
            + "                </tr> <!-- COPY -->\n"
            + "            </table>\n"
            + "        </td>\n"
            + "    </tr>\n"
            + "</table>\n"
            + "</body>\n"
            + "</html>\n";

}
