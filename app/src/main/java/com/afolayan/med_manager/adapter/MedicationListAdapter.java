package com.afolayan.med_manager.adapter;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.afolayan.med_manager.R;
import com.afolayan.med_manager.database.model.Medication;
import com.afolayan.med_manager.utils.Utilities;

import java.text.SimpleDateFormat;
import java.util.List;



public class MedicationListAdapter extends RecyclerView.Adapter<MedicationListAdapter.MedicationsViewHolder> {


    private Activity activity;
    private List<Medication> medications;
    public static final String TAG = MedicationListAdapter.class.getSimpleName();

    public MedicationListAdapter(Activity activity, List<Medication> medications) {
        super();
        this.activity = activity;
        this.medications = medications;
    }

    private View.OnClickListener historyItemClickListener = view -> {

    };

    @Override
    public MedicationsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View mainView = LayoutInflater.from(activity).inflate(R.layout.layout_item_single_medication, parent, false);
        return new MedicationsViewHolder(mainView);
    }

    @Override
    public void onBindViewHolder(MedicationsViewHolder holder, int position) {
        Medication medication = medications.get(position);
        SimpleDateFormat dateFormat = Utilities.DATE_FORMAT;

        if(medication != null) {
            long dateStart = medication.getDateFrom();
            long dateEnd = medication.getDateTo();

            String dateStartString = dateFormat.format(dateStart);
            String dateEndString = dateFormat.format(dateEnd);

            String medName = medication.getName();

            //holder.itemView.setOnClickListener(historyItemClickListener);
            holder.itemView.setTag(medication);

            if(!TextUtils.isEmpty(medName)) {
                holder.tvMedName.setText(medName);
            }
            if (!TextUtils.isEmpty(dateStartString)) {
                holder.tvMedStart.setText(dateStartString);
            }
            if (!TextUtils.isEmpty(dateEndString)) {
                holder.tvMedEnd.setText(dateEndString);
            }
        }
    }


    @Override
    public int getItemCount() {
        return medications.size();
    }

    public class MedicationsViewHolder extends RecyclerView.ViewHolder{
        TextView tvMedName, tvMedStart, tvMedEnd;
        MedicationsViewHolder(View itemView) {
            super(itemView);
            tvMedName = itemView.findViewById(R.id.tv_med_name);
            tvMedStart = itemView.findViewById(R.id.tv_date_start);
            tvMedEnd = itemView.findViewById(R.id.tv_date_end);
        }
    }
}
