package edu.estg.utils;

import java.util.ArrayList;
import java.util.Objects;

public class LocalNode {
    private final String name;
    private final String username;
    private final String password;
    private final ArrayList<Passenger> passengers;
    private final ArrayList<TrainLine> trainLines;

    public LocalNode(String name, String username, String password) {
        this.name = name;
        this.username = username;
        this.password = password;
        this.passengers = new ArrayList<>();
        this.trainLines = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public ArrayList<Passenger> getPassengers() {
        return passengers;
    }

    public ArrayList<TrainLine> getTrainLines() {
        return trainLines;
    }

    public ArrayList<String> getTrainLinesStringList() {
        ArrayList<String> list = new ArrayList<>();
        for (TrainLine trainLine : this.trainLines) {
            list.add(trainLine.toString());
        }

        return list;
    }

    public void addTrainLine(String beginning, String end) {
        this.trainLines.add(new TrainLine(beginning, end));
    }

    public void addPassenger(Passenger passenger) {
        this.passengers.add(passenger);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LocalNode localNode = (LocalNode) o;
        return Objects.equals(username, localNode.username);
    }
}
