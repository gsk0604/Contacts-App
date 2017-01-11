package com.android.flipmapapp;

/**
 * Created by techjini on 7/1/17.
 */

public class DataProvider {
    private  String name;
    private String email;
    private String mobNumber;
    private String fixNumber;

    DataProvider(String name , String email, String mobNumber, String fixNumber){
        this.setName(name);
        this.setEmail(email);
        this.setMobNumber(mobNumber);
        this.setFixNumber(fixNumber);
    }

    public DataProvider() {

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMobNumber() {
        return mobNumber;
    }

    public void setMobNumber(String mobNumber) {
        this.mobNumber = mobNumber;
    }

    public String getFixNumber() {
        return fixNumber;
    }

    public void setFixNumber(String fixNumber) {
        this.fixNumber = fixNumber;
    }
}
