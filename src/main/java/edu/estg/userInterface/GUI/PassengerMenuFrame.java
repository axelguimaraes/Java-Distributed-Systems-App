package edu.estg.userInterface.GUI;

import com.google.gson.Gson;
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
    private JButton sendEventMessageButton;
    private JButton exitButton;
    private JScrollPane scrollLinesAssociated;
    private JList<String> listLinesAssociated;
    private final Gson jsonHelper;
    private final Passenger passenger;
    private final Client client;
    private final InitialFrame initialFrame;

    public PassengerMenuFrame(InitialFrame initialFrame, Client client, Passenger passenger) {
        setContentPane(mainPanel);

        this.jsonHelper = new Gson();
        this.initialFrame = initialFrame;
        this.client = client;
        this.passenger = passenger;
        this.passengerNameLabel.setText("Hello " + this.passenger.getName());

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

    }

    private void configTabTrainLines() {
        loadJList(scrollLinesAssociated, listLinesAssociated, this.passenger.getAddedTrainLines());
    }
}

