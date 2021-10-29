package com.example.liststopwatchjava;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.wearable.activity.WearableActivity;

import androidx.recyclerview.widget.RecyclerView;
import androidx.wear.widget.drawer.WearableActionDrawerView;
import androidx.wear.widget.drawer.WearableDrawerLayout;
import androidx.wear.widget.drawer.WearableNavigationDrawerView;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;

public class MainActivity extends WearableActivity {

    private WearableDrawerLayout wearableDrawerLayout;
    private WearableNavigationDrawerView wearableNavigationDrawer;
    private WearableActionDrawerView wearableActionDrawer;
    private ArrayList<Integer> intentId = new ArrayList<Integer>();
    private CategoryData categoryData = new CategoryData();
    private Context context;
    public static Context CONTEXT;

    public void OnResume(CustomIO.IOType _ioType)
    {
        super.onResume();

        RecyclerView recyclerList = (RecyclerView)findViewById(R.id.category_list);

        CustomIO customIO = new CustomIO(getFilesDir(), intentId);
        try {
            if(_ioType == CustomIO.IOType.CATEGORY) {
                categoryData.sequence.add(Collections.max(categoryData.sequence) + 1);
                categoryData.categoryData.add(new CategoryData());
            }
            else {
                categoryData.sequence.add(Collections.min(categoryData.sequence) - 1);
                categoryData.doListData.add(new DoListData());
            }

            customIO.Save(categoryData);
            customIO.Load(categoryData);
        } catch (IOException e) {
            e.printStackTrace();
        }
        CustomAdapter adapter = new CustomAdapter(categoryData, new TouchListener());
   //    recyclerList.setLayoutManager(
     //           new CustomScrollingLayoutCallback(this));
        recyclerList.setAdapter(adapter);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        context = this;

        Intent intent = getIntent();
        if(intent.getSerializableExtra("ID") != null)
            intentId = (ArrayList<Integer>) intent.getSerializableExtra("ID");
        else {
            intentId.add(0);
            categoryData.name = "메인 카테고리";
        }
        RecyclerView recyclerList = (RecyclerView)findViewById(R.id.category_list);

        CustomIO customIO = new CustomIO(getFilesDir(), intentId);

        try {
            customIO.Load(categoryData);
        } catch (IOException e) {
            e.printStackTrace();
        }

        CustomAdapter adapter = new CustomAdapter(categoryData, new TouchListener());
        recyclerList.setLayoutManager(
                new CustomScrollingLayoutCallback(this));
        recyclerList.setAdapter(adapter);
    }

    private class TouchListener implements CustomAdapter.OnItemClickListener
    {
        @Override
        public void onCategoryClick(int position, CategoryData _categoryData) {
                Intent intent = new Intent(
                        getApplicationContext(), // 현재 화면의 제어권자
                        MainActivity.class); // 다음 넘어갈 클래스 지정
                ArrayList<Integer> nextId = new ArrayList<Integer>();
                nextId.addAll(intentId);
                nextId.add(position);
                intent.putExtra("ID", nextId);
                startActivity(intent);
        }

        @Override
        public void onItemClick(int position, CategoryData _categoryData) {

        }

        @Override
        public void onAddClick(int position) {
            Intent intent = new Intent(
                    getApplicationContext(), // 현재 화면의 제어권자
                    AddItemSettingActivity.class); // 다음 넘어갈 클래스 지정
            ArrayList<Integer> nextId = new ArrayList<Integer>();
            nextId.addAll(intentId);
            nextId.add(categoryData.sequence.size());

            intent.putExtra("ID", nextId);
            CONTEXT = context;
            startActivity(intent);
        }
    }
}

