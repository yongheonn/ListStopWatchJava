package com.example.liststopwatchjava;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class SpinnerAdapter extends ArrayAdapter {

    private Context mContext;
    private ArrayList<String> data;
    public SpinnerAdapter(Context context, int textViewResourceId,
                          ArrayList<String> _data) {
        super(context, textViewResourceId, _data);
        mContext = context;
        data = _data;
    }

    public View getCustomView(int position, View convertView,
                              ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View layout = inflater.inflate(R.layout.spinner_item, parent, false);

        TextView itemText = (TextView) layout
                .findViewById(R.id.spinner_item_text);

        itemText.setText(data.get(position));

        if(position == 0) {
            itemText.setEnabled(false);
            itemText.setBackground(mContext.getDrawable(R.drawable.grey_corner_background));
        }

        return layout;
    }

    public View getSelectedView(int position, View convertView,
                              ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View layout = inflater.inflate(R.layout.spinner_selected, parent, false);

        TextView itemText = (TextView) layout
                .findViewById(R.id.spinner_selected_text);

        itemText.setText(data.get(position));

        itemText.setTextSize(15);

        return layout;
    }

    // It gets a View that displays in the drop down popup the data at the specified position
    @Override
    public View getDropDownView(int position, View convertView,
                                ViewGroup parent) {
        return getCustomView(position, convertView, parent);
    }

    // It gets a View that displays the data at the specified position
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return getSelectedView(position, convertView, parent);
    }
}
