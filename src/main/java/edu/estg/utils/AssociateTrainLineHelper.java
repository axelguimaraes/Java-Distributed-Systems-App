package edu.estg.utils;

public class AssociateTrainLineHelper {
    String passenger;
    String lineToAdd;

    public AssociateTrainLineHelper(String passenger, String lineToAdd) {
        this.passenger = passenger;
        this.lineToAdd = lineToAdd;
    }

    public String getPassenger() {
        return passenger;
    }

    public String getLineToAdd() {
        return lineToAdd;
    }
}
