package com.afolayan.med_manager.database.model;


import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

import java.io.Serializable;

/**
 * Created by Oluwaseyi AFOLAYAN on 4/8/2018.
 */
@Entity
public class User implements Serializable{

    @PrimaryKey(autoGenerate = true)
    private long id;
    private String name;
    private String email;
    private String allergies;
    private String photoUrl;
    private int age;
    
    public User() {

    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
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

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getAllergies() {
        return allergies;
    }

    public void setAllergies(String allergies) {
        this.allergies = allergies;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    @Override
    public String toString() {
        return "User info\nname:"+name
                +"\nemail: "+email
                +"\nage: "+age
                +"\nphotoUrl: "+photoUrl
                +"\nallergies: "+allergies;
    }
}
