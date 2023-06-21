/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package ServiceLayers;

import Models.Reader;

/**
 *
 * @author jarro
 */
public interface MailService_Interface {
    public String sendMail(String recipientEmail, String emailContent, String subject);
    public String sendVerficationEmail(Reader reader);
    public String sendVerificationEmailWithHTML(Reader reader);
    public String sendRefferalEmail(String recipientEmail);
}
