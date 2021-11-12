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

    public void setTotalPercentage(ArrayList<Integer> _page, ArrayList<Integer> _pageTotal
            , ArrayList<Double> _time, ArrayList<String> _unitTime) {
        int page = 0;
        int pageTotal = 0;
        double time = 0;
        double percentage = 0;
        for(int i : _page)
            page += i;
        for(int i : _pageTotal)
            pageTotal += i;
        for(double d : _time)
            time += d;
        percentage = (double) page / (double) pageTotal;
        int unitTime = 0;
        for(String s : _unitTime) {
            if(s.equals("시간"))
                unitTime = 1;
            else if(s.equals("분") && unitTime != 1)
                unitTime = 2;
            else if(s.equals("초") && unitTime != 1 && unitTime != 2)
                unitTime = 3;
        }

        if(unitTime == 1)
            time /= 1000 * 60 * 60;
        else if(unitTime == 2)
            time /= 1000 * 60;
        else if(unitTime == 3)
            time /= 1000 * 60;

        String info = "총합 정보\n" + (int)(percentage * 100) + "%\n" + String.format("%.1f", time);
        if(unitTime == 1)
            info += "시간";
        else if(unitTime == 2)
            info += "분";
        else if(unitTime == 3)
            info += "초";

        totalPercentageBar.setText(info);
        totalPercentageProgress.setWidth((int)(totalPercentageBar.getWidth() * percentage));
    }
}
