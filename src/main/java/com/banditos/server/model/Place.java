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

    private String vkDomain;

    public Place() {
    }

    public String getName() {
        return name;
    }

    public Place setName(String name) {
        this.name = name;
        return this;
    }

    public String getAddress() {
        return address;
    }

    public Place setAddress(String address) {
        this.address = address;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public Place setDescription(String description) {
        this.description = description;
        return this;
    }

    public List<Tusovka> getTusovkas() {
        return tusovkas;
    }

    public Place setTusovkas(List<Tusovka> tusovkas) {
        this.tusovkas = tusovkas;
        return this;
    }

    public Float getLatitude() {
        return latitude;
    }

    public Place setLatitude(Float latitude) {
        this.latitude = latitude;
        return this;
    }

    public Float getLongitude() {
        return longitude;
    }

    public Place setLongitude(Float longitude) {
        this.longitude = longitude;
        return this;
    }

    public String getFacebookDomain() {
        return facebookDomain;
    }

    public Place setFacebookDomain(String facebookDomain) {
        this.facebookDomain = facebookDomain;
        return this;
    }

    public String getVkDomain() {
        return vkDomain;
    }

    public Place setVkDomain(String vkDomain) {
        this.vkDomain = vkDomain;
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
                ", vkDomain='" + vkDomain + '\'' +
                '}';
    }
}
