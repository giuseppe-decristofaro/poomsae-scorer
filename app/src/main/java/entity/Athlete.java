package entity;

import java.util.ArrayList;

public class Athlete {

    private int id;
    private String name;
    private float score;
    private int numValuation;
    private ArrayList<Float> valuations;

    public Athlete(){}

    public Athlete(int numReferees){
        this.valuations = new ArrayList<Float>();
        float valuation = -1;
        for(int i=0; i<numReferees; i++){
            valuations.add(valuation);
        }
    }

    public Athlete(int id, float score, int numValuation) {
        this.id = id;
        this.score = score;
        this.numValuation = numValuation;
    }

    public Athlete(int id, String name, float score, int numValuation) {
        this.id = id;
        this.name = name;
        this.score = score;
        this.numValuation = numValuation;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public float getScore() {
        return score;
    }

    public void setScore(float score) {
        this.score = score;
    }

    public int getNumValuation() {
        return numValuation;
    }

    public void setNumValuation(int numValuation) {
        this.numValuation = numValuation;
    }

    public ArrayList<Float> getValuations() {
        return valuations;
    }

    public void setValuations(ArrayList<Float> valuations) {
        this.valuations = valuations;
    }
}
