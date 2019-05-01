package com.banditos.server.model;

import java.time.LocalDateTime;
import java.util.Objects;
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

    private LocalDateTime date;

    private String dateStr;

    @Column(length = 2000)
    private String description;

    @ManyToOne
    @JoinColumn(name = "place_id")
    private Place place;

    private URL link;

    private Integer price;

    protected Tusovka() {}

    public Tusovka(LocalDateTime date, String name, String description, Place place, URL link, Integer price) {
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

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public String getDateStr() {
        return dateStr;
    }

    public void setDateStr(String dateStr) {
        this.dateStr = dateStr;
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

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Tusovka tusovka = (Tusovka) o;
        return Objects.equals(id, tusovka.id) &&
                Objects.equals(name, tusovka.name) &&
                Objects.equals(date, tusovka.date) &&
                Objects.equals(dateStr, tusovka.dateStr) &&
                Objects.equals(description, tusovka.description) &&
                Objects.equals(place, tusovka.place) &&
                Objects.equals(link, tusovka.link) &&
                Objects.equals(price, tusovka.price);
    }

    @Override
    public int hashCode() {
        return Objects
                .hash(id, name, date, dateStr, description, place, link, price);
    }

    @Override
    public String toString() {
        return "Tusovka{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", date=" + date +
                ", dateStr='" + dateStr + '\'' +
                ", description='" + description + '\'' +
                ", place=" + place +
                ", link=" + link +
                ", price=" + price +
                '}';
    }
}
