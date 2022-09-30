package com.pinkhairafeff.poomsaescorer.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.pinkhairafeff.poomsaescorer.R;
import com.pinkhairafeff.poomsaescorer.activity.referee.ScoringActivity;
import com.pinkhairafeff.poomsaescorer.activity.referee.StartScoringActivity;
import com.pinkhairafeff.poomsaescorer.activity.supervisor.MatchActivity;
import com.pinkhairafeff.poomsaescorer.activity.supervisor.NewMatchActivity;

import database.FirebaseDB;

public class MainActivity extends AppCompatActivity {

    FirebaseDB firebaseDB = new FirebaseDB();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    protected void onStart() {
        super.onStart();

        Button joinBtn = findViewById(R.id.join_btn);
        Button newMatchBtn = findViewById(R.id.new_match_btn);
        Button showMatchBtn = findViewById(R.id.show_match_btn);

        joinBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

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
                    AlertDialog dialog = createEd(MainActivity.this, "JOIN").create();
                    dialog.show();
                }
                else
                    Toast.makeText(getApplicationContext(), "Connessione ad Internet non buona.\n" +
                            "Riprovare con una connesione più stabile.", Toast.LENGTH_SHORT).show();
            }
        });

        newMatchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

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
                    Intent intent = new Intent(getApplicationContext(), NewMatchActivity.class);
                    MainActivity.this.startActivity(intent);
                }
                else
                    Toast.makeText(getApplicationContext(), "Connessione ad Internet non buona.\n" +
                            "Riprovare con una connesione più stabile.", Toast.LENGTH_SHORT).show();

            }
        });

        showMatchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

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
                    AlertDialog dialog = createEd(MainActivity.this, "SHOW").create();
                    dialog.show();
                }
                else
                    Toast.makeText(getApplicationContext(), "Connessione ad Internet non buona.\n" +
                            "Riprovare con una connesione più stabile.", Toast.LENGTH_SHORT).show();


            }
        });

    }


    //Crea l'AlertDialog con l'EditText per mettere il codice della Match
    private AlertDialog.Builder createEd(Context ctx, String action) {
        AlertDialog.Builder builder = new AlertDialog.Builder(ctx);
        builder.setTitle("Codice turno");
        builder.setMessage("Inserisci il codice del turno");

        final EditText input = new EditText(ctx);
        input.setGravity(Gravity.CENTER);
        input.setTextSize(30);

        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT
        );

        input.setLayoutParams(lp);
        builder.setView(input);

        builder.setPositiveButton("Entra", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {

                String idInput = input.getText().toString().toUpperCase();

                firebaseDB.getDatabase().getReference(idInput).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(snapshot.exists()){

                            Intent intent;

                            int numReferees = snapshot.child("numReferees").getValue(Integer.class);

                            Log.d(this.toString(), "Num Referees: " + String.valueOf(numReferees));

                            switch (action){
                                case "JOIN":
                                    intent = new Intent(getApplicationContext(), StartScoringActivity.class);
                                    intent.putExtra("idMatch", idInput);
                                    intent.putExtra("numReferees", numReferees);
                                    MainActivity.this.startActivity(intent);
                                    break;

                                case "SHOW":
                                    intent = new Intent(getApplicationContext(), MatchActivity.class);
                                    intent.putExtra("idMatch", idInput);
                                    intent.putExtra("numReferees", numReferees);
                                    MainActivity.this.startActivity(intent);
                                    break;

                                default:
                                    Toast.makeText(getApplicationContext(), "Qualcosa è andato storto", Toast.LENGTH_SHORT).show();
                            }

                        }
                        else
                            Toast.makeText(getApplicationContext(), "Il turno non esiste", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

            }
        });
        builder.setNegativeButton("Annulla", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
            }
        });

        return builder;
    }

}