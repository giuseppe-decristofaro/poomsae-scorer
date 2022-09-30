package database;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import entity.Athlete;
import entity.Match;

public class FirebaseDB {

    private static FirebaseDatabase database;
    public static int NUMBER_OF_REFEREE = 3;

    public FirebaseDB(){
        //getInstance vuole l'URL del database poichè questo è situato in Belgio (Europa).
        database = FirebaseDatabase.getInstance("https://poomsae-scorer-default-rtdb.europe-west1.firebasedatabase.app/");
    }

    public FirebaseDatabase getDatabase() {
        return database;
    }

    public void setDatabase(FirebaseDatabase database) {
        database = database;
    }

    public void newMatch(Match match){

        //.getReference(qualcosa) fa mettere "qualcosa" come chiave.
        //per mettere il valore, fare .setValue di quel DatabaseReference creato con "qualcosa"
        DatabaseReference matchReference = database.getReference(match.getIdMatch());
        matchReference.setValue(match);

    }

}
