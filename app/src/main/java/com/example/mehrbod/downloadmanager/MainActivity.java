package com.example.mehrbod.downloadmanager;

import android.Manifest;
import android.app.Activity;
import android.app.DownloadManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.webkit.URLUtil;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.mehrbod.downloadmanager.Downloader.DownloadHelper;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MainActivity extends AppCompatActivity {

    private ProgressBar progressBar;
    private EditText urlEditText;
    private TextView nameTextView;

    private DownloadHelper downloadHelper = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.INTERNET, Manifest.permission.ACCESS_NETWORK_STATE}, 1);




        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        urlEditText = (EditText) findViewById(R.id.mainActivityUrlEditText);
        nameTextView = (TextView) findViewById(R.id.mainActivityNameTextView);

        urlEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                nameTextView.setText(URLUtil.guessFileName(
                        urlEditText.getText().toString(), null, null
                ));
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }

    public void onDownloadButtonClickListener(View view) {
        String url = urlEditText.getText().toString();

        downloadHelper = new DownloadHelper(
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


