package com.example.liststopwatchjava;

import android.os.Bundle;
import android.support.wearable.activity.WearableActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.Switch;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import static com.example.liststopwatchjava.MainActivity.CONTEXT;

public class AddItemSettingActivity extends WearableActivity {
    private ItemType itemType = ItemType.CATEGORY;
    private String unitTime = "초";
    private boolean isAdd = false;
    IntentId intentId;
    CategoryData categoryData;

    @Override
    public void onBackPressed() {
        if(isAdd)
            ((MainActivity) CONTEXT.get(CONTEXT.size() - 1)).checkChanged();
        else
            ((MainActivity) CONTEXT.get(CONTEXT.size() - 1)).OnResume();
        CONTEXT.remove(CONTEXT.size() - 1);
        super.onBackPressed();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_add_item_setting);

        final Button isCategory = (Button) findViewById(R.id.is_category);
        final Button isDoList = (Button) findViewById(R.id.is_doList);

        final EditText editText = (EditText) findViewById(R.id.add_editText_name);

        final LinearLayout linearLayout_toDo = (LinearLayout) findViewById(R.id.add_toDoOnly);
        final EditText editText_pageTotal = (EditText) findViewById(R.id.add_editText_pageTotal);
        final EditText editText_pageName = (EditText) findViewById(R.id.add_editText_pageName);
        final EditText editText_pageTarget = (EditText) findViewById(R.id.add_editText_pageTarget);
        final EditText editText_timeTarget = (EditText) findViewById(R.id.add_editText_timeTarget);
        final Button button_second = (Button) findViewById(R.id.add_button_timeSecond);
        final Button button_minute = (Button) findViewById(R.id.add_button_timeMinute);
        final Button button_hour = (Button) findViewById(R.id.add_button_timeHour);
        final Switch switch_isStep = (Switch) findViewById(R.id.add_switch);

        final Button confirm = (Button) findViewById(R.id.add_confirm);

        intentId = new IntentId(getIntent());
        categoryData = new CategoryData(getFilesDir(), intentId.getIntentId());
        categoryData.load();

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
                unitTime = "시간";
            }
        });

        isCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isCategory.setEnabled(false);
                isDoList.setEnabled(true);
                linearLayout_toDo.setVisibility(View.GONE);
                itemType = ItemType.CATEGORY;
            }
        });

        isDoList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isDoList.setEnabled(false);
                isCategory.setEnabled(true);
                linearLayout_toDo.setVisibility(View.VISIBLE);
                itemType = ItemType.TODO;
            }
        });

        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (itemType == ItemType.CATEGORY) {
                    String itemName = editText.getText().toString();
                    categoryData.addCategory(itemName);
                }
                else {
                    String itemName = editText.getText().toString();
                    double pagePerTime = Double.parseDouble(editText_pageTarget.getText().toString())
                            / Double.parseDouble(editText_timeTarget.getText().toString());
                    int pageTotal = Integer.parseInt(editText_pageTotal.getText().toString());
                    String pageName = editText_pageName.getText().toString();
                    boolean isStep = switch_isStep.isChecked();

                    categoryData.addToDo(itemName, pageTotal, pagePerTime
                            , unitTime, pageName, isStep);
                }
                isAdd = true;
                onBackPressed();
            }
        });
    }
}
