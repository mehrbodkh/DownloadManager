package com.example.mehrbod.downloadmanager;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ProgressBar;

import com.example.mehrbod.downloadmanager.Adapter.DownloadsAdapter;
import com.example.mehrbod.downloadmanager.Downloader.DownloadHelper;
import com.example.mehrbod.downloadmanager.Model.Download;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView = null;
    private DownloadsAdapter adapter = null;
    private List<Download> downloadList = null;

    private DownloadHelper downloadHelper = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.INTERNET, Manifest.permission.ACCESS_NETWORK_STATE}, 1);


        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);

        downloadList = new ArrayList<>();
        adapter = new DownloadsAdapter(this, downloadList);

        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(this, 1);
        recyclerView.setLayoutManager(layoutManager);
        Animation animation = AnimationUtils.loadAnimation(this, R.anim.slide_up);
        recyclerView.setAnimation(animation);
        recyclerView.setAdapter(adapter);

        Download download1 = new Download("www.lksjfls", "hello", new ProgressBar(this));
        downloadList.add(download1);



    }

    public void onAddButtonClickListener(View view) {
        Intent intent = new Intent(this, AddDownload.class);
        startActivity(intent);
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


