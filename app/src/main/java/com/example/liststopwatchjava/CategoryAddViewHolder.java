package com.example.liststopwatchjava;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

public class CategoryAddViewHolder extends RecyclerView.ViewHolder {

    public Button button;
    public TextView textView;

    public CategoryAddViewHolder(View itemLayoutView) {
        super(itemLayoutView);

        textView = (TextView) itemLayoutView.findViewById(R.id.add_textView);
        button = (Button) itemLayoutView.findViewById(R.id.add_button);
    }


    public TextView getTextView() {
        return textView;
    }
}
