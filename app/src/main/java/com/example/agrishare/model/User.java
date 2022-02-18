package com.example.agrishare.model;

import java.util.ArrayList;

public class User {

    String Name;
    String Email;
    String Address;
    String phoneNumber;
    String Id;
    ArrayList<Post> posts;

    public User(String name, String Email, String Id, String address,String PhoneNumber){
        this.Name = name;
        this.Email = Email;
        this.Address = address;
        this.phoneNumber = PhoneNumber;
        this.Id=Id;
        this.posts = new ArrayList<>();
    }
    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getAddress() {
        return Address;
    }

    public void setAddress(String address) {
        Address = address;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public ArrayList<Post> getPosts() { return posts; }

    public void setPosts(ArrayList<Post> posts) { this.posts = posts; }
}
