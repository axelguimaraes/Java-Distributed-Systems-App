package edu.estg.userInterface.GUI;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import edu.estg.userInterface.Client;
import edu.estg.utils.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Objects;

import static javax.swing.JOptionPane.showMessageDialog;

public class PassengerMenuFrame extends JFrame {
    private JPanel mainPanel;
    private JLabel passengerNameLabel;
    private JButton sendEventMessageButton;
    private JButton exitButton;
    private JScrollPane scrollLinesAssociated;
    private JList<String> listLinesAssociated;
    private JTextArea messagesReceivedTextArea;
    private JTextField sendMessageTextField;
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

        sendEventMessageButton.addActionListener(e -> {
            if (Objects.equals(sendMessageTextField.getText(), "")) {
                showMessageDialog(new JFrame(), "Please write a message", "", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (listLinesAssociated.isSelectionEmpty()) {
                showMessageDialog(new JFrame(), "Please select a train line", "", JOptionPane.ERROR_MESSAGE);
                return;
            }

            PassengerMessage passengerMessage = new PassengerMessage(this.passenger.getName(), sendMessageTextField.getText(), listLinesAssociated.getSelectedValue());
            Request<PassengerMessage> request = new Request<>(RequestType.PASSENGER_MESSAGE, passengerMessage);
            this.client.sendMessage(this.jsonHelper.toJson(request)); // TODO: message not passing through local nodes
        });
    }

    private void loadJList(JScrollPane scrollPane, JList<String> list, ArrayList<String> data) {
        DefaultListModel<String> listModel = new DefaultListModel<>();
        listModel.addAll(data);
        list.setModel(listModel);
        scrollPane.setViewportView(list);
    }

    public void processMessage(String message, RequestType type) {
        switch (type) {
            case PASSENGER_MESSAGE_FEEDBACK:
                PassengerMessage passengerMessage = this.jsonHelper.<Response<PassengerMessage>>fromJson(message, new TypeToken<Response<PassengerMessage>>() {
                }.getType()).getData();

                String messageToShow = LocalDateTime.now() + "\n" + passengerMessage.getPassenger() + " -> " + passengerMessage.getMessage() + "\n\n";
                this.sendMessageTextField.setText("");

                messagesReceivedTextArea.setText(messagesReceivedTextArea.getText() + messageToShow);
                break;
        }

    }

    private void configTabTrainLines() {
        loadJList(scrollLinesAssociated, listLinesAssociated, this.passenger.getAddedTrainLinesToString());
    }
}

