package edu.estg.models.schemas;

public class TrainLine {
    private String startPoint;
    private String endPoint;

    public TrainLine(String startPoint, String endPoint) {
        this.startPoint = startPoint;
        this.endPoint = endPoint;
    }
}
