package edu.estg.userInterface.GUI;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import edu.estg.userInterface.Client;
import edu.estg.utils.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;

public class PassengerMenuFrame extends JFrame {
    private JPanel mainPanel;
    private JLabel passengerNameLabel;
    private JList list1;
    private JButton associateNewTrainLineButton;
    private JButton sendEventMessageButton;
    private JButton exitButton;
    private JScrollPane scrollLinesAssociated;
    private JList listLinesAssociated;
    private Gson jsonHelper;
    private Passenger passenger;
    private ArrayList<String> trainLinesAssociated;
    private Client client;
    private final InitialFrame initialFrame;
    private ArrayList<LocalNode> localNodes;

    public PassengerMenuFrame(InitialFrame initialFrame, Client client, Passenger passenger) {
        setContentPane(mainPanel);

        this.jsonHelper = new Gson();
        this.initialFrame = initialFrame;
        this.client = client;
        this.passenger = passenger;
        this.passengerNameLabel.setText("Hello " + this.passenger.getName());
        this.trainLinesAssociated = new ArrayList<>();

        Request<ArrayList<LocalNode>> request = new Request<>(RequestType.GET_CURRENT_LOCAL_NODES);
        this.client.sendMessage(this.jsonHelper.toJson(request));

        configButtons();
        configTabTrainLines();
    }

    public void configFrame() {
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                dispose();
            }
        });
        setPreferredSize(new Dimension(900, 650));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        pack();
        setVisible(true);
        setLocationRelativeTo(null);
    }

    public void configButtons() {
        associateNewTrainLineButton.addActionListener(e -> {
            new TrainLinesListPopupWindow(this.localNodes, this.client, this.passenger);
        });

        exitButton.addActionListener(e -> {
            exitButton.setEnabled(false);
            this.initialFrame.setVisible(true);
            this.dispose();
        });
    }

    private void loadJList(JScrollPane scrollPane, JList<String> list, ArrayList<String> data) {
        DefaultListModel<String> listModel = new DefaultListModel<>();
        listModel.addAll(data);
        list.setModel(listModel);
        scrollPane.setViewportView(list);
    }

    public void processMessage(String message, RequestType type) {
        Response<Object> response = jsonHelper.<Response<Object>>fromJson(message, Response.class);

        switch (response.type) {
            case GET_CURRENT_LOCAL_NODES:
                this.localNodes = this.jsonHelper.<Response<ArrayList<LocalNode>>>fromJson(message, new TypeToken<Response<ArrayList<LocalNode>>>() {
                }.getType()).getData();
                break;
            case ASSOCIATE_TRAIN_LINE:
                AssociateTrainLineHelper trainLineHelper = this.jsonHelper.<Response<AssociateTrainLineHelper>>fromJson(message, new TypeToken<Response<AssociateTrainLineHelper>>() {
                }.getType()).getData();

                this.trainLinesAssociated.add(trainLineHelper.getLineToAdd());
                loadJList(scrollLinesAssociated, listLinesAssociated, this.trainLinesAssociated);
        }
    }

    private void configTabTrainLines() { // TODO: List is not getting previous existing lines
        loadJList(scrollLinesAssociated, listLinesAssociated, this.trainLinesAssociated);
    }
}

