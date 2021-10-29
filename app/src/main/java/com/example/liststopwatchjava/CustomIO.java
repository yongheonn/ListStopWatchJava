package com.example.liststopwatchjava;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class CustomIO {
    private File mDir;
    private File mFile;

    public enum IOType {CATEGORY, DOLIST}

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

    public void Save(CategoryData _data) {
        try {
            BufferedWriter writer = new BufferedWriter(
                    new FileWriter(mFile, false));

            writer.append(_data.StreamData() + "\n");
            writer.flush();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void Save(DoListData _data) {
        try {
            BufferedWriter writer = new BufferedWriter(
                    new FileWriter(mFile, false));

            writer.append(_data.StreamgData() + "\n");
            writer.flush();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void Load(CategoryData _data) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(mFile));

        String name = reader.readLine();
        if(name != null) {
            _data.Reset();
            _data.name = name;
            int categorySize = Integer.parseInt(reader.readLine());
            int doListSize = Integer.parseInt(reader.readLine());

            for (int i = 1; i < categorySize + doListSize + 1; i++) {
                int type = Integer.parseInt(reader.readLine());
                _data.sequence.add(type);
                CustomIO io = new CustomIO(new File(mFile + "_" + i));
                if (type > 0) {
                    CategoryData itemData = new CategoryData();
                    io.Load(itemData);
                    _data.categoryData.add(itemData);
                }
                else {
                    DoListData itemData = new DoListData();
                    io.Load(itemData);
                    _data.doListData.add(itemData);
                }
            }
        }
        reader.close();
    }

    public void Load(DoListData _data) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(mFile));

        _data.name = reader.readLine();
        _data.time = Integer.parseInt(reader.readLine());
        _data.page_progress = Integer.parseInt(reader.readLine());
        _data.page_total = Integer.parseInt(reader.readLine());
        _data.difficulty = Integer.parseInt(reader.readLine());
        _data.pagePerTime_target = Double.parseDouble(reader.readLine());
        _data.unitTime = reader.readLine();
        _data.pageName = reader.readLine();

        reader.close();
    }
}
