package com.example.liststopwatchjava;

import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.support.wearable.activity.WearableActivity;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import static android.view.View.GONE;

public class ToDoActivity extends WearableActivity {
    ToDoData toDoData;

    Button title;
    TextView currentTime;
    TextView elapse;
    Button startBtn;
    Button resetBtn;
    EditText pageProgress;

    final int IDLE = 0;
    final int RUNNING = 1;
    final int PAUSE = 2;
    int status = IDLE;

    long baseTime;
    long pauseTime;
    long startTime;
    long endTime;
    SimpleDateFormat currentTimeFormat = new SimpleDateFormat("a hh:mm");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.todo_stopwatch);

        title = (Button) findViewById(R.id.todo_title);
        currentTime = (TextView) findViewById(R.id.todo_currentTime);
        elapse = (TextView) findViewById(R.id.todo_elapse);
        startBtn = (Button) findViewById(R.id.todo_startBtn);
        resetBtn = (Button) findViewById(R.id.todo_resetBtn);
        pageProgress = (EditText) findViewById(R.id.todo_pageProgress);
        toDoData = (ToDoData) getIntent().getSerializableExtra("TODO_DATA");


        title.setText(toDoData.name);
        elapse.setText(toDoData.getStringTime());
        currentTime.setText(getCurrentTime());
        currentTimer.sendEmptyMessage(0);

        setTitleBtnListener();
        setStartBtnListener();
        setResetBtnListener();
        setPageProgressListener();
    }

    @Override
    protected void onDestroy() {
        timer.removeMessages(0);
        currentTimer.removeMessages(0);
        super.onDestroy();
    }

    Handler currentTimer = new Handler() {
        public void handleMessage(android.os.Message msg) {
            currentTime.setText(getCurrentTime());
            try {
                Thread.sleep(40);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            currentTimer.sendEmptyMessage(0);
        }

        ;
    };

    Handler timer = new Handler() {
        public void handleMessage(android.os.Message msg) {
            elapse.setText(getElapse());

            timer.sendEmptyMessage(0);
        }

        ;
    };

    private String getCurrentTime() {
        long now = System.currentTimeMillis();
        Date date = new Date(now);

        return currentTimeFormat.format(date);
    }

    private String getElapse() {
        long now = SystemClock.elapsedRealtime();
        long ell = now - baseTime;

        String sEll;
        if (ell / 1000 / 60 / 60 >= 1)
            sEll = String.format("%d:%02d:%02d.%02d", ell / 1000 / 60 / 60,
                    (ell / 1000 / 60) % 60, (ell / 1000) % 60, (ell % 1000) / 10);
        else
            sEll = String.format("%02d:%02d.%02d", ell / 1000 / 60, (ell / 1000) % 60, (ell % 1000) / 10);
        return sEll;
    }

    private void setTitleBtnListener() {
        title.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

            }
        });
    }

    private void setStartBtnListener() {
        startBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (status == IDLE) {
                    baseTime = SystemClock.elapsedRealtime() - toDoData.display_time;
                    startTime = System.currentTimeMillis();
                    timer.sendEmptyMessage(0);
                    startBtn.setText("중지");
                    status = RUNNING;
                    return;
                }
                if (status == RUNNING) {
                    timer.removeMessages(0);
                    pauseTime = SystemClock.elapsedRealtime();
                    endTime = System.currentTimeMillis();

                    startBtn.setText("시작");
                    resetBtn.setEnabled(true);
                    resetBtn.setVisibility(View.VISIBLE);
                    status = PAUSE;

                    InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                    pageProgress.setEnabled(true);
                    pageProgress.requestFocus();
                    imm.showSoftInput(pageProgress, 0);

                    return;
                }
                if (status == PAUSE) {
                    long now = SystemClock.elapsedRealtime();
                    baseTime += (now - pauseTime);
                    timer.sendEmptyMessage(0);

                    startBtn.setText("중지");
                    resetBtn.setEnabled(false);
                    resetBtn.setVisibility(GONE);
                    status = RUNNING;

                    return;
                }
            }
        });
    }

    private void setResetBtnListener() {
        resetBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (status == PAUSE) {
                    timer.removeMessages(0);
                    long now = SystemClock.elapsedRealtime();
                    baseTime += now - pauseTime;
                    toDoData.resetDisplayTime((int) (now - baseTime), 0);
                    elapse.setText("00:00.00");
                    IntentId intentId = new IntentId(getIntent());
                    CustomIO customIO = new CustomIO(getFilesDir(), intentId.getIntentId());
                    customIO.Save(toDoData.streamData());
                    resetBtn.setEnabled(false);
                    resetBtn.setVisibility(GONE);
                    status = IDLE;
                    return;
                }
            }
        });
    }

    private void setPageProgressListener() {
        pageProgress.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_ENTER) {
                    long now = SystemClock.elapsedRealtime();
                    baseTime += now - pauseTime;
                    int page = Integer.parseInt(pageProgress.getText().toString());
                    pageProgress.setText("");
                    IntentId intentId = new IntentId(getIntent());
                    CustomIO customIO = new CustomIO(getFilesDir(), intentId.getIntentId());
                    if (toDoData.isStep) {
                        toDoData.addStepRecord((int) (now - baseTime), now - baseTime, page
                                , startTime, endTime);
                        try {
                            toDoData.updateCategory(getFilesDir(), intentId.getIntentId(), page
                                    , now - baseTime, toDoData.pageName);
                        } catch (IOException | ParseException e) {
                            e.printStackTrace();
                        }
                    } else {
                        toDoData.addRecord((int) (now - baseTime), now - baseTime, page
                                , startTime, endTime);
                        try {
                            toDoData.updateCategory(getFilesDir(), intentId.getIntentId(), page - toDoData.page_progress
                                    , now - baseTime, toDoData.pageName);
                        } catch (IOException | ParseException e) {
                            e.printStackTrace();
                        }
                    }

                    customIO.Save(toDoData.streamData());

                    pageProgress.setEnabled(false);
                }
                return true;
            }
        });
    }
}
