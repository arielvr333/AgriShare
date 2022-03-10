package com.example.agrishare.model;

import java.util.HashMap;
import java.util.Map;

public class User {

    String Name;
    String Email;
    String Address;
    String phoneNumber;
    String Id;
    String AvatarUrl;

    public User(String name, String Email, String Id, String address,String PhoneNumber,String avatarUrl){
        this.Name = name;
        this.Email = Email;
        this.Address = address;
        this.phoneNumber = PhoneNumber;
        this.Id=Id;
        this.AvatarUrl=avatarUrl;
    }
    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getAddress() {
        return Address;
    }

    public void setAddress(String address) {
        Address = address;
    }

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public String getEmail() { return Email; }

    public String getPhoneNumber() { return phoneNumber; }

    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }

    public String getAvatarUrl() { return AvatarUrl; }

    public void setAvatarUrl(String avatarUrl) { AvatarUrl = avatarUrl; }

     public Map<String, Object> usertoDB() {
        Map<String, Object> DbUser = new HashMap<>();
         DbUser.put("name", Name);
         DbUser.put("email", Email);
         DbUser.put("address", Address);
         DbUser.put("phoneNumber", phoneNumber);
         DbUser.put("Id", Id);
         DbUser.put("avatarUrl",AvatarUrl);
        return DbUser;
    }



}
