package com.example.mehrbod.downloadmanager;

import android.Manifest;
import android.app.Activity;
import android.app.DownloadManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.widget.ProgressBar;

import com.example.mehrbod.downloadmanager.Downloader.DownloadHelper;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MainActivity extends AppCompatActivity {

    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.INTERNET, Manifest.permission.ACCESS_NETWORK_STATE}, 1);

        String url = "http://dl.pop-music.ir/music/1395/Tir/Reza%20Yazdani%20-%20Az%20Tanhayi%20Mitarsam.mp3";

        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        DownloadHelper downloadHelper = new DownloadHelper(
                this,
                url ,
                DownloadManager.Request.NETWORK_WIFI
        );


        downloadHelper.prepareDownload();
        downloadHelper.startDownload();

        ExecutorService executorService = Executors.newCachedThreadPool();
        executorService.execute(new Progress(this, progressBar, downloadHelper));
    }
}

class Progress implements Runnable {
    private Activity activity;
    private ProgressBar progressBar;
    private DownloadHelper downloadHelper;

    public Progress(Activity activity, ProgressBar progressBar, DownloadHelper downloadHelper) {
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


