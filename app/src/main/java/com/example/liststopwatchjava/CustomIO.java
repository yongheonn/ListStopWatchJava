package com.example.liststopwatchjava;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class CustomIO {
    private File mDir;
    private File mFile;

    public enum IOType {CATEGORY, DOLIST}

    public CustomIO(File filesDir, ArrayList<Integer> id) {

        mDir = new File(filesDir + "/resources/");
        if (!mDir.exists())
            mDir.mkdir();

        String file = "";
        for (int i : id)
            file += "_" + i;

        mFile = new File(filesDir + "/resources/" + file);

        if (!mFile.exists()) {
            try {
                mFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void Save(DoListData data) {
        try {
            BufferedWriter writer = new BufferedWriter(
                    new FileWriter(mFile, true));

            writer.append(data.StringData() + "\n");
            writer.flush();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void Save(ArrayList<DoListData> data, boolean option) {
        if (option == true) {
            try {
                BufferedWriter writer = new BufferedWriter(
                        new FileWriter(mFile, true));
                for (DoListData i : data)
                    writer.append(i.StringData() + "\n");
                writer.flush();
                writer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            try {
                BufferedWriter writer = new BufferedWriter(
                        new FileWriter(mFile, false));
                for (DoListData i : data)
                    writer.append(i.StringData() + "\n");
                writer.flush();
                writer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public ArrayList<DoListData> Load() {
        ArrayList<DoListData> data = new ArrayList<DoListData>();
        try {
            BufferedReader reader = new BufferedReader(new FileReader(mFile));

            while (LoadItemData(reader, data));
            reader.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return data;
    }

    private boolean LoadItemData(BufferedReader reader, ArrayList<DoListData> data)
    {
        String io = "";
        String name = "";
        String time = "";
        String page_progress = "";
        String page_total = "";
        String pagePerTime_target = "";
        String unitTime = "";
        String pageName = "";
        try {
            if((io = reader.readLine()) == null)
                return false;
            if((name = reader.readLine()) == null)
                return false;
            if((time = reader.readLine()) == null)
                return false;
            if((page_progress = reader.readLine()) == null)
                return false;
            if((page_total = reader.readLine()) == null)
                return false;
            if((pagePerTime_target = reader.readLine()) == null)
                return false;
            if((unitTime = reader.readLine()) == null)
                return false;
            if((pageName = reader.readLine()) == null)
                return false;
        } catch (IOException e) {
            e.printStackTrace();
        }

        if(io.equals("CATEGORY"))
            data.add(new DoListData(IOType.CATEGORY, name, Integer.parseInt(time),
                    Integer.parseInt(page_progress), Integer.parseInt(page_total),
                    Double.parseDouble(pagePerTime_target), unitTime, pageName));
        else
            data.add(new DoListData(IOType.DOLIST, name, Integer.parseInt(time),
                    Integer.parseInt(page_progress), Integer.parseInt(page_total),
                    Double.parseDouble(pagePerTime_target), unitTime, pageName));

        return true;
    }
}
