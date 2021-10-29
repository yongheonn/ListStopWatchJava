package com.example.liststopwatchjava;

import java.util.ArrayList;

public class DoListData {
    public String name;
    public int time;
    public int page_progress;
    public int page_total;
    public int difficulty;
    public double pagePerTime_target;
    public String unitTime;
    public String pageName;

    DoListData()
    {
        name = null;
    }

    DoListData(String _name)
    {
        name = _name;
    }

    DoListData(String _name, int _page_total, double _pagePerTime_target,
               String _unitTime, String _pageName)
    {
        name = _name;
        time = 0;
        page_progress = 0;
        page_total = _page_total;
        difficulty = 50;
        pagePerTime_target = _pagePerTime_target;
        unitTime = _unitTime;
        pageName = _pageName;
    }

    DoListData(String _name, int _time, int _page_progress,
               int _difficulty, int _page_total, double _pagePerTime_target,
               String _unitTime, String _pageName)
    {
        name = _name;
        time = _time;
        page_progress = _page_progress;
        page_total = _page_total;
        difficulty = _difficulty;
        pagePerTime_target = _pagePerTime_target;
        unitTime = _unitTime;
        pageName = _pageName;
    }

    public void Reset()
    {
        name = null;
        time = 0;
        page_progress = 0;
        page_total = 0;
        difficulty = 0;
        pagePerTime_target = 0;
        unitTime = null;
        pageName = null;
    }

    public String StreamgData()
    {
        return name + "\n" + time  + "\n" +  page_progress + "\n" + page_total
                + "\n" + difficulty + "\n" + pagePerTime_target + "\n" + unitTime + "\n" + pageName;
    }
}
