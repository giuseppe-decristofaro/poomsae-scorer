package com.pinkhairafeff.poomsaescorer.activity.supervisor;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.pinkhairafeff.poomsaescorer.R;
import com.pinkhairafeff.poomsaescorer.activity.MainActivity;

import java.util.ArrayList;
import java.util.Collections;

import adapter.AthleteAdapter;
import database.FirebaseDB;
import entity.Athlete;
import entity.Match;

public class MatchActivity extends AppCompatActivity {

    FirebaseDB firebaseDB = new FirebaseDB();

    private ArrayList<Athlete> athletes = new ArrayList<>();
    private ListView athletesListView;
    private AthleteAdapter athletesAdapter;

    TextView idMatchTextView;
    TextView ageCategoryText;
    TextView beltCategoryText;
    TextView numRefereesText;
    TextView sexCategoryText;
    String idMatch;
    int numAthletes;
    int numReferees;
    String actionUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_match);


        Intent intent = getIntent();
        numAthletes = intent.getIntExtra("numAthletes", 1);
        idMatch = intent.getStringExtra("idMatch");
        numReferees = intent.getIntExtra("numReferees", -1);

        idMatchTextView = findViewById(R.id.id_match_text);
        ageCategoryText = findViewById(R.id.age_category_text);
        beltCategoryText = findViewById(R.id.belt_category_text);
        numRefereesText = findViewById(R.id.num_referees_text);
        sexCategoryText = findViewById(R.id.sex_category_text);

        idMatchTextView.setText("Codice Match: " + idMatch);
        numRefereesText.setText("N° Arbitri: " + String.valueOf(numReferees));

        athletesListView = (ListView) findViewById(R.id.athletes_list_view);
        athletesAdapter = new AthleteAdapter(MatchActivity.this, R.layout.athlete_item, athletes);

        athletesListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                AlertDialog dialog = createEd(MatchActivity.this, position).create();
                dialog.show();
            }
        });

        firebaseDB.getDatabase().getReference(idMatch).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                athletes.clear();

                Match match = new Match();
                match.setIdMatch(idMatch);
                match.setAgeCategory(snapshot.child("ageCategory").getValue(String.class));
                match.setBeltCategory(snapshot.child("beltCategory").getValue(String.class));
                match.setSexCategory(snapshot.child("sexCategory").getValue(String.class));

                ageCategoryText.setText(match.getAgeCategory());
                beltCategoryText.setText(match.getBeltCategory());
                sexCategoryText.setText(match.getSexCategory());

                Athlete athlete;
                for(DataSnapshot snap : snapshot.child("athletes").getChildren()){
                    athlete = snap.getValue(Athlete.class);
                    athletes.add(snap.getValue(Athlete.class));
                }

                athletesListView.setAdapter(athletesAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        athletesListView.setAdapter(athletesAdapter);
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("idMatch", idMatch);
        outState.putInt("numAthletes", numAthletes);
        outState.putString("actionUser", actionUser);

    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        idMatch = savedInstanceState.getString("idMatch");
        numAthletes = savedInstanceState.getInt("numAthletes");
        actionUser = savedInstanceState.getString("actionUser");
    }

    //Crea l'AlertDialog con l'EditText per cambiare il nome dell'atleta
    private AlertDialog.Builder createEd(Context ctx, int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(ctx);
        //builder.setTitle("Cambio nome");
        builder.setMessage("Inserisci il nome dell'atleta");

        final EditText input = new EditText(ctx);
        input.setGravity(Gravity.CENTER);
        input.setTextSize(30);

        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT
        );

        input.setLayoutParams(lp);
        builder.setView(input);

        builder.setPositiveButton("Cambia", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                String nameInput = input.getText().toString().toUpperCase();
                athletes.get(position).setName(nameInput);

                firebaseDB.getDatabase().getReference(idMatch).child("athletes")
                        .child(String.valueOf(position))
                        .child("name").setValue(nameInput);

                athletesAdapter.notifyDataSetChanged();
            }
        });
        builder.setNegativeButton("Annulla", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
            }
        });

        return builder;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}