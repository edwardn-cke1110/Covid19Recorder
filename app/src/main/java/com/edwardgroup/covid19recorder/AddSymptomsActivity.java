package com.edwardgroup.covid19recorder;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import org.w3c.dom.Document;

import java.util.ArrayList;
import java.util.List;

public class AddSymptomsActivity extends AppCompatActivity {

    DocumentSnapshot document;
    String TAG = "AddSymptomsActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_symptoms);

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        FirebaseAuth auth = FirebaseAuth.getInstance();
        DocumentReference documentReference = db.collection("usr").document(auth.getCurrentUser().getUid());

        // set checkbox variables
        CheckBox cough = (CheckBox) findViewById(R.id.coughCheckbox);
        CheckBox sob = (CheckBox) findViewById(R.id.sobCheckbox);
        CheckBox sneeze = (CheckBox) findViewById(R.id.sneezeCheckbox);
        CheckBox tasteLoss = (CheckBox) findViewById(R.id.tasteLossCheckbox);
        CheckBox fever = (CheckBox) findViewById(R.id.feverCheckbox);
        CheckBox musclePain = (CheckBox) findViewById(R.id.musclePainCheckbox);
        CheckBox soreThroat = (CheckBox) findViewById(R.id.soreThroatCheckbox);
        CheckBox fatigue = (CheckBox) findViewById(R.id.fatigueCheckbox);

        Button saveBtn = (Button) findViewById(R.id.saveButton);

        ArrayList<String> symptoms = new ArrayList<String>();

        documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful())
                {
                    document = task.getResult();
                    if (document.exists())
                    {
                        for (String symptom:
                                (List<String>)document.getData().get("symptoms"))
                        {
                            if (symptom.equals("cough"))
                            {
                                cough.setChecked(true);
                            }
                            else if (symptom.equals("sob"))
                            {
                                sob.setChecked(true);
                            }
                            else if (symptom.equals("sneeze"))
                            {
                                sneeze.setChecked(true);
                            }
                            else if (symptom.equals("tasteLoss"))
                            {
                                tasteLoss.setChecked(true);
                            }
                            else if (symptom.equals("fever"))
                            {
                                fever.setChecked(true);
                            }
                            else if (symptom.equals("musclePain"))
                            {
                                musclePain.setChecked(true);
                            }
                            else if (symptom.equals("soreThroat"))
                            {
                                soreThroat.setChecked(true);
                            }
                            else if (symptom.equals("fatigue"))
                            {
                                fatigue.setChecked(true);
                            }
                        }
                    }
                }
            }
        });

        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // add symptoms to ArrayList
                if (cough.isChecked())
                {
                    symptoms.add("cough");
                }
                if (sob.isChecked())
                {
                    symptoms.add("sob");
                }
                if (sneeze.isChecked())
                {
                    symptoms.add("sneeze");
                }
                if (tasteLoss.isChecked())
                {
                    symptoms.add("tasteLoss");
                }
                if (fever.isChecked())
                {
                    symptoms.add("fever");
                }
                if (musclePain.isChecked())
                {
                    symptoms.add("musclePain");
                }
                if (soreThroat.isChecked())
                {
                    symptoms.add("soreThroat");
                }
                if (fatigue.isChecked())
                {
                    symptoms.add("fatigue");
                }
            documentReference.update("symptoms", symptoms)
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
                Toast.makeText(AddSymptomsActivity.this, "Updated symptoms!", Toast.LENGTH_LONG).show();
            startActivity(new Intent(AddSymptomsActivity.this, HomeActivity.class));
            finish();
            }
        });
    }
}