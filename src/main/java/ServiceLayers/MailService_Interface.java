/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package ServiceLayers;

import Models.Reader;
import java.util.List;

/**
 *
 * @author Jarrod
 */
public interface MailService_Interface {
    public String sendMail(String recipientEmail, String emailContent, String subject);
    public String sendVerificationEmail(Reader reader);
    public String sendReferralEmail(String recipientEmail, String recipientName, String phoneNumber);
    public String notifyApprovedWriters(List<Integer> accountIds, Boolean approved);
    public String notifyWriterOfStorySubmission(Integer writerId, Boolean approved);
    public String notifyBlockedWriters(List<Integer> accountIds);
    public String sendChangePasswordEmail(String email);
}
