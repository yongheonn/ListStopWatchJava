package com.example.liststopwatchjava;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.wearable.activity.WearableActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

import static com.example.liststopwatchjava.BuildConfig.VERSION_CODE;

public class MainSettingActivity extends WearableActivity {
    Button backupBtn;
    Button loadBtn;
    final int BACKUP_VERSION = 1;
    final int SELECT_FILE = 5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_setting);

        backupBtn = findViewById(R.id.main_setting_backup);
        loadBtn = findViewById(R.id.main_setting_load);

        backupBtn.setOnClickListener(new BackupListener());
        loadBtn.setOnClickListener(new LoadListener());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == SELECT_FILE) {
            Cursor cursor = getContentResolver().query(data.getData(), null, null, null, null);

            cursor.moveToNext();

            String path = cursor.getString(cursor.getColumnIndex("_data"));

            cursor.close();

            File tempBackup = new File(getFilesDir() + "/TempBackup");
            if(!tempBackup.exists())
                tempBackup.mkdir();
            File before = new File(getFilesDir() + "/resources");

            if(before.exists()) {
                File beforeTemp = new File(getFilesDir() + "/TempBackup/resources");
                if(beforeTemp.exists()) {
                    deleteFile(beforeTemp);
                }
                before.renameTo(new File(tempBackup.toString() + "/resources"));
            }

            extractZipFiles(path, getFilesDir().toString());
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private class BackupListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            initBackupFile();

        }
    }

    private class LoadListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
            intent.addCategory(Intent.CATEGORY_OPENABLE);
            intent.setType("application/octet-stream");
            startActivityForResult(intent, SELECT_FILE);
        }
    }

    private void deleteFile(File _file) {
        if(_file.isDirectory()) {
            for(File f : _file.listFiles())
                deleteFile(f);
            _file.delete();
        }
        else {
            _file.delete();
        }
    }

    private void initBackupFile() {
        String datPath = getFilesDir() + "/resources";
        String backupPath = "/storage/emulated/0/Download/ListStopWatch_Backup";
        File dataDir = new File(datPath);

        if (!dataDir.canRead() || !dataDir.exists()) {
            Toast.makeText(this, "백업할 데이터가 없습니다.", Toast.LENGTH_SHORT).show();
            return;
        }

        File backupDir = new File(backupPath);

        try {
            if (!backupDir.exists())
                backupDir.mkdirs();
        } catch (Exception e) {
            e.printStackTrace();
        }

        String time = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy_MM_dd_HHmmss"));

        try {
            ZipOutputStream backup_out = new ZipOutputStream(new BufferedOutputStream(
                    new FileOutputStream(backupPath + "/" + time
                            + ".bin"), 2048));

            zipFolder("", dataDir, backup_out);

            backup_out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initBackupInfo() {
        File backupInfo = new File(getFilesDir() + "/backup_temp.info");
        try {
            if (!backupInfo.exists()) {
                backupInfo.createNewFile();
            }

            BufferedWriter writer = new BufferedWriter(
                    new FileWriter(backupInfo, false));

            writer.append(BACKUP_VERSION + "\n");
            writer.flush();
            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void zipFolder(String parent, File file, ZipOutputStream zout) throws IOException {
        byte[] data = new byte[2048];
        int read;

        if (file.isFile()) {
            ZipEntry entry = new ZipEntry(parent + file.getName());
            zout.putNextEntry(entry);
            BufferedInputStream instream = new BufferedInputStream(new FileInputStream(file));

            while ((read = instream.read(data, 0, 2048)) != -1)
                zout.write(data, 0, read);

            zout.flush();
            zout.closeEntry();
            instream.close();

        } else if (file.isDirectory()) {
            String parentString = file.getPath().replace(getFilesDir().toString(), "");
            parentString = parentString.substring(0, parentString.length() - file.getName().length());
            ZipEntry entry = new ZipEntry(parentString + file.getName() + "/");
            zout.putNextEntry(entry);

            String[] list = file.list();
            if (list != null) {
                int len = list.length;
                for (int i = 0; i < len; i++) {
                    zipFolder(entry.getName(), new File(file.getPath() + "/" + list[i]), zout);
                }
            }
        }
    }

    public static boolean extractZipFiles(String zip_file, String directory) {
        boolean result = false;

        byte[] data = new byte[2048];
        ZipEntry entry = null;
        ZipInputStream zipstream = null;
        FileOutputStream out = null;

        if (!(directory.charAt(directory.length() - 1) == '/'))
            directory += "/";

        File destDir = new File(directory);
        boolean isDirExists = destDir.exists();
        boolean isDirMake = destDir.mkdirs();

        try {
            zipstream = new ZipInputStream(new FileInputStream(zip_file));

            while ((entry = zipstream.getNextEntry()) != null) {

                int read = 0;
                File entryFile;

                //디렉토리의 경우 폴더를 생성한다.
                if (entry.isDirectory()) {
                    File folder = new File(directory + entry.getName());
                    if (!folder.exists()) {
                        folder.mkdirs();
                    }
                    continue;
                } else {
                    entryFile = new File(directory + entry.getName());
                }

                if (!entryFile.exists()) {
                    boolean isFileMake = entryFile.createNewFile();
                }

                out = new FileOutputStream(entryFile);
                while ((read = zipstream.read(data, 0, 2048)) != -1)
                    out.write(data, 0, read);

                zipstream.closeEntry();

            }

            result = true;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            result = false;
        } catch (IOException e) {
            e.printStackTrace();
            result = false;
        } finally {
            if (out != null) {
                try {
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            if (zipstream != null) {
                try {
                    zipstream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return result;
    }
}
