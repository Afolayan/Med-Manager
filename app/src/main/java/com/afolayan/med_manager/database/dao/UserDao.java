package com.afolayan.med_manager.database.dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.afolayan.med_manager.database.model.User;

import java.util.List;

@Dao
public interface UserDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertUser(User user);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertMultipleUsers(List<User> users);

    @Query("SELECT * FROM User")
    LiveData<List<User>> getSavedUsers();

    @Query("SELECT * FROM User WHERE id=:userId")
    User getSingleUser(String userId);

    @Query("SELECT * FROM User WHERE email=:userEmail")
    User getSingleUserByEmail(String userEmail);

    @Update
    void updateRecord(User user);

    @Delete
    void deleteRecord(User user);
}
