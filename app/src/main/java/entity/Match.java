package entity;

import java.util.ArrayList;

public class Match {

    private String idMatch;
    private int numAthletes;
    private int numReferees;
    private ArrayList<Athlete> athletes;
    private String ageCategory;
    private String beltCategory;
    private String sexCategory;

    public Match(){;}

    public Match(String idMatch, int numAthletes){
        this.idMatch = idMatch;
        this.athletes = new ArrayList<Athlete>();
        Athlete athlete;
        for(int i=0; i<numAthletes; i++){
            athlete = new Athlete();
            athlete.setId(i);
            athlete.setName("Atleta " + String.valueOf(i+1));
            athletes.add(athlete);
        }
        this.ageCategory = "";
        this.beltCategory = "";
        this.sexCategory = "";
    }

    public Match(String idMatch, int numAthletes, int numReferees){
        this.idMatch = idMatch;
        this.numReferees = numReferees;
        this.athletes = new ArrayList<Athlete>();
        Athlete athlete;
        for(int i=0; i<numAthletes; i++){
            athlete = new Athlete(numReferees);
            athlete.setId(i);
            athlete.setName("Atleta " + String.valueOf(i+1));
            athletes.add(athlete);
        }
        this.ageCategory = "";
        this.beltCategory = "";
        this.sexCategory = "";
    }

    public Match(String idMatch, int numAthletes, ArrayList<Athlete> athletes, String ageCategory, String beltCategory) {
        this.idMatch = idMatch;
        this.numAthletes = numAthletes;
        this.numReferees = 3;
        this.athletes = new ArrayList<>();
        Athlete athlete;
        for(int i=0; i<numAthletes; i++){
            athlete = new Athlete();
            athlete.setId(i);
            athlete.setName("Atleta " + String.valueOf(i+1));
            athletes.add(athlete);
        }
        this.ageCategory = ageCategory;
        this.beltCategory = beltCategory;
        this.sexCategory = "";
    }

    public Match(String idMatch, int numAthletes, int numReferees, ArrayList<Athlete> athletes, String ageCategory, String beltCategory) {
        this.idMatch = idMatch;
        this.numAthletes = numAthletes;
        this.numReferees = numReferees;
        this.athletes = new ArrayList<>();
        Athlete athlete;
        for(int i=0; i<numAthletes; i++){
            athlete = new Athlete();
            athlete.setId(i);
            athlete.setName("Atleta " + String.valueOf(i+1));
            athletes.add(athlete);
        }
        this.ageCategory = ageCategory;
        this.beltCategory = beltCategory;
        this.sexCategory = "";
    }

    public Match(String idMatch, int numAthletes, int numReferees, ArrayList<Athlete> athletes, String ageCategory, String beltCategory, String sexCategory) {
        this.idMatch = idMatch;
        this.numAthletes = numAthletes;
        this.numReferees = numReferees;
        this.athletes = new ArrayList<>();
        Athlete athlete;
        for(int i=0; i<numAthletes; i++){
            athlete = new Athlete();
            athlete.setId(i);
            athlete.setName("Atleta " + String.valueOf(i+1));
            athletes.add(athlete);
        }
        this.ageCategory = ageCategory;
        this.beltCategory = beltCategory;
        this.sexCategory = sexCategory;
    }

    public String getIdMatch() {
        return idMatch;
    }

    public void setIdMatch(String idMatch) {
        this.idMatch = idMatch;
    }

    public int getNumAthletes() {
        return numAthletes;
    }

    public void setNumAthletes(int numAthletes) {
        this.numAthletes = numAthletes;
    }

    public int getNumReferees() {
        return numReferees;
    }

    public void setNumReferees(int numReferees) {
        this.numReferees = numReferees;
    }

    public ArrayList<Athlete> getAthletes() {
        return athletes;
    }

    public void setAthletes(ArrayList<Athlete> athletes) {
        this.athletes = athletes;
    }

    public String getAgeCategory() {
        return ageCategory;
    }

    public void setAgeCategory(String ageCategory) {
        this.ageCategory = ageCategory;
    }

    public String getBeltCategory() {
        return beltCategory;
    }

    public void setBeltCategory(String beltCategory) {
        this.beltCategory = beltCategory;
    }

    public String getSexCategory() {
        return sexCategory;
    }

    public void setSexCategory(String sexCategory) {
        this.sexCategory = sexCategory;
    }
}
