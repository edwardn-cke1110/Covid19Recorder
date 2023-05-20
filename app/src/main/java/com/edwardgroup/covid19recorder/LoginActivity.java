package com.edwardgroup.covid19recorder;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {

    public final String TAG = "LoginActivity";
    private FirebaseAuth auth = FirebaseAuth.getInstance();

    public void signIn(String email, String password)
    {
        this.auth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful())
                {
                    // Sign in success
                    Log.d(TAG, "signInWithEmailAndPassword: success");
                    startActivity(new Intent(LoginActivity.this, HomeActivity.class));
                    finish();
                } else
                {
                    // sign in fails
                    Log.w(TAG, "signInWithEmailAndPassword: failure", task.getException());
                    Toast.makeText(LoginActivity.this, "An error occurred, please try again", Toast.LENGTH_LONG).show();
                }
            }
        });;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Button loginBtn = (Button) findViewById(R.id.loginBtn);
        EditText email = (EditText) findViewById(R.id.usernameText);
        EditText password = (EditText) findViewById(R.id.passwordText);

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signIn(email.getText().toString(), password.getText().toString());
            }
        });
    }


}