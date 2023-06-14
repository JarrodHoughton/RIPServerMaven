package Utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class GetProperties {
    Properties config = null;
    FileInputStream fis = null;
    File configFile;

    public GetProperties(String configFile) {
        this.configFile = new File(configFile);
    }

    public String get(String property) {
        try {
            if (configFile.exists()) {
                fis = new FileInputStream(configFile);
                config = new Properties();
                config.load(fis);
                return config.getProperty(property);
            } else {
                System.out.println("File not found");
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fis!=null) {
                try {
                    fis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    public void printAll() {
        try {
            if (configFile.exists()) {
                fis = new FileInputStream(configFile);
                config = new Properties();
                config.load(fis);

            } else {
                System.out.println("File not found");
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fis!=null) {
                try {
                    fis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
