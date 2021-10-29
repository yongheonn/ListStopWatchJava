package com.example.liststopwatchjava;

import java.util.ArrayList;

public class CategoryData {
    public String name;
    public ArrayList<CategoryData> categoryData;
    public ArrayList<DoListData> doListData;
    public ArrayList<Integer> sequence;

    CategoryData() {
        name = null;
        categoryData = new ArrayList<CategoryData>();
        doListData = new ArrayList<DoListData>();
        sequence = new ArrayList<Integer>();
        sequence.add(0);
    }

    CategoryData(String _name) {
        name = _name;
        categoryData = new ArrayList<CategoryData>();
        doListData = new ArrayList<DoListData>();
        sequence = new ArrayList<Integer>();
        sequence.add(0);
    }

    public void Reset()
    {
        name = null;
        categoryData.clear();
        doListData.clear();
        sequence.clear();
        sequence.add(0);
    }

    public String StreamData() {
        String stream = name + "\n" + categoryData.size()
                + "\n" + doListData.size();
        for (int i = 1; i < sequence.size(); i++) {
            stream += "\n" + sequence.get(i);
        }
        return stream;
    }
}
