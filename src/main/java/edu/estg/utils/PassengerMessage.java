package edu.estg.utils;

public class PassengerMessage {
    private final String passenger;
    private final String message;
    private final String trainLine;

    public PassengerMessage(String passenger, String message, String trainLine) {
        this.passenger = passenger;
        this.message = message;
        this.trainLine = trainLine;
    }

    public String getPassenger() {
        return this.passenger;
    }

    public String getMessage() {
        return this.message;
    }

    public String getTrainLine() {
        return this.trainLine;
    }
}
