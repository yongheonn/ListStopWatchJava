package com.example.liststopwatchjava;

import android.content.Intent;
import android.os.Bundle;
import android.support.wearable.activity.WearableActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import java.util.ArrayList;

public class AddItemSettingActivity extends WearableActivity {
    private CustomIO.IOType intentType = CustomIO.IOType.CATEGORY;
    private String unitTime = "초";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_add_item_setting);
        Intent intent = getIntent();
        final ArrayList<Integer> id = (ArrayList<Integer>) intent.getSerializableExtra("ID");
        final Button isCategory = (Button) findViewById(R.id.is_category);
        final Button isDoList = (Button) findViewById(R.id.is_doList);
        final Button confirm = (Button) findViewById(R.id.add_confirm);
        final EditText editText = (EditText) findViewById(R.id.add_editText_name);
        final LinearLayout linearLayout = (LinearLayout) findViewById(R.id.add_itemOnly);
        final EditText editText_pageTotal = (EditText) findViewById(R.id.add_editText_pageTotal);
        final EditText editText_pageName = (EditText) findViewById(R.id.add_editText_pageName);
        final EditText editText_pageTarget = (EditText) findViewById(R.id.add_editText_pageTarget);
        final EditText editText_timeTarget = (EditText) findViewById(R.id.add_editText_timeTarget);
        final Button button_second = (Button) findViewById(R.id.add_button_timeSecond);
        final Button button_minute = (Button) findViewById(R.id.add_button_timeMinute);
        final Button button_hour = (Button) findViewById(R.id.add_button_timeHour);

        button_second.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                button_second.setEnabled(false);
                button_minute.setEnabled(true);
                button_hour.setEnabled(true);
                unitTime = "초";
            }
        });

        button_minute.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                button_second.setEnabled(true);
                button_minute.setEnabled(false);
                button_hour.setEnabled(true);
                unitTime = "분";
            }
        });

        button_hour.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                button_second.setEnabled(true);
                button_minute.setEnabled(true);
                button_hour.setEnabled(false);
                unitTime = "시";
            }
        });

        isCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isCategory.setEnabled(false);
                isDoList.setEnabled(true);
                linearLayout.setVisibility(View.GONE);
                intentType = CustomIO.IOType.CATEGORY;
            }
        });

        isDoList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isDoList.setEnabled(false);
                isCategory.setEnabled(true);
                linearLayout.setVisibility(View.VISIBLE);
                intentType = CustomIO.IOType.DOLIST;
            }
        });

        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CustomIO customIO = new CustomIO(getFilesDir(), id);
                if (intentType == CustomIO.IOType.CATEGORY)
                    customIO.Save(new CategoryData(editText.getText().toString()));
                else {
                    double pagePerTime = Double.parseDouble(editText_pageTarget.getText().toString())
                            / Double.parseDouble(editText_timeTarget.getText().toString());
                    int pageTotal = Integer.parseInt(editText_pageTotal.getText().toString());
                    customIO.Save(new DoListData(editText.getText().toString(),
                            pageTotal, pagePerTime, unitTime, editText_pageName.getText().toString()));
                }
                ((MainActivity) MainActivity.CONTEXT).OnResume(intentType);
                onBackPressed();
            }
        });
    }
}
