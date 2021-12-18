package com.example.liststopwatchjava;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class TotalPercentageVH extends RecyclerView.ViewHolder {
    TextView totalPercentageBar;
    TextView totalPercentageProgress;
    public TotalPercentageVH(View itemLayoutView) {
        super(itemLayoutView);
        totalPercentageBar = (TextView) itemLayoutView.findViewById(R.id.percentage_total_bar);
        totalPercentageProgress = (TextView) itemLayoutView.findViewById(R.id.percentage_total_progress);
    }

    public void setTotalPercentage(double _percentage, String _info) {
        totalPercentageBar.setText(_info);
        totalPercentageProgress.setWidth((int)(totalPercentageBar.getWidth() * _percentage));
        totalPercentageProgress.setHeight(totalPercentageBar.getHeight());
    }
}
