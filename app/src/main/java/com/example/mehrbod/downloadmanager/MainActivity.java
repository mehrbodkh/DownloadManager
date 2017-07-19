package com.example.mehrbod.downloadmanager;

import android.Manifest;
import android.app.Activity;
import android.app.DownloadManager;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.webkit.URLUtil;
import android.widget.ProgressBar;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MainActivity extends AppCompatActivity {
    private long downloadId;
    private DownloadManager downloadManager = null;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.INTERNET, Manifest.permission.ACCESS_NETWORK_STATE}, 1);
        downloadManager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);

        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        String url =
                "http://dl.nicmusic.net/nicmusic/011/054/Reza%20Yazdani%20-%20Vaghte%20Mojeze.mp3";

        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url));

        String theName = URLUtil.guessFileName(url, null, null);
        request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI
                | DownloadManager.Request.NETWORK_MOBILE);
        request.setDescription("Downloading a music file");

        request.setVisibleInDownloadsUi(true);
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);

        DownloadManager.Query q = new DownloadManager.Query();
        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, "/BDownload/"
                + theName);
        downloadId = downloadManager.enqueue(request);

        q.setFilterById(downloadId);
        Cursor c = downloadManager.query(q);
        c.moveToNext();
        theName = c.getString(c.getColumnIndex(DownloadManager.COLUMN_TITLE));
        request.setTitle(theName);

        ExecutorService executorService = Executors.newCachedThreadPool();
        executorService.execute(new MyRunnable(this, downloadManager, downloadId, progressBar));
    }
}

class MyRunnable implements Runnable {

    private long downloadId;
    private DownloadManager downloadManager = null;
    private boolean downloading;
    private ProgressBar progressBar;
    private Activity activity;

    public MyRunnable(Activity activity, DownloadManager downloadManager, long downloadId,
                      ProgressBar progressBar) {
        this.activity = activity;
        this.downloadManager = downloadManager;
        this.downloadId = downloadId;
        this.progressBar = progressBar;
    }

    @Override
    public void run() {
        downloading = true;

        while (downloading) {
            DownloadManager.Query q = new DownloadManager.Query();
            q.setFilterById(downloadId);


            Cursor c = downloadManager.query(q);
            c.moveToFirst();

            int completedBytes = c.getColumnIndex(DownloadManager.COLUMN_BYTES_DOWNLOADED_SO_FAR);
            int totalBytes = c.getColumnIndex(DownloadManager.COLUMN_TOTAL_SIZE_BYTES);

            if (c.getInt(c.getColumnIndex(DownloadManager.COLUMN_STATUS))
                    == DownloadManager.STATUS_SUCCESSFUL) {
                downloading = false;
            }

            final double dl_progress = ((completedBytes) / totalBytes);

            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    progressBar.setProgress((int)(dl_progress));
                }
            });
        }
    }
}
