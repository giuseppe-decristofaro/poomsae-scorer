package com.pinkhairafeff.poomsaescorer.activity.supervisor;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.NumberPicker;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;

import com.pinkhairafeff.poomsaescorer.R;
import com.pinkhairafeff.poomsaescorer.activity.MainActivity;

import database.FirebaseDB;
import entity.Match;

public class NewMatchActivity extends AppCompatActivity {

    String ageChoiced;
    String beltChoiced;
    String sexChoiced = "M";
    FirebaseDB firebaseDB = new FirebaseDB();
    static int LENGTH_COD_MATCH = 5;

    int numRefereesChoiced = 3;

    private static final String ALPHA_NUMERIC_STRING = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";

    public static String randomAlphaNumeric(int count) {
        StringBuilder builder = new StringBuilder();
        while (count-- != 0) {
            int character = (int)(Math.random()*ALPHA_NUMERIC_STRING.length());
            builder.append(ALPHA_NUMERIC_STRING.charAt(character));
        }
        return builder.toString();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_match);

        NumberPicker numberPickerAthletes = findViewById(R.id.n_athletes_picker);
        numberPickerAthletes.setMinValue(1);
        numberPickerAthletes.setMaxValue(100);
        numberPickerAthletes.setWrapSelectorWheel(false);

        RadioGroup numReferees = findViewById(R.id.num_referees_radio_group);

        numReferees.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton radioButton = findViewById(checkedId);

                boolean checked = ((RadioButton) radioButton).isChecked();

                // Check which radio button was clicked
                switch(checkedId) {
                    case R.id.three_referees_radio_btn:
                        if (checked)
                            numRefereesChoiced = 3;
                        break;
                    case R.id.five_referees_radio_btn:
                        if (checked)
                            numRefereesChoiced = 5;
                        break;
                    default:
                        numRefereesChoiced = -1;
                        break;
                }
            }
        });

        Spinner ageCategorySpinner = (Spinner) findViewById(R.id.age_category_spinner);
        ArrayAdapter<CharSequence> ageAdapter = ArrayAdapter.createFromResource(this,
                R.array.age_array, android.R.layout.simple_spinner_item);
        ageAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        ageCategorySpinner.setAdapter(ageAdapter);

        ageCategorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                ageChoiced = (String)(parent.getItemAtPosition(position));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        Spinner beltCategorySpinner = (Spinner) findViewById(R.id.belt_category_spinner);
        ArrayAdapter<CharSequence> beltAdapter = ArrayAdapter.createFromResource(this,
                R.array.belt_array, android.R.layout.simple_spinner_item);
        beltAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        beltCategorySpinner.setAdapter(beltAdapter);

        beltCategorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                beltChoiced = (String)(parent.getItemAtPosition(position));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        RadioGroup sexCategory = findViewById(R.id.sex_category_radio_group);

        sexCategory.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton radioButton = findViewById(checkedId);

                boolean checked = ((RadioButton) radioButton).isChecked();

                // Check which radio button was clicked
                switch(checkedId) {
                    case R.id.male_radio_btn:
                        if (checked)
                            sexChoiced = "M";
                        break;
                    case R.id.female_radio_btn:
                        if (checked)
                            sexChoiced = "F";
                        break;
                    default:
                        sexChoiced = "";
                        break;
                }
            }
        });

        Button createBtn = findViewById(R.id.create_btn);

        createBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int numAthletes = numberPickerAthletes.getValue();

                String idMatch = randomAlphaNumeric(NewMatchActivity.LENGTH_COD_MATCH);

                Match newMatch = new Match(idMatch, numAthletes, numRefereesChoiced);

                newMatch.setIdMatch(idMatch);
                newMatch.setAgeCategory(ageChoiced);
                newMatch.setBeltCategory(beltChoiced);
                newMatch.setNumAthletes(numAthletes);
                newMatch.setSexCategory(sexChoiced);


                firebaseDB.newMatch(newMatch);

                Intent intent = new Intent(getApplicationContext(), MatchActivity.class);
                intent.putExtra("numAthletes", numAthletes);
                intent.putExtra("idMatch", idMatch);
                intent.putExtra("numReferees", numRefereesChoiced);

                NewMatchActivity.this.startActivity(intent);
                finish();
            }
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

}