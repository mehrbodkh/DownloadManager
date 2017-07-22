package com.example.mehrbod.downloadmanager.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.webkit.URLUtil;

/**
 * Created by mehrbod on 7/22/17.
 */

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "downloads.db";
    private static final String TABLE_NAME = "download_table";
    private static final String COL_ID = "ID";
    private static final String COL_URL = "URL";
    private static final String COL_START_HOUR = "START_HOUR";
    private static final String COL_START_MINUTE = "START_MINUTE";
    private static final String COL_FINISH_HOUR = "FINISH_HOUR";
    private static final String COL_FINISH_MINUTE = "FINISH_MINUTE";
    private static final String COL_PRIORITY = "PRIORITY";
    private static final String COL_NAME = "NAME";

    private static final String CREATE_DB_TABLE_QUERY =
            "CREATE TABLE " + TABLE_NAME
                    + "(ID INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + "URL TEXT, "
                    + "START_HOUR TEXT, "
                    + "START_MINUTE TEXT, "
                    + "FINISH_HOUR TEXT, "
                    + "FINISH_MINUTE TEXT, "
                    + "PRIORITY TEXT, "
                    + "NAME TEXT)";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_DB_TABLE_QUERY);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXIST " + TABLE_NAME);
    }

    public boolean insertData(String url, int startHour, int startMinute, int finishHour,
                              int finishMinute, int priority)
    {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();

        contentValues.put(COL_URL, url);
        contentValues.put(COL_START_HOUR, startHour);
        contentValues.put(COL_START_MINUTE, startMinute);
        contentValues.put(COL_FINISH_HOUR, finishHour);
        contentValues.put(COL_FINISH_MINUTE, finishMinute);
        contentValues.put(COL_PRIORITY, priority);
        contentValues.put(COL_NAME, URLUtil.guessFileName(url, null, null));

        long result = db.insert(TABLE_NAME, null, contentValues);

        if (result == -1) {
            return false;
        }
        return true;
    }

    public Cursor getAllData() {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.rawQuery("SELECT * FROM " + TABLE_NAME, null);
    }
}
