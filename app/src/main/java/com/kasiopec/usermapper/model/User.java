package com.kasiopec.usermapper.model;

public class User {
    private int id;
    private String name;
    private String image;
    private double lng;
    private double lat;
    private String address;

    public User(int id, String name, String image, double lat, double lng){
        this.id = id;
        this.name = name;
        this.image = image;
        this.lng = lng;
        this.lat = lat;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getImage() {
        return image;
    }

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }

    public double getLat() {

        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
