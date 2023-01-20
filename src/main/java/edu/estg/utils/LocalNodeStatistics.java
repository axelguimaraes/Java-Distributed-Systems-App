package edu.estg.utils;

public class LocalNodeStatistics {
    private final String localNodeName;
    private final int messagesSent;
    private final int passengersNotified;

    public LocalNodeStatistics(String localNodeName, int messagesSent, int passengersNotified) {
        this.localNodeName = localNodeName;
        this.messagesSent = messagesSent;
        this.passengersNotified = passengersNotified;
    }

    public String getLocalNode() {
        return this.localNodeName;
    }

    public int getMessagesSent() {
        return this.messagesSent;
    }

    public int getPassengersNotified() {
        return this.passengersNotified;
    }

    public String toString() {
        return "LOCAL NODE: " + this.localNodeName + " | MESSAGES SENT: " + this.messagesSent + " | PASSENGERS NOTIFIED: " + this.passengersNotified;
    }
}
