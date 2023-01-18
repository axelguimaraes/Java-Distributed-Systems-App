package edu.estg.utils;

import java.util.ArrayList;

public class MessageToPassenger {
    private LocalNode localNode;
    private ArrayList<TrainLine> trainLines;
    private String message;

    public MessageToPassenger(LocalNode localNode, ArrayList<TrainLine> trainLines, String message) {
        this.localNode = localNode;
        this.trainLines = trainLines;
        this.message = message;
    }

    public LocalNode getLocalNode() {
        return localNode;
    }

    public ArrayList<TrainLine> getTrainLines() {
        return trainLines;
    }

    public String getMessage() {
        return message;
    }
}
