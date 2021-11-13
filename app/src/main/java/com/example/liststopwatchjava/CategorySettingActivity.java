package com.example.liststopwatchjava;

import android.os.Bundle;
import android.os.Handler;
import android.support.wearable.activity.WearableActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.IOException;
import java.text.ParseException;
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
    private int selectedPosition = -1;

    @Override
    public void onBackPressed() {
        try {
            ((MainActivity) CONTEXT.get(CONTEXT.size() - 1)).OnResumeSetting();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
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

        CustomIO customIO = new CustomIO(getFilesDir(), intentId.getIntentId());
        categoryData = new CategoryData();

        try {
            customIO.load(categoryData);
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }

        title.setText(categoryData.name + " 설정");
        name.setText(categoryData.name);

        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int addPageTarget = Integer.parseInt(pageTarget.getText().toString());
                String addPageName = pageName.getSelectedItem().toString();
                String addUnitTime = unitTime.getSelectedItem().toString();
                adapter.addItem(addPageTarget, addPageName, addUnitTime);

                list.setSelected(false);
                pageTarget.setText(null);
                pageName.setSelection(0);
                unitTime.setSelection(0);
            }
        });

        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adapter.deleteItem(selectedPosition);

                pageTarget.setText(null);
                selectedPosition = -1;
                list.setSelected(false);
                pageName.setSelection(0);
                unitTime.setSelection(0);
            }
        });

        editBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int addPageTarget = Integer.parseInt(pageTarget.getText().toString());
                String addPageName = pageName.getSelectedItem().toString();
                String addUnitTime = unitTime.getSelectedItem().toString();
                adapter.editItme(selectedPosition, addPageTarget, addPageName, addUnitTime);

                pageTarget.setText(null);
                selectedPosition = -1;
                list.setSelected(false);
                pageName.setSelection(0);
                unitTime.setSelection(0);
            }
        });

        resetSelectBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                list.setSelected(false);
                selectedPosition = -1;
            }
        });

        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!isNameEmpty()) {
                    categoryData.name = name.getText().toString();
                    categoryData.dailyTarget.add(new DailyTarget(adapter.getPageTarget(), adapter.getPageName()
                    ,adapter.getUnitTime()));
                    IntentId intentId = new IntentId(getIntent());
                    CustomIO customIO = new CustomIO(getFilesDir(), intentId.getIntentId());
                    customIO.Save(categoryData.streamData());
                    onBackPressed();
                }
            }
        });

        ArrayList<String> pageNameList = new ArrayList<String>();
        pageNameList.add("하루 목표 분량 단위");
        pageNameList.addAll(getPageNameList(categoryData));

        SpinnerAdapter pageNameAdapter = new SpinnerAdapter(this
                , R.layout.spinner_item, pageNameList);
        pageNameAdapter.setDropDownViewResource(R.layout.spinner_item);
        pageName.setAdapter(pageNameAdapter);

        ArrayList<String> unitTimeList = new ArrayList<String>();
        unitTimeList.add("하루 목표 시간 단위");
        unitTimeList.add("시간");
        unitTimeList.add("분");
        unitTimeList.add("초");

        SpinnerAdapter unitTimeAdapter = new SpinnerAdapter(this
                , R.layout.spinner_item, unitTimeList);
        unitTimeAdapter.setDropDownViewResource(R.layout.spinner_item);

        unitTime.setAdapter(unitTimeAdapter);

        ArrayList<Integer> pageTarget2 = categoryData.dailyTarget.get(categoryData.dailyTarget.size() - 1)
                .pageTarget;
        ArrayList<String> pageName2 = categoryData.dailyTarget.get(categoryData.dailyTarget.size() - 1)
                .pageName;
        ArrayList<String> unitTime2 = categoryData.dailyTarget.get(categoryData.dailyTarget.size() - 1)
                .unitTime;

        adapter = new DailyTargetListAdapter(pageTarget2, pageName2, unitTime2, new TouchListener());

        list.setLayoutManager(new LinearLayoutManager(this));
        list.setAdapter(adapter);
    }

    private class TouchListener implements DailyTargetListAdapter.OnItemClickListener {
        @Override
        public void onItemClick(int _position) {
            int selectedPageTarget = categoryData.dailyTarget.get(categoryData.dailyTarget.size() - 1)
                    .pageTarget.get(_position);
            String selectedPageName = categoryData.dailyTarget.get(categoryData.dailyTarget.size() - 1)
                    .pageName.get(_position);
            String selectedUnitTime = categoryData.dailyTarget.get(categoryData.dailyTarget.size() - 1)
                    .unitTime.get(_position);
            pageTarget.setText(selectedPageTarget);

            selectedPosition = _position;

            pageName.setSelected(false);
            unitTime.setSelected(false);

            deleteBtn.setEnabled(true);
            editBtn.setEnabled(true);
        }
    }

    private boolean isEmpty() {
        if(pageTarget.getText().toString().equals(""))
            return true;
        if(!pageName.getSelectedItem().toString().equals("하루 목표 분량 단위"))
            return true;
        if(!unitTime.getSelectedItem().toString().equals("하루 목표 시간 단위"))
            return true;
        return false;
    }

    private boolean isNameEmpty() {
        if(name.getText().toString().equals(""))
            return true;
        return false;
    }

    private ArrayList<String> getPageNameList(CategoryData _data) {
        ArrayList<String> pageNameList = new ArrayList<String>();
        for(ToDoData data : _data.toDoData) {
            if(!pageNameList.contains(data.pageName))
                pageNameList.add(data.pageName);
        }
        for(CategoryData data : _data.categoryData) {
            for(String s : getPageNameList(data)) {
                if (!pageNameList.contains(s))
                    pageNameList.add(s);
            }
        }
        return pageNameList;
    }
}
