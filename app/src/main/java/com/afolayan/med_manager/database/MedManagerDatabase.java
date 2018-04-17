package com.afolayan.med_manager.database;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

import com.afolayan.med_manager.database.dao.MedicationDao;
import com.afolayan.med_manager.database.dao.UserDao;
import com.afolayan.med_manager.database.model.Medication;
import com.afolayan.med_manager.database.model.User;


@Database(
        entities = {User.class,Medication.class
            }, version = 1, exportSchema = false)
public abstract class MedManagerDatabase extends RoomDatabase {
    private static MedManagerDatabase INSTANCE;

    public static MedManagerDatabase getDatabase(Context context) {
        if(INSTANCE == null) {
            INSTANCE = Room.databaseBuilder(context.getApplicationContext(), MedManagerDatabase.class,
                    "medmanager_db").build();
        }
        return INSTANCE;
    }

    public void destroyInstance() {
        INSTANCE = null;
    }

    public abstract UserDao userDao();
    public abstract MedicationDao medicationDao();


}
