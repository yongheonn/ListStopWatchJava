package com.example.liststopwatchjava;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

public class DailyTargetViewHolder extends RecyclerView.ViewHolder {
    public Button button;

    public DailyTargetViewHolder(View itemLayoutView) {
        super(itemLayoutView);
        button = (Button) itemLayoutView.findViewById(R.id.daily_item_textView);
    }

    public Button getButton() {
        return button;
    }

    public void setTextView(int _pageTarget, String _pageName, String _unitTime) {
        button.setText(_pageTarget + " " + _pageName + "   " + "(" + _unitTime + ")");
    }
}
