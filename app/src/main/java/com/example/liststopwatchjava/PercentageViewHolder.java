package com.example.liststopwatchjava;

import android.graphics.drawable.shapes.Shape;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class PercentageViewHolder extends RecyclerView.ViewHolder {
    TextView percentageBar;
    TextView percentageProgress;

    public PercentageViewHolder(View itemLayoutView) {
        super(itemLayoutView);
        percentageBar = (TextView) itemLayoutView.findViewById(R.id.percentage_bar);
        percentageProgress = (TextView) itemLayoutView.findViewById(R.id.percentage_progress);
    }

    public void setPercentage(double _percentage, String _info) {

        percentageBar.setText(_info);
        if(_percentage <= 1)
            percentageProgress.setWidth((int)(percentageBar.getWidth() * _percentage));
        else
            percentageProgress.setWidth(percentageBar.getWidth());
        percentageProgress.setHeight(percentageBar.getHeight());
    }
}
