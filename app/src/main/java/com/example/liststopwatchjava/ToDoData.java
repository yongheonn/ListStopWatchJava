package com.example.liststopwatchjava;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.TimeZone;

public class ToDoData implements Serializable {
    private String name;
    private long time;
    private long display_time;
    private int page_progress;
    private int page_total;
    private int difficulty;
    private double pagePerTime_target;
    private String unitTime;
    private String pageName;
    private boolean isStep;

    private RecordDate recordDate;
    private DailyData dailyData;
    private File mDir;
    private File mFile;
    private ArrayList<Integer> id;

    class RecordDate {
        private ArrayList<LocalDateTime> startDate;
        private ArrayList<LocalDateTime> endDate;
        private ArrayList<Long> time;
        private ArrayList<Integer> page;
        private int size;

        RecordDate() {
            startDate = new ArrayList<LocalDateTime>();
            endDate = new ArrayList<LocalDateTime>();
            time = new ArrayList<Long>();
            page = new ArrayList<Integer>();
            size = 0;
        }

        public void addRecord(long _startDate, long _endDate, long _time, int _page) {
            startDate.add(LocalDateTime.ofInstant(Instant.ofEpochMilli(_startDate)
                    , TimeZone.getDefault().toZoneId()));
            endDate.add(LocalDateTime.ofInstant(Instant.ofEpochMilli(_endDate)
                    , TimeZone.getDefault().toZoneId()));
            time.add(_time);
            page.add(_page);
            size++;
        }

        public int getStartTime() {
            return rearStartDate().getHour() * 3600 + rearStartDate().getMinute() * 60
                    + rearStartDate().getSecond();
        }

        public int getEndTime() {
            return rearEndDate().getHour() * 3600 + rearEndDate().getMinute() * 60
                    + rearEndDate().getSecond();
        }

        public String streamData() {
            String stream = "\n" + size;
            for (LocalDateTime l : startDate)
                stream += "\n" + l.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            for (LocalDateTime l : endDate)
                stream += "\n" + l.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            for (long l : time)
                stream += "\n" + l;
            for (int i : page)
                stream += "\n" + i;

            return stream;
        }

        public void load(BufferedReader _reader) {
            try {
                size = Integer.parseInt(_reader.readLine());

                for (int i = 0; i < size; i++)
                    startDate.add(LocalDateTime.parse(_reader.readLine()
                            , DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
                for (int i = 0; i < size; i++)
                    endDate.add(LocalDateTime.parse(_reader.readLine()
                            , DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
                for (int i = 0; i < size; i++)
                    time.add(Long.parseLong(_reader.readLine()));
                for (int i = 0; i < size; i++)
                    page.add(Integer.parseInt(_reader.readLine()));

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        public boolean isDayPassed() {
            if (size > 0)
                return !((startDate.get(rearIndex()).getDayOfYear() == endDate.get(rearIndex()).getDayOfYear())
                        && (startDate.get(rearIndex()).getYear() == endDate.get(rearIndex()).getYear()));
            return false;
        }

        public void clear() {
            startDate.clear();
            endDate.clear();
            time.clear();
            page.clear();
            size = 0;
        }

        private int rearIndex() {
            return size - 1;
        }

        private LocalDateTime rearStartDate() {
            return startDate.get(rearIndex());
        }

        private LocalDateTime rearEndDate() {
            return endDate.get(rearIndex());
        }

        private long rearTime() {
            return time.get(rearIndex());
        }

        private int rearPage() {
            return page.get(rearIndex());
        }
    }

    class DailyData {
        private ArrayList<Long> time;
        private ArrayList<Integer> page;
        private ArrayList<LocalDate> date;
        private int size;

        DailyData() {
            size = 0;
            time = new ArrayList<Long>();
            page = new ArrayList<Integer>();
            date = new ArrayList<LocalDate>();
            time.add((long) 0);
            page.add(0);
            date.add(LocalDate.now());
        }

        public int getRearPage() {
            if(size > 0)
                return page.get(size - 1);
            return 0;
        }

        public int getRearPage(int _index) {
            return page.get(size - 1 - _index);
        }

        public long getRearTime() {
            return time.get(size - 1);
        }

        public long getRearTime(int _index) {
            return time.get(size - 1 - _index);
        }

        public void add(long _time, int _page) {
            time.add(_time);
            page.add(_page);
            date.add(LocalDate.now());
            size++;
        }

        public void edit(long _time, int _page) {
            if (isDayDifferent()) {
                add(_time, _page);
            } else {
                time.set(rearIndex(), time.get(rearIndex()) + _time);
                page.set(rearIndex(), _page);
            }
        }

        public void editStep(long _time, int _page) {
            if (isDayDifferent()) {
                add(_time, _page);
            } else {
                time.set(rearIndex(), time.get(rearIndex()) + _time);
                page.set(rearIndex(), page.get(rearIndex()) + _page);
            }
        }

        public void clear() {
            time.clear();
            page.clear();
            date.clear();
            size = 0;
        }

        public String streamData() {
            String stream = "\n" + size;

            for (long l : time)
                stream += "\n" + l;
            for (int i : page)
                stream += "\n" + i;
            for (LocalDate l : date)
                stream += "\n" + l.format(DateTimeFormatter.BASIC_ISO_DATE);

            return stream;
        }

        public void load(BufferedReader _reader) {
            try {
                size = Integer.parseInt(_reader.readLine());

                for (int i = 0; i < size; i++)
                    time.add(Long.parseLong(_reader.readLine()));
                for (int i = 0; i < size; i++)
                    page.add(Integer.parseInt(_reader.readLine()));
                for (int i = 0; i < size; i++)
                    date.add(LocalDate.parse(_reader.readLine(), DateTimeFormatter.BASIC_ISO_DATE));

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        private boolean isDayDifferent() {
            if (size > 0)
                return !((LocalDate.now().getDayOfYear() == date.get(rearIndex()).getDayOfYear())
                        && LocalDate.now().getYear() == date.get(rearIndex()).getYear());
            return true;
        }

        private int rearIndex() {
            return size - 1;
        }
    }

    ToDoData(File _dir, File _file) {
        mDir = _dir;
        mFile = _file;

        recordDate = new RecordDate();
        dailyData = new DailyData();
        id = new ArrayList<Integer>();
        name = null;
        time = 0;
        display_time = 0;
        page_progress = 0;
        isStep = true;
    }

    ToDoData(File _filesDir, ArrayList<Integer> _id) {
        recordDate = new RecordDate();
        dailyData = new DailyData();
        id = new ArrayList<Integer>();
        name = null;
        time = 0;
        display_time = 0;
        page_progress = 0;
        isStep = true;

        convertIdToFile(_filesDir, _id);
    }

    ToDoData(String _name, File _filesDir, ArrayList<Integer> _id) {
        name = _name;
        time = 0;
        display_time = 0;
        page_progress = 0;
        isStep = true;
        recordDate = new RecordDate();
        dailyData = new DailyData();
        id = new ArrayList<Integer>();

        convertIdToFile(_filesDir, _id);
    }

    ToDoData(String _name, int _page_total, double _pagePerTime_target,
             String _unitTime, String _pageName, boolean _isStep
            , File _filesDir, ArrayList<Integer> _id) {
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
        recordDate = new RecordDate();
        dailyData = new DailyData();
        id = new ArrayList<Integer>();

        convertIdToFile(_filesDir, _id);
    }

    public String getStringTime() {
        if (display_time / 1000 / 60 / 60 >= 1)
            return String.format("%d:%02d:%02d.%02d", display_time / 1000 / 60 / 60,
                    (display_time / 1000 / 60) % 60, (display_time / 1000) % 60, (display_time % 1000) / 10);
        else
            return String.format("%02d:%02d.%02d", display_time / 1000 / 60
                    , (display_time / 1000) % 60, (display_time % 1000) / 10);
    }

    public String getName() {
        return name;
    }

    public String getPageName() {
        return pageName;
    }

    public boolean isStep() {
        return isStep;
    }

    public long getDisplayTime() {
        return display_time;
    }

    public void resetDisplayTime(int _time) {
        time = _time;
        display_time = 0;
    }

    public String getTotalInfo() {
         return "총합정보\n( " + page_progress + " / " + page_total + " )" + pageName + "\n"
                + getConvertedTime() + unitTime;
    }

    public void addRecord(long _display_time
            , int _page_progress, long _startTime, long _endTime) {
        time += _display_time;
        recordDate.addRecord(_startTime, _endTime, _display_time, _page_progress);
        if (isStep == false) {
            page_progress = _page_progress;
            refreshDaily(_display_time, _page_progress);
        } else {
            page_progress += _page_progress;
            refreshDailyStep(_display_time, _page_progress);
        }
    }

    public boolean isComplete() {
        if (isStep)
            return false;
        if (page_total > page_progress)
            return false;
        return true;
    }

    public void clear() {
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
        recordDate.clear();
        dailyData.clear();
    }

    public void rename(int _index) {
        id.set(id.size() - 1, _index);
        String file = "";
        for (int i : id)
            file += "_" + i;
        if (mFile.exists()) {
            mFile.renameTo(new File(mDir + "/resources/" + file));
            mFile = new File(mDir + "/resources/" + file);
        }
    }

    public void rename(int _pos, int _index) {
        id.set(_pos, _index);
        String file = "";
        for (int i : id)
            file += "_" + i;
        if (mFile.exists()) {
            mFile.renameTo(new File(mDir + "/resources/" + file));
            mFile = new File(mDir + "/resources/" + file);
        }
    }

    public void delete() {
        ArrayList<Integer> tempId = new ArrayList<Integer>();
        tempId.addAll(id);
        while (tempId.size() > 2) {
            tempId.remove(tempId.size() - 1);
            CategoryData categoryData = new CategoryData(mDir, tempId);
            categoryData.load();
            categoryData.exceptToDoDaily(dailyData.time, dailyData.page, dailyData.date, pageName);
            categoryData.save();
        }
        deleteFile();
    }

    public void deleteFile() {
        if(mFile.exists())
            mFile.delete();
    }

    public String streamData() {
        SimpleDateFormat dataFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String stream = name + "\n" + time + "\n" + display_time + "\n" + page_progress
                + "\n" + page_total + "\n" + difficulty + "\n" + pagePerTime_target
                + "\n" + unitTime + "\n" + pageName + "\n" + isStep;
        stream += recordDate.streamData();
        stream += dailyData.streamData();
        return stream;
    }

    public void save() {
        try {
            BufferedWriter writer = new BufferedWriter(
                    new FileWriter(mFile, false));

            writer.append(streamData() + "\n");
            writer.flush();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void load() {
        clear();
        try {
            BufferedReader reader = new BufferedReader(new FileReader(mFile));

            name = reader.readLine();
            time = Long.parseLong(reader.readLine());
            display_time = Long.parseLong(reader.readLine());
            page_progress = Integer.parseInt(reader.readLine());
            page_total = Integer.parseInt(reader.readLine());
            difficulty = Integer.parseInt(reader.readLine());
            pagePerTime_target = Double.parseDouble(reader.readLine());
            unitTime = reader.readLine();
            pageName = reader.readLine();
            isStep = Boolean.parseBoolean(reader.readLine());

            recordDate.load(reader);

            dailyData.load(reader);

            reader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void updateCategory(long _time, int _page) {
        ArrayList<Integer> tempId = new ArrayList<Integer>();
        tempId.addAll(id);
        while (tempId.size() > 2) {
            tempId.remove(tempId.size() - 1);
            CategoryData categoryData = new CategoryData(mDir, tempId);
            categoryData.load();
            categoryData.updateDaily(_page, _time, pageName);
        }
    }

    private void updateCategory(long _beforeTime, int _beforePage, long _afterTime, int _afterPage) {
        ArrayList<Integer> tempId = new ArrayList<Integer>();
        tempId.addAll(id);
        while (tempId.size() > 2) {
            tempId.remove(tempId.size() - 1);
            CategoryData categoryData = new CategoryData(mDir, tempId);
            categoryData.load();
            categoryData.updateDaily(_beforePage, _beforeTime
                    , _afterPage, _afterTime, pageName);
        }
    }

    private void convertIdToFile(File _filesDir, ArrayList<Integer> _id) {
        mDir = new File(_filesDir.toString());
        File directory = new File(_filesDir.toString() + "/resources");
        if (!directory.exists())
            directory.mkdir();

        String file = "";
        for (int i : _id)
            file += "_" + i;

        mFile = new File(directory.toString() + "/" + file);

        if (!mFile.exists()) {
            try {
                mFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        id.addAll(_id);
    }

    private void refreshDaily(long _time, int _page) {
        if (recordDate.isDayPassed()) {
            long dayTime = 24 * 3600;
            long beforeTime = dayTime - recordDate.getStartTime();
            int stepPage = _page - dailyData.getRearPage();
            int beforePage = dailyData.getRearPage() + (int) (stepPage * (beforeTime / _time));
            dailyData.edit(beforeTime, beforePage);
            long afterTime = _time - beforeTime;
            dailyData.add(afterTime, _page);
            save();
            int beforeStepPage = (int) (stepPage * (beforeTime / _time));
            int afterStepPage = stepPage - beforeStepPage;
            updateCategory(beforeTime, beforeStepPage, afterTime, afterStepPage);
        } else {
            int stepPage = _page - dailyData.getRearPage();
            dailyData.edit(_time, _page);
            save();
            updateCategory(_time, stepPage);
        }
    }

    private void refreshDailyStep(long _time, int _page) {
        if (recordDate.isDayPassed()) {
            long dayTime = 24 * 3600;
            long beforeTime = dayTime - recordDate.getStartTime();
            int beforePage = (int) (_page * (beforeTime / _time));
            dailyData.editStep(beforeTime, beforePage);
            long afterTime = _time - beforeTime;
            int afterPage = _page - beforePage;
            dailyData.add(afterTime, afterPage);
            save();
            updateCategory(beforeTime, beforePage, afterTime, afterPage);
        } else {
            dailyData.editStep(_time, _page);
            save();
            updateCategory(_time, _page);
        }
    }

    private String getConvertedTime() {
        double tempTime = time;
        if (unitTime.equals("시간"))
            tempTime /= 1000 * 60 * 60;
        if (unitTime.equals("분"))
            tempTime /= 1000 * 60;
        if (unitTime.equals("초"))
            tempTime /= 1000;

        return String.format("%.1f", tempTime);
    }
}
