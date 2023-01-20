package edu.estg.utils;

import java.util.ArrayList;
import java.util.Objects;

public class Passenger {
    private final String name;
    private final String username;
    private final String password;
    private final ArrayList<TrainLine> addedTrainLines;

    public Passenger(String name, String username, String password, ArrayList<TrainLine> addedTrainLines) {
        this.name = name;
        this.username = username;
        this.password = password;
        this.addedTrainLines = addedTrainLines;
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

    public ArrayList<TrainLine> getAddedTrainLines() {
        return this.addedTrainLines;
    }

    public ArrayList<String> getAddedTrainLinesToString() {
        ArrayList<String> list = new ArrayList<>();
        for (int i = 0; i < this.addedTrainLines.size(); i++) {
            list.add(this.addedTrainLines.get(i).toString());
        }
        return list;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Passenger passenger = (Passenger) o;
        return Objects.equals(username, passenger.username);
    }
}
