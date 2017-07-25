package com.example.mehrbod.downloadmanager.Receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.example.mehrbod.downloadmanager.Downloader.DownloadRunnable;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by mehrbod on 7/22/17.
 */

public class MyReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        // start downloads for the highest priorities

        ExecutorService executorService = Executors.newCachedThreadPool();
        executorService.execute(new DownloadRunnable(context));

        executorService.shutdown();

    }
}
