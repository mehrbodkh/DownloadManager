package com.example.mehrbod.downloadmanager.Downloader;

import android.app.DownloadManager;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.webkit.URLUtil;

/**
 * Created by mehrbod on 7/19/17.
 */

public class DownloadHelper {
    private DownloadManager downloadManager = null;
    private String url = null;
    private String fileName = null;
    private DownloadManager.Request request = null;
    private Context context = null;
    private long downloadId = 0;
    private int allowedNetworkType = 0;
    private DownloadManager.Query query = null;

    public DownloadHelper(Context context, String fileUrl, int allowedNetworkType) {
        this.url = fileUrl;
        this.context = context;
        this.allowedNetworkType = allowedNetworkType;
    }

    public void prepareDownload() {
        downloadManager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
        Uri uri = Uri.parse(url);
        fileName = URLUtil.guessFileName(url, null, null);

        request = new DownloadManager.Request(uri);

        request.setAllowedNetworkTypes(allowedNetworkType);
        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, "/BDownload/" +
                fileName);
        request.setVisibleInDownloadsUi(true);
        request.setNotificationVisibility(
                DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        request.setTitle(fileName);
    }

    public void startDownload() {
        downloadId = downloadManager.enqueue(request);
        query = new DownloadManager.Query();
        query.setFilterById(downloadId);
    }

    public int getProgress() {
        Cursor cursor = downloadManager.query(query);
        cursor.moveToFirst();

        int bytesDownloadedSoFar = cursor.getInt(
                cursor.getColumnIndex(DownloadManager.COLUMN_BYTES_DOWNLOADED_SO_FAR)
        );
        int totalBytes = cursor.getInt(
                cursor.getColumnIndex(DownloadManager.COLUMN_TOTAL_SIZE_BYTES)
        );

        double temp = (double)bytesDownloadedSoFar / (double)totalBytes;

        cursor.close();
        return (int) (temp * 100);
    }
}
