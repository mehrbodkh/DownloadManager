package com.example.mehrbod.downloadmanager;

import android.Manifest;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ProgressBar;

import com.example.mehrbod.downloadmanager.Adapter.DownloadsAdapter;
import com.example.mehrbod.downloadmanager.Database.MyDatabase;
import com.example.mehrbod.downloadmanager.Model.Download;
import com.example.mehrbod.downloadmanager.Receiver.MyReceiver;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Scanner;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView = null;
    private DownloadsAdapter adapter = null;
    private List<Download> downloadList = null;
    private int startHour;
    private int startMinute;
    private int finishHour;
    private int finishMinute;

    public static Activity mActivity = null;

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
    }

    @Override
    protected void onStart() {
        super.onStart();
        mActivity = this;
    }

    public void onAddButtonClickListener(View view) {
        Intent intent = new Intent(this, AddDownload.class);
        startActivity(intent);
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        setReceiver();
    }

    private void setReceiver() {

        try {
            FileInputStream fileInputStream = openFileInput("time.txt");
            Scanner inputFile = new Scanner(fileInputStream);
            startHour = inputFile.nextInt();
            Log.d("MainActivity", "startHour: " + startHour);
            startMinute = inputFile.nextInt();
            Log.d("MainActivity", "startMinute: " + startMinute);
            finishHour = inputFile.nextInt();
            Log.d("MainActivity", "finishHour: " + finishHour);
            finishMinute = inputFile.nextInt();
            Log.d("MainActivity", "finishMinute: " + finishMinute);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            startHour = 0;
            startMinute = 0;
            finishHour = 0;
            finishMinute = 0;
        }

//        boolean alarmUp = (PendingIntent.getBroadcast(this, 0,
//                new Intent(this, MyReceiver.class),
//                PendingIntent.FLAG_UPDATE_CURRENT) != null);
//
//            Log.d("alarm", "up");



        Log.d("alarm", "starting");
        Intent intent = new Intent(this, MyReceiver.class);

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.HOUR_OF_DAY, startHour);
        calendar.set(Calendar.MINUTE, startMinute);
        calendar.set(Calendar.SECOND, 0);

        int hour = Calendar.HOUR;
        int minute = Calendar.MINUTE;

        Log.d("hour", "" + hour);
        Log.d("minute", "" + minute);

        if (hour > startHour) {
            Log.d("MainActivity", "broadcast not running");
        } else if (hour == startHour && minute >= startMinute) {
            Log.d("MainActivity", "broadcast not running");
        } else {
            Log.d("StartHour", "" + startHour);
            Log.d("StartMinute", "" + startMinute);

            PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, intent,
                    PendingIntent.FLAG_UPDATE_CURRENT);
            AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
            alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        downloadList = new ArrayList<>();
        Cursor cursor = MyDatabase.getInstance(this).getAllData();

        while (cursor.moveToNext()) {
            String status = cursor.getString(4);

            if (!status.equals("COMPLETE")) {
                Download download = new Download(cursor.getString(1), cursor.getString(3),
                        new ProgressBar(this), Integer.parseInt(cursor.getString(2)),
                        cursor.getString(4));
                downloadList.add(download);
            }
        }

        adapter = new DownloadsAdapter(this, downloadList);
        recyclerView.setAdapter(adapter);


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.time_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    public void onSetTimeItemClickListener(MenuItem item) {
        startActivity(new Intent(this, SetTimeActivity.class));
    }
}





