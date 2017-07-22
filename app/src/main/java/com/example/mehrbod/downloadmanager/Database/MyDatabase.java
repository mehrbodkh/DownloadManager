package com.example.mehrbod.downloadmanager.Database;

import android.content.Context;

/**
 * Created by mehrbod on 7/22/17.
 */

public class MyDatabase {
    private static DatabaseHelper databaseHelper = null;

    private MyDatabase() {}

    public static DatabaseHelper getInstance(Context context) {
        if (databaseHelper == null) {
            synchronized (MyDatabase.class) {
                databaseHelper = new DatabaseHelper(context);
            }
        }

        return databaseHelper;
    }
}
