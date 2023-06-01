package com.example.liststopwatchjava;

import android.os.Bundle;
import android.support.wearable.activity.WearableActivity;
import android.widget.TextView;

public class ToDoInfoActivity extends WearableActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.todo_info);

        TextView title = (TextView) findViewById(R.id.todo_info_title);
        TextView total_info = (TextView) findViewById(R.id.todo_info_total);

        IntentId intentId = new IntentId(getIntent());
        ToDoData toDoData = new ToDoData(getFilesDir(), intentId.getIntentId());
        toDoData.load();
        title.setText(toDoData.getName());
        total_info.setText(toDoData.getTotalInfo());
        title.setSelected(true);
    }
}
