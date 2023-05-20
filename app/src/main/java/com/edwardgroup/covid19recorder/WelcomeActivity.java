package com.edwardgroup.covid19recorder;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;

public class WelcomeActivity extends AppCompatActivity {
    private final String TAG = "WelcomeActivity";
    private FirebaseAuth auth = FirebaseAuth.getInstance();

    @Override
    public void onStart() {
        super.onStart();
        if(auth.getCurrentUser() != null){
            Log.d(TAG, "onStart: Logged in, switch to HomeActivity");
            startActivity(new Intent(WelcomeActivity.this, HomeActivity.class));
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        Button login = (Button) findViewById(R.id.welcomeLoginBtn);
        TextView register = (TextView) findViewById(R.id.signupText);


        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(WelcomeActivity.this, LoginActivity.class));
                Log.d(TAG,"Switch to login activity");
            }
        });
        
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(WelcomeActivity.this, RegisterActivity.class));
                Log.d(TAG, "onClick: Switch to register activity");
            }
        });
    }
}