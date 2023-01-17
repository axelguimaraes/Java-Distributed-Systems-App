package edu.estg.utils;

import java.util.Objects;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TrainLine trainLine = (TrainLine) o;
        return Objects.equals(beginning, trainLine.beginning) && Objects.equals(end, trainLine.end);
    }
}
