package edu.estg;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import edu.estg.utils.LocalNode;
import edu.estg.utils.Passenger;
import edu.estg.utils.TrainLine;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class JsonFileHelper {
    private final String diretoryName;
    private final Gson jsonHelper;

    public JsonFileHelper(String directoryName) throws IOException {
        Files.createDirectories(Paths.get(directoryName));
        this.jsonHelper = configGson();
        this.diretoryName = directoryName;
    }

    private Gson configGson() {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.setPrettyPrinting();
        gsonBuilder.serializeNulls();

        return gsonBuilder.create();
    }

    public synchronized <T> void serialize(String filename, T obj) throws IOException {
        try (Writer writer = new FileWriter(diretoryName + filename + ".json")) {
            writer.write(jsonHelper.toJson(obj));
        }
    }

    public synchronized <T> List<T> deserializeArray(String filename, Class<T[]> type) throws IOException {
        if (!new File(diretoryName + filename + ".json").exists()) return new ArrayList<>();

        String jsonString = new String(Files.readAllBytes(Paths.get(diretoryName + filename + ".json")));

        if (jsonString.isEmpty())
            return new ArrayList<>();

        return Arrays.asList(jsonHelper.fromJson(jsonString, type));
    }

    public synchronized void updateLocalNodes(ArrayList<LocalNode> localNodes) throws IOException {
        serialize("localNodes", new HashSet<>(localNodes));
    }

    public synchronized ArrayList<LocalNode> getLocalNodes() throws IOException {
        return new ArrayList<>(deserializeArray("localNodes", LocalNode[].class));
    }


    public synchronized void updatePassengers(ArrayList<Passenger> passengers) throws IOException {
        serialize("passengers", new HashSet<>(passengers));
    }


    public synchronized ArrayList<Passenger> getPassengers() throws IOException {
        return new ArrayList<>(deserializeArray("passengers", Passenger[].class));
    }

    // TODO: here
    public synchronized void updateTrainLines(ArrayList<TrainLine> trainLines) throws IOException {
        serialize("trainLines", new HashSet<>(trainLines));
    }

    public synchronized ArrayList<TrainLine> getTrainLines() throws IOException {
        return new ArrayList<>(deserializeArray("trainLines", TrainLine[].class));
    }

}
