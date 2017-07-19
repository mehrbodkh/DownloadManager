package com.example.mehrbod.downloadmanager;

import android.Manifest;
import android.app.DownloadManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;

import com.example.mehrbod.downloadmanager.Downloader.DownloadHelper;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.INTERNET, Manifest.permission.ACCESS_NETWORK_STATE}, 1);

        String url = "http://dl.pop-music.ir/music/1395/Tir/Reza%20Yazdani%20-%20Az%20Tanhayi%20Mitarsam.mp3";
        String url2 = "http://dl.pop-music.ir/music/1395/Tir/Reza%20Yazdani%20-%20Darkam%20Kon.mp3";

        DownloadHelper downloadHelper = new DownloadHelper(
                this,
                url ,
                DownloadManager.Request.NETWORK_WIFI
        );

        DownloadHelper downloadHelper1 = new DownloadHelper(
                this,
                url2,
                DownloadManager.Request.NETWORK_WIFI
        );

        downloadHelper.prepareDownload();
        downloadHelper1.prepareDownload();
        downloadHelper.startDownload();
        downloadHelper1.startDownload();
    }
}


