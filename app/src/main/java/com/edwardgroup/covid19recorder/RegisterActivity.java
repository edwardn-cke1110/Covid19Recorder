package com.edwardgroup.covid19recorder;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class RegisterActivity extends AppCompatActivity {

    // variable declarations
    private final String TAG = "RegisterActivity";

    private AuthManager auther = new AuthManager();



    // methods
    // user creation


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        Button registerBtn = (Button) findViewById(R.id.registerBtn);
        EditText emailTxt = (EditText) findViewById(R.id.usernameText);
        EditText confirmEmailTxt = (EditText) findViewById(R.id.confirmUsernameText);
        EditText passwordTxt = (EditText) findViewById(R.id.passwordText);
        EditText confirmPasswordTxt = (EditText) findViewById(R.id.confirmPasswordText);

        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String registerEmail = emailTxt.getText().toString();
                String registerPassword = passwordTxt.getText().toString();

                auther.createUser(registerEmail, registerPassword);
//                auther.addUserToFirebase(registerEmail, registerPassword);

                if (auther.checkAccount(auther.getUser()))
                {
                    Log.d(TAG, "onClick: user created, switch to home activity");
                    startActivity(new Intent(RegisterActivity.this, HomeActivity.class));
                }
                else
                {
                    Log.d(TAG, "onClick: account creation failed");
                    Toast.makeText(RegisterActivity.this, "An error occurred, please try again", Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}