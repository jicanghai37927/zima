package com.haiyunshan.zima.note.dataset;

import android.os.Environment;

import com.google.gson.Gson;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.List;

public class NoteManager {

    private static NoteManager sInstance;

    NoteDataset mDataset;

    public static final NoteManager instance() {
        if (sInstance == null) {
            sInstance = new NoteManager();
        }

        return sInstance;
    }

    private NoteManager() {
        this.mDataset = read();
    }

    public NoteEntry put(String id) {
        NoteEntry item = this.obtain(id);
        if (item == null) {
            item = new NoteEntry(id);
            mDataset.add(item);
        }

        return item;
    }

    public NoteEntry obtain(String id) {
        NoteEntry item = mDataset.obtain(id);
        return item;
    }

    public List<NoteEntry> getList() {
        return mDataset.getList();
    }

    public void save() {

        try {
            File file = getFile();
            file.createNewFile();
            FileOutputStream fos = new FileOutputStream(file);
            OutputStreamWriter writer = new OutputStreamWriter(fos, "utf-8");

            Gson gson = new Gson();
            gson.toJson(mDataset, writer);

            writer.close();
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    NoteDataset read() {
        File file = getFile();
        if (!file.exists()) {
            return new NoteDataset();
        }

        NoteDataset ds = null;

        try {
            FileInputStream fis = new FileInputStream(file);
            InputStreamReader reader = new InputStreamReader(fis, "utf-8");

            Gson gson = new Gson();
            ds = gson.fromJson(reader, NoteDataset.class);

            reader.close();
            fis.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (ds == null) {
            ds = new NoteDataset();
        }

        return ds;
    }

    File getFile() {
        File file = new File(Environment.getExternalStorageDirectory(), "zima");
        file.mkdirs();

        file = new File(file, "note_ds.json");
        return file;
    }
}
