package com.afolayan.med_manager.adapter;

import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.afolayan.med_manager.R;
import com.afolayan.med_manager.database.model.Medication;
import com.afolayan.med_manager.utils.Utilities;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import io.github.luizgrp.sectionedrecyclerviewadapter.SectionParameters;
import io.github.luizgrp.sectionedrecyclerviewadapter.StatelessSection;

/**
 * Created by Oluwaseyi AFOLAYAN on 4/13/2018.
 */

public class MedicationListSection extends StatelessSection {

    private List<Medication> medicationList;

    public MedicationListSection(List<Medication> medications) {
        super(SectionParameters.builder()
                .itemResourceId(R.layout.layout_item_single_medication)
                .headerResourceId(R.layout.list_section_header)
                .build());

        this.medicationList = medications;
    }

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
    public class ItemViewHolder extends RecyclerView.ViewHolder{
        TextView tvMedName, tvMedStart, tvMedEnd;
        ItemViewHolder(View itemView) {
            super(itemView);
            tvMedName = itemView.findViewById(R.id.tv_med_name);
            tvMedStart = itemView.findViewById(R.id.tv_date_start);
            tvMedEnd = itemView.findViewById(R.id.tv_date_end);
        }
    }
}