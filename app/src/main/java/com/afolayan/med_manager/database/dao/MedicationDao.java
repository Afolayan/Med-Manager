package com.afolayan.med_manager.database.dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.afolayan.med_manager.database.model.Medication;

import java.util.List;

/**
 * Created by Oluwaseyi AFOLAYAN on 2/28/2018.
 */

@Dao
public interface MedicationDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertMedication(Medication medication);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertMultipleMedications(List<Medication> histories);

    @Query("SELECT * FROM Medication WHERE email=:email ORDER BY dateCreated DESC")
    LiveData<List<Medication>> fetchAllMedications(String email);

    @Query("SELECT * FROM Medication ORDER BY dateCreated DESC")
    List<Medication> fetchAllMedicationsList();

    @Query("SELECT * FROM Medication WHERE id=:medicationId AND email=:email")
    Medication getSingleMedication(String medicationId, String email);

    @Query("SELECT COUNT(*) FROM Medication WHERE email=:email")
    int getMedicationCount(String email);

    @Update
    void updateRecord(Medication medication);

    @Query("select * from Medication where name LIKE :query")
    LiveData<List<Medication>> searchMedication(String query);

    @Delete
    void deleteRecord(Medication medication);

    @Query("DELETE FROM Medication WHERE email=:email")
    void deleteMedicationForUser(String email);

}
