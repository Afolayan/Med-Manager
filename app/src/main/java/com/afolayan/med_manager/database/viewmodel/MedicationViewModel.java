package com.afolayan.med_manager.database.viewmodel;

import android.app.Activity;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;
import android.support.annotation.NonNull;

import com.afolayan.med_manager.database.MedManagerDatabase;
import com.afolayan.med_manager.database.interfaces.DeleteMedicationListener;
import com.afolayan.med_manager.database.interfaces.SingleMedicationListener;
import com.afolayan.med_manager.database.model.Medication;

import java.util.List;

/**
 * Created by Oluwaseyi Afolayan on 3/14/2018.
 */

public class MedicationViewModel extends AndroidViewModel {
    private MedManagerDatabase managerDatabase;
    private LiveData<List<Medication>> medicationList;
    int count = 0;

    public MedicationViewModel(@NonNull Activity activity) {
        super(activity.getApplication());
        managerDatabase = MedManagerDatabase.getDatabase(activity.getApplication());
    }

    public LiveData<List<Medication>> fetchAllMedications(String email){
        return managerDatabase.medicationDao().fetchAllMedications(email);
    }

    public void insertSingleMedication(Medication medication){
        new InsertSingleMedication(managerDatabase, medication).execute();
    }

     public void insertMultipleHistories(List<Medication> histories){
        new InsertMultipleMedication(managerDatabase, histories).execute();
    }

    public void updateSingleMedication(Medication medication){
        new UpdateSingleMedication(managerDatabase, medication).execute();
    }

    public void fetchSingleMedication(SingleMedicationListener listener, String medicationId, String userId){
        new FetchSingleMedication(managerDatabase, listener).execute(medicationId, userId);
    }

    public void deleteSingleMedication(Medication medication){
        new DeleteSingleMedication(managerDatabase, medication).execute();
    }

    public void deleteUserMedication(String userEmail, DeleteMedicationListener listener){
        new DeleteUserMedication(managerDatabase, listener, userEmail).execute();
    }

    public LiveData<List<Medication>> getSearchMedications(String query) {
        return managerDatabase.medicationDao().searchMedication("%"+query+"%");
    }
    private static class InsertSingleMedication extends AsyncTask<Void, Void, Void>{
        MedManagerDatabase medicationDb;
        Medication medication;

        InsertSingleMedication(MedManagerDatabase medicationDb, Medication medication) {
            this.medicationDb = medicationDb;
            this.medication = medication;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            medicationDb.medicationDao().insertMedication(medication);
            return null;
        }
    }
    private static class InsertMultipleMedication extends AsyncTask<Void, Void, Void>{
        MedManagerDatabase medicationDb;
        List<Medication> histories;

        InsertMultipleMedication(MedManagerDatabase medicationDb, List<Medication> histories) {
            this.medicationDb = medicationDb;
            this.histories = histories;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            medicationDb.medicationDao().insertMultipleMedications(histories);
            return null;
        }
    }

    private static class UpdateSingleMedication extends AsyncTask<Void, Void, Void>{
        MedManagerDatabase medicationDb;
        Medication medication;

        UpdateSingleMedication(MedManagerDatabase medicationDb, Medication medication) {
            this.medicationDb = medicationDb;
            this.medication = medication;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            medicationDb.medicationDao().updateRecord(medication);
            return null;
        }
    }

    private static class FetchSingleMedication extends AsyncTask<String, Void, Medication>{
        MedManagerDatabase medicationDb;
        SingleMedicationListener medicationListener;

        FetchSingleMedication(MedManagerDatabase medicationDb, SingleMedicationListener listener) {
            this.medicationDb = medicationDb;
            medicationListener = listener;
        }

        @Override
        protected Medication doInBackground(String... strings) {
            return medicationDb.medicationDao().getSingleMedication(strings[0], strings[1]);
        }

        @Override
        protected void onPostExecute(Medication medication) {
            super.onPostExecute(medication);
            medicationListener.onSingleMedicationFetched(medication);
        }
    }

    private static class DeleteSingleMedication extends AsyncTask<Void, Void, Void>{
        MedManagerDatabase medicationDb;
        Medication medication;

        DeleteSingleMedication(MedManagerDatabase medicationDb, Medication medication) {
            this.medicationDb = medicationDb;
            this.medication = medication;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            medicationDb.medicationDao().deleteRecord(medication);
            return null;
        }
    }
    private static class DeleteUserMedication extends AsyncTask<Void, Void, Void>{
        MedManagerDatabase medicationDb;
        String userEmail;
        DeleteMedicationListener listener;

        DeleteUserMedication(MedManagerDatabase medicationDb, DeleteMedicationListener listener, String userEmail) {
            this.medicationDb = medicationDb;
            this.userEmail = userEmail;
            this.listener = listener;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            medicationDb.medicationDao().deleteMedicationForUser(userEmail);
            listener.onDeleteMedication();
            return null;
        }
    }

}
