package edu.estg.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MessageFromPassenger {
    private final String passenger;
    private final String message;
    private final String trainLine;
    private final LocalNode localNode;

    public MessageFromPassenger(String passenger, String message, String trainLine) {
        this.passenger = passenger;
        this.message = message;
        this.trainLine = trainLine;
        this.localNode = null;
    }

    public MessageFromPassenger(String passenger, String message, String trainLine, LocalNode localNode) {
        this.passenger = passenger;
        this.message = message;
        this.trainLine = trainLine;
        this.localNode = localNode;
    }

    public String getPassenger() {
        return this.passenger;
    }

    public String getMessage() {
        return this.message;
    }

    public String getTrainLine() {
        return this.trainLine;
    }

    public LocalNode getLocalNode() {
        return this.localNode;
    }

    public static TrainLine getTrainLineFromString(String string) {
        TrainLine trainLine = null;

        String newString = string.replaceAll("\\s-\\s", " ");
        String rxFirst = "(\\S+)";
        String rxLast = "\\s(.*)";

        Pattern pFirst = Pattern.compile(rxFirst);
        Pattern pLast = Pattern.compile(rxLast);

        Matcher matcherFirst = pFirst.matcher(newString);
        Matcher matcherLast = pLast.matcher(newString);

        if (matcherFirst.find() && matcherLast.find()) {
            trainLine = new TrainLine(matcherFirst.group(0).toLowerCase(), matcherLast.group(0).replaceAll("\\s", "").toLowerCase());
        }

        return trainLine;
    }

    public String toString() {
        return this.trainLine + " :: " + this.message;
    }
}
