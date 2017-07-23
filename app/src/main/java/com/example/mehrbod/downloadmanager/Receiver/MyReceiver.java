package com.example.mehrbod.downloadmanager.Receiver;

import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.widget.ProgressBar;

import com.example.mehrbod.downloadmanager.Database.MyDatabase;
import com.example.mehrbod.downloadmanager.Downloader.DownloadHelper;
import com.example.mehrbod.downloadmanager.MainActivity;
import com.example.mehrbod.downloadmanager.Model.Download;
import com.example.mehrbod.downloadmanager.Model.ProgressBarController;

import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by mehrbod on 7/22/17.
 */

public class MyReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        // start downloads for the highest priorities

        Cursor cursor = MyDatabase.getInstance(context).getAllData();

        ArrayList<Download> downloadList = new ArrayList<>();
        while (cursor.moveToNext()) {

            Download download = new Download(cursor.getString(1), cursor.getString(7),
                    new ProgressBar(context), Integer.parseInt(cursor.getString(6)));
            downloadList.add(download);
        }

        int minPriority = 999999999;

        for (Download download : downloadList) {
            if (minPriority > download.getPriority()) {
                minPriority = download.getPriority();
            }
        }

        ExecutorService executorService = Executors.newCachedThreadPool();
        for (Download download : downloadList) {
            if (download.getPriority() == minPriority) {
                DownloadHelper downloadHelper = new DownloadHelper(context, download.getUrl(),
                        DownloadManager.Request.NETWORK_WIFI);
                downloadHelper.prepareDownload();
                downloadHelper.startDownload();

                executorService.execute(new ProgressBarController(
                        MainActivity.mActivity,
                        download.getProgressBar(),
                        downloadHelper
                ));

            }
        }

    }
}
