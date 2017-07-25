package com.example.mehrbod.downloadmanager.Receiver;

import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.util.Log;
import android.widget.ProgressBar;

import com.example.mehrbod.downloadmanager.Database.DatabaseHelper;
import com.example.mehrbod.downloadmanager.Database.MyDatabase;
import com.example.mehrbod.downloadmanager.Downloader.DownloadRunnable;
import com.example.mehrbod.downloadmanager.Model.Download;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by mehrbod on 7/23/17.
 */

public class DownloadCompleteReceiver extends BroadcastReceiver {
    private int counter = 0;
    private boolean downloadAgain = true;
    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        long downloadId = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, 0);

        if (DownloadManager.ACTION_DOWNLOAD_COMPLETE.equals(action)) {
            Cursor cursor = MyDatabase.getInstance(context).getAllData();

            ArrayList<Download> downloadList = new ArrayList<>();
            while (cursor.moveToNext()) {
                if (!cursor.getString(4).equals("COMPLETE")) {
                    counter++;
                    Download download = new Download(cursor.getString(1), cursor.getString(3),
                            new ProgressBar(context), Integer.parseInt(cursor.getString(2)),
                            cursor.getString(4));
                    download.setDownloadId(Long.parseLong(cursor.getString(5)));
                    downloadList.add(download);
                }
            }

            int minPriority = 0;
            if (!downloadList.isEmpty()) {
                minPriority = downloadList.get(0).getPriority();
            }

            for (Download download : downloadList) {

                if (minPriority > download.getPriority()) {
                    minPriority = download.getPriority();
                }

            }

            int finishHour = 0;
            int finishMinute = 0;
            try {
                FileInputStream fileInputStream = context.openFileInput("time.txt");
                Scanner input = new Scanner(fileInputStream);
                input.nextInt();
                input.nextInt();
                finishHour = input.nextInt();
                finishMinute = input.nextInt();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
//        ExecutorService executorService = Executors.newCachedThreadPool();
            int hour = Calendar.HOUR;
            int minute = Calendar.MINUTE;

            if (hour >= finishHour && minute >= finishMinute) {
                downloadAgain = false;
                Log.d("Boolean", "false");
            }
            for (Download download : downloadList) {
                if (downloadId == download.getDownloadId()) {

                    Log.d("DownloadId", "" + download.getDownloadId());
                    Log.d("IntentId", "" + downloadId);

                    DatabaseHelper db = MyDatabase.getInstance(context);
                    db.updateData(download.getUrl(), download.getPriority(),
                            "COMPLETE", downloadId);
                    Log.d("Completed", download.getName());

                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    if (downloadAgain) {
                        ExecutorService executorService = Executors.newCachedThreadPool();
                        executorService.execute(new DownloadRunnable(context));
                        executorService.shutdown();
                    }
                    break;
//
//                executorService.execute(new ProgressBarController(
//                        MainActivity.mActivity,
//                        download.getProgressBar(),
//                        downloadHelper
//                ));

                }
            }



            if (counter == 0) {
                context.unregisterReceiver(new DownloadCompleteReceiver());
                Log.d("Download", "Unregistered");
            }
        }
    }
}
