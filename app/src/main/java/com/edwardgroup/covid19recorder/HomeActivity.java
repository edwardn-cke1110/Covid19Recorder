package com.edwardgroup.covid19recorder;

import androidx.annotation.GravityInt;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class HomeActivity extends AppCompatActivity {

    private FirebaseAuth auth = FirebaseAuth.getInstance();
    private String TAG = "HomeActivity";

    private DocumentSnapshot document;

    // retrieve data from firestore
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    DocumentReference documentReference = db.collection("usr").document(auth.getCurrentUser().getUid());

    LinearLayout listLayout;

    ImageButton settingsBtn;
    Button isInfectedBtn;
    Button changeSymptomsBtn;

    ScrollView symptomView;
    TextView userName; // name of user, not the username
    TextView tv;

    @Override
    protected void onStart() {
        super.onStart();
        checkLoggedIn();
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateSymptomList();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        listLayout = findViewById(R.id.symptomListLayout);

        settingsBtn = (ImageButton) findViewById(R.id.settingsBtn);
        isInfectedBtn = (Button) findViewById(R.id.isInfectedBtn);
        changeSymptomsBtn = (Button) findViewById(R.id.changeSymptomsButton);

        symptomView = (ScrollView) findViewById(R.id.symptomView);
        userName = (TextView) findViewById(R.id.userNameTv); // name of user, not the username
        tv = new TextView(HomeActivity.this);

        checkLoggedIn();

        settingsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomeActivity.this, SettingsActivity.class));
            }
        });

        changeSymptomsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomeActivity.this, AddSymptomsActivity.class));
            }
        });

        isInfectedBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomeActivity.this, AddSymptomsActivity.class));
            }
        });
    }

    private void checkLoggedIn()
    {
        if (auth.getCurrentUser() == null)
        {
            Toast.makeText(this, "You have been logged out. Please log in again.", Toast.LENGTH_LONG).show();
            startActivity(new Intent(HomeActivity.this, WelcomeActivity.class));
            finish();
        }
    }

    private LinearLayout addSymptom(String symptom)
    {
        LinearLayout ll = new LinearLayout(HomeActivity.this);

        //attributes
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        params.setMargins(10,10,10,10);
        ll.setLayoutParams(params);
        ll.setOrientation(LinearLayout.VERTICAL);
        ll.setGravity(Gravity.CENTER);
        ll.setMinimumHeight(150);
        ll.setBackgroundColor(getColor(R.color.gray));

        // symptom name
        TextView sTextView = new TextView(HomeActivity.this);
        sTextView.setGravity(Gravity.CENTER);
        sTextView.setWidth(200);

        // convert symptom name
        String display = "";
        if (symptom.equals("cough"))
        {
            display = "Coughing";
        }
        else if (symptom.equals("sob"))
        {
            display = "Shortness of Breath";
        }
        else if (symptom.equals("sneeze"))
        {
            display = "Sneezing";
        }
        else if (symptom.equals("tasteLoss"))
        {
            display = "Loss of Taste";
        }
        else if (symptom.equals("fever"))
        {
            display = "Fever";
        }
        else if (symptom.equals("musclePain"))
        {
            display = "Muscle pain";
        }
        else if (symptom.equals("soreThroat"))
        {
            display = "Sore throat";
        }
        else if (symptom.equals("fatigue"))
        {
            display = "Fatigue";
        }
        sTextView.setText(display);

        ll.addView(sTextView);

        return ll;
    }


    private boolean userIsInfected() {
        ArrayList<String> symptomList = (ArrayList<String>)document.getData().get("symptoms");
        if (!symptomList.isEmpty())
        {
            return true;
        }
        return false;
    }

    private void updateSymptomList()
    {
        listLayout.removeAllViews();
        documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    document = task.getResult();
                    if (document.exists()) {
                        Log.d(TAG, "DocumentSnapshot data: " + document.getData());
                        userName.setText(document.getData().get("name").toString());
                        if (userIsInfected())
                        {
                            // log down list of symptoms - can be commented out
                            Log.d(TAG, "document.get().onComplete: \"symptoms\": " + document.getData().get("symptoms"));

                            // generate list if infected
                            for ( String symptom : (List<String>) Objects.requireNonNull(document.getData().get("symptoms"))
                            )
                            {
                                listLayout.addView(addSymptom(symptom));
                            }
                            isInfectedBtn.setText("I\'m no longer infected");
                            changeSymptomsBtn.setVisibility(View.VISIBLE);
                        }
                        else
                        {
                            // set text if not infected

                            tv.setText(R.string.not_affected);
                            listLayout.addView(tv);
                        }

                    } else {
                        Log.d(TAG, "No such document");
                    }
                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });
    }
}