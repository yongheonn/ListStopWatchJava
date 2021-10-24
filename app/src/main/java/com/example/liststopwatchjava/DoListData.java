package com.example.liststopwatchjava;

import java.util.ArrayList;

public class DoListData {
    public CustomIO.IOType ioType;
    public String name;
    public int time;
    public int page_progress;
    public int page_total;
    public double pagePerTime_target;
    public String unitTime;
    public String pageName;

    DoListData()
    {
        ioType = null;
        name = null;
    }

    DoListData(CustomIO.IOType _ioType, String _name)
    {
        ioType = _ioType;
        name = _name;
    }

    DoListData(CustomIO.IOType _ioType, String _name, int _page_total, double _pagePerTime_target,
               String _unitTime, String _pageName)
    {
        ioType = _ioType;
        name = _name;
        page_total = _page_total;
        pagePerTime_target = _pagePerTime_target;
        unitTime = _unitTime;
        pageName = _pageName;
    }

    DoListData(CustomIO.IOType _ioType, String _name, int _time, int _page_progress,
               int _page_total, double _pagePerTime_target,
               String _unitTime, String _pageName)
    {
        ioType = _ioType;
        name = _name;
        time = _time;
        page_progress = _page_progress;
        page_total = _page_total;
        pagePerTime_target = _pagePerTime_target;
        unitTime = _unitTime;
        pageName = _pageName;
    }

    public String StringData()
    {
        if(ioType == CustomIO.IOType.CATEGORY)
            return "CATEGORY\n" + name + "\n" + time  + "\n" +  page_progress
                    + "\n" + page_total + "\n" + pagePerTime_target + "\n" + unitTime + "\n" + pageName;
        else
            return "DOLIST\n" + name + "\n" + time  + "\n" +  page_progress
                    + "\n" + page_total + "\n" + pagePerTime_target + "\n" + unitTime + "\n" + pageName;
    }
}
