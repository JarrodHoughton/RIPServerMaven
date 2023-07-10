/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ServiceLayers;

import Models.SMSRequest;
import Models.SMSResponse;
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
import jakarta.xml.bind.Unmarshaller;
import java.io.StringReader;
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
    private static final String SMSGATEWAY = "http://196.41.180.157:8080/sms/sms_request";
    private static final String USER = "GROUP4";
    private static final String PASSWORD = "g4roup";
    private final Client client;
    private WebTarget webTarget;
    private Response response;
    private final DateTimeFormatter outputFormatter;
    
    public SMSService_Impl(){
        client = ClientBuilder.newClient();
        outputFormatter = DateTimeFormatter.ofPattern("yyyy/MM/dd,hh:mm:ss");
    }
    
    @Override
    public String sendSMS(String phoneNumber, String message) {
        
        //hits the SMS gateway and receives a response
        SMSRequest smsRequest = new SMSRequest(LocalDateTime.now().format(outputFormatter), USER, PASSWORD, phoneNumber, message);
        webTarget = client.target(SMSGATEWAY);
        response = webTarget.request(MediaType.APPLICATION_XML).post(Entity.xml(jaxbObjectToXML(smsRequest)));
        
        
        if (response.getStatus() != Response.Status.OK.getStatusCode()){
            return "Failed to send SMS";
        }
        
        try{
            //converts the xml response to an SMSResponse object
            SMSResponse smsResponse = XMLToJaxbObject(response.readEntity(String.class));
            response.close();
            if(smsResponse.getResponseCode().equals("0000")){
                System.out.println("SMS send succesfully");
                return "SMS send succesfully";
            }else{
                System.out.println("Failed to send SMS.  Error: " + smsResponse.getDescription());
                return "Failed to send SMS.  Error: " + smsResponse.getDescription();
            }
        }finally{
            if(response!=null){
                response.close();
            }
        }
    }
    
    private static String jaxbObjectToXML(SMSRequest smsRequest) {
        String xmlString = "";
        try {
            JAXBContext context = JAXBContext.newInstance(SMSRequest.class);
            Marshaller m = context.createMarshaller();

            m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE); 
            m.setProperty(Marshaller.JAXB_FRAGMENT, Boolean.TRUE);
            StringWriter sw = new StringWriter();
            m.marshal(smsRequest, sw);
            xmlString = sw.toString();
        } catch (JAXBException ex) {
            Logger.getLogger(SMSService_Impl.class.getName()).log(Level.SEVERE, null, ex);
        }

        return xmlString;
    }
    
    private static SMSResponse XMLToJaxbObject(String xmlString) {
    SMSResponse smsResponse = null;
    try {
        JAXBContext context = JAXBContext.newInstance(SMSResponse.class);
        Unmarshaller um = context.createUnmarshaller();

        StringReader sr = new StringReader(xmlString);
        smsResponse = (SMSResponse) um.unmarshal(sr);
    } catch (JAXBException ex) {
        Logger.getLogger(SMSService_Impl.class.getName()).log(Level.SEVERE, null, ex);
    }

    return smsResponse;
}
    
}
