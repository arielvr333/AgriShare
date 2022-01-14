package com.example.agrishare.model;

public class User {
    String Name;
    String UserName;
  //  url profilePic;
    int Age;
    String City;

    public User(String name, String UserName, int age, String city){
        this.Name = name;
        this.Age = age;
        this.UserName = UserName;
        this.City = city;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getUserName() {
        return UserName;
    }

    public void setUserName(String userName) {
        UserName = userName;
    }

    public int getAge() {
        return Age;
    }

    public void setAge(int age) {
        Age = age;
    }

    public String getCity() {
        return City;
    }

    public void setCity(String city) {
        City = city;
    }
}
