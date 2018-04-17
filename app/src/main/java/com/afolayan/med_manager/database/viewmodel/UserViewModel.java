package com.afolayan.med_manager.database.viewmodel;

import android.app.Activity;
import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;

import com.afolayan.med_manager.database.MedManagerDatabase;
import com.afolayan.med_manager.database.interfaces.SingleUserListener;
import com.afolayan.med_manager.database.model.User;

import java.util.List;

public class UserViewModel {
    private MedManagerDatabase managerDatabase;
    private LiveData<List<User>> userList;

    public UserViewModel(Activity activity){
        Application application = activity.getApplication();
        managerDatabase = MedManagerDatabase.getDatabase(application);
        userList = managerDatabase.userDao().getSavedUsers();
    }

    public LiveData<List<User>> fetchAllUsers(){
        return userList;
    }

    public void insertUser(User user){
        new InsertSingleUser(managerDatabase,user).execute();
    }

    public void fetchSingleUser(String userId, SingleUserListener listener){
        new FetchSingleUser(managerDatabase, listener).execute(userId);
    }

     public void fetchSingleUserByEmail(String email, SingleUserListener listener){
        new FetchSingleUserByEmail(managerDatabase, listener).execute(email);
    }

    public void updateSingleUser(User user){
        new UpdateSingleUser(managerDatabase, user).execute();
    }

    public void deleteSingleUser(User user){
        new DeleteSingleUser(managerDatabase, user).execute();
    }

    public static class InsertSingleUser extends AsyncTask<Void, Void, Void> {
        MedManagerDatabase medManagerDatabase;
        User user;

        InsertSingleUser(MedManagerDatabase medManagerDatabase, User user) {
            this.medManagerDatabase = medManagerDatabase;
            this.user = user;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            medManagerDatabase.userDao().insertUser(user);
            return null;
        }
    }

    private static class FetchSingleUser extends AsyncTask<String, Void, User>{
        MedManagerDatabase medManagerDatabase;
        SingleUserListener singleUserListener;

        FetchSingleUser(MedManagerDatabase medManagerDatabase, SingleUserListener listener) {
            this.medManagerDatabase = medManagerDatabase;
            singleUserListener = listener;
        }

        @Override
        protected User doInBackground(String... strings) {
            return medManagerDatabase.userDao().getSingleUser(strings[0]);
        }

        @Override
        protected void onPostExecute(User user) {
            super.onPostExecute(user);
            singleUserListener.onSingleUserFetched(user);
        }
    }

    private static class FetchSingleUserByEmail extends AsyncTask<String, Void, User>{
        MedManagerDatabase medManagerDatabase;
        SingleUserListener singleUserListener;

        FetchSingleUserByEmail(MedManagerDatabase medManagerDatabase, SingleUserListener listener) {
            this.medManagerDatabase = medManagerDatabase;
            singleUserListener = listener;
        }

        @Override
        protected User doInBackground(String... strings) {
            return medManagerDatabase.userDao().getSingleUserByEmail(strings[0]);
        }

        @Override
        protected void onPostExecute(User user) {
            super.onPostExecute(user);
            singleUserListener.onSingleUserFetched(user);
        }
    }


    private static class UpdateSingleUser extends AsyncTask<Void, Void, Void>{
        MedManagerDatabase medManagerDatabase;
        User user;

        UpdateSingleUser(MedManagerDatabase medManagerDatabase, User user) {
            this.medManagerDatabase = medManagerDatabase;
            this.user = user;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            medManagerDatabase.userDao().updateRecord(user);
            return null;
        }
    }

    private static class DeleteSingleUser extends AsyncTask<Void, Void, Void>{
        MedManagerDatabase medManagerDatabase;
        User user;

        DeleteSingleUser(MedManagerDatabase medManagerDatabase, User user) {
            this.medManagerDatabase = medManagerDatabase;
            this.user = user;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            medManagerDatabase.userDao().deleteRecord(user);
            return null;
        }
    }







}
