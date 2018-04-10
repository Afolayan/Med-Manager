package com.afolayan.med_manager.database.interfaces;

import com.afolayan.med_manager.database.model.Medication;

/**
 * Created by Oluwaseyi AFOLAYAN on 4/10/2018.
 */

public interface SingleMedicationListener {
    void onSingleMedicationFetched(Medication  medication);
}
