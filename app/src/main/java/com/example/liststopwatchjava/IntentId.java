package com.example.liststopwatchjava;

import android.content.Intent;

import java.util.ArrayList;

public class IntentId {
    private Intent intent;

    IntentId(Intent _intent) {
        intent = _intent;
    }

    public ArrayList<Integer> getIntentId() {
        return getIntentId(receiveIntentId());
    }

    public ArrayList<Integer> getNextIntentId(int position) {
        ArrayList<Integer> nextId = new ArrayList<Integer>();
        nextId.addAll(getIntentId());
        nextId.add(position);
        return nextId;
    }

    private ArrayList<Integer> getIntentId(ArrayList<Integer> _receivedIntentId) {
        if(_receivedIntentId == null) {
            _receivedIntentId = new ArrayList<Integer>();
            _receivedIntentId.add(0);
        }

        return  _receivedIntentId;
    }

    private ArrayList<Integer> receiveIntentId() {
        return (ArrayList<Integer>) intent.getSerializableExtra("INTENT_ID");
    }
}
