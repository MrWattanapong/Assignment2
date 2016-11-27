package com.egco428.a23281;

/**
 * Created by Lostl2ose on 11/26/2016.
 */
public class Person {

    private String username;
    private String password;
    private double latitude;
    private double longitude;

    public Person(String username, String password, double latitude, double longitude){
        this.username = username;
        this.password = password;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public Person(){}

    public String getUsername(){
        return username;
    }

    public String getPassword(){
        return password;
    }

    public double getLatitude(){
        return latitude;
    }

    public double getLongitude(){
        return longitude;
    }
}
