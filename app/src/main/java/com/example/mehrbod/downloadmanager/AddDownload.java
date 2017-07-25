package com.example.mehrbod.downloadmanager;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.webkit.URLUtil;
import android.widget.EditText;
import android.widget.TextView;

import com.example.mehrbod.downloadmanager.Database.DatabaseHelper;
import com.example.mehrbod.downloadmanager.Database.MyDatabase;

public class AddDownload extends AppCompatActivity {
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

    public void onAddDownloadButtonClickListener(View view) {
        EditText numberPriority = (EditText) findViewById(R.id.addDownloadActivityPriorityEditText);
        priority = Integer.parseInt(numberPriority.getText().toString());

        DatabaseHelper db = MyDatabase.getInstance(this);
        db.insertData(url, priority, "PENDING");

        finish();
    }
}
