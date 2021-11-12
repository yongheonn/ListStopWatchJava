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
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;

public class MainActivity extends WearableActivity {

    private WearableDrawerLayout wearableDrawerLayout;
    private WearableNavigationDrawerView wearableNavigationDrawer;
    private WearableActionDrawerView wearableActionDrawer;

    private IntentId intentId;
    private CategoryData categoryData = new CategoryData();
    private CategoryListAdapter adapter;
    private Context context;
    public static ArrayList<Context> CONTEXT = new ArrayList<Context>();

    public void OnResume(CustomIO.IOType _ioType) {
        super.onResume();
        categoryChangeApply(_ioType);
        adapter.notifyDataSetChanged();
    }

    public void OnResumeSetting() throws IOException, ParseException {
        super.onResume();
        CustomIO customIO = new CustomIO(getFilesDir(), intentId.getIntentId());
        customIO.load(categoryData);
        adapter.notifyItemChanged(0);
    }

    public void checkChanged() {
        CustomIO customIO = new CustomIO(getFilesDir(), intentId.getIntentId());
        try {
            customIO.load(categoryData);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onBackPressed() {
        if(CONTEXT.size() > 0) {
            ((MainActivity) MainActivity.CONTEXT.get(CONTEXT.size() - 1)).checkChanged();
            CONTEXT.remove(CONTEXT.size() - 1);
        }
        super.onBackPressed();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context = this;

        RecyclerView recyclerList = (RecyclerView) findViewById(R.id.category_list);

        intentId = new IntentId(getIntent());

        CustomIO customIO = new CustomIO(getFilesDir(), intentId.getIntentId());

        try {
            customIO.load(categoryData);
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }

        adapter = new CategoryListAdapter(categoryData, new TouchListener());
        recyclerList.setLayoutManager(
                new CustomScrollingLayoutCallback(this));
        recyclerList.setAdapter(adapter);

    }

    private class TouchListener implements CategoryListAdapter.OnItemClickListener {
        @Override
        public void onTitleClick() {
            if(intentId.getIntentId().size() > 1) {
                Intent intent = new Intent(getApplicationContext(), CategoryInfoActivity.class);
                intent.putExtra("INTENT_ID", intentId.getIntentId());
                startActivity(intent);
            }
        }

        @Override
        public void onSettingClick() {
            if(intentId.getIntentId().size() > 1) {
                Intent intent = new Intent(getApplicationContext(), CategorySettingActivity.class);
                intent.putExtra("INTENT_ID", intentId.getIntentId());
                CONTEXT.add(context);
                startActivity(intent);
            }
        }

        @Override
        public void onCategoryClick(int position) {
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            intent.putExtra("INTENT_ID", intentId.getNextIntentId(position));
            CONTEXT.add(context);
            startActivity(intent);
        }

        @Override
        public void onToDoClick(int position) {
            Intent intent = new Intent(getApplicationContext(), ToDoActivity.class);
            int index = categoryData.sequence.get(position);
            intent.putExtra("INTENT_ID", intentId.getNextIntentId(position));
            intent.putExtra("TODO_DATA", (Serializable) categoryData.toDoData.get(index));
            startActivity(intent);
        }

        @Override
        public void onAddClick(int position) {
            Intent intent = new Intent(getApplicationContext(), AddItemSettingActivity.class);
            int endPosition = categoryData.sequence.size();
            intent.putExtra("INTENT_ID", intentId.getNextIntentId(endPosition));
            CONTEXT.add(context);     //돌아올 때 지금 Context로 돌아오기
            startActivity(intent);
        }
    }

    private void categoryChangeApply(CustomIO.IOType _ioType) {
        CustomIO customIO = new CustomIO(getFilesDir(), intentId.getIntentId());
        try {
            if (_ioType == CustomIO.IOType.CATEGORY) {
                categoryData.sequence.addCategory();
                categoryData.categoryData.add(new CategoryData());
            } else {
                categoryData.sequence.addToDo();
                categoryData.toDoData.add(new ToDoData());
            }

            customIO.Save(categoryData.streamData());
            customIO.load(categoryData);
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
    }

}

