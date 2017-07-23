package com.example.mehrbod.downloadmanager;

import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TimePicker;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;

public class SetTimeActivity extends AppCompatActivity {

    private int startHour;
    private int startMinute;

    private int finishHour;
    private int finishMinute;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_time);
    }

    public void onStartTimeButtonClickListener(View view) {
        Calendar mCurrentTime = Calendar.getInstance();
        int hour = mCurrentTime.get(Calendar.HOUR_OF_DAY);
        int minute = mCurrentTime.get(Calendar.MINUTE);
        TimePickerDialog mTimePicker;
        mTimePicker = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                startHour = selectedHour;
                startMinute = selectedMinute;
            }
        }, hour, minute, true);
        mTimePicker.setTitle("Select Start Time");
        mTimePicker.show();
    }

    public void onFinishTimeButtonClickListener(View view) {
        Calendar mCurrentTime = Calendar.getInstance();
        int hour = mCurrentTime.get(Calendar.HOUR_OF_DAY);
        int minute = mCurrentTime.get(Calendar.MINUTE);
        TimePickerDialog mTimePicker;
        mTimePicker = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                finishHour = selectedHour;
                finishMinute = selectedMinute;
            }
        }, hour, minute, true);
        mTimePicker.setTitle("Select Finish Time");
        mTimePicker.show();
    }

    @Override
    protected void onPause() {
        super.onPause();

        String time = String.format(
                "%d%n%d%n%d%n%d", startHour, startMinute, finishHour, finishMinute);

        try {
            FileOutputStream fileOutputStream = openFileOutput("time.txt", MODE_PRIVATE);
            fileOutputStream.write(time.getBytes());
            fileOutputStream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
