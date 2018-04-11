package com.afolayan.med_manager;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

import com.afolayan.med_manager.database.viewmodel.UserViewModel;
import com.afolayan.med_manager.utils.AccountUtils;
import com.squareup.picasso.Picasso;

/**
 * A placeholder fragment containing a simple view.
 */
public class UserProfileFragment extends Fragment {


    public UserProfileFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user_profile, container, false);
        ImageView userImageView = view.findViewById(R.id.iv_user_image);
        EditText etName = view.findViewById(R.id.et_user_name);
        EditText etAge = view.findViewById(R.id.et_user_age);
        EditText etAllergies = view.findViewById(R.id.et_user_allergies);


        UserViewModel viewModel = new UserViewModel(getActivity());
        String userEmail = AccountUtils.getUserEmail(getActivity());
        viewModel.fetchSingleUser(userEmail, user -> {
            if(user != null){
                String photoUrl = user.getPhotoUrl();
                String name = user.getName();
                int age = user.getAge();
                String allergies = user.getAllergies();

                if(!TextUtils.isEmpty(photoUrl)){
                    Picasso.get().load(photoUrl).into(userImageView);
                }

                if(!TextUtils.isEmpty(name)){
                    etName.setText(name);
                }
                if(!TextUtils.isEmpty(allergies)){
                    etAllergies.setText(allergies);
                }
                if(age == 0){
                    etAge.setText(String.valueOf(age));
                }

            }
        });
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {




    }
}
