/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Models;

/**
 *
 * @author jarro
 */
public class Application {
    private Integer readerId;
    private String motivation;

    public Application() {}

    public Application(Integer readerId, String motivation) {
        this.readerId = readerId;
        this.motivation = motivation;
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
    
    @Override
    public String toString() {
        return "Application{" + "readerId=" + readerId + ", motivation=" + motivation + '}';
    }
}
