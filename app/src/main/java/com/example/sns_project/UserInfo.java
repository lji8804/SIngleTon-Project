package com.example.sns_project;

public class UserInfo {
    private String Uid;
    private String name;
    private String phoneNumber;
    private String birthDay;
    private String address;
    private String photoUrl;

    public UserInfo(String Uid,String name, String phoneNumber, String birthDay, String address, String photoUrl){
        this.Uid = Uid;
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.birthDay = birthDay;
        this.address = address;
        this.photoUrl = photoUrl;
    }

    public UserInfo(String name, String phoneNumber, String birthDay, String address){
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.birthDay = birthDay;
        this.address = address;
    }

    public String getUid() {
        return Uid;
    }
    public void setUid(String uid) {
        Uid = uid;
    }
    public String getName(){
        return this.name;
    }
    public void setName(String name){
        this.name = name;
    }
    public String getPhoneNumber(){
        return this.phoneNumber;
    }
    public void setPhoneNumber(String phoneNumber){
        this.phoneNumber = phoneNumber;
    }
    public String getBirthDay(){
        return this.birthDay;
    }
    public void setBirthDay(String birthDay){
        this.birthDay = birthDay;
    }
    public String getAddress(){
        return this.address;
    }
    public void setAddress(String address){
        this.address = address;
    }
    public String getPhotoUrl(){
        return this.photoUrl;
    }
    public void setPhotoUrl(String photoUrl){
        this.photoUrl = photoUrl;
    }
}
