package com.afolayan.med_manager.adapter;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.afolayan.med_manager.R;
import com.afolayan.med_manager.model.Frequency;

import java.util.List;

/**
 * Created by Oluwaseyi AFOLAYAN on 4/8/2018.
 */

public class FrequencyListAdapter extends BaseAdapter {

    private static final String TAG = FrequencyListAdapter.class.getSimpleName();
    private List<Frequency> frequencyList;
    private Context mContext;

    public FrequencyListAdapter(List<Frequency> frequencies, Context context) {
        super();
        this.frequencyList = frequencies;
        mContext = context;
    }

    @Override
    public int getCount() {
        Log.e(TAG, "getCount: size == "+ frequencyList.size() );
        if(frequencyList != null){
            return frequencyList.size();
        } else {
            return 0;
        }
    }

    @Override
    public Frequency getItem(int position) {
        return frequencyList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return frequencyList.indexOf(frequencyList.get(position));
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        SpinnerViewHolder spinnerViewHolder;
        if( convertView == null) {

            LayoutInflater inflater = ((Activity) mContext).getLayoutInflater();
            convertView = inflater.inflate(R.layout.layout_single_spinner_item, parent, false);

            spinnerViewHolder = new SpinnerViewHolder();
            spinnerViewHolder.tvLocationName = convertView.findViewById(R.id.tv_frequency_name);
            (convertView.findViewById(R.id.tv_frequency_count)).setVisibility(View.GONE);

            convertView.setTag(spinnerViewHolder);
        } else {
            spinnerViewHolder = (SpinnerViewHolder) convertView.getTag();
        }

        Frequency location = frequencyList.get(position);
        if(location != null) {
            String address = location.getName();
            spinnerViewHolder.tvLocationName.setText(address);
        }
        return convertView;
    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

    @Override
    public int getViewTypeCount() {
        return super.getViewTypeCount();
    }

    private static class SpinnerViewHolder{
        TextView tvLocationName;
    }
}
