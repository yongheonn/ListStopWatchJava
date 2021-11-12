package com.example.liststopwatchjava;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class ToDoData implements Serializable {
    public String name;
    public int time;
    public long display_time;
    public int page_progress;
    public int page_total;
    public int difficulty;
    public double pagePerTime_target;
    public String unitTime;
    public String pageName;
    public boolean isStep;
    public ArrayList<Date> startDate;
    public ArrayList<Date> endDate;

    ToDoData()
    {
        startDate = new ArrayList<Date>();
        endDate = new ArrayList<Date>();
        name = null;
        time = 0;
        display_time = 0;
        page_progress = 0;
        isStep = true;
    }

    ToDoData(String _name)
    {
        startDate = new ArrayList<Date>();
        endDate = new ArrayList<Date>();
        name = _name;
        time = 0;
        display_time = 0;
        page_progress = 0;
        isStep = true;
    }

    ToDoData(String _name, int _page_total, double _pagePerTime_target,
             String _unitTime, String _pageName, boolean _isStep)
    {
        startDate = new ArrayList<Date>();
        endDate = new ArrayList<Date>();
        name = _name;
        time = 0;
        display_time = 0;
        page_progress = 0;
        page_total = _page_total;
        difficulty = 50;
        pagePerTime_target = _pagePerTime_target;
        unitTime = _unitTime;
        pageName = _pageName;
        isStep = _isStep;
    }

    public String getStringTime() {
        if(display_time / 1000 / 60 / 60 >= 1)
            return String.format("%d:%02d:%02d.%02d", display_time / 1000 / 60 / 60,
                    (display_time / 1000 / 60) % 60, (display_time / 1000) % 60, (display_time % 1000) / 10);
        else
            return String.format("%02d:%02d.%02d", display_time / 1000 / 60
                    , (display_time / 1000) % 60, (display_time % 1000) / 10);
    }

    public void resetDisplayTime(int _time, long _display_time) {
        time += time;
        display_time = _display_time;
    }

    public void addRecord(int _time, long _display_time, int _page_progress, long _startTime, long _endTime) {
        time += _time;
        display_time = _display_time;
        page_progress = _page_progress;
        startDate.add(new Date(_startTime));
        endDate.add(new Date(_endTime));
    }

    public void addStepRecord(int _time, long _display_time, int _page_progress, long _startTime, long _endTime) {
        time += _time;
        display_time = _display_time;
        page_progress += _page_progress;
        startDate.add(new Date(_startTime));
        endDate.add(new Date(_endTime));
    }

    public boolean isComplete() {
        if(isStep)
            return false;
        if(page_total > page_progress)
            return false;
        return true;
    }

    public void reset()
    {
        name = null;
        time = 0;
        display_time = 0;
        page_progress = 0;
        page_total = 0;
        difficulty = 0;
        pagePerTime_target = 0;
        unitTime = null;
        pageName = null;
        isStep = true;
        startDate.clear();
        endDate.clear();
    }

    public String streamData()
    {
        SimpleDateFormat dataFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String stream =  name + "\n" + time  + "\n" + display_time + "\n" +  page_progress
                + "\n" + page_total + "\n" + difficulty + "\n" + pagePerTime_target
                + "\n" + unitTime + "\n" + pageName + "\n" + isStep;
        for(int i = 0; i < startDate.size(); i++) {
            stream += "\n" + dataFormat.format(startDate.get(i));
            stream += "\n" + dataFormat.format(endDate.get(i));
        }
        return stream;
    }

    public void updateCategory(File _filesDir, ArrayList<Integer> _id, int _page, double _time,
                               String _pageName) throws IOException, ParseException {
        ArrayList<Integer> id = new ArrayList<Integer>();
        id.addAll(_id);
        while(id.size() > 1) {
            id.remove(id.size() - 1);
            CustomIO customIO = new CustomIO(_filesDir, id);
            CategoryData categoryData = new CategoryData();
            customIO.load(categoryData);
            categoryData.updateTarget(_page, _time, _pageName);
            customIO.Save(categoryData.streamData());
        }
    }
}
