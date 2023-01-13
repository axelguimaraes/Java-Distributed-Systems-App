package edu.estg.utils;

public class AddTrainLinesToLocalNodeHelper {
    private String localNode;
    private String beginning;
    private String end;

    public AddTrainLinesToLocalNodeHelper(String localNode, String beginning, String end) {
        this.localNode = localNode;
        this.beginning = beginning;
        this.end = end;
    }

    public String getLocalNode() {
        return localNode;
    }

    public String getBeginning() {
        return beginning;
    }

    public String getEnd() {
        return end;
    }
}
