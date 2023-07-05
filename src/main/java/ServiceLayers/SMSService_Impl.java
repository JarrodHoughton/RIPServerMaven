/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ServiceLayers;

import Controllers.SMSController;
import Models.SMSRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.client.WebTarget;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Marshaller;
import java.io.StringWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Kylynn van der Merwe
 */
public class SMSService_Impl implements SMSService_Interface{
    private static final String smsGateway = "http://196.41.180.157:8080/sms/sms_request";
    private final Client client;
    private WebTarget webTarget;
    private final ObjectMapper mapper;
    private Response response;
    private DateTimeFormatter outputFormatter;
    
    public SMSService_Impl(){
        client = ClientBuilder.newClient();
        mapper = new ObjectMapper();
        outputFormatter = DateTimeFormatter.ofPattern("yyyy/MM/dd,hh:mm:ss");
    }
    
    @Override
    public String sendSMS(String phoneNumber, String message) {
        
        SMSRequest request = new SMSRequest(LocalDateTime.now().format(outputFormatter), "user", "password", phoneNumber, message);
        webTarget = client.target(smsGateway);
        response = webTarget.request(MediaType.APPLICATION_XML).post(Entity.xml(jaxbObjectToXML(request)));
        
        return response.readEntity(String.class);
    }
    
    private static String jaxbObjectToXML(SMSRequest request) {
        String xmlString = "";
        try {
            JAXBContext context = JAXBContext.newInstance(SMSRequest.class);
            Marshaller m = context.createMarshaller();

            m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE); 

            StringWriter sw = new StringWriter();
            m.marshal(request, sw);
            xmlString = sw.toString();
            System.out.println(xmlString);
        } catch (JAXBException ex) {
            Logger.getLogger(SMSController.class.getName()).log(Level.SEVERE, null, ex);
        }

        return xmlString;
    }
    
}
