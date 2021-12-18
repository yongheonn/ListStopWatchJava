package com.example.liststopwatchjava;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.wearable.activity.WearableActivity;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.IOException;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class CategoryInfoActivity extends WearableActivity {
    CategoryData categoryData;
    CategoryInfoListAdapter adapter;
    Button datePicker;
    DatePickerDialog datePickerDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.category_info);

        TextView title = (TextView) findViewById(R.id.category_info_title);
        title.setSelected(true);
        datePicker = (Button) findViewById(R.id.category_info_datepicker);

        RecyclerView percentList = (RecyclerView) findViewById(R.id.category_info_list);
        IntentId intentId = new IntentId(getIntent());
        categoryData = new CategoryData(getFilesDir(), intentId.getIntentId());
        categoryData.load();

        title.setText(categoryData.getName());
        datePicker.setText(LocalDate.now()
                .format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));

        adapter = new CategoryInfoListAdapter(categoryData.dailyContain(LocalDate.now()));

        percentList.setAdapter(adapter);

        percentList.setLayoutManager(new LinearLayoutManager(this));

        datePickerDialog = new DatePickerDialog(this
                , new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                DailyData dailyData = categoryData.dailyContain(LocalDate.of(year, month + 1, dayOfMonth));
                datePicker.setText(dailyData.getDate()
                        .format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
                adapter.setData(dailyData);
                adapter.notifyDataSetChanged();

            }
        }, adapter.getDate().getYear(), adapter.getDate().getMonthValue() - 1
                , adapter.getDate().getDayOfMonth());

        datePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datePickerDialog.show();
            }
        });
    }
}
