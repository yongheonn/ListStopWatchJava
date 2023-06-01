package com.example.liststopwatchjava;

import android.os.Bundle;
import android.support.wearable.activity.WearableActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import static com.example.liststopwatchjava.MainActivity.CONTEXT;

public class CategorySettingActivity extends WearableActivity {
    private CategoryData categoryData;
    private TextView title;
    private EditText name;
    private RecyclerView list;
    private EditText pageTarget;
    private Spinner pageName;
    private Spinner unitTime;
    private Button addBtn;
    private Button deleteBtn;
    private Button editBtn;
    private Button resetSelectBtn;
    private Button saveBtn;
    private DailyTargetListAdapter adapter;
    private ArrayList<String> pageNameList;
    private ArrayList<String> unitTimeList;
    private int selectedPosition = 0;

    @Override
    public void onBackPressed() {
        ((MainActivity) CONTEXT.get(CONTEXT.size() - 1)).OnResumeSetting();

        CONTEXT.remove(CONTEXT.size() - 1);
        super.onBackPressed();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.category_setting);

        title = (TextView) findViewById(R.id.category_setting_title);
        name = (EditText) findViewById(R.id.category_setting_name);
        list = (RecyclerView) findViewById(R.id.category_setting_dailyList);
        pageTarget = (EditText) findViewById(R.id.category_setting_pageTarget);
        pageName = (Spinner) findViewById(R.id.category_setting_pageName);
        unitTime = (Spinner) findViewById(R.id.category_setting_unitTime);
        addBtn = (Button) findViewById(R.id.category_setting_add);
        deleteBtn = (Button) findViewById(R.id.category_setting_delete);
        editBtn = (Button) findViewById(R.id.category_setting_edit);
        resetSelectBtn = (Button) findViewById(R.id.category_setting_resetSelect);
        saveBtn = (Button) findViewById(R.id.category_setting_save);

        IntentId intentId = new IntentId(getIntent());

        categoryData = new CategoryData(getFilesDir(), intentId.getIntentId());
        categoryData.load();

        title.setText(categoryData.getName() + " 설정");
        name.setText(categoryData.getName());

        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isEmpty()) {
                    int addPageTarget = Integer.parseInt(pageTarget.getText().toString());
                    String addPageName = pageName.getSelectedItem().toString();
                    String addUnitTime = unitTime.getSelectedItem().toString();
                    adapter.addItem(addPageTarget, addPageName, addUnitTime);

                    list.setSelected(false);
                    pageTarget.setText(null);
                    pageName.setSelection(0);
                    unitTime.setSelection(0);
                }
            }
        });

        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selectedPosition > 0) {
                    adapter.deleteItem(selectedPosition - 1);

                    pageTarget.setText(null);
                    selectedPosition = 0;
                    list.setSelected(false);
                    pageName.setSelection(0);
                    unitTime.setSelection(0);
                }
            }
        });

        editBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isEmpty() && selectedPosition > 0) {
                    int addPageTarget = Integer.parseInt(pageTarget.getText().toString());
                    String addPageName = pageName.getSelectedItem().toString();
                    String addUnitTime = unitTime.getSelectedItem().toString();
                    adapter.editItme(selectedPosition - 1, addPageTarget, addPageName, addUnitTime);

                    pageTarget.setText(null);
                    selectedPosition = 0;
                    list.setSelected(false);
                    pageName.setSelection(0);
                    unitTime.setSelection(0);
                }
            }
        });

        resetSelectBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                list.setSelected(false);
                pageTarget.setText(null);
                pageName.setSelection(0);
                unitTime.setSelection(0);
                selectedPosition = 0;
            }
        });

        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isNameEmpty()) {
                    categoryData.setName(name.getText().toString());
                    try {
                        categoryData.addDailyTarget(adapter.getPageTarget(), adapter.getPageName()
                                , adapter.getUnitTime());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    onBackPressed();
                }
            }
        });

        pageNameList = new ArrayList<String>();
        pageNameList.add("하루 목표 분량 단위");
        pageNameList.addAll(categoryData.getPageNameList());

        SpinnerAdapter pageNameAdapter = new SpinnerAdapter(this
                , R.layout.spinner_item, pageNameList);
        pageNameAdapter.setDropDownViewResource(R.layout.spinner_item);
        pageName.setAdapter(pageNameAdapter);

        unitTimeList = new ArrayList<String>();
        unitTimeList.add("하루 목표 시간 단위");
        unitTimeList.add("시간");
        unitTimeList.add("분");
        unitTimeList.add("초");

        SpinnerAdapter unitTimeAdapter = new SpinnerAdapter(this
                , R.layout.spinner_item, unitTimeList);
        unitTimeAdapter.setDropDownViewResource(R.layout.spinner_item);

        unitTime.setAdapter(unitTimeAdapter);

        ArrayList<Integer> pageTargetTemp = categoryData.rearDailyPageTarget();
        ArrayList<String> pageNameTemp = categoryData.rearDailyPageName();
        ArrayList<String> unitTimeTemp = categoryData.rearDailyUnitTime();

        adapter = new DailyTargetListAdapter(pageTargetTemp, pageNameTemp, unitTimeTemp, new TouchListener());

        list.setLayoutManager(new LinearLayoutManager(this));
        list.setAdapter(adapter);
    }

    private class TouchListener implements DailyTargetListAdapter.OnItemClickListener {
        @Override
        public void onItemClick(int _position) {
            String selectedPageTarget = "" + adapter.getPageTarget().get(_position);
            String selectedPageName = adapter.getPageName().get(_position);
            String selectedUnitTime = adapter.getUnitTime().get(_position);
            pageTarget.setText(selectedPageTarget);
            pageName.setSelection(pageNameList.indexOf(selectedPageName));
            unitTime.setSelection(unitTimeList.indexOf(selectedUnitTime));

            selectedPosition = _position + 1;
        }
    }

    private boolean isEmpty() {
        if (pageTarget.getText().toString().equals(""))
            return true;
        if (pageName.getSelectedItem().toString().equals("하루 목표 분량 단위"))
            return true;
        if (unitTime.getSelectedItem().toString().equals("하루 목표 시간 단위"))
            return true;
        return false;
    }

    private boolean isNameEmpty() {
        if (name.getText().toString().equals(""))
            return true;
        return false;
    }
}
