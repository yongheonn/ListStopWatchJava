package com.example.liststopwatchjava;

import android.Manifest;
import android.app.AlertDialog;
import android.content.ClipData;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.PorterDuff;
import android.os.Build;
import android.os.Bundle;
import android.support.wearable.activity.WearableActivity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.ItemTouchHelper;
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

    final int REQUEST_ALL_PERMISSION = 1;
    String[] PERMISSIONS = {Manifest.permission.WRITE_EXTERNAL_STORAGE
    , Manifest.permission.READ_EXTERNAL_STORAGE};

    private IntentId intentId;
    private CategoryData categoryData;
    private CategoryListAdapter adapter;
    private Context context;
    public static ArrayList<Context> CONTEXT = new ArrayList<Context>();
    public Button deleteButton;
    public boolean isDeleteOn = false;

    public void OnResume() {
        super.onResume();
        adapter.notifyDataSetChanged();
    }

    public void OnResumeSetting() {
        super.onResume();
        categoryData.load();
        adapter.notifyItemChanged(0);
    }

    public void checkChanged() {
        categoryData.load();
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onBackPressed() {
        if (isDeleteOn) {
            deleteButton.setVisibility(View.GONE);
            deleteButton.setEnabled(false);
            isDeleteOn = false;
        } else {
            if (CONTEXT.size() > 0) {
                ((MainActivity) MainActivity.CONTEXT.get(CONTEXT.size() - 1)).checkChanged();
                CONTEXT.remove(CONTEXT.size() - 1);
            }
            super.onBackPressed();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context = this;
        if (!hasPermissions(this, PERMISSIONS))
            requestPermissions(PERMISSIONS, REQUEST_ALL_PERMISSION);

        RecyclerView recyclerList = (RecyclerView) findViewById(R.id.category_list);

        intentId = new IntentId(getIntent());

        categoryData = new CategoryData(getFilesDir(), intentId.getIntentId());
        categoryData.load();

        adapter = new CategoryListAdapter(categoryData, new TouchListener());
        recyclerList.setLayoutManager(
                new CustomScrollingLayoutCallback(this));
        ItemTouchHelper mItemTouchHelper = new ItemTouchHelper(new ItemTouchHelperCallback(adapter));

        mItemTouchHelper.attachToRecyclerView(recyclerList);
        recyclerList.setAdapter(adapter);

        recyclerList.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN: {
                        if (isDeleteOn) {
                            deleteButton.setVisibility(View.GONE);
                            deleteButton.setEnabled(false);
                            isDeleteOn = false;
                        }
                        break;
                    }
                    default: {

                        break;
                    }
                }
                return false;
            }
        });
    }

    private boolean hasPermissions(Context context, String[] permissions) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission)
                        != PackageManager.PERMISSION_GRANTED
                ) {
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(final int requestCode, String[] permissions,
                                           int[] grantResults) {
        if (requestCode != -1) {
            switch (requestCode) {
                case REQUEST_ALL_PERMISSION: {
                    // If request is cancelled, the result arrays are empty.
                    if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                        Toast.makeText(this, "Permissions granted!", Toast.LENGTH_SHORT).show();
                    } else {
                        requestPermissions(permissions, requestCode);
                        Toast.makeText(this, "Permissions must be granted", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        }
    }

    private class TouchListener implements CategoryListAdapter.OnItemClickListener {
        @Override
        public void onTitleClick() {
            if (intentId.getIntentId().size() > 1) {
                Intent intent = new Intent(getApplicationContext(), CategoryInfoActivity.class);
                intent.putExtra("INTENT_ID", intentId.getIntentId());
                startActivity(intent);
            }
        }

        @Override
        public void onSettingClick() {
            if (intentId.getIntentId().size() > 1) {
                Intent intent = new Intent(getApplicationContext(), CategorySettingActivity.class);
                intent.putExtra("INTENT_ID", intentId.getIntentId());
                CONTEXT.add(context);
                startActivity(intent);
                return;
            }
            if (intentId.getIntentId().size() == 1) {
                Intent intent = new Intent(getApplicationContext(), MainSettingActivity.class);
                intent.putExtra("INTENT_ID", intentId.getIntentId());
                startActivity(intent);
                return;
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
            intent.putExtra("INTENT_ID", intentId.getNextIntentId(position));
            startActivity(intent);
        }

        @Override
        public void onAddClick(int position) {
            Intent intent = new Intent(getApplicationContext(), AddItemSettingActivity.class);
            intent.putExtra("INTENT_ID", intentId.getIntentId());
            CONTEXT.add(context);     //돌아올 때 지금 Context로 돌아오기
            startActivity(intent);
        }

        @Override
        public void onLongClick(Button button) {
            if (deleteButton != null) {
                deleteButton.setVisibility(View.GONE);
                deleteButton.setEnabled(false);
            }
            deleteButton = button;
            isDeleteOn = true;
        }

        @Override
        public void onCategoryDelete(final int _position) {
            AlertDialog.Builder msgBuilder = new AlertDialog.Builder(MainActivity.this)
                    .setTitle(categoryData.loadCategory(_position).getName())
                    .setMessage("이 카테고리를 제거하시겠습니까?")
                    .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            categoryData.deleteCategory(_position);
                            checkChanged();
                        }
                    }).setNegativeButton("취소", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                        }
                    });
            AlertDialog msgDlg = msgBuilder.create();
            msgDlg.show();
        }

        @Override
        public void onToDoDelete(final int _position) {
            AlertDialog.Builder msgBuilder = new AlertDialog.Builder(MainActivity.this)
                    .setTitle(categoryData.loadToDo(_position).getName())
                    .setMessage("이 할일을 제거하시겠습니까?")
                    .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            categoryData.deleteToDo(_position);
                            checkChanged();
                        }
                    }).setNegativeButton("취소", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                        }
                    });
            AlertDialog msgDlg = msgBuilder.create();
            msgDlg.show();
        }

    }
}

