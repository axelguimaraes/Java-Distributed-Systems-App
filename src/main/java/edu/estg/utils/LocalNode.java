package edu.estg.utils;

import java.util.ArrayList;
import java.util.Objects;

public class LocalNode {
    private String name;
    private String username;
    private String password;
    private ArrayList<Passenger> passengers;
    private ArrayList<TrainLine> trainLines;

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
        for (int i = 0; i < this.trainLines.size(); i++) {
            list.add(this.trainLines.get(i).toString());
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
