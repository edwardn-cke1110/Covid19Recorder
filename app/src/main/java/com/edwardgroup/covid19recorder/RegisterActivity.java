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
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {

    // variable declarations
    private final String TAG = "RegisterActivity";

    private FirebaseAuth auth = FirebaseAuth.getInstance();
    private FirebaseFirestore db = FirebaseFirestore.getInstance();



    // methods
    // user creation

    public void createUser(String email, String password)
    {
        auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful())
                        {
                            // Sign in success
                            Log.d(TAG, "createUserWithEmail: success");
                            addUserToFirestore(auth.getUid());
                            startActivity(new Intent(RegisterActivity.this, HomeActivity.class));
                            finish();
                        } else
                        {
                            // sign in fails
                            Log.w(TAG, "createUserWithEmail: failure", task.getException());
                            Toast.makeText(RegisterActivity.this, "An error occurred, please try again", Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }

    public void addUserToFirestore(String uid)
    {
        // add blank BSON file to Firestore
        Map<String, Object> newUser = new HashMap<>();

        newUser.put("name", "New User");
        newUser.put("phoneNo", "");
        newUser.put("symptoms", Collections.emptyList());

        db.collection("usr").document(uid)
                .set(newUser)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "Database entry written");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error writing document", e);
                    }
                });

        Map<String, Object> newMessage = new HashMap<>();
        newMessage.put("message", "Account created");
        newMessage.put("timestamp", Timestamp.now());

        db.collection("usr")
                .document(uid)
                .collection("messages")
                .add(newMessage);
    }

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
                String registerEmailConfirm = confirmEmailTxt.getText().toString();
                String registerPassword = passwordTxt.getText().toString();
                String registerPasswordConfirm = confirmPasswordTxt.getText().toString();

                if (!registerEmail.equals(registerEmailConfirm))
                {
                    Toast.makeText(RegisterActivity.this, "Emails do not match", Toast.LENGTH_LONG).show();
                    return;
                }

                if (!registerPassword.equals(registerPasswordConfirm))
                {
                    Toast.makeText(RegisterActivity.this, "Passwords do not match", Toast.LENGTH_LONG).show();
                    return;
                }
                createUser(registerEmail, registerPassword);
            }
        });
    }
}