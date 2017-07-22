package com.example.mehrbod.downloadmanager.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.mehrbod.downloadmanager.Model.Download;
import com.example.mehrbod.downloadmanager.R;

import java.util.List;

/**
 * Created by mehrbod on 7/22/17.
 */

public class DownloadsAdapter extends RecyclerView.Adapter<DownloadsAdapter.MyViewHolder> {

    private Context mContext;
    private List<Download> downloadList;

    public class MyViewHolder extends RecyclerView.ViewHolder {

        public TextView name;
        public ProgressBar progressBar;

        public MyViewHolder(View itemView) {
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.cardViewNameTextView);
            progressBar = (ProgressBar) itemView.findViewById(R.id.cardViewProgressBar);
        }
    }

    public DownloadsAdapter(Context context, List<Download> downloadList) {
        this.mContext = context;
        this.downloadList = downloadList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.download_card, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Download download = downloadList.get(position);
        holder.name.setText(download.getName());
        holder.progressBar.setProgress(download.getProgressBar().getProgress());
    }

    @Override
    public int getItemCount() {
        return downloadList.size();
    }
}
