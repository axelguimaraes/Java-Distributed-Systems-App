package edu.estg.utils;

public class TrainLine {
    private String beginning;
    private String end;

    public TrainLine(String beginning, String end) {
        this.beginning = beginning;
        this.end = end;
    }

    public String getBeginning() {
        return beginning;
    }

    public String getEnd() {
        return end;
    }

    public String toString() {
        return this.beginning.toUpperCase() + " - " + this.end.toUpperCase();
    }
}
