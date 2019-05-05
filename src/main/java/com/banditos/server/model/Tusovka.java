package com.banditos.server.model;

import com.banditos.server.model.pojo.EventDate;
import java.time.LocalDateTime;
import java.util.Objects;
import javax.persistence.*;
import java.net.URL;
import java.util.Date;

@Entity
@Table(name = "tusovka")
public class Tusovka {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private LocalDateTime beginDate;

    private LocalDateTime endDate;

    private String dateStr;

    @Column(length = 2000)
    private String description;

    @ManyToOne
    @JoinColumn(name = "place_id")
    private Place place;

    private URL link;

    private Integer price;

    public Tusovka() {
    }

    public Tusovka(String name, LocalDateTime beginDate,
            LocalDateTime endDate, String dateStr, String description,
            Place place, URL link, Integer price) {
        this.name = name;
        this.beginDate = beginDate;
        this.endDate = endDate;
        this.dateStr = dateStr;
        this.description = description;
        this.place = place;
        this.link = link;
        this.price = price;
    }

    public Tusovka(String name, EventDate eventDate, String dateStr,
            String description, Place place, URL link, Integer price) {
        this.name = name;
        this.beginDate = eventDate.getBeginDate();
        this.endDate = eventDate.getEndDate();
        this.dateStr = dateStr;
        this.description = description;
        this.place = place;
        this.link = link;
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public Tusovka setName(String name) {
        this.name = name;
        return this;
    }

    public LocalDateTime getBeginDate() {
        return beginDate;
    }

    public Tusovka setBeginDate(LocalDateTime beginDate) {
        this.beginDate = beginDate;
        return this;
    }
    public LocalDateTime getEndDate() {
        return endDate;
    }

    public Tusovka setEndDate(LocalDateTime endDate) {
        this.endDate = endDate;
        return this;
    }
    public Tusovka setDate(EventDate date) {
        this.beginDate = date.getBeginDate();
        this.endDate = date.getEndDate();
        return this;
    }

    public String getDateStr() {
        return dateStr;
    }

    public Tusovka setDateStr(String dateStr) {
        this.dateStr = dateStr;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public Tusovka setDescription(String description) {
        this.description = description;
        return this;
    }

    public Place getPlace() {
        return place;
    }

    public Tusovka setPlace(Place place) {
        this.place = place;
        return this;
    }

    public URL getLink() {
        return link;
    }

    public Tusovka setLink(URL link) {
        this.link = link;
        return this;
    }

    public Integer getPrice() {
        return price;
    }

    public Tusovka setPrice(Integer price) {
        this.price = price;
        return this;
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
                Objects.equals(beginDate, tusovka.beginDate) &&
                Objects.equals(endDate, tusovka.endDate) &&
                Objects.equals(dateStr, tusovka.dateStr) &&
                Objects.equals(description, tusovka.description) &&
                Objects.equals(place, tusovka.place) &&
                Objects.equals(link, tusovka.link) &&
                Objects.equals(price, tusovka.price);
    }

    @Override
    public int hashCode() {
        return Objects
                .hash(id, name, beginDate, endDate, dateStr, description, place,
                        link, price);
    }

    @Override
    public String toString() {
        return "Tusovka{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", beginDate=" + beginDate +
                ", endDate=" + endDate +
                ", dateStr='" + dateStr + '\'' +
                ", description='" + description + '\'' +
                ", place=" + place +
                ", link=" + link +
                ", price=" + price +
                '}';
    }
}
