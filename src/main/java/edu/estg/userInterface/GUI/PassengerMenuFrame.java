package edu.estg.userInterface.GUI;

import com.google.gson.Gson;
import edu.estg.userInterface.Client;
import edu.estg.utils.Passenger;
import edu.estg.utils.RequestType;
import edu.estg.utils.TrainLine;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;

public class PassengerMenuFrame extends JFrame {
    private JPanel mainPanel;
    private JLabel passengerNameLabel;
    private JList list1;
    private JRadioButton radioButton1;
    private JRadioButton radioButton2;
    private JRadioButton radioButton3;
    private JTextField textField1;
    private JButton reportProblemButton;
    private Gson jsonHelper;
    private Passenger passenger;
    private ArrayList<TrainLine> trainLines;
    private Client client;
    private final InitialFrame initialFrame;

    public PassengerMenuFrame(InitialFrame initialFrame, Client client, Passenger passenger) {
        setContentPane(mainPanel);

        this.jsonHelper = new Gson();
        this.initialFrame = initialFrame;
        this.client = client;
        this.passenger = passenger;
        this.passengerNameLabel.setText("Hello " + this.passenger.getName());
        configButtons();
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

    }

    private void loadJList(JScrollPane scrollPane, JList<String> list, ArrayList<String> data) {
        DefaultListModel<String> listModel = new DefaultListModel<>();
        listModel.addAll(data);
        list.setModel(listModel);
        scrollPane.setViewportView(list);
    }

    public void processMessage(String message, RequestType type) {

    }
}

