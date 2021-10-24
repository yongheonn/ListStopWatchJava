package com.example.liststopwatchjava;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.wearable.activity.WearableActivity;

import androidx.recyclerview.widget.RecyclerView;
import androidx.wear.widget.drawer.WearableActionDrawerView;
import androidx.wear.widget.drawer.WearableDrawerLayout;
import androidx.wear.widget.drawer.WearableNavigationDrawerView;

import java.util.ArrayList;

public class MainActivity extends WearableActivity {

    private WearableDrawerLayout wearableDrawerLayout;
    private WearableNavigationDrawerView wearableNavigationDrawer;
    private WearableActionDrawerView wearableActionDrawer;
    private ArrayList<Integer> intentId = new ArrayList<Integer>();
    private CustomIO.IOType intentType = CustomIO.IOType.CATEGORY;
    private String title = "메인 카테고리";
    public static Context CONTEXT;

    public void OnResume()
    {
        super.onResume();

        RecyclerView recyclerList = (RecyclerView)findViewById(R.id.category_list);

        CustomIO customIO = new CustomIO(getFilesDir(), intentId);

        CustomAdapter adapter = new CustomAdapter(customIO.Load(), title, new TouchListener());
   //    recyclerList.setLayoutManager(
     //           new CustomScrollingLayoutCallback(this));
        recyclerList.setAdapter(adapter);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        CONTEXT = this;

        Intent intent = getIntent();
        if(intent.getSerializableExtra("ID") != null)
            intentId = (ArrayList<Integer>) intent.getSerializableExtra("ID");
        else
            intentId.add(0);
        if(intent.getSerializableExtra("TYPE") != null)
            intentType = (CustomIO.IOType) intent.getSerializableExtra("TYPE");
        if(intent.getSerializableExtra("NAME") != null)
            title = intent.getStringExtra("NAME");

        RecyclerView recyclerList = (RecyclerView)findViewById(R.id.category_list);

        CustomIO customIO = new CustomIO(getFilesDir(), intentId);

        CustomAdapter adapter = new CustomAdapter(customIO.Load(), title, new TouchListener());
        recyclerList.setLayoutManager(
                new CustomScrollingLayoutCallback(this));
        recyclerList.setAdapter(adapter);
    }

    private class TouchListener implements CustomAdapter.OnItemClickListener
    {
        @Override
        public void onCategoryClick(int position, ArrayList<DoListData> dataSet) {
                Intent intent = new Intent(
                        getApplicationContext(), // 현재 화면의 제어권자
                        MainActivity.class); // 다음 넘어갈 클래스 지정
                ArrayList<Integer> nextId = new ArrayList<Integer>();
                nextId.addAll(intentId);
                nextId.add(position);
                intent.putExtra("ID", nextId);
                intent.putExtra("NAME", dataSet.get(position - 1).name);
             //   intent.putExtra("TYPE", )
                startActivity(intent);
        }

        @Override
        public void onItemClick(int position, ArrayList<DoListData> dataSet) {

        }

        @Override
        public void onAddClick(int position) {
            Intent intent = new Intent(
                    getApplicationContext(), // 현재 화면의 제어권자
                    AddItemSettingActivity.class); // 다음 넘어갈 클래스 지정

            intent.putExtra("ID", intentId);
            intent.putExtra("INTENT", getIntent());
            startActivity(intent);
        }
    }
}

