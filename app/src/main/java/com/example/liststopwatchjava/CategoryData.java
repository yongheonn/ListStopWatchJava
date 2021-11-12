package com.example.liststopwatchjava;

import android.os.SystemClock;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.Locale;

public class CategoryData {
    public String name;
    public ArrayList<DailyTarget> dailyTarget;
    public ArrayList<CategoryData> categoryData;
    public ArrayList<ToDoData> toDoData;
    public Sequence sequence;
    //CATEGORY : 양의 정수, 정수값이 categoryData 인덱스
    //TODO : 음의 정수, 정수값의 절대값이 toDoData 인덱스

    CategoryData() {
        name = null;
        dailyTarget = new ArrayList<DailyTarget>();
        categoryData = new ArrayList<CategoryData>();
        toDoData = new ArrayList<ToDoData>();
        sequence = new Sequence();
    }

    CategoryData(String _name) {
        name = _name;
        dailyTarget = new ArrayList<DailyTarget>();
        categoryData = new ArrayList<CategoryData>();
        toDoData = new ArrayList<ToDoData>();
        sequence = new Sequence();
    }

    CategoryData(String _name, DailyTarget _dailyTarget) {
        name = _name;
        dailyTarget = new ArrayList<DailyTarget>();
        dailyTarget.add(_dailyTarget);
        categoryData = new ArrayList<CategoryData>();
        toDoData = new ArrayList<ToDoData>();
        sequence = new Sequence();
    }

    public void reset() {
        name = null;
        dailyTarget.clear();
        categoryData.clear();
        toDoData.clear();
        sequence.reset();
    }

    public void clear() {
        name = null;
        dailyTarget.clear();
        categoryData.clear();
        toDoData.clear();
        sequence.clear();
    }

    public String streamData() {

        String stream = name + "\n" + dailyTarget.size() + "\n" + categoryData.size()
                + "\n" + toDoData.size();
        for (int i = 0; i < dailyTarget.size(); i++) {
            stream += dailyTarget.get(i).streamData();
        }
        stream += sequence.streamData();
        return stream;
    }

    public void updateTarget(int _page, double _time, String _pageName) {
        dailyTarget.add(dailyTarget.get(dailyTarget.size() - 1).updateTarget(_page, _time, _pageName));
    }

    public void loadData(String _name, ArrayList<DailyTarget> _dailyTarget,
                         ArrayList<CategoryData> _categoryData, ArrayList<ToDoData> _toDoData,
                         Sequence _sequence) {
        clear();
        name = _name;
        dailyTarget.addAll(_dailyTarget);
        categoryData.addAll(_categoryData);
        toDoData.addAll(_toDoData);
        sequence.addAll(_sequence);
    }

    public void updateData(CategoryData data) {
    }
}

class Sequence {
    private ArrayList<Integer> index;

    Sequence() {
        index = new ArrayList<Integer>();
        index.add(0);
    }

    public void reset() {
        index.clear();
        index.add(0);
    }

    public void clear() {
        index.clear();
    }

    public boolean isCategory(int _index) {
        if (index.get(_index) > 0)
            return true;
        return false;
    }

    public boolean isToDo(int _index) {
        if (index.get(_index) < 0)
            return true;
        return false;
    }

    public String streamData() {
        String stream = "";
        for (int i  = 1; i < index.size(); i++) {
            stream += "\n" + index.get(i);
        }
        return stream;
    }

    public int size() {
        return index.size();
    }

    public void add(int data) {
        index.add(data);
    }

    public void addAll(Sequence _sequence) {
        index.addAll(_sequence.index);
    }

    public int get(int _index) {
        return Math.abs(index.get(_index)) - 1;
    }

    public void addCategory() {
        index.add(Collections.max(index) + 1);
    }

    public void addToDo() {
        index.add(Collections.min(index) - 1);
    }
}

class DailyTarget {
    public ArrayList<Integer> page;
    public ArrayList<Integer> pageTarget;
    public ArrayList<Double> time;
    public ArrayList<String> unitTime;
    public ArrayList<String> pageName;
    public Date date;

    DailyTarget() {
        page = new ArrayList<Integer>();
        pageTarget = new ArrayList<Integer>();
        time = new ArrayList<Double>();
        unitTime = new ArrayList<String>();
        pageName = new ArrayList<String>();
        date = new Date();
    }

    DailyTarget(ArrayList<Integer> _pageTarget, ArrayList<String> _pageName
            , ArrayList<String> _unitTime) {
        page = new ArrayList<Integer>();
        pageTarget = new ArrayList<Integer>();
        time = new ArrayList<Double>();
        unitTime = new ArrayList<String>();
        pageName = new ArrayList<String>();
        date = new Date(System.currentTimeMillis());

        pageTarget.addAll(_pageTarget);
        pageName.addAll(_pageName);
        unitTime.addAll(_unitTime);
        for(int i = 0; i < _pageName.size(); i++) {
            page.add(0);
            time.add(0.0);
        }
    }

    DailyTarget(ArrayList<Integer> _page, ArrayList<Integer> _pageTarget
            , ArrayList<Double> _time, ArrayList<String> _unitTime
            , ArrayList<String> _pageName, Date _date) {
        page = new ArrayList<Integer>();
        pageTarget = new ArrayList<Integer>();
        time = new ArrayList<Double>();
        unitTime = new ArrayList<String>();
        pageName = new ArrayList<String>();
        date = new Date();

        page.addAll(_page);
        pageTarget.addAll(_pageTarget);
        time.addAll(_time);
        unitTime.addAll(_unitTime);
        pageName.addAll(_pageName);
        date = _date;
    }

    public String streamData() {
        String dateFormat = new SimpleDateFormat("yyyy MM dd EE")
                .format(date);
        String stream = "\n" + pageName.size();
        for (int i : page)
            stream += "\n" + i;
        for (int i : pageTarget)
            stream += "\n" + i;
        for (double d : time)
            stream += "\n" + d;
        for (String s : unitTime)
            stream += "\n" + s;
        for (String s : pageName)
            stream += "\n" + s;
        stream += "\n" + dateFormat;
        return stream;
    }

    public void loadData(ArrayList<Integer> _page, ArrayList<Integer> _pageTarget
            , ArrayList<Double> _time, ArrayList<String> _unitTime
            , ArrayList<String> _pageName, Date _date) {
        page.addAll(_page);
        pageTarget.addAll(_pageTarget);
        time.addAll(_time);
        unitTime.addAll(_unitTime);
        pageName.addAll(_pageName);
        date = _date;
    }

    public DailyTarget updateTarget(int _page, double _time, String _pageName) {
        int index = pageName.indexOf(_pageName);
        Calendar cal = Calendar.getInstance();
        double now_time = cal.get(cal.HOUR_OF_DAY) * 3600 + cal.get(cal.MINUTE) * 60
                + cal.get(cal.SECOND);
        double el_time = _time / 1000 / 60 / 60 + (_time / 1000 / 60) % 60 + (_time / 1000) % 60;
        if (el_time > now_time) {
            ArrayList<Integer> afterPage = new ArrayList<Integer>();
            ArrayList<Double> afterTime = new ArrayList<Double>();
            time.set(index, time.get(index) + el_time - now_time);
            int beforePage = (int) (_page * (el_time - now_time) / _time);
            page.set(index, page.get(index) + beforePage);
            afterPage.set(index, _page - beforePage);
            afterTime.set(index, now_time);

            return new DailyTarget(afterPage, pageTarget, afterTime
                    , unitTime, pageName, new Date(System.currentTimeMillis()));
        } else {
            time.set(index, time.get(index) + el_time);
            page.set(index, page.get(index) + _page);
            return null;
        }
    }

    public boolean isComplete() {
        for (int i = 0; i < page.size(); i++)
            if (page.get(i) < pageTarget.get(i))
                return false;
        return true;
    }
}
