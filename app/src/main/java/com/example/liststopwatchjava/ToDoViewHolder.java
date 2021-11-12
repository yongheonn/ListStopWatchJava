package com.example.liststopwatchjava;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

public class ToDoViewHolder extends RecyclerView.ViewHolder {

    public Button button;

    public ToDoViewHolder(View itemLayoutView) {
        super(itemLayoutView);

        button = (Button) itemLayoutView.findViewById(R.id.item_button);
    }

}
