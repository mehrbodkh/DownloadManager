package com.example.mehrbod.downloadmanager.Model;

import android.widget.ProgressBar;

/**
 * Created by mehrbod on 7/22/17.
 */

public class Download {
    private String url;
    private String name;
    private ProgressBar progressBar = null;
    private int priority = 1;
    private String status;
    private long downloadId;


    public Download(String url, String name, ProgressBar progressBar, int priority, String status) {
        this.url = url;
        this.name = name;
        this.progressBar = progressBar;
        this.priority = priority;
        this.status = status;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }




    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ProgressBar getProgressBar() {
        return progressBar;
    }

    public void setProgressBar(ProgressBar progressBar) {
        this.progressBar = progressBar;
    }

    public long getDownloadId() {
        return downloadId;
    }

    public void setDownloadId(long downloadId) {
        this.downloadId = downloadId;
    }
}
