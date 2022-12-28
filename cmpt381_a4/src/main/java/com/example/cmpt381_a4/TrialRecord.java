package com.example.cmpt381_a4;

public class TrialRecord {
    private double elapsedTime;
    private double ID;

    public double getElapsedTime() {
        return elapsedTime;
    }

    public void setElapsedTime(double elapsedTime) {
        this.elapsedTime = elapsedTime;
    }

    public double getID() {
        return ID;
    }

    public void setID(double ID) {
        this.ID = ID;
    }

    TrialRecord(double time, double ID){
        this.elapsedTime = time;
        this.ID = ID;
    }

    @Override
    public String toString() {
        return "TrialRecord{" +
                "elapsedTime=" + elapsedTime +
                ", ID=" + ID +
                '}';
    }
}
