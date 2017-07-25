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
    private static final String COL_PRIORITY = "PRIORITY";
    private static final String COL_NAME = "NAME";
    private static final String COL_STATUS = "STATUS";

    private static final String CREATE_DB_TABLE_QUERY =
            "CREATE TABLE " + TABLE_NAME
                    + "(ID INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + "URL TEXT, "
                    + "PRIORITY TEXT, "
                    + "NAME TEXT, "
                    + "STATUS TEXT)";

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

    public boolean insertData(String url, int priority, String status)
    {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();

        contentValues.put(COL_URL, url);
        contentValues.put(COL_PRIORITY, priority);
        contentValues.put(COL_NAME, URLUtil.guessFileName(url, null, null));
        contentValues.put(COL_STATUS, status);

        long result = db.insert(TABLE_NAME, null, contentValues);

        if (result == -1) {
            return false;
        }
        return true;
    }

    public boolean updateData(String url, int priority, String status) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();

        contentValues.put(COL_URL, url);
        contentValues.put(COL_PRIORITY, priority);
        contentValues.put(COL_NAME, URLUtil.guessFileName(url, null, null));
        contentValues.put(COL_STATUS, status);

        int  result = db.update(TABLE_NAME, contentValues, "URL = ?", new String[]{url});

        if (result == 0) {
            return false;
        }
        return true;
    }

    public Cursor getAllData() {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.rawQuery("SELECT * FROM " + TABLE_NAME, null);
    }
}
