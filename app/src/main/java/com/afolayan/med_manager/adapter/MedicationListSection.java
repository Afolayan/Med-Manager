package com.afolayan.med_manager.adapter;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.afolayan.med_manager.MedicationActivity;
import com.afolayan.med_manager.MedicationDetailsDialogFragment;
import com.afolayan.med_manager.R;
import com.afolayan.med_manager.database.model.Medication;
import com.afolayan.med_manager.utils.Utilities;
import com.google.gson.Gson;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import io.github.luizgrp.sectionedrecyclerviewadapter.SectionParameters;
import io.github.luizgrp.sectionedrecyclerviewadapter.StatelessSection;

import static com.afolayan.med_manager.utils.Utilities.MEDICATION;

/**
 * Created by Oluwaseyi AFOLAYAN on 4/13/2018.
 */

public class MedicationListSection extends StatelessSection {

    private List<Medication> medicationList;
    private View.OnClickListener deleteImageClickListener;
    private MedicationActivity activity;

    public MedicationListSection(MedicationActivity activity, List<Medication> medications) {
        super(SectionParameters.builder()
                .itemResourceId(R.layout.layout_item_single_medication)
                .headerResourceId(R.layout.list_section_header)
                .build());

        this.activity = activity;
        this.medicationList = medications;
    }

    public void setDeleteImageClickListener(View.OnClickListener deleteImageClickListener) {
        this.deleteImageClickListener = deleteImageClickListener;
    }

    private View.OnClickListener medicationItemClickListener = view -> {
        MedicationDetailsDialogFragment dialogFragment = new MedicationDetailsDialogFragment();
        FragmentManager manager = activity.getSupportFragmentManager();
        Medication selectedMed = (Medication) view.getTag();
        Bundle bundle = new Bundle();
        bundle.putString(MEDICATION, new Gson().toJson(selectedMed));
        dialogFragment.setArguments(bundle);
        dialogFragment.setStyle(DialogFragment.STYLE_NORMAL, R.style.Dialog_Medication_Details);
        dialogFragment.show(manager, "details");

    };
    @Override
    public int getContentItemsTotal() {
        return medicationList.size();
    }

    @Override
    public RecyclerView.ViewHolder getItemViewHolder(View view) {
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindItemViewHolder(RecyclerView.ViewHolder holder, int position) {
        final ItemViewHolder itemHolder = (ItemViewHolder) holder;

        Medication medication = medicationList.get(position);

        SimpleDateFormat dateFormat = Utilities.DATE_FORMAT;

        if(medication != null) {
            long dateStart = medication.getDateFrom();
            long dateEnd = medication.getDateTo();

            String dateStartString = dateFormat.format(dateStart);
            String dateEndString = dateFormat.format(dateEnd);

            String medName = medication.getName();

            //holder.itemView.setOnClickListener(historyItemClickListener);
            itemHolder.itemView.setTag(medication);

            if(!TextUtils.isEmpty(medName)) {
                itemHolder.tvMedName.setText(medName);
            }
            if (!TextUtils.isEmpty(dateStartString)) {
                itemHolder.tvMedStart.setText(dateStartString);
            }
            if (!TextUtils.isEmpty(dateEndString)) {
                itemHolder.tvMedEnd.setText(dateEndString);
            }
            if(deleteImageClickListener != null) {
                itemHolder.ivDelete.setTag(medication);
                itemHolder.ivDelete.setOnClickListener(deleteImageClickListener);
            }
            itemHolder.itemView.setOnClickListener(medicationItemClickListener);
        }
    }

    @Override
    public RecyclerView.ViewHolder getHeaderViewHolder(View view) {
        return new HeaderViewHolder(view);
    }

    @Override
    public void onBindHeaderViewHolder(RecyclerView.ViewHolder holder) {
        HeaderViewHolder headerHolder = (HeaderViewHolder) holder;
        if(medicationList.size() > 0) {
            String dateMonthYear = Utilities.DATE_FORMAT_YEAR_MONTH.format(new Date(medicationList.get(0).getDateCreated()));
            headerHolder.tvTitle.setText(dateMonthYear);
        }
    }
    private class HeaderViewHolder extends RecyclerView.ViewHolder {

        private final TextView tvTitle;

        HeaderViewHolder(View view) {
            super(view);

            tvTitle = view.findViewById(R.id.tvTitle);
        }
    }
    class ItemViewHolder extends RecyclerView.ViewHolder{
        TextView tvMedName, tvMedStart, tvMedEnd;
        ImageView ivDelete;
        ItemViewHolder(View itemView) {
            super(itemView);
            tvMedName = itemView.findViewById(R.id.tv_med_name);
            tvMedStart = itemView.findViewById(R.id.tv_date_start);
            tvMedEnd = itemView.findViewById(R.id.tv_date_end);
            ivDelete = itemView.findViewById(R.id.img_delete);
        }
    }
}