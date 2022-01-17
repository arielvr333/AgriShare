package com.example.agrishare.model;

public class User {

    String Name;
    String Email;
    String Address;
    String phoneNumber;
    int Id;

    public User(String name, String Email, int Id, String address,String PhoneNumber){
        this.Name = name;
        this.Email = Email;
        this.Address = address;
        this.phoneNumber = PhoneNumber;
        this.Id=Id;
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

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

}
