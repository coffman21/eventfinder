package com.banditos.server.model;

import javax.persistence.*;
import java.net.URL;
import java.util.Date;

@Entity
@Table(name="tusovka")
public class Tusovka {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    private String name;

    private Date date;

    @Column(length = 2000)
    private String description;

    @ManyToOne
    @JoinColumn(name = "place_id")
    private Place place;

    private URL link;

    private int price;

    protected Tusovka() {}

    public Tusovka(Date date, String name, String description, Place place, URL link, int price) {
        this.date = date;
        this.name = name;
        this.description = description;
        this.place = place;
        this.link = link;
        this.price = price;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Place getPlace() {
        return place;
    }

    public void setPlace(Place place) {
        this.place = place;
    }

    public URL getLink() {
        return link;
    }

    public void setLink(URL link) {
        this.link = link;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    @Override
    public String toString() {
        return "Tusovka{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", date=" + date +
                ", description='" + description + '\'' +
                ", place=" + place +
                ", link=" + link +
                ", price=" + price +
                '}';
    }
}
