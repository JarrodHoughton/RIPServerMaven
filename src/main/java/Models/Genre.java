/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Models;

/**
 *
 * @author jarro
 */
public class Genre {
    private Integer id;
    private String name;
    private Integer viewCount;

    public Genre() {
    }

    public Genre(Integer id, String name, Integer viewCount) {
        this.id = id;
        this.name = name;
        this.viewCount = viewCount;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getViewCount() {
        return viewCount;
    }

    public void setViewCount(Integer viewCount) {
        this.viewCount = viewCount;
    }

    @Override
    public String toString() {
        return "Genre{" + "id=" + id + ", name=" + name + ", viewCount=" + viewCount + '}';
    }
}
