package com.example.liststopwatchjava;

import android.os.Bundle;
import android.support.wearable.activity.WearableActivity;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.IOException;
import java.text.ParseException;

public class CategoryInfoActivity extends WearableActivity {
    CategoryData categoryData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.category_info);

        TextView title = (TextView) findViewById(R.id.category_info_title);
        RecyclerView percentList = (RecyclerView) findViewById(R.id.category_info_list);
        IntentId intentId = new IntentId(getIntent());

        CustomIO customIO = new CustomIO(getFilesDir(), intentId.getIntentId());
        categoryData = new CategoryData();

        try {
            customIO.load(categoryData);
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }

        title.setText(categoryData.name);

        CategoryInfoListAdapter adapter = new CategoryInfoListAdapter(categoryData.dailyTarget
                .get(categoryData.dailyTarget.size() - 1));

        percentList.setLayoutManager(new LinearLayoutManager(this));
        percentList.setAdapter(adapter);
    }
}
