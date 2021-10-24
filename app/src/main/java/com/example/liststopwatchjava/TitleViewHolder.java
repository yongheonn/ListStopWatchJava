package com.example.liststopwatchjava;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

public class TitleViewHolder extends RecyclerView.ViewHolder {

    public TextView textView;

    public TitleViewHolder(View itemLayoutView) {
        super(itemLayoutView);

        textView = (TextView) itemLayoutView.findViewById(R.id.title_textView);
    }


    public TextView getTextView() {
        return textView;
    }
}