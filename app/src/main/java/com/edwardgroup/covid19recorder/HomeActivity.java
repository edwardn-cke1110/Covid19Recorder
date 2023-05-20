package com.edwardgroup.covid19recorder;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

public class HomeActivity extends AppCompatActivity {

    private AuthManager auther = new AuthManager();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        TextView test = (TextView) findViewById(R.id.TextViewTest);

        test.setText(auther.getUser().getEmail());
    }
}