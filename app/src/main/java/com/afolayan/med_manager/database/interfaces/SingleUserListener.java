package com.afolayan.med_manager.database.interfaces;

import com.afolayan.med_manager.database.model.User;

/**
 * Created by Oluwaseyi AFOLAYAN on 4/10/2018.
 */

public interface SingleUserListener {
    void onSingleUserFetched(User user);
}
