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

    public void setPercentage(int _pageProgress, int _page, double _time, String _pageName, String _unitTime) {
        double percentage = (double)_pageProgress / (double)_page;

        String info = "( " + _pageProgress + " / " + _page + " ) " + _pageName + "\n" + (int)(percentage * 100)
                + "%\n" + getUnitTime(_time, _unitTime) + _unitTime;
        percentageBar.setText(info);
        if(percentage <= 1)
            percentageProgress.setWidth((int)(percentageBar.getWidth() * percentage));
        else
            percentageProgress.setWidth(percentageBar.getWidth());
    }

    private double getUnitTime(double _time, String _unitTime) {
        if(_unitTime.equals("시간"))
            return _time / 1000 / 60 / 60;
        if(_unitTime.equals("분"))
            return _time / 1000 / 60;
        if(_unitTime.equals("초"))
            return _time / 1000;
        return _time;
    }
}
