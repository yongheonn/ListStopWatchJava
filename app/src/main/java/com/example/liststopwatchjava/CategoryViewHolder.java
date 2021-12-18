package com.example.liststopwatchjava;

import android.view.View;
import android.widget.Button;

import androidx.recyclerview.widget.RecyclerView;

public class CategoryViewHolder extends RecyclerView.ViewHolder {

    public Button button;
    public Button deleteButton;

    public CategoryViewHolder(View itemLayoutView) {
        super(itemLayoutView);
        button = (Button) itemLayoutView.findViewById(R.id.item_category_button);
        deleteButton = (Button) itemLayoutView.findViewById(R.id.item_category_deleteButton);
    }
}