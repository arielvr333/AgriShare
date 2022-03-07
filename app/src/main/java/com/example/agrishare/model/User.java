package com.example.agrishare.model;

public class User {

    String Name;
    String Email;
    String Address;
    String phoneNumber;
    String Id;

    public User(String name, String Email, String Id, String address,String PhoneNumber){
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
}
