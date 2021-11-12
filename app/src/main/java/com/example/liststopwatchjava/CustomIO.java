package com.example.liststopwatchjava;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class CustomIO {
    private File mDir;
    private File mFile;
    public enum IOType {CATEGORY, TODO}

    public CustomIO(File _filesDir, ArrayList<Integer> _id) {
        mDir = new File(_filesDir + "/resources/");
        if (!mDir.exists())
            mDir.mkdir();

        String file = "";
        for (int i : _id)
            file += "_" + i;

        mFile = new File(_filesDir + "/resources/" + file);

        if (!mFile.exists()) {
            try {
                mFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public CustomIO(File _file) {
        mFile = _file;
    }

    public void Save(String streamData) {
        try {
            BufferedWriter writer = new BufferedWriter(
                    new FileWriter(mFile, false));

            writer.append(streamData + "\n");
            writer.flush();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void load(CategoryData _data) throws IOException, ParseException {
        BufferedReader reader = new BufferedReader(new FileReader(mFile));
        String name = reader.readLine();
        if (name != null) {

            ArrayList<DailyTarget> dailyTarget = new ArrayList<DailyTarget>();
            int dailySize = Integer.parseInt(reader.readLine());
            int categorySize = Integer.parseInt(reader.readLine());
            int toDoSize = Integer.parseInt(reader.readLine());
            int sequenceSize = categorySize + toDoSize + 1;

            loadDaily(reader, dailyTarget, dailySize);

            ArrayList<CategoryData> categoryData = new ArrayList<CategoryData>();
            ArrayList<ToDoData> toDoData = new ArrayList<ToDoData>();
            Sequence sequence = new Sequence();

            loadSequence(reader, sequence, sequenceSize);
            loadItem(sequence, categoryData, toDoData);

            _data.loadData(name, dailyTarget, categoryData, toDoData, sequence);
        } else {
            _data.name = "메인 카테고리";
        }
        reader.close();
    }

    public void load(ToDoData _data) throws IOException, ParseException {
        BufferedReader reader = new BufferedReader(new FileReader(mFile));

        _data.name = reader.readLine();
        _data.time = Integer.parseInt(reader.readLine());
        _data.display_time = Integer.parseInt(reader.readLine());
        _data.page_progress = Integer.parseInt(reader.readLine());
        _data.page_total = Integer.parseInt(reader.readLine());
        _data.difficulty = Integer.parseInt(reader.readLine());
        _data.pagePerTime_target = Double.parseDouble(reader.readLine());
        _data.unitTime = reader.readLine();
        _data.pageName = reader.readLine();
        _data.isStep = Boolean.parseBoolean(reader.readLine());

        SimpleDateFormat dataFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String s1, s2;
        while ((s1 = reader.readLine()) != null && (s2 = reader.readLine()) != null) {
            _data.startDate.add(dataFormat.parse(s1));
            _data.endDate.add(dataFormat.parse(s2));
        }

        reader.close();
    }

    private void loadDaily(BufferedReader reader, ArrayList<DailyTarget> _dailyTarget
    , int _dailySize) throws IOException, ParseException {

        for (int i = 0; i < _dailySize; i++) {
            int pageNameSize = Integer.parseInt(reader.readLine());
            ArrayList<Integer> page = new ArrayList<Integer>();
            ArrayList<Integer> pageTarget = new ArrayList<Integer>();
            ArrayList<Double> time = new ArrayList<Double>();
            ArrayList<String> unitTime = new ArrayList<String>();
            ArrayList<String> pageName = new ArrayList<String>();

            Date date = new Date();
            SimpleDateFormat format = new SimpleDateFormat("yyyy MM dd EE");

            for(int j = 0; j < pageNameSize; j++)
                page.add(Integer.parseInt(reader.readLine()));
            for(int j = 0; j < pageNameSize; j++)
                pageTarget.add(Integer.parseInt(reader.readLine()));
            for(int j = 0; j < pageNameSize; j++)
                time.add(Double.parseDouble(reader.readLine()));
            for(int j = 0; j < pageNameSize; j++)
                unitTime.add(reader.readLine());
            for(int j = 0; j < pageNameSize; j++)
                pageName.add(reader.readLine());
            String dateString = reader.readLine();
            date = format.parse(dateString);
            _dailyTarget.add(new DailyTarget(page, pageTarget, time, unitTime, pageName, date));
        }
    }

    private void loadSequence(BufferedReader reader, Sequence _sequence
    , int _sequenceSize) throws IOException {
        for (int i = 1; i < _sequenceSize; i++) {
            int type = Integer.parseInt(reader.readLine());
            _sequence.add(type);
        }
    }

    private void loadItem(Sequence _sequence, ArrayList<CategoryData> _categoryData
    , ArrayList<ToDoData> _toDoData) throws IOException, ParseException {
        for (int i = 1; i < _sequence.size(); i++) {
            CustomIO io = new CustomIO(new File(mFile + "_" + i));
            if (_sequence.isCategory(i))
                _categoryData.add(loadCategory(io));
            else if (_sequence.isToDo(i))
                _toDoData.add(loadToDo(io));
        }
    }

    private CategoryData loadCategory(CustomIO _io) throws IOException, ParseException {
        CategoryData itemData = new CategoryData();
        _io.load(itemData);
        return  itemData;
    }

    private ToDoData loadToDo(CustomIO _io) throws IOException, ParseException {
        ToDoData itemData = new ToDoData();
        _io.load(itemData);
        return itemData;
    }
}
