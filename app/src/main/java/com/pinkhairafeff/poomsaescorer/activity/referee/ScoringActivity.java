package com.pinkhairafeff.poomsaescorer.activity.referee;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.pinkhairafeff.poomsaescorer.R;
import com.pinkhairafeff.poomsaescorer.activity.MainActivity;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Collections;

import database.FirebaseDB;
import entity.Athlete;
import entity.Match;

public class ScoringActivity extends AppCompatActivity {


    FirebaseDB firebaseDB = new FirebaseDB();
    float totalPoints = 10f;

    int idCurrentAthlete;
    String nameCurrentAthlete;
    String idMatch;
    int numReferees;
    boolean isValued;

    Button lightErrorBtn;
    Button heavyErrorBtn;
    Button clearBtn;
    Button submitBtn;
    TextView athleteTextView;
    TextView totalPointsView;
    TextView lightErrorView;
    TextView heavyErrorView;

    NumberFormat formatter = new DecimalFormat("#0.00");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scoring);

        System.out.println("ON_CREATE CHIAMATO");

        isValued = false;

        athleteTextView = findViewById(R.id.athlete_text_view);
        totalPointsView = findViewById(R.id.total_points);
        lightErrorView = findViewById(R.id.light_error_text);
        heavyErrorView = findViewById(R.id.heavy_error_text);

        lightErrorBtn = findViewById(R.id.light_error_button);
        heavyErrorBtn = findViewById(R.id.heavy_error_button);
        clearBtn = findViewById(R.id.restore_btn);
        submitBtn = findViewById(R.id.submit_btn);

        Intent intent = getIntent();
        idCurrentAthlete = intent.getIntExtra("idCurrentAthlete", -1);
        nameCurrentAthlete = intent.getStringExtra("nameCurrentAthlete");
        idMatch = intent.getStringExtra("idMatch");
        numReferees = intent.getIntExtra("numReferees", -1);

        athleteTextView.setText(nameCurrentAthlete);
        totalPointsView.setText(String.valueOf(totalPoints));

        if(savedInstanceState != null) {

            isValued = savedInstanceState.getBoolean("isValued");

            if (isValued) {
                submitBtn.setVisibility(View.GONE);
                clearBtn.setVisibility(View.GONE);
                lightErrorView.setVisibility(View.GONE);
                heavyErrorView.setVisibility(View.GONE);
                lightErrorBtn.setVisibility(View.GONE);
                heavyErrorBtn.setVisibility(View.GONE);
            }
        }
    }

    @Override
    protected void onStart() {
        super.onStart();

        isValued = false;


        lightErrorBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                totalPoints -= 0.25f;
                totalPoints = checkPoint(totalPoints);
                totalPointsView.setText(formatter.format(totalPoints));
            }
        });

        heavyErrorBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                totalPoints -= 0.5f;
                totalPoints = checkPoint(totalPoints);
                totalPointsView.setText(formatter.format(totalPoints));
            }
        });

        clearBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {

                AlertDialog.Builder builder = new AlertDialog.Builder(ScoringActivity.this);
                builder.setMessage("Sei sicuro?");
                builder.setCancelable(true);

                builder.setPositiveButton(
                        "Si",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {

                                totalPoints = 10f;
                                totalPointsView.setText(formatter.format(totalPoints));

                                dialog.cancel();
                            }
                        });

                builder.setNegativeButton(
                        "No",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });

                AlertDialog alertSubmit = builder.create();
                alertSubmit.show();


            }
        });


        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder builder = new AlertDialog.Builder(ScoringActivity.this);
                builder.setMessage("Sei sicuro?");
                builder.setCancelable(true);

                builder.setPositiveButton(
                        "Si",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {

                                boolean connected = false;
                                ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                                if(connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                                        connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
                                    //we are connected to a network
                                    connected = true;
                                }
                                else
                                    connected = false;

                                if(connected) {
                                    firebaseDB.getDatabase().getReference(idMatch).child("athletes")
                                            .child(String.valueOf(idCurrentAthlete))
                                            .addListenerForSingleValueEvent(insertScoringListener);

                                    firebaseDB.getDatabase().getReference(idMatch).child("athletes")
                                            .child(String.valueOf(idCurrentAthlete))
                                            .addValueEventListener(checkValuationRealtime);

                                    isValued = true;

                                    //Blocca su questa activity

                                    submitBtn.setVisibility(View.GONE);
                                    clearBtn.setVisibility(View.GONE);
                                    lightErrorView.setVisibility(View.GONE);
                                    heavyErrorView.setVisibility(View.GONE);
                                    lightErrorBtn.setVisibility(View.GONE);
                                    heavyErrorBtn.setVisibility(View.GONE);
                                }
                                else
                                    Toast.makeText(getApplicationContext(), "Connessione ad Internet non buona.\n" +
                                            "Riprovare con una connesione più stabile.", Toast.LENGTH_SHORT).show();

                                dialog.cancel();
                            }
                        }).setNegativeButton(
                        "No",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });

                AlertDialog alertSubmit = builder.create();
                alertSubmit.show();

            }
        });
    }



    public float checkPoint(float newPoints){
        if(newPoints < 0)
            newPoints = 0;

        return newPoints;
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("idCurrentAthlete", idCurrentAthlete);
        outState.putString("idMatch", idMatch);

        outState.putFloat("totalPoints", totalPoints);

        outState.putBoolean("isValued", isValued);

    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        idCurrentAthlete = savedInstanceState.getInt("idCurrentAthlete");
        idMatch = savedInstanceState.getString("idMatch");

        totalPoints = savedInstanceState.getFloat("totalPoints");
        totalPointsView.setText(String.valueOf(formatter.format(totalPoints)));
        isValued = savedInstanceState.getBoolean("isValued");

        Log.println(Log.DEBUG, "isValued?", String.valueOf(isValued));

    }

    @Override
    public void onBackPressed() {
        Toast.makeText(getApplicationContext(), "Al momento non puoi tornare indietro", Toast.LENGTH_SHORT).show();
    }


    ValueEventListener checkValuationRealtime = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot snapshot) {

            if(snapshot.exists()) {

                Athlete athleteTemp = snapshot.getValue(Athlete.class);

                if (athleteTemp.getId() == idCurrentAthlete && athleteTemp.getNumValuation() >= numReferees) {
                    ScoringActivity.this.finish();
                }
            }

        }

        @Override
        public void onCancelled(@NonNull DatabaseError error) {

        }
    };

    ValueEventListener insertScoringListener = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot snapshot) {

            if(snapshot.exists()){

                Athlete athleteTemp = snapshot.getValue(Athlete.class);

                Log.d(this.toString(), "Num Referees Match: " + String.valueOf(numReferees));
                Log.d(this.toString(), "ID: " + idCurrentAthlete);
                Log.d(this.toString(), "Num of Valuations are: " + String.valueOf(athleteTemp.getNumValuation()));

                if(athleteTemp.getId() == idCurrentAthlete){

                    for(int i=0; i<athleteTemp.getValuations().size(); i++){
                        if((int) (athleteTemp.getValuations().get(i).floatValue()) == -1){

                            athleteTemp.setScore( athleteTemp.getScore() + totalPoints );
                            athleteTemp.setNumValuation( athleteTemp.getNumValuation() + 1);

                            athleteTemp.getValuations().set(i, totalPoints);

                            break;
                        }
                    }

                    if(numReferees == 5 && athleteTemp.getNumValuation() == 5){

                        Collections.sort(athleteTemp.getValuations());

                        Log.d("Val1", String.valueOf(athleteTemp.getValuations().get(1).floatValue()));
                        Log.d("Val2", String.valueOf(athleteTemp.getValuations().get(2).floatValue()));
                        Log.d("Val3", String.valueOf(athleteTemp.getValuations().get(3).floatValue()));

                        float pointsWithoutHighLow = athleteTemp.getValuations().get(1).floatValue() +
                                athleteTemp.getValuations().get(2).floatValue() +
                                athleteTemp.getValuations().get(3).floatValue();

                        athleteTemp.setScore(pointsWithoutHighLow);

                    }

                    firebaseDB.getDatabase().getReference(idMatch).child("athletes").child(String.valueOf(idCurrentAthlete))
                            .child("numValuation").setValue(athleteTemp.getNumValuation());

                    firebaseDB.getDatabase().getReference(idMatch).child("athletes").child(String.valueOf(idCurrentAthlete))
                            .child("score").setValue(athleteTemp.getScore());

                    firebaseDB.getDatabase().getReference(idMatch).child("athletes").child(String.valueOf(idCurrentAthlete))
                            .child("valuations").setValue(athleteTemp.getValuations());


                }
            }
        }

        @Override
        public void onCancelled(@NonNull DatabaseError error) {

        }
    };
}