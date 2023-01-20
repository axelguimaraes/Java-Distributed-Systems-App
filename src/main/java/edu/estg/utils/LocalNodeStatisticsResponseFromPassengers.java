package edu.estg.utils;

public class LocalNodeStatisticsResponseFromPassengers {
    private Passenger passenger;
    private int messagesReceived;

    public LocalNodeStatisticsResponseFromPassengers(Passenger passenger, int messagesReceived) {
        this.passenger = passenger;
        this.messagesReceived = messagesReceived;
    }

    public Passenger getPassenger() {
        return passenger;
    }

    public int getMessagesReceived() {
        return messagesReceived;
    }
}
