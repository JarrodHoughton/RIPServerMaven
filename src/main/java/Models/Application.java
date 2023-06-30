/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Models;

/**
 *
 * @author Jarrod
 */
public class Application {
    private Integer readerId;
    private String motivation;
    private String readerName;
    private String readerSurname;

    public Application() {}

    public Application(Integer readerId, String motivation) {
        this.readerId = readerId;
        this.motivation = motivation;
    }

    public Application(Integer readerId, String motivation, String readerName, String readerSurname) {
        this.readerId = readerId;
        this.motivation = motivation;
        this.readerName = readerName;
        this.readerSurname = readerSurname;
    }
    
    

    public String getMotivation() {
        return motivation;
    }

    public void setMotivation(String motivation) {
        this.motivation = motivation;
    }

    public Integer getReaderId() {
        return readerId;
    }

    public void setReaderId(Integer readerId) {
        this.readerId = readerId;
    }

    public String getReaderName() {
        return readerName;
    }

    public void setReaderName(String readerName) {
        this.readerName = readerName;
    }

    public String getReaderSurname() {
        return readerSurname;
    }

    public void setReaderSurname(String readerSurname) {
        this.readerSurname = readerSurname;
    }
    
    @Override
    public String toString() {
        return "Application{" + "readerId=" + readerId + ", motivation=" + motivation + '}';
    }
}
