package com.afolayan.med_manager.database.model;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import java.io.Serializable;

/**
 * Created by Oluwaseyi AFOLAYAN on 3/14/2018.
 */

@Entity
public class Medication implements Serializable, Comparable<Medication>{

    @PrimaryKey(autoGenerate = true)
    private long id;
    private long dateCreated;
    private String name;
    private String email;
    private String description;
    private long dateFrom;
    private long dateTo;
    private String frequency;
    private int frequencyCount;


    public Medication() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(long dateCreated) {
        this.dateCreated = dateCreated;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public long getDateFrom() {
        return dateFrom;
    }

    public void setDateFrom(long dateFrom) {
        this.dateFrom = dateFrom;
    }

    public long getDateTo() {
        return dateTo;
    }

    public void setDateTo(long dateTo) {
        this.dateTo = dateTo;
    }

    public String getFrequency() {
        return frequency;
    }

    public void setFrequency(String frequency) {
        this.frequency = frequency;
    }

    public int getFrequencyCount() {
        return frequencyCount;
    }

    public void setFrequencyCount(int frequencyCount) {
        this.frequencyCount = frequencyCount;
    }

    @Override
    public String toString() {

        return "Medication: "+
                "\nid: "+id
                +"\nname: "+name
                +"\ndateCreated: "+dateCreated
                +"\ndesc: "+description
                +"\ndateFrom: "+dateFrom
                +"\ndateTo: "+dateTo
                +"\nfrequency: "+frequency
                +"\nfrequencyCount: "+frequencyCount;

    }

    @Override
    public boolean equals(Object obj) {
        Medication medication = (Medication) obj;
        return medication != null && name.toLowerCase().trim().equals(medication.getName().toLowerCase().trim());
    }

    public boolean contains(Medication medication){
        return id == medication.getId();
    }
    @Override
    public int compareTo(@NonNull Medication o) {
        Long h1Time = dateCreated;
        Long h2Time = o.getDateCreated();
        if( h1Time.compareTo(h2Time) < 0) return 1;
        if( h1Time.compareTo(h2Time) > 0) return -1;
        return 0;
    }
}
