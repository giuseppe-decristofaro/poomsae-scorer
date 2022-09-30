package com.pinkhairafeff.poomsaescorer.activity.referee;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.pinkhairafeff.poomsaescorer.R;
import com.pinkhairafeff.poomsaescorer.activity.MainActivity;

import database.FirebaseDB;
import entity.Athlete;
import entity.Match;

public class StartScoringActivity extends AppCompatActivity {

    FirebaseDB firebaseDB = new FirebaseDB();
    private String idMatch;
    private int idCurrentAthlete = -1;
    int numReferees = -1;
    String nameCurrentAthlete;

    Button startScoringBtn;
    TextView currentAthleteTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_scoring);

        startScoringBtn = findViewById(R.id.start_scoring_btn);
        currentAthleteTextView = findViewById(R.id.start_scoring_text_view);

        Intent intent = getIntent();
        idMatch = intent.getStringExtra("idMatch");
        numReferees = intent.getIntExtra("numReferees", -1);
        Log.d(this.toString(), "Num Referees: " + String.valueOf(numReferees));

    }

    @Override
    protected void onStart() {
        super.onStart();

        firebaseDB.getDatabase().getReference(idMatch).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                boolean athleteFound = false;

                Athlete athlete;

                for(DataSnapshot snap : snapshot.child("athletes").getChildren()){
                    athlete = snap.getValue(Athlete.class);
                    if(athlete.getNumValuation() < numReferees){
                        idCurrentAthlete = athlete.getId();
                        nameCurrentAthlete = athlete.getName();
                        currentAthleteTextView.setText("Prossimo atleta: \n" + athlete.getName());
                        athleteFound = true;
                        break;
                    }
                }

                if(!athleteFound) {
                    currentAthleteTextView.setText("Nessun atleta da valutare");
                    startScoringBtn.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {


            }
        });

        startScoringBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getApplicationContext(), ScoringActivity.class);
                intent.putExtra("idCurrentAthlete", idCurrentAthlete);
                intent.putExtra("nameCurrentAthlete", nameCurrentAthlete);
                intent.putExtra("idMatch", idMatch);
                intent.putExtra("numReferees", numReferees);
                StartScoringActivity.this.startActivity(intent);
            }
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}