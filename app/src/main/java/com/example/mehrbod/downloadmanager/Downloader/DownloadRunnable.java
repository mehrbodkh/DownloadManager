package com.example.mehrbod.downloadmanager.Downloader;

import android.app.AlarmManager;
import android.app.DownloadManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.widget.ProgressBar;

import com.example.mehrbod.downloadmanager.Database.DatabaseHelper;
import com.example.mehrbod.downloadmanager.Database.MyDatabase;
import com.example.mehrbod.downloadmanager.Model.Download;
import com.example.mehrbod.downloadmanager.Receiver.MyReceiver;

import java.util.ArrayList;

import static android.content.Context.ALARM_SERVICE;

/**
 * Created by mehrbod on 7/23/17.
 */

public class DownloadRunnable implements Runnable {
    private Context context;
    private static int runtimes = 0;

    public DownloadRunnable(Context context) {
        this.context = context;
    }

    @Override
    public void run() {

        Cursor cursor = MyDatabase.getInstance(context).getAllData();

        if (cursor.getCount() == 0) {

            Intent intent1 = new Intent(context, MyReceiver.class);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intent1,
                    PendingIntent.FLAG_UPDATE_CURRENT);
            AlarmManager alarmManager = (AlarmManager) context.getSystemService(ALARM_SERVICE);
            alarmManager.cancel(pendingIntent);

        }

        else {
            ArrayList<Download> downloadList = new ArrayList<>();
            while (cursor.moveToNext()) {
                if (!cursor.getString(4).equals("COMPLETE")) {
                    Download download = new Download(cursor.getString(1), cursor.getString(3),
                            new ProgressBar(context), Integer.parseInt(cursor.getString(2)),
                            cursor.getString(4));
                    downloadList.add(download);
                }
            }

            int minPriority = 999999999;

            for (Download download : downloadList) {
                if (minPriority > download.getPriority()) {
                    minPriority = download.getPriority();
                }
            }

            ArrayList<Download> minDownloadList = new ArrayList<>();
//        ExecutorService executorService = Executors.newCachedThreadPool();

            for (Download download : downloadList) {
                if (download.getPriority() == minPriority) {
                    Download download1 = download;
                    minDownloadList.add(download1);
                }
            }

            int counter = 0;
            for (Download download : minDownloadList) {
                if (download.getStatus().equals("PENDING")) {
                    DownloadHelper downloadHelper = new DownloadHelper(context, download.getUrl(),
                            DownloadManager.Request.NETWORK_WIFI);
                    downloadHelper.prepareDownload();
                    downloadHelper.startDownload();
                    DatabaseHelper db = MyDatabase.getInstance(context);
                    db.updateData(download.getUrl(), download.getPriority(), "RUNNING",
                            downloadHelper.getDownloadId());

                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    if (runtimes >= 1) {
                        break;
                    }

                    if (++counter >= 3) {
                        runtimes++;
                        break;
                    }
                }
//
//                executorService.execute(new ProgressBarController(
//                        MainActivity.mActivity,
//                        download.getProgressBar(),
//                        downloadHelper
//                ));
            }
        }
    }
}
