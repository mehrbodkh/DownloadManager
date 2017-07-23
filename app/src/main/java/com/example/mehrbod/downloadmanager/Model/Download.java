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


    public Download(String url, String name, ProgressBar progressBar, int priority) {
        this.url = url;
        this.name = name;
        this.progressBar = progressBar;
        this.priority = priority;
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
}
