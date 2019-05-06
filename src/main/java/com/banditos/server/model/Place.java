package com.banditos.server.model;

import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name="place")
public class Place {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String address;

    @Column(length = 2000)
    private String description;

    @OneToMany(mappedBy = "place")
    private List<Tusovka> tusovkas;

    private Float latitude;

    private Float longitude;

    private String facebookDomain;

    public Place() {
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<Tusovka> getTusovkas() {
        return tusovkas;
    }

    public void setTusovkas(List<Tusovka> tusovkas) {
        this.tusovkas = tusovkas;
    }

    public Float getLatitude() {
        return latitude;
    }

    public void setLatitude(Float latitude) {
        this.latitude = latitude;
    }

    public Float getLongitude() {
        return longitude;
    }

    public void setLongitude(Float longitude) {
        this.longitude = longitude;
    }

    public String getFacebookDomain() {
        return facebookDomain;
    }

    public Place setFacebookDomain(String facebookDomain) {
        this.facebookDomain = facebookDomain;
        return this;
    }

    @Override
    public String toString() {
        return "Place{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", address='" + address + '\'' +
                ", description='" + description + '\'' +
                ", tusovkas=" + tusovkas +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                ", facebookDomain='" + facebookDomain + '\'' +
                '}';
    }
}
