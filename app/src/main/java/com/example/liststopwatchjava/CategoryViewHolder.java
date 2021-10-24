package com.example.liststopwatchjava;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

public class CategoryViewHolder extends RecyclerView.ViewHolder {

    public Button button;
    public TextView textView;

    public CategoryViewHolder(View itemLayoutView) {
        super(itemLayoutView);

        textView = (TextView) itemLayoutView.findViewById(R.id.category_textView);
        button = (Button) itemLayoutView.findViewById(R.id.category_button);
    }


    public TextView getTextView() {
        return textView;
    }
}
