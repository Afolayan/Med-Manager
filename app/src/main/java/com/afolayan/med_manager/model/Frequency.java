package com.afolayan.med_manager.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Oluwaseyi AFOLAYAN on 4/8/2018.
 */

public class Frequency {

    private String name;
    private int count;

    public Frequency() {
    }

    /**
     * @param name Name of this Frequency Type
     * @param count How many times this occurs in 24 hours
     */
    public Frequency(String name, int count) {
        this.name = name;
        this.count = count;
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

    @Override
    public String toString() {
        return "Frequency-\nName: "+name+"\nCount: "+count;
    }

    public static List<Frequency> loadFrequencyTypes(){
        List<Frequency> frequencies = new ArrayList<>();

        Frequency onceFrequency = new Frequency("Once a day", 1);
        Frequency twiceADay = new Frequency("Twice a day", 2);
        Frequency thriceADay = new Frequency("Three time a day", 3);
        Frequency afterMeal = new Frequency("After each meal", 3);
        Frequency sixHours = new Frequency("Every 6 hours", 4);
        Frequency everyHour = new Frequency("Every 1 hour", 24);

        frequencies.add(onceFrequency);
        frequencies.add(twiceADay);
        frequencies.add(thriceADay);
        frequencies.add(afterMeal);
        frequencies.add(sixHours);
        frequencies.add(everyHour);

        return frequencies;
    }
}
