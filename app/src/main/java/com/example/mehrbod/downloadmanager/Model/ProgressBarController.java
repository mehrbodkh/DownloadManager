package com.example.mehrbod.downloadmanager.Model;

import android.app.Activity;
import android.widget.ProgressBar;

import com.example.mehrbod.downloadmanager.Downloader.DownloadHelper;

/**
 * Created by mehrbod on 7/22/17.
 */

public class ProgressBarController implements Runnable{

    private Activity activity;
    private ProgressBar progressBar;
    private DownloadHelper downloadHelper;

    public ProgressBarController(Activity activity, ProgressBar progressBar,
                                 DownloadHelper downloadHelper) {
        this.activity = activity;
        this.progressBar = progressBar;
        this.downloadHelper = downloadHelper;
    }

    @Override
    public void run() {
        while (true) {

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    progressBar.setProgress(downloadHelper.getProgress());
                }
            });
        }
    }
}