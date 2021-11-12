package com.example.liststopwatchjava;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

public class AddItemViewHolder extends RecyclerView.ViewHolder {

    public Button button;

    public AddItemViewHolder(View itemLayoutView) {
        super(itemLayoutView);

        button = (Button) itemLayoutView.findViewById(R.id.add_button);
    }
}
