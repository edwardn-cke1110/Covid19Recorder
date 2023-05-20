package com.edwardgroup.covid19recorder;

import android.util.Log;

import androidx.annotation.NonNull;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class AuthManager {
    private final String TAG = "AuthManager";

    private FirebaseAuth auth = FirebaseAuth.getInstance();
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    public FirebaseUser getUser()
    {
        return auth.getCurrentUser();
    }

    public void refreshStatus()
    {
        auth.getCurrentUser().reload().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful())
                {
                    // Sign in success
                    Log.d(TAG, "refreshStatus: success");
                } else
                {
                    // sign in fails
                    Log.w(TAG, "refreshStatus: failure", task.getException());
                }
            }
        });
    }


    public boolean checkAccount(FirebaseUser account)
    {
        if(account != null)
        {
            Log.d(TAG, "checkForAccount: User already signed in");
            return true;
        }
        else
        {
            FirebaseAuth.getInstance().signOut(); // prevent null user errors
            return false;
        }
    }

    public void addUserToFirebase(String email)
    {
        // NOT WORKING - please check and revise

        FirebaseUser usr = FirebaseAuth.getInstance().getCurrentUser();
        String uid = usr.getUid();

        Map<String, Object> newUser = new HashMap<>();
        newUser.put("email", email);

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
    }

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
                        } else
                        {
                            // sign in fails
                            Log.w(TAG, "createUserWithEmail: failure", task.getException());
                        }
                    }
                });
    }
}
