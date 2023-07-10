package Utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 @author Jarrod Houghton
 */
public class GetProperties {

    Properties config = null;
    InputStream inputStream = null;
    String fileName = null;

    public GetProperties(String fileName) {
        this.fileName = fileName;
    }

    public String get(String property) {
        try {
            inputStream = getClass().getClassLoader().getResourceAsStream(fileName);
            config = new Properties();
            config.load(inputStream);
            return config.getProperty(property);
        } catch (IOException e) {
            Logger.getLogger(GetProperties.class.getName()).log(Level.SEVERE, null, e);
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException ex) {
                    Logger.getLogger(GetProperties.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        return null;
    }
}
