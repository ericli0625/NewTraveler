package com.example.eric.newtraveler.network.responseData;

public class SpotDetail {
    private String id;
    private String name;
    private String city;
    private String county;
    private String category;
    private String address;
    private String telephone;
    private String longitude;
    private String latitude;
    private String content;

    public SpotDetail setId(String id) {
        this.id = id;
        return this;
    }

    public SpotDetail setName(String name) {
        this.name = name;
        return this;
    }

    public SpotDetail setCity(String city) {
        this.city = city;
        return this;
    }

    public SpotDetail setCounty(String county) {
        this.county = county;
        return this;
    }

    public SpotDetail setCategory(String category) {
        this.category = category;
        return this;
    }

    public SpotDetail setAddress(String address) {
        this.address = address;
        return this;
    }

    public SpotDetail setTelephone(String telephone) {
        this.telephone = telephone;
        return this;
    }

    public SpotDetail setLongitude(String longitude) {
        this.longitude = longitude;
        return this;
    }

    public SpotDetail setLatitude(String latitude) {
        this.latitude = latitude;
        return this;
    }

    public SpotDetail setContent(String content) {
        this.content = content;
        return this;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getCity() {
        return city;
    }

    public String getCounty() {
        return county;
    }

    public String getCategory() {
        return category;
    }

    public String getAddress() {
        return address;
    }

    public String getTelephone() {
        return telephone;
    }

    public String getLongitude() {
        return longitude;
    }

    public String getLatitude() {
        return latitude;
    }

    public String getContent() {
        return content;
    }
}
