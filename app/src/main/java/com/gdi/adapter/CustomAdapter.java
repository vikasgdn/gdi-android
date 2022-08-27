package com.gdi.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.gdi.hotel.mystery.audits.R;

import java.util.List;

public class CustomAdapter extends ArrayAdapter<String> {

    LayoutInflater flater;

    public CustomAdapter(Activity context,int resouceId, int textviewId, List<String> list){

        super(context,resouceId,textviewId, list);
        flater = context.getLayoutInflater();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = super.getView(position, convertView, parent);
        view.setPadding(0, view.getPaddingTop(), view.getPaddingRight(), view.getPaddingBottom());
        return view;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        if(convertView == null){
            convertView = flater.inflate(R.layout.spinner_row,parent, false);
        }

        TextView txtTitle = (TextView) convertView.findViewById(R.id.cust_view);
        txtTitle.setText(getItem(position));

        return convertView;
    }


}
