package com.afolayan.med_manager.model;

/**
 * Created by Oluwaseyi AFOLAYAN on 4/8/2018.
 */

public class Frequency {

    String name;
    int count;
    double quantity;

    public Frequency() {
    }

    public Frequency(String name, int count, double quantity) {
        this.name = name;
        this.count = count;
        this.quantity = quantity;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public double getQuantity() {
        return quantity;
    }

    public void setQuantity(double quantity) {
        this.quantity = quantity;
    }

    @Override
    public String toString() {
        return "Frequency-\nName: "+name+"\nCount: "+count+"\nQuantity: "+quantity;
    }
}
