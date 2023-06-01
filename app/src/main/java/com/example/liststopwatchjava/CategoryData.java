package com.example.liststopwatchjava;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;

public class CategoryData {
    private String name;
    private ArrayList<DailyData> dailyData;
    private ArrayList<ItemType> itemList;
    private ArrayList<Integer> id;
    private int categorySize;
    private int toDoSize;
    private File mDir;
    private File mFile;

    CategoryData(File _filesDir, ArrayList<Integer> _id) {
        name = null;
        dailyData = new ArrayList<DailyData>();
        itemList = new ArrayList<ItemType>();
        id = new ArrayList<Integer>();
        categorySize = 0;
        toDoSize = 0;
        dailyData.add(new DailyData());

        convertIdToFile(_filesDir, _id);
    }

    CategoryData(String _name, File _filesDir, ArrayList<Integer> _id) {
        name = _name;
        dailyData = new ArrayList<DailyData>();
        itemList = new ArrayList<ItemType>();
        id = new ArrayList<Integer>();
        categorySize = 0;
        toDoSize = 0;
        dailyData.add(new DailyData());

        convertIdToFile(_filesDir, _id);
    }

    CategoryData(String _name, DailyData _dailyTarget, File _filesDir, ArrayList<Integer> _id) {
        name = _name;
        dailyData = new ArrayList<DailyData>();
        dailyData.add(_dailyTarget);
        itemList = new ArrayList<ItemType>();
        id = new ArrayList<Integer>();
        categorySize = 0;
        toDoSize = 0;
        dailyData.add(new DailyData());

        convertIdToFile(_filesDir, _id);
    }

    public void reset() {
        name = null;
        dailyData.clear();
        itemList.clear();
        categorySize = 0;
        toDoSize = 0;
    }

    public void clear() {
        name = null;
        dailyData.clear();
        dailyData.add(new DailyData());
        itemList.clear();
        categorySize = 0;
        toDoSize = 0;
    }

    public boolean isItemCategory(int _index) {
        return itemList.get(_index) == ItemType.CATEGORY;
    }

    public boolean isItemToDo(int _index) {
        return itemList.get(_index) == ItemType.TODO;
    }

    public DailyData getRearDaily() {
        if (dailyData.size() > 0)
            return dailyData.get(dailyData.size() - 1);
        return new DailyData();
    }

    public String getName() {
        return name;
    }

    public void setName(String _name) {
        name = _name;
    }

    public int itemSize() {
        return itemList.size();
    }

    public ArrayList<Integer> rearDailyPageTarget() {
        if (dailyData.size() > 0)
            return dailyData.get(dailyData.size() - 1).getPageTarget();
        return new ArrayList<Integer>();
    }

    public ArrayList<String> rearDailyPageName() {
        if (dailyData.size() > 0)
            return dailyData.get(dailyData.size() - 1).getPageNameTarget();
        return new ArrayList<String>();
    }

    public ArrayList<String> rearDailyUnitTime() {
        if (dailyData.size() > 0)
            return dailyData.get(dailyData.size() - 1).getUnitTimeTarget();
        return new ArrayList<String>();
    }

    public ArrayList<String> getPageNameList() {
        ArrayList<String> pageNameList = new ArrayList<String>();
        for (int i = 0; i < itemSize(); i++) {
            ArrayList<Integer> itemId = new ArrayList<Integer>();
            itemId.addAll(id);
            itemId.add(i);
            if (itemList.get(i) == ItemType.TODO) {
                ToDoData toDoData = new ToDoData(mDir, itemId);
                toDoData.load();
                if (!pageNameList.contains(toDoData.getPageName()))
                    pageNameList.add(toDoData.getPageName());
            } else {
                CategoryData categoryData = new CategoryData(mDir, itemId);
                categoryData.load();

                for (String s : categoryData.getPageNameList()) {
                    if (!pageNameList.contains(s))
                        pageNameList.add(s);
                }
            }
        }

        return pageNameList;
    }

    public void updateDaily(int _page, long _time, String _pageName) {
        if(dailyData.get(dailyData.size() - 1).getDate().isEqual(LocalDate.now()))
            dailyData.get(dailyData.size() - 1).updateDaily(_page, _time, _pageName);
        else {
            dailyData.add(new DailyData(dailyData.get(dailyData.size() - 1)));
            dailyData.get(dailyData.size() - 1).updateDaily(_page, _time, _pageName);
        }
        save();
    }

    public void updateDaily(int _beforePage, long _beforeTime, int _afterPage
            , long _afterTime, String _pageName) {
        DailyData rearDaily = dailyData.get(dailyData.size() - 1);
        if(dailyData.get(dailyData.size() - 1).getDate().isEqual(LocalDate.now())) {
            rearDaily.updateDaily(_beforePage, _beforeTime, _pageName);
            dailyData.add(new DailyData(rearDaily));
            rearDaily = dailyData.get(dailyData.size() - 1);
            rearDaily.updateDaily(_afterPage, _afterTime, _pageName);
        }
        else {
            dailyData.add(new DailyData(rearDaily));
            rearDaily = dailyData.get(dailyData.size() - 1);
            rearDaily.updateDaily(_beforePage, _beforeTime, _pageName);
            dailyData.add(new DailyData(rearDaily));
            rearDaily = dailyData.get(dailyData.size() - 1);
            rearDaily.updateDaily(_afterPage, _afterTime, _pageName);
        }
        save();
    }

    public void swapItem(int _from, int _to) {
        if(isItemCategory(_from)) {
            CategoryData fromData = loadCategory(_from);
            if(isItemCategory(_to)) {
                CategoryData toData = loadCategory(_to);
                fromData.rename(id.size(), -1);
                toData.rename(id.size(), _from);
                fromData.rename(id.size(), _to);
            }
            else {
                ToDoData toData = loadToDo(_to);
                fromData.rename(id.size(), -1);
                toData.rename(_from);
                fromData.rename(id.size(), _to);
            }
        }
        else {
            ToDoData fromData = loadToDo(_from);
            if(isItemCategory(_to)) {
                CategoryData toData = loadCategory(_to);
                fromData.rename(-1);
                toData.rename(id.size(), _from);
                fromData.rename(_to);
            }
            else {
                ToDoData toData = loadToDo(_to);
                fromData.rename(-1);
                toData.rename(_from);
                fromData.rename(_to);
            }
        }
        Collections.swap(itemList, _from, _to);
        save();
    }

    public void addCategory(String _name) {
        CategoryData categoryData = new CategoryData(_name, mDir, newItemId());
        itemList.add(ItemType.CATEGORY);
        categorySize++;
        categoryData.save();
        save();
    }

    public void addToDo(String _name, int _page_total, double _pagePerTime_target,
                        String _unitTime, String _pageName, boolean _isStep) {
        try {
            ToDoData toDoData = new ToDoData(_name, _page_total, _pagePerTime_target
                    , _unitTime, _pageName, _isStep, mDir, newItemId());
            itemList.add(ItemType.TODO);
            toDoSize++;
            toDoData.save();
            updateAllDaily();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void deleteCategory(int _index) {
        CategoryData categoryData = loadCategory(_index);
        categoryData.delete();
        for (int i = _index + 1; i < itemSize(); i++) {
            if (itemList.get(i) == ItemType.CATEGORY) {
                CategoryData data = loadCategory(i);
                data.rename(id.size(), i - 1);
            } else {
                ToDoData data = loadToDo(i);
                data.rename(id.size(), i - 1);
            }
        }
        itemList.remove(_index);
        categorySize--;
        save();

        load();
        updateAllDaily();
        save();

    }

    public void deleteToDo(int _index) {
        ToDoData toDoData = new ToDoData(mDir, itemId(_index));
        toDoData.load();
        toDoData.delete();
        for (int i = _index + 1; i < itemSize(); i++) {
            if (itemList.get(i) == ItemType.CATEGORY) {
                CategoryData data = new CategoryData(mDir, itemId(i));
                data.load();
                data.rename(id.size(), i - 1);
            } else {
                ToDoData data = new ToDoData(mDir, itemId(i));
                data.load();
                data.rename(id.size(), i - 1);
            }
        }
        itemList.remove(_index);
        toDoSize--;
        save();
        load();
        updateAllDaily();
        save();
    }

    public void updateAllDaily() {
        ArrayList<Integer> tempId = new ArrayList<Integer>();
        tempId.addAll(id);
        dailyData.get(dailyData.size() - 1).updatePageNameList(getPageNameList());
        save();
        while (tempId.size() > 2) {
            tempId.remove(tempId.size() - 1);
            CategoryData categoryData = new CategoryData(mDir, tempId);
            categoryData.load();
            categoryData.dailyData.get(categoryData.dailyData.size() - 1).updatePageNameList(categoryData.getPageNameList());
            categoryData.save();
        }
    }

    public void addDailyTarget(ArrayList<Integer> _pageTarget, ArrayList<String> _pageName
            , ArrayList<String> _unitTime) {
        dailyData.get(dailyData.size() - 1).updateTarget(_pageTarget, _pageName, _unitTime);
        save();
    }

    public CategoryData loadCategory(int _index) {
        CategoryData categoryData = new CategoryData(mDir, itemId(_index));
        categoryData.load();
        return categoryData;
    }

    public ToDoData loadToDo(int _index) {
        ToDoData toDoData = new ToDoData(mDir, itemId(_index));
        toDoData.load();
        return toDoData;
    }

    public void rename(int _pos, int _index) {
        for(int i = 0; i < itemSize(); i++) {
            if(isItemCategory(i)) {
                CategoryData data = loadCategory(i);
                data.rename(_pos, _index);
            }
            else {
                ToDoData data = loadToDo(i);
                data.rename(_pos, _index);
            }
        }
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
            categoryData.exceptToDoDaily(dailyData);
            categoryData.save();
        }
        deleteCategoryFile();
        deleteFile();
    }

    public void deleteCategoryFile() {
        for (int i = 0; i < itemSize(); i++) {
            if (itemList.get(i) == ItemType.CATEGORY) {
                CategoryData categoryData = new CategoryData(mDir, itemId(i));
                categoryData.load();
                categoryData.deleteCategoryFile();
                categoryData.deleteFile();
            } else {
                ToDoData toDoData = new ToDoData(mDir, itemId(i));
                toDoData.load();
                toDoData.deleteFile();
            }
        }
    }

    public void deleteFile() {
        if (mFile.exists())
            mFile.delete();
    }

    public void exceptToDoDaily(ArrayList<DailyData> _dailyData) {
        int day = 0;
        for (DailyData d : dailyData) {
            for (int i = day; i < _dailyData.size(); i++) {
                if (d.isDoDay(_dailyData.get(i).getDate())) {
                    d.exceptDeleteData(_dailyData.get(i).getTime(), _dailyData.get(i).getPage()
                            , _dailyData.get(i).getPageName());
                    day++;
                    break;
                }
            }
        }
    }

    public void exceptToDoDaily(ArrayList<Long> _time, ArrayList<Integer> _page,
                                ArrayList<LocalDate> _date, String _pageName) {
        int day = 0;
        for (DailyData d : dailyData) {
            for (int i = day; i < _date.size(); i++) {
                if (d.isDoDay(_date.get(i))) {
                    d.exceptDeleteData(_time.get(i), _page.get(i), _pageName);
                    day++;
                    break;
                }
            }
        }
    }

    public DailyData dailyContain(LocalDate _date) {
        for(int i = 0; i < dailyData.size(); i++) {
            if(dailyData.get(i).isDoDay(_date))
                return dailyData.get(i);
            else if(dailyData.get(i).isDayAfter(_date)) {
                if(i == 0)
                    return new DailyData(_date);
                else
                    return new DailyData(dailyData.get(i - 1), _date);
            }
        }
        return new DailyData(getRearDaily(), _date);
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
            if (name != null) {

                int dailySize = Integer.parseInt(reader.readLine());
                categorySize = Integer.parseInt(reader.readLine());
                toDoSize = Integer.parseInt(reader.readLine());
                int itemSize = categorySize + toDoSize;

                dailyData.get(0).load(reader);
                for (int i = 1; i < dailySize; i++) {
                    dailyData.add(new DailyData());
                    dailyData.get(i).load(reader);
                }

                for (int i = 0; i < itemSize; i++)
                    itemList.add(ItemType.valueOf(reader.readLine()));

            } else {
                name = "메인 카테고리";
            }
            reader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private ArrayList<Integer> newItemId() {
        ArrayList<Integer> itemId = new ArrayList<Integer>();
        itemId.addAll(id);
        itemId.add(itemSize());
        return itemId;
    }

    private ArrayList<Integer> itemId(int _index) {
        ArrayList<Integer> itemId = new ArrayList<Integer>();
        itemId.addAll(id);
        itemId.add(_index);
        return itemId;
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

    private String streamData() {
        String stream = name + "\n" + dailyData.size() + "\n" + categorySize
                + "\n" + toDoSize;
        for (int i = 0; i < dailyData.size(); i++) {
            stream += dailyData.get(i).streamData();
        }
        for (int i = 0; i < itemSize(); i++) {
            stream += "\n" + itemList.get(i).toString();
        }
        return stream;
    }
}

class DailyData {
    private ArrayList<Integer> page;
    private ArrayList<Long> time;
    private ArrayList<String> pageName;
    private DailyTarget target;
    private LocalDate date;
    private int size;

    class DailyTarget {
        private ArrayList<Integer> page;
        private ArrayList<String> unitTime;
        private ArrayList<String> pageName;
        private int size;

        DailyTarget() {
            page = new ArrayList<Integer>();
            unitTime = new ArrayList<String>();
            pageName = new ArrayList<String>();
            size = 0;
        }

        public String streamData() {
            String stream = "\n" + size;
            for (int i : page)
                stream += "\n" + i;
            for (String s : unitTime)
                stream += "\n" + s;
            for (String s : pageName)
                stream += "\n" + s;
            return stream;
        }

        public void load(BufferedReader _reader) {
            try {
                size = Integer.parseInt(_reader.readLine());
                for (int i = 0; i < size; i++)
                    page.add(Integer.parseInt(_reader.readLine()));
                for (int i = 0; i < size; i++)
                    unitTime.add(_reader.readLine());
                for (int i = 0; i < size; i++)
                    pageName.add(_reader.readLine());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        public void update(ArrayList<Integer> _page, ArrayList<String> _unitTime, ArrayList<String> _pageName) {
            assert (_page.size() == _unitTime.size() && _unitTime.size() == _pageName.size());
            clear();
            page.addAll(_page);
            pageName.addAll(_pageName);
            unitTime.addAll(_unitTime);
            size = pageName.size();
        }

        public void addAll(DailyTarget _dailyTarget) {
            clear();
            page.addAll(_dailyTarget.page);
            pageName.addAll(_dailyTarget.pageName);
            unitTime.addAll(_dailyTarget.unitTime);
            size = _dailyTarget.size;
        }

        public void clear() {
            pageName.clear();
            page.clear();
            unitTime.clear();
            size = 0;
        }
    }

    DailyData() {
        page = new ArrayList<Integer>();
        time = new ArrayList<Long>();
        pageName = new ArrayList<String>();
        target = new DailyTarget();
        date = LocalDate.now();
        size = 0;
    }

    DailyData(LocalDate _date) {
        page = new ArrayList<Integer>();
        time = new ArrayList<Long>();
        pageName = new ArrayList<String>();
        target = new DailyTarget();
        date = _date;
        size = 0;
    }

    DailyData(DailyData _dailydata) {
        page = new ArrayList<Integer>();
        time = new ArrayList<Long>();
        pageName = new ArrayList<String>();
        target = new DailyTarget();

        for (int i = 0; i < _dailydata.size; i++) {
            page.add(0);
            time.add((long) 0);
        }
        for (String s : _dailydata.pageName)
            pageName.add(s);
        target.addAll(_dailydata.target);
        date = LocalDate.now();
        size = _dailydata.size;
    }

    DailyData(DailyData _dailydata, LocalDate _date) {
        page = new ArrayList<Integer>();
        time = new ArrayList<Long>();
        pageName = new ArrayList<String>();
        target = new DailyTarget();

        for (int i = 0; i < _dailydata.size; i++) {
            page.add(0);
            time.add((long) 0);
        }
        for (String s : _dailydata.pageName)
            pageName.add(s);
        target.addAll(_dailydata.target);
        date = _date;
        size = _dailydata.size;
    }

    public int getSize() {
        return size;
    }

    public int getTargetSize() {
        return target.size;
    }

    public boolean isDoDay(LocalDate _date) {
        return date.isEqual(_date);
    }

    public boolean isDayBefore(LocalDate _date) {
        return date.isBefore(_date);
    }

    public boolean isDayAfter(LocalDate _date) {
        return date.isAfter(_date);
    }

    public LocalDate getDate() {
        return date;
    }

    public ArrayList<Integer> getPageTarget() {
        return target.page;
    }

    public ArrayList<String> getPageNameTarget() {
        return target.pageName;
    }

    public ArrayList<String> getUnitTimeTarget() {
        return target.unitTime;
    }

    public ArrayList<Long> getTime() {
        return time;
    }

    public ArrayList<Integer> getPage() {
        return page;
    }

    public ArrayList<String> getPageName() {
        return pageName;
    }

    public double percentage(int _index) {
        return (double) page.get(searchIndex(_index)) / (double) target.page.get(_index);
    }

    public String info(int _index) {
        return "( " + page.get(_index) + " / " + target.page.get(_index) + " ) " + pageName.get(_index)
                + "\n" + (int) (percentage(_index) * 100) + "%\n"
                + getConvertedTime(_index) + target.unitTime.get(_index);
    }

    public String infoReset(int _index) {
        return "( 0" + " / " + target.page.get(_index) + " ) " + pageName.get(_index)
                + "\n" + "0%\n"
                + "0" + target.unitTime.get(_index);
    }

    public double totalPercentage() {
        int totalPage = 0;
        int totalPageTarget = 0;
        for (int i = 0; i < target.size; i++)
            totalPage += page.get(searchIndex(i));
        for (int i : target.page)
            totalPageTarget += i;
        if (totalPageTarget > 0)
            return (double) totalPage / (double) totalPageTarget;
        return 0;
    }

    public String totalInfo() {
        String unitTimeType = checkUnitTime();
        String info = "총합 정보\n" + (int) (totalPercentage() * 100) + "%\n"
                + String.format("%.1f", convertTotalTime(unitTimeType)) + unitTimeType;

        return info;
    }

    public void clear() {
        page.clear();
        time.clear();
        pageName.clear();
        target.clear();
        size = 0;
    }

    public String streamData() {

        String stream = "\n" + size;

        for (int i : page)
            stream += "\n" + i;
        for (Long l : time)
            stream += "\n" + l;
        for (String s : pageName)
            stream += "\n" + s;
        stream += "\n" + date.format(DateTimeFormatter.BASIC_ISO_DATE);
        stream += target.streamData();

        return stream;
    }

    public void load(BufferedReader _reader) {
        clear();
        try {
            size = Integer.parseInt(_reader.readLine());

            for (int j = 0; j < size; j++)
                page.add(Integer.parseInt(_reader.readLine()));
            for (int j = 0; j < size; j++)
                time.add(Long.parseLong(_reader.readLine()));
            for (int j = 0; j < size; j++)
                pageName.add(_reader.readLine());
            date = LocalDate.parse(_reader.readLine(), DateTimeFormatter.BASIC_ISO_DATE);

            target.load(_reader);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void exceptDeleteData(ArrayList<Long> _time, ArrayList<Integer> _page, ArrayList<String> _pageName) {
        for (int i = 0; i < _pageName.size(); i++) {
            int index = pageName.indexOf(_pageName.get(i));
            assert index >= 0;
            time.set(index, time.get(index) - _time.get(i));
            page.set(index, page.get(index) - _page.get(i));
        }
    }

    public void exceptDeleteData(long _time, int _page, String _pageName) {
        int index = pageName.indexOf(_pageName);
        assert index >= 0;
        time.set(index, time.get(index) - _time);
        page.set(index, page.get(index) - _page);
    }

    public void updateTarget(ArrayList<Integer> _pageTarget, ArrayList<String> _pageName
            , ArrayList<String> _unitTime) {
        target.update(_pageTarget, _unitTime, _pageName);
    }

    public void updateDaily(int _page, long _time, String _pageName) {
        int index = pageName.indexOf(_pageName);
        assert index >= 0 : "cant find pageName";
        page.set(index, page.get(index) + _page);
        time.set(index, time.get(index) + _time);
    }

    public void updatePageNameList(ArrayList<String> _pageName) {
        ArrayList<String> tempPageName = new ArrayList<String>();
        ArrayList<Integer> tempPage = new ArrayList<Integer>();
        ArrayList<Long> tempTime = new ArrayList<Long>();

        for (int i = 0; i < size; i++) {
            if (_pageName.contains(pageName.get(i))) {
                tempPageName.add(pageName.get(i));
                tempPage.add(page.get(i));
                tempTime.add(time.get(i));
            } else {
                int index = target.pageName.indexOf(pageName.get(i));
                assert index >= 0;
                target.pageName.remove(index);
                target.page.remove(index);
                target.unitTime.remove(index);
                target.size--;
            }
        }

        for (int i = 0; i < _pageName.size(); i++) {
            if (!pageName.contains(_pageName.get(i))) {
                tempPageName.add(_pageName.get(i));
                tempPage.add(0);
                tempTime.add((long) 0);
            }
        }
        pageName.clear();
        page.clear();
        time.clear();
        pageName.addAll(tempPageName);
        page.addAll(tempPage);
        time.addAll(tempTime);
        size = pageName.size();
    }

    public boolean isComplete() {
        for (int i = 0; i < target.size; i++)
            if (page.get(searchIndex(i)) < target.page.get(i))
                return false;
        return true;
    }

    public String checkUnitTime() {
        String unitTimeType = "초";
        for (String s : target.unitTime) {
            if (s.equals("시간"))
                unitTimeType = s;
            else if (s.equals("분") && !unitTimeType.equals("시간"))
                unitTimeType = s;
            else if (s.equals("초") && !unitTimeType.equals("시간") && !unitTimeType.equals("분"))
                unitTimeType = s;
        }
        return unitTimeType;
    }

    private String getConvertedTime(int _index) {
        double convertedTime = time.get(searchIndex(_index));
        if (target.unitTime.get(_index).equals("시간"))
            convertedTime /= 1000 * 60 * 60;
        if (target.unitTime.get(_index).equals("분"))
            convertedTime /= 1000 * 60;
        if (target.unitTime.get(_index).equals("초"))
            convertedTime /= 1000;

        return String.format("%.1f", convertedTime);
    }

    private double convertTotalTime(String _unitTimeType) {
        double totalTime = 0;

        for (int i = 0; i < target.size; i++) {
            totalTime += time.get(searchIndex(i));
        }

        if (_unitTimeType.equals("시간"))
            totalTime /= 1000 * 60 * 60;
        else if (_unitTimeType.equals("분"))
            totalTime /= 1000 * 60;
        else if (_unitTimeType.equals("초"))
            totalTime /= 1000;

        return totalTime;
    }

    private int searchIndex(int _index) {
        return pageName.indexOf(target.pageName.get(_index));
    }

    private int rearIndex() {
        return size - 1;
    }
}
