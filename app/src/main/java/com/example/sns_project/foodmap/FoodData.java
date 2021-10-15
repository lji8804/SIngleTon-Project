package com.example.sns_project.foodmap;

import java.io.Serializable;

public class FoodData implements Serializable {

    private String name;
    private String categoryName;
    private String roadAddressName;
    private String phone;
    private String Longitude;
    private String Latitude;
    private String placeUrl;

    public FoodData(String name, String categoryName, String roadAddressName, String phone, String longitude, String latitude, String placeUrl) {
        this.name = name;
        this.categoryName = categoryName;
        this.roadAddressName = roadAddressName;
        this.phone = phone;
        this.Longitude = longitude;
        this.Latitude = latitude;
        this.placeUrl = placeUrl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getRoadAddressName() {
        return roadAddressName;
    }

    public void setRoadAddressName(String roadAddressName) {
        this.roadAddressName = roadAddressName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getLongitude() {
        return Longitude;
    }

    public void setLongitude(String longitude) {
        Longitude = longitude;
    }

    public String getLatitude() {
        return Latitude;
    }

    public void setLatitude(String latitude) {
        Latitude = latitude;
    }

    public String getPlaceUrl() {
        return placeUrl;
    }

    public void setPlaceUrl(String placeUrl) {
        this.placeUrl = placeUrl;
    }

    @Override
    public String toString() {
        return "FoodData{" +
                "name='" + name + '\'' +
                ", categoryName='" + categoryName + '\'' +
                ", roadAddressName='" + roadAddressName + '\'' +
                ", phone='" + phone + '\'' +
                ", Longitude='" + Longitude + '\'' +
                ", Latitude='" + Latitude + '\'' +
                ", placeUrl='" + placeUrl + '\'' +
                '}';
    }
}
