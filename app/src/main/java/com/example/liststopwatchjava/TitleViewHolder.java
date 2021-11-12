package com.example.liststopwatchjava;

import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

public class TitleViewHolder extends RecyclerView.ViewHolder {
    public TextView textView;
    public Button button;
    public ImageButton setting;

    public TitleViewHolder(View itemLayoutView) {
        super(itemLayoutView);

        textView = (TextView) itemLayoutView.findViewById(R.id.title_textView);
        button = (Button) itemLayoutView.findViewById(R.id.title_button);
        setting = (ImageButton) itemLayoutView.findViewById(R.id.title_setting);
    }
}