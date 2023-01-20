package edu.estg.utils;

import java.util.ArrayList;

public class LocalNodeStatisticsRequestToPassengers {
    private ArrayList<TrainLine> trainLines;

    public LocalNodeStatisticsRequestToPassengers(ArrayList<TrainLine> trainLines) {
        this.trainLines = trainLines;
    }

    public ArrayList<TrainLine> getTrainLines() {
        return trainLines;
    }
}
