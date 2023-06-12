package com.edwardgroup.covid19recorder;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.sql.Time;
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

    LinearLayout symptomListLayout;
    LinearLayout logListLayout;

    ImageButton settingsBtn;
    Button isInfectedBtn;
    Button changeSymptomsBtn;
    Button addMessageBtn;

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
        updateMessageList();
        updateSymptomList();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        symptomListLayout = findViewById(R.id.symptomListLayout);
        logListLayout = findViewById(R.id.logListLayout);

        settingsBtn = (ImageButton) findViewById(R.id.settingsBtn);
        isInfectedBtn = (Button) findViewById(R.id.isInfectedBtn);
        changeSymptomsBtn = (Button) findViewById(R.id.changeSymptomsButton);
        addMessageBtn = (Button) findViewById(R.id.addMessageButton);

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

        addMessageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomeActivity.this, AddMessageActivity.class));
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

    private LinearLayout addMessage(String message, Timestamp timestamp)
    {
        LinearLayout ll = new LinearLayout(HomeActivity.this);

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        params.setMargins(10,10,10,10);
        ll.setLayoutParams(params);
        ll.setOrientation(LinearLayout.HORIZONTAL);
        ll.setGravity(Gravity.CENTER);
        ll.setBackgroundColor(getColor(R.color.gray));

        // set message
        LinearLayout.LayoutParams messsageParams = new LinearLayout.LayoutParams(
                0,
                LinearLayout.LayoutParams.WRAP_CONTENT,
                2
        );

        TextView messageTv = new TextView(HomeActivity.this);
        messageTv.setLayoutParams(messsageParams);
        messageTv.setGravity(Gravity.LEFT);
        messageTv.setText(message);

        LinearLayout.LayoutParams timestampParams = new LinearLayout.LayoutParams(
                0,
                LinearLayout.LayoutParams.WRAP_CONTENT,
                1
        );

        TextView timestampTv = new TextView(HomeActivity.this);
        timestampTv.setLayoutParams(timestampParams);
        timestampTv.setGravity(Gravity.RIGHT);
        timestampTv.setText(timestamp.toDate().toString());

        ll.addView(messageTv);
        ll.addView(timestampTv);

        return ll;
    };


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
        symptomListLayout.removeAllViews();
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
                                symptomListLayout.addView(addSymptom(symptom));
                            }
                            isInfectedBtn.setText(R.string.no_longer_affected);
                            isInfectedBtn.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    removeAllSymptoms();
                                    recreate();
                                }
                            });
                            changeSymptomsBtn.setVisibility(View.VISIBLE);
                        }
                        else
                        {
                            // set text if not infected

                            tv.setText(R.string.not_affected);
                            symptomListLayout.addView(tv);
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

    private void removeAllSymptoms()
    {
        documentReference.update("symptoms", Collections.emptyList())
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "DocumentSnapshot successfully updated!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error updating document", e);
                    }
                });
    }

    private void updateMessageList()
    {
        logListLayout.removeAllViews();

        documentReference
                .collection("messages")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
//                                Log.d(TAG, document.getId() + " => " + document.getData());
                                logListLayout.addView(addMessage((String) document.getData().get("message"), (Timestamp) document.getData().get("time")));
                            }
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });
    }
}