package com.example.mehrbod.downloadmanager;

import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.webkit.URLUtil;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;

import com.example.mehrbod.downloadmanager.Database.DatabaseHelper;
import com.example.mehrbod.downloadmanager.Database.MyDatabase;

import java.util.Calendar;

public class AddDownload extends AppCompatActivity {

    private int startHour = 0;
    private int startMinute = 0;

    private int finishHour = 0;
    private int finishMinute = 0;

    private String url;

    private int priority = 1;

    private EditText urlEditText = null;
    private TextView nameTextView = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_download);

        urlEditText = (EditText) findViewById(R.id.addDownloadActivityUrlEditText);
        nameTextView = (TextView) findViewById(R.id.addDownloadActivityNameTextview);

        urlEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                url = urlEditText.getText().toString();
                nameTextView.setText(
                        URLUtil.guessFileName(url,
                        null,
                        null));
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
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

    public void onAddDownloadButtonClickListener(View view) {
        EditText numberPriority = (EditText) findViewById(R.id.addDownloadActivityPriorityEditText);
        priority = Integer.parseInt(numberPriority.getText().toString());

        DatabaseHelper db = MyDatabase.getInstance(this);
        db.insertData(url, startHour, startMinute, finishHour, finishMinute, priority);

        finish();
    }
}
